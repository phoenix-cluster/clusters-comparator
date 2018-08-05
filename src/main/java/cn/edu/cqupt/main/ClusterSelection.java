package cn.edu.cqupt.main;

import cn.edu.cqupt.clustering.controller.AreaPieChartController;
import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.clustering.view.*;
import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.List;

/**
 * 显示clustering文件的内容，始终以clusteringFileI指向的文件作为显示的对象；
 * 交换显示时，交换变量clusteringFileI和clusteringFileII，然后重新调用create方法。
 * <p>
 * Display the contents of the clustering file,
 * always make the file pointed to by clusteringFileI as the display target;
 * when swapping the display, exchange the variables clusteringFileI and clusteringFileII,
 * and then call the create method.
 */
public class ClusterSelection {
    private File clusteringFileI;
    private File clusteringFileII;
    private GridPane clusterSelectionPane;
    private int pageSize = 8;

    public File getClusteringFileI() {
        return clusteringFileI;
    }

    public File getClusteringFileII() {
        return clusteringFileII;
    }

    public GridPane getClusterSelection() {
        return clusterSelectionPane;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ClusterSelection(File clusteringFileI, File clusteringFileII) {
        this.clusteringFileI = clusteringFileI;
        this.clusteringFileII = clusteringFileII;
        this.clusterSelectionPane = new GridPane();
        setClusterSelectionPane();
    }

    public ClusterSelection(File clusteringFileI, File clusteringFileII, int pageSize) {
        this.clusteringFileI = clusteringFileI;
        this.clusteringFileII = clusteringFileII;
        this.clusterSelectionPane = new GridPane();
        this.pageSize = pageSize;
        setClusterSelectionPane();
    }

    /**
     * Note: all tables and charts only are created once and then filled by different data
     *
     * @return
     * @throws Exception
     */
    public GridPane create() throws Exception {

        // make clusteringFileI as the display target
        List<Cluster> releaseIList = ClusteringFileHandler.getAllClusters(clusteringFileI);
        List<Cluster> releaseIIList = ClusteringFileHandler.getAllClusters(clusteringFileII);
        String releaseIName = clusteringFileI.getName();
        String releaseIIName = clusteringFileII.getName();

        /** make a display framework **/
        // cluster table
        ClusterTable clusterTable = new ClusterTable();
        BorderPane clusterTablePane = clusterTable.create(releaseIList, pageSize);

        // spectrum table
        SpectrumTable spectrumTable = new SpectrumTable();
        TableView<Spectrum> spectrumTableView = spectrumTable.createSpectrumTableView(); // create a framework

        // peak map
        PeakMap peakMap = new PeakMap();
        WebView peakMapPane = peakMap.getWebView();

        // pie chart
        AreaPieChart pieChart = new AreaPieChart(50, 50);

        // network graph
        NetworkGraph netWorkGraph = new NetworkGraph();
        GridPane networkGraphPane = netWorkGraph.getNetworkGraphPane();

        // layout
        GridPane tablePane = new GridPane();
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(50);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        tablePane.getColumnConstraints().addAll(col1, col1);
        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(100);
        tablePane.getRowConstraints().add(row0);
        tablePane.add(clusterTablePane, 0, 0);
        tablePane.add(new ScrollPane(spectrumTableView), 1, 0);
        tablePane.setGridLinesVisible(true);
        clusterSelectionPane.add(tablePane, 0, 0, 3, 1);
        clusterSelectionPane.add(new ScrollPane(peakMapPane), 0, 1);
        clusterSelectionPane.add(new ScrollPane(pieChart.getDefaultLayout()), 1, 1);
        clusterSelectionPane.add(new ScrollPane(networkGraphPane), 2, 1);

        /** add event to cluster table: focus on the row of the cluster table to display
         * the corresponding table and charts
         */
        AreaPieChartController pieChartController = new AreaPieChartController(releaseIName, releaseIIName);
        pieChart.clickedClusterIdProperty().addListener((observable, oldValue, newValue) ->
                pieChartController.clusterComposition(newValue));
        clusterTable.getClusterTableView().getFocusModel().focusedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    TableView<Cluster> clusterTableView = clusterTable.getClusterTableView();

                    // acquire the focused row number
                    int row = newValue.intValue();
                    if (row != -1 && clusterTableView.getItems().get(row) != null) {

                        // obtain current cluster
                        Cluster currentCluster = clusterTableView.getItems().get(row);

                        // set spectrum table view
                        spectrumTable.setTableView(spectrumTableView, currentCluster.getSpectra());

                        // chart
                        NetworkGraphService ngs = null;
                        try {
                            ngs = new NetworkGraphService(currentCluster, releaseIName, releaseIIName,
                                    releaseIList, releaseIIList);

                            UndirectedGraph<Vertex, Edge> graph = ngs.getUndirectedGraph();
                            Vertex focusedVertex = ngs.getFocusVertex();

                            // peak map
                            peakMap.updateData(currentCluster.getId(),
                                    currentCluster.getMzValues(),
                                    currentCluster.getIntensValues());

                            System.out.println("pie chart ing");
                            // pie chart
                            pieChartController.organizeData(graph, focusedVertex);
                            pieChart.setData(pieChartController.getOverlapSpectraCount());
                            System.out.println("pie chart end");

                            // network graph
                            netWorkGraph.create(releaseIName, releaseIIName,
                                    graph, focusedVertex);

                        } catch (Exception e) {

                        }

                    }
                }
        );
//        clusterTable.getClusterTableView().getFocusModel().focus(0);
        // trigger cluster table view's event to display chars and tables
//        clusterTable.getClusterTableView().getFocusModel().focus(1);
//        clusterTable.getClusterTableView().getFocusModel().focus(0);

        return clusterSelectionPane;
    }

    private void setClusterSelectionPane() {
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        clusterSelectionPane.getColumnConstraints().addAll(col0, col1, col2);

        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(50);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        clusterSelectionPane.getRowConstraints().addAll(row0, row1);
    }
}
