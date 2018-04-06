package cn.edu.cqupt.service;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.view.ClusterSelection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

public class NetworkGraphServiceTest {
    public static void main(String[] args) {
        ClusterTableService serviceReleaseI = new ClusterTableService(new
                File("C:\\Users\\huangjs\\Desktop\\compare\\compare_5.clustering"));
        ClusterTableService serviceReleaseII = new ClusterTableService(new
                File("C:\\Users\\huangjs\\Desktop\\compare\\compare_6.clustering"));
        String releaseIName = "compare_5.clustering";
        String releaseIIName = "compare_6.clustering";
        Cluster cluster = serviceReleaseI.getCurrentPageClusters(1, 8)
                .getDataList().get(0);
        NetworkGraphService nwgs = null;
        try {
            nwgs = new NetworkGraphService(cluster, releaseIName, releaseIIName,
                    serviceReleaseI.getAllClusters(), serviceReleaseII.getAllClusters());
        } catch (Exception e) {

        }
        UndirectedGraph<Vertex, Edge> graph = nwgs.getUndirectedGraph();

        // gson
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        Type type = new TypeToken<UndirectedGraph<Vertex, Edge>>() {
//        }.getType();
//        String graphStr = gson.toJson(graph, type);
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String graphStr = gson.toJson(graph);
        System.out.println(graphStr);
    }
}

