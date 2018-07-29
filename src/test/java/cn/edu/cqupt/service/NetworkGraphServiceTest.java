package cn.edu.cqupt.service;


import cn.edu.cqupt.clustering.ClusteringFileHandler;
import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;

import java.io.File;
import java.util.List;
import java.util.Map;

public class NetworkGraphServiceTest {
    public static void main(String[] args) {
        // organize data
        String releaseIName = "compare_5.clustering";
        String releaseIIName = "compare_6.clustering";

        List<Cluster> releaseI = null;

        List<Cluster> releaseII = null;
        try {
            releaseI = ClusteringFileHandler.getAllClusters(
                    new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_5.clustering"));
            releaseII = ClusteringFileHandler.getAllClusters(
                    new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_6.clustering"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cluster cluster = releaseI.get(0);

        NetworkGraphService ngs = null;
        try {
            ngs = new NetworkGraphService(cluster, releaseIName, releaseIIName,
                    releaseI, releaseII);
        } catch (Exception e) {

        }
        UndirectedGraph<Vertex, Edge> graph = ngs.getUndirectedGraph();
        Vertex focusVertex = ngs.getFocusVertex();

        System.out.println(focusVertex.getCluster().getId());
        for (Map.Entry<Vertex, Edge> link : graph.getAdjacencyTableOfVertex(focusVertex).entrySet()) {
            Vertex vertex = link.getKey();
            Edge edge = link.getValue();
            String id = vertex.getCluster().getId();
            System.out.println("->" + id + ": " + edge.getWeight());
        }

    }
}