package cn.edu.cqupt.mgf.ncluster.model;


import cn.edu.cqupt.graph.UndirectedGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NetworkGraphTest extends Application {
    static int count;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File mgfFile = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\spectra2000.mgf");
        File labelFile1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\bin=0.2threshold=0.1.txt");
        File labelFile2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\bin=0.2threshold=0.2.txt");

//        File mgfFile = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\test.mgf");
//        File labelFile1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\test_label1.txt");
//        File labelFile2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\test_label2.txt");


        NetworkGraphData networkGraphData = new NetworkGraphData(mgfFile, labelFile1, labelFile2);

        Map<Integer, List<Integer>> group1 = networkGraphData.getGroup1();

        count = group1.size();
        List<NetworkGraphService> serviceList = group1.keySet().parallelStream()
                .map((Integer focusLabel) -> {
                    System.out.println("remain: " + count--);
                    NetworkGraphService service =  new NetworkGraphService(networkGraphData,
                            new MSVertex(networkGraphData.getReleaseIName(),
                                    focusLabel, group1.get(focusLabel).size()));
                    service.doCompare(focusLabel, group1, group1.get(focusLabel));
                    return service;
                })
                .sorted(Comparator.comparingInt((NetworkGraphService service) ->
                        service.getGraph().getEdgeCount()).reversed())
                .collect(Collectors.toList());
        UndirectedGraph<MSVertex, MSEdge> graph = serviceList.get(0).getGraph();
//        int focusLabel = 12;
//        UndirectedGraph<MSVertex, MSEdge> graph = networkGraphData.createNetworkGraph(focusLabel, group1);

        System.out.println("======================^_^================");
        System.out.println("focus vertex: " + serviceList.get(0).getFocusVertex());
        graph.printAdjacencyTable();
        System.out.println("=========================================");

        NetworkGraph networkGraph = new NetworkGraph(networkGraphData.getReleaseIName(),
                networkGraphData.getReleaseIIName(),
                graph, serviceList.get(0).getFocusVertex());

        BorderPane root = new BorderPane();
        root.setTop(networkGraph.getController());
        root.setCenter(networkGraph.getGroup());
        Scene scene = new Scene(root, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}