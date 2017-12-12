//package cn.edu.cqupt.graph;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//public class GraphTest extends Application {
//	public static void main(String[] args) {
////		UndirectedGraph graph = new UndirectedGraph();
////		Vertex nodeA = new Vertex("A", 5);
////		graph.addEdge(nodeA, new Vertex("B", 7), new Edge(10));
////		graph.addEdge(new Vertex("A", 5), new Vertex("C", 10), new Edge(9));
////		graph.addEdge(new Vertex("A", 5), new Vertex("D", 3), new Edge(8));
////		graph.addEdge(new Vertex("C", 10), new Vertex("D", 2), new Edge(7));
////		graph.dfs(nodeA);
//		launch(args);
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		UndirectedGraph graph = new UndirectedGraph();
//		Vertex nodeA = new Vertex("A", 5, true);
//		graph.addEdge(nodeA, new Vertex("B", 4, false), new Edge(2));
//		graph.addEdge(new Vertex("A", 5, true), new Vertex("C", 3, false), new Edge(1));
//		graph.addEdge(new Vertex("A", 5, true), new Vertex("D", 5, false), new Edge(2));
//		graph.addEdge(new Vertex("B", 3, false), new Vertex("E", 3, true), new Edge(2));
//		graph.addEdge(new Vertex("C", 3, false), new Vertex("E", 3, true), new Edge(1));
//		graph.addEdge(new Vertex("C", 3, false), new Vertex("F", 4, false), new Edge(1));
//		graph.addEdge(new Vertex("D", 5, false), new Vertex("F", 4, true), new Edge(3));
//		
//		/****************************/
////		Vertex nodeA = new Vertex("A", 5, true);
////		Vertex nodeB = new Vertex("B", 3, false);
////		Vertex nodeC = new Vertex("C", 3, false);
////		Vertex nodeD = new Vertex("D", 5, false);
////		Vertex nodeE = new Vertex("E", 3, true);
////		Vertex nodeF = new Vertex("F", 4, true);
////		graph.addEdge(nodeA, nodeB, new Edge(2));
////		graph.addEdge(nodeA, nodeC, new Edge(1));
////		graph.addEdge(nodeA, nodeD, new Edge(2));
////		graph.addEdge(nodeB, nodeE, new Edge(2));
////		graph.addEdge(nodeC, nodeE, new Edge(1));
////		graph.addEdge(nodeC, nodeF, new Edge(1));
////		graph.addEdge(nodeD, nodeF, new Edge(3));
////		graph.dfs(nodeA);
////		graph.bfs(nodeA);
////		
////		Scene scene = new Scene(graph.networkGraph(nodeA), 500, 500);
////		primaryStage.setScene(scene);
////		primaryStage.show();
//		
//	}
//}
