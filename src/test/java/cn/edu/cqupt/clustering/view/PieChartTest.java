package cn.edu.cqupt.clustering.view;


import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class PieChartTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // organize data
        String releaseIName = "compare_5.clustering";
        String releaseIIName = "compare_6.clustering";

        PieChart pieChart = new PieChart(releaseIName, releaseIIName);

        List<Cluster> releaseI = ClusteringFileHandler.getAllClusters(
               new File(this.getClass().getResource("/testdata/clustering/compare_5.clustering").getPath()));

        List<Cluster> releaseII = ClusteringFileHandler.getAllClusters(
                new File(this.getClass().getResource("/testdata/clustering/compare_6.clustering").getPath()));

        Cluster cluster = releaseI.get(0);

        NetworkGraphService ngs = null;
        try {
            ngs = new NetworkGraphService(cluster, releaseIName, releaseIIName,
                    releaseI, releaseII);
        } catch (Exception e) {

        }
        UndirectedGraph<Vertex, Edge> graph = ngs.getUndirectedGraph();
        Vertex focusVertex = ngs.getFocusVertex();

        // plot
        pieChart.organize(graph, focusVertex);

        Scene scene = new Scene(pieChart.getWebView());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}