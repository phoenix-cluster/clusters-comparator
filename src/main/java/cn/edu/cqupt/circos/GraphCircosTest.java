package cn.edu.cqupt.circos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.ClusterTableService;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GraphCircosTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClusterTableService serviceReleaseI = new ClusterTableService(
				new File("C:\\Users\\huangjs\\Desktop\\clustering\\cli_clustering.pxd000021.0.7_4.clustering"));
		ClusterTableService serviceReleaseII = new ClusterTableService(
				new File("C:\\Users\\huangjs\\Desktop\\clustering\\hdp_clustering.pxd000021.0.7_4.clustering"));
		Cluster currentCluster = serviceReleaseI.getCurrentPageClusters(48, 16).getDataList().get(0);
		System.out.println("currentCluster id: " + currentCluster.getId());
		String releaseIName = "cli_clustering.pxd000021.0.7_4.clustering";
		String releaseIIName = "hdp_clustering.pxd000021.0.7_4.clustering";
		NetworkGraphService networtGraphService = new NetworkGraphService(currentCluster, releaseIName, releaseIIName,
				serviceReleaseI.getAllClusters(), serviceReleaseII.getAllClusters());
		UndirectedGraph<Vertex, Edge> graph = networtGraphService.getUndirectedGraph();
		HashMap<Vertex, HashMap<Vertex, Edge>> unredundantAdjacencyTable = graph.getUnredundantAdjacencyTable();

		List<Vertex> releaseIVertices = new ArrayList<>();
		List<Vertex> releaseIIVertices = new ArrayList<>();
		List<Double> releaseIVerticesValue = new ArrayList<>();
		List<Double> releaseIIVerticesValue = new ArrayList<>();
		for (Vertex vertex : graph.getAllVertices()) {
			if (vertex.getReleaseName().equals(releaseIName)) {
				releaseIVertices.add(vertex);
				releaseIVerticesValue.add(vertex.getWeight());
			} else {
				releaseIIVertices.add(vertex);
				releaseIIVerticesValue.add(vertex.getWeight());
			}
		}

		Circos circos = new Circos(releaseIVerticesValue, releaseIIVerticesValue);

		for (Vertex vertex1 : unredundantAdjacencyTable.keySet()) {
			int fromIndex = 0, toIndex = 0;
			if (vertex1.getReleaseName().equals(releaseIName)) {
				fromIndex = releaseIVertices.indexOf(vertex1);
			} else {
				toIndex = releaseIIVertices.indexOf(vertex1);
			}
			for (Vertex vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
				if (vertex2.getReleaseName().equals(releaseIName)) {
					fromIndex = releaseIVertices.indexOf(vertex2);
				} else {
					toIndex = releaseIIVertices.indexOf(vertex2);
				}
				System.out.println(fromIndex + " -> " + toIndex);
				circos.createPath(fromIndex, toIndex, graph.getEdge(vertex1, vertex2).getWeight());
			}
		}

		Group group = circos.getGroup();
		StackPane stackPane = new StackPane(new ScrollPane(group));
		Scene scene = new Scene(stackPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
