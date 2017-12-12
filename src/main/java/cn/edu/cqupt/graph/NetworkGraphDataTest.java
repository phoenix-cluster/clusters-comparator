//package cn.edu.cqupt.graph;
//
//import java.util.ArrayList;
//
//import cn.edu.cqupt.model.Vertex;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//public class NetworkGraphDataTest extends Application{
//
//	@Override
//	public void start(Stage primaryStage) {
//
//		NetworkGraphData data = new NetworkGraphData("releaseI", "releaseII");
//		ArrayList<String> spectra = new ArrayList<>();
//		spectra.add("1");
//		spectra.add("2");
//		data.createNetworkGraphData("1.1", spectra, true);
//		Vertex vertex = data.createVertex("releaseI", "1.1", true);
//		UndirectedGraph graph = data.getGraph();
//		graph.printAdjacencyTable();
//		PaintGraph paint = new PaintGraph();
//		GridPane pane = paint.networkGraph(graph.getAdjacencyTable(), vertex);
//
//		/*********************/
//		Scene scene = new Scene(pane);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
