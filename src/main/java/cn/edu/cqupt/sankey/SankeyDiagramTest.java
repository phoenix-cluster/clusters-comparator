package cn.edu.cqupt.sankey;

import java.io.File;
import java.util.HashMap;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.ClusterTableService;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SankeyDiagramTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// ClusterTableService serviceReleaseI = new ClusterTableService(
		// new
		// File("C:\\Users\\huangjs\\Desktop\\clustering\\mydata7\\cluster7_1.clustering"));
		// ClusterTableService serviceReleaseII = new ClusterTableService(
		// new
		// File("C:\\Users\\huangjs\\Desktop\\clustering\\mydata7\\cluster7_2.clustering"));
		// Cluster currentCluster = serviceReleaseI.getCurrentPageClusters(1,
		// 8).getDataList().get(0);
		// String releaseIName = "cluster7_1.clustering";
		// String releaseIIName = "cluster7_2.clustering";
		// NetworkGraphService networtGraphService = new
		// NetworkGraphService(currentCluster, releaseIName, releaseIIName,
		// serviceReleaseI.getAllClusters(), serviceReleaseII.getAllClusters());
		// UndirectedGraph<Vertex, Edge> graph =
		// networtGraphService.getUndirectedGraph();
		// List<Vertex > allVerticesList = new ArrayList<>();
		//
		// for(Vertex ver : graph.getAllVertices()) {
		// System.out.println(ver);
		// allVerticesList.add(ver);
		// }
		// Vertex focusVertex = allVerticesList.get(2);

		ClusterTableService serviceReleaseI = new ClusterTableService(
				new File("C:\\Users\\huangjs\\Desktop\\clustering\\cli_clustering.pxd000021.0.7_4.clustering"));
		ClusterTableService serviceReleaseII = new ClusterTableService(
				new File("C:\\Users\\huangjs\\Desktop\\clustering\\hdp_clustering.pxd000021.0.7_4.clustering"));
		Cluster currentCluster = serviceReleaseI.getCurrentPageClusters(16, 8).getDataList().get(0);

		// ClusterTableService serviceReleaseI = new ClusterTableService(
		// new File("C:\\Users\\huangjs\\Desktop\\clustering\\compare_1.clustering"));
		// ClusterTableService serviceReleaseII = new ClusterTableService(
		// new File("C:\\Users\\huangjs\\Desktop\\clustering\\compare_2.clustering"));
		// Cluster currentCluster = serviceReleaseI.getCurrentPageClusters(1,
		// 3).getDataList().get(1);

		System.out.println("currentCluster id: " + currentCluster.getId());
		String releaseIName = "cli_clustering.pxd000021.0.7_4.clustering";
		String releaseIIName = "hdp_clustering.pxd000021.0.7_4.clustering";
		NetworkGraphService networtGraphService = new NetworkGraphService(currentCluster, releaseIName, releaseIIName,
				serviceReleaseI.getAllClusters(), serviceReleaseII.getAllClusters());
		Vertex focusVertex = networtGraphService.getFocusVertex();
		UndirectedGraph<Vertex, Edge> graph = networtGraphService.getUndirectedGraph();

		// print graph data
		System.out.println("================redundant===============");
		HashMap<Vertex, HashMap<Vertex, Edge>> adj = graph.getAdjacencyTable();
		for (Vertex vertex1 : adj.keySet()) {
			System.out.println(vertex1 + "->");
			for (Vertex vertex2 : adj.get(vertex1).keySet()) {
				System.out.println("\t" + vertex2);
			}
		}
		System.out.println("=============================================");
		
		// print graph data
		System.out.println("================unredundant===============");
		HashMap<Vertex, HashMap<Vertex, Edge>> unadj = graph.getUnredundantAdjacencyTable();
		for (Vertex vertex1 : unadj.keySet()) {
			System.out.println(vertex1 + "->");
			for (Vertex vertex2 : unadj.get(vertex1).keySet()) {
				System.out.println("\t" + vertex2);
			}
		}
		System.out.println("==========================================");

		// sankey diagram
		ClusteringSankeyDiagram sankey = new ClusteringSankeyDiagram(releaseIName, releaseIIName, graph, focusVertex);
		GridPane controller = sankey.getController();
		Group group = sankey.getGroup();
		VBox vbox = new VBox(controller, new ScrollPane(group));
		// VBox vbox = new VBox(controller, group);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(vbox);
		// Scene scene = new Scene((Parent)group);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}

		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
