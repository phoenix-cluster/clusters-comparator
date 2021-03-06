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

public class PeakMapTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // organize data
        String releaseIName = "compare_5.clustering";
        String releaseIIName = "compare_6.clustering";

        PeakMap peakMap = new PeakMap();

        List<Cluster> releaseI = ClusteringFileHandler.getAllClusters(
                new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_5.clustering"));

        List<Cluster> releaseII = ClusteringFileHandler.getAllClusters(
                new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_6.clustering"));

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
        peakMap.updateData(cluster.getId(), cluster.getMzValues(), cluster.getIntensValues());

        Scene scene = new Scene(peakMap.getWebView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}