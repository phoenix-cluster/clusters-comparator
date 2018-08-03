package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.main.Application;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.websocket.EchartsServer;
import cn.edu.cqupt.websocket.Parameters;
import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.java_websocket.WebSocket;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class PieChart {
    private WebView webView;
    private Gson gson;
    private boolean isFirstRun = true;


    // the cluster and its own cluster id which overlap with the focused cluster
    // (重叠的cluster id=>重叠的cluster)
    private static HashMap<String, Cluster> overlapCluster;

    // the cluster id and spectra which overlap with the focused cluster
    // (重叠的cluster id=>重叠的spectra)
    private static HashMap<String, List<Spectrum>> overlapSpectra;

    // missing data
    private static List<Spectrum> missingSpectra;

    // cluster focused by cluster table
    private static Cluster focusedCluster;

    private static String releaseIName;
    private static String releaseIIName;

    public WebView getWebView() {
        return webView;
    }

    public static HashMap<String, Cluster> getOverlapCluster() {
        return overlapCluster;
    }

    public static HashMap<String, List<Spectrum>> getOverlapSpectra() {
        return overlapSpectra;
    }

    public static List<Spectrum> getMissingSpectra() {
        return missingSpectra;
    }

    public static Cluster getFocusedCluster() {
        return focusedCluster;
    }

    public PieChart(String releaseIName, String releaseIIName) {
        webView = new WebView();
        gson = new Gson();
        this.releaseIName = releaseIName;
        this.releaseIIName = releaseIIName;

        WebEngine webEngine = webView.getEngine();
        URL url = PeakMap.class.getResource("/html/PieChart.html");
        webEngine.load(url.toExternalForm());

    }

    public void organize(UndirectedGraph<Vertex, Edge> undirectedGraph, Vertex focusedVertex) throws CloneNotSupportedException {

        // focused cluster
        focusedCluster = focusedVertex.getCluster();

        /** get pie chart data from graph: the adjacent vertices of the focused cluster(vertex) **/
        // the cluster and its own cluster id which overlap with the focused cluster
        // (重叠的cluster id=>重叠的cluster)
        overlapCluster = new HashMap<>();

        // the cluster id and spectra which overlap with the focused cluster
        // (重叠的cluster id=>重叠的spectra)
        overlapSpectra = new HashMap<>();

        // the number of overlap spectra of cluster which overlaps with the focused cluster
        // (重叠的cluster id=>重叠数)
        List<NameValue> overlapSpectraCount = new ArrayList<>();

        // the number of spectra of cluster which overlaps with the focused cluster
        // (重叠的cluster id=>发生重叠的cluster拥有的spectra数目)
        List<NameValue> spectraCountOfOverlapCluster = new ArrayList<>();

        // missing data
        missingSpectra = focusedVertex.getCluster().clone().getSpectra();

        for (Map.Entry<Vertex, Edge> link : undirectedGraph.getAdjacencyTableOfVertex(focusedVertex).entrySet()) {
            Vertex vertex = link.getKey();
            Edge edge = link.getValue();
            String id = vertex.getCluster().getId();

            // save for spectrum table
            overlapCluster.put(id, vertex.getCluster());
            overlapSpectra.put(id, edge.getOverlapSpectra());

            // save for pie chart
            overlapSpectraCount.add(new NameValue(id, edge.getWeight()));
            spectraCountOfOverlapCluster.add(new NameValue(id, vertex.getCluster().getSpecCount()));
            missingSpectra.removeAll(edge.getOverlapSpectra());
        }

        // there is missing spectra in algorithm if the size of the variable missingSpectra is not 0
        if (missingSpectra.size() > 0) {
            overlapSpectraCount.add(new NameValue("missing data", missingSpectra.size()));
        }

        // format
        String focusedClusterStr = gson.toJson(new NameValue(focusedCluster.getId(),
                focusedCluster.getSpecCount()));
        String overlapSpectraCountStr = gson.toJson(overlapSpectraCount);
        String spectraCountOfOverlapClusterStr = gson.toJson(spectraCountOfOverlapCluster);
        String data = "{\"type\":\"PieChart\"," +
                "\"focusedCluster\":" + focusedClusterStr + "," +
                "\"overlapCluster\":" + spectraCountOfOverlapClusterStr + "," +
                "\"overlapSpectraCount\":" + overlapSpectraCountStr + "}";
        if (isFirstRun) {
            EchartsServer.webSocketMap.addListener(
                    (MapChangeListener.Change<? extends String, ? extends WebSocket> change) -> {
                        if (change.wasAdded()) {
                            System.out.println("added = " + change.getKey());
                            if (change.getKey().matches("PieChart")) {
                                System.out.println("send message to pie chart: " + data);
                                change.getValueAdded().send(data);
                            }


                            if (change.getKey().matches("\\{\"type\":\"PieChartClickEvent\",\"data\":\".*\"\\}")) {

                              Parameters params = gson.fromJson(change.getKey(), Parameters.class);

                                System.out.println(params);
//                                createClusterComparison(params.getData());
                            }
                        }
                    });
            isFirstRun = false;
        } else {
            EchartsServer.webSocketMap.get("PieChart").send(data);
        }
    }

    public void createClusterComparison(String overlapClusterId) {

        Tab clusterComparisonTab = Application.tabPaneExpansion.getTabByText("Cluster Comparison");
        if (Objects.isNull(clusterComparisonTab)) {
            System.out.println("there is NullPointerException!!");
        } else {
            System.out.println("没有空，you know compiler!!!");
        }

        // spectrum table
//        SpectrumTable spectrumTable = new SpectrumTable();
//        if (overlapClusterId.equals("missing data")) {
//            System.out.println("miss-overlapClusterId:" + overlapClusterId);
//            TableView spectrumTableView = spectrumTable.createSpectrumTableView();
//            spectrumTable.setTableView(spectrumTableView, missingSpectra);
//            clusterComparisonTab.setContent(new BorderPane(spectrumTableView));
//        } else {
//            Cluster cluster = overlapCluster.get(overlapClusterId);
//            List<Spectrum> spectra = overlapSpectra.get(overlapClusterId);
//            TableView spectrumTableView1 = spectrumTable.createSpectrumTableView();
//            TableView spectrumTableView2 = spectrumTable.createSpectrumTableView();
//            GridPane spectrumTablePane = spectrumTable.setTableView(spectrumTableView1, spectrumTableView2,
//                    releaseIName, releaseIIName, focusedCluster, cluster,
//                    spectra);
////            // layout
////            GridPane clusterComparisonPane = new GridPane();
////            clusterComparisonPane.add(spectrumTablePane, 0, 0, 2, 1);
//            clusterComparisonTab.setContent(spectrumTablePane);
//        }

        TextArea ta = new TextArea("XXXXXXXX\nXXXXXXXXX\nXXXXXXXX\nXXXXXXXXX\nXXXXXXXX\nXXXXXXXXX\nXXXXXXXX\nXXXXXXXXX\n");
        clusterComparisonTab.setContent(ta);
        System.out.println(Application.tabPaneExpansion.getTabPane().getTabs().size());
        Application.tabPaneExpansion.getTabPane().getSelectionModel().select(clusterComparisonTab);


//        System.out.println("Application.tabPaneExpansion is null :" + (Application.tabPaneExpansion == null));
//        System.out.println("clusterComparisonTab is null : " + (clusterComparisonTab == null));
//        Application.tabPaneExpansion.addTab(clusterComparisonTab);
//        Application.tabPaneExpansion.getTabPane().getSelectionModel().select(clusterComparisonTab);

    }
}

class NameValue {
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public NameValue(String name, int value) {
        this.name = name;
        this.value = value;
    }
}