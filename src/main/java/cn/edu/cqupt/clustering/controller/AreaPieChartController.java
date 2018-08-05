package cn.edu.cqupt.clustering.controller;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.main.Application;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.clustering.view.SpectrumTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AreaPieChartController {

    // the cluster and its own cluster id which overlap with the focused cluster
    // (重叠的cluster id=>重叠的cluster)
    private HashMap<String, Cluster> overlapCluster;

    // the cluster id and spectra which overlap with the focused cluster
    // (重叠的cluster id=>重叠的spectra)
    private HashMap<String, List<Spectrum>> overlapSpectra;

    // missing data
    private List<Spectrum> missingSpectra;

    // cluster id => the spectra count of cluster
    private HashMap<String, Integer> overlapClusterCount;

    // cluster id => the overlap spectra count(except "missing data" => count)
    private HashMap<String, Number> overlapSpectraCount;

    // cluster focused by cluster table
    private Cluster focusedCluster;

    private String releaseIName;

    private String releaseIIName;

    public HashMap<String, Cluster> getOverlapCluster() {
        return overlapCluster;
    }

    public HashMap<String, List<Spectrum>> getOverlapSpectra() {
        return overlapSpectra;
    }

    public List<Spectrum> getMissingSpectra() {
        return missingSpectra;
    }

    public HashMap<String, Integer> getOverlapClusterCount() {
        return overlapClusterCount;
    }

    public HashMap<String, Number> getOverlapSpectraCount() {
        return overlapSpectraCount;
    }

    public Cluster getFocusedCluster() {
        return focusedCluster;
    }

    public String getReleaseIName() {
        return releaseIName;
    }

    public String getReleaseIIName() {
        return releaseIIName;
    }

    public AreaPieChartController(String releaseIName, String releaseIIName) {
        this.releaseIName = releaseIName;
        this.releaseIIName = releaseIIName;
    }

    public void organizeData(UndirectedGraph<Vertex, Edge> undirectedGraph,
                             Vertex focusedVertex) throws CloneNotSupportedException {
        // focused cluster
        focusedCluster = focusedVertex.getCluster();

        /** get pie chart data from graph:
         * the adjacent vertices of the focused cluster(vertex)
         */
        overlapCluster = new HashMap<>();
        overlapSpectra = new HashMap<>();
        overlapClusterCount = new HashMap<>();
        overlapSpectraCount = new HashMap<>();

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
            overlapSpectraCount.put(id, edge.getWeight());
            overlapClusterCount.put(id, vertex.getCluster().getSpecCount());
            missingSpectra.removeAll(edge.getOverlapSpectra());
        }

        // there is missing spectra in algorithm if the size of the variable missingSpectra is not 0
        if (missingSpectra.size() > 0) {
            overlapSpectraCount.put("missing data", missingSpectra.size());
        }
    }

    public void clusterComposition(String clickedClusterId) {
        if (clickedClusterId != null) {

            // cluster comparison tab
            Tab clusterComparisonTab = Application.tabPaneExpansion.getTabByText("Cluster Comparison") == null
                    ? new Tab("Cluster Comparison")
                    : Application.tabPaneExpansion.getTabByText("Cluster Comparison");
            Application.tabPaneExpansion.addTab(clusterComparisonTab);

            // spectrum table
            SpectrumTable spectrumTable = new SpectrumTable();
            if (clickedClusterId.equals("missing data")) {
                System.out.println("miss-overlapClusterId:" + clickedClusterId);
                TableView spectrumTableView = spectrumTable.createSpectrumTableView();
                spectrumTable.setTableView(spectrumTableView, missingSpectra);
                clusterComparisonTab.setContent(new BorderPane(spectrumTableView));
            } else {
                Cluster cluster = overlapCluster.get(clickedClusterId);
                List<Spectrum> spectra = overlapSpectra.get(clickedClusterId);
                TableView spectrumTableView1 = spectrumTable.createSpectrumTableView();
                TableView spectrumTableView2 = spectrumTable.createSpectrumTableView();
                GridPane spectrumTablePane = spectrumTable.setTableView(
                        spectrumTableView1, spectrumTableView2,
                        releaseIName, releaseIIName,
                        focusedCluster, cluster, spectra);
                // layout
                GridPane clusterComparisonPane = new GridPane();
                clusterComparisonPane.add(spectrumTablePane, 0, 0, 2, 1);
                clusterComparisonTab.setContent(spectrumTablePane);

                Application.tabPaneExpansion.getTabPane().
                        getSelectionModel().select(clusterComparisonTab);
            }
        }

    }
}