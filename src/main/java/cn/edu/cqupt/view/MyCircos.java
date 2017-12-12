//package cn.edu.cqupt.view;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.edu.cqupt.graph.UndirectedGraph;
//import cn.edu.cqupt.graph.circos.gui.Trials;
//import cn.edu.cqupt.graph.circos.widget.Circos;
//import cn.edu.cqupt.graph.circos.widget.Link;
//import cn.edu.cqupt.graph.circos.widget.UnconsistentDataException;
//import cn.edu.cqupt.graph.circos.widget.eventHandlers.ArcEventHandler;
//import cn.edu.cqupt.graph.circos.widget.eventHandlers.LinkEventHandler;
//import cn.edu.cqupt.model.Cluster;
//import cn.edu.cqupt.model.Edge;
//import cn.edu.cqupt.model.Vertex;
//import cn.edu.cqupt.service.NetworkGraphService;
//import javafx.scene.control.Tab;
//
//public class MyCircos {
//
//	public MyCircos(Cluster cluster, List<Cluster> releaseI, List<Cluster> releaseII) {
//		NetworkGraphService networtGraphService = null;
//		try {
//			networtGraphService = new NetworkGraphService(cluster, releaseI, releaseII);
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//
//		// obtain network graph(data)
//		UndirectedGraph<Vertex, Edge> undirectedGraph = networtGraphService.getUndirectedGraph();
//		long[] data = new long[undirectedGraph.getAllVertices().size()];
//		ArrayList<Vertex> allVertices = new ArrayList<>();
//		List<Link> linkList = new ArrayList<>();
//
//		// vertex
//		int i = 0;
//		for (Vertex vertex : undirectedGraph.getAllVertices()) {
//			allVertices.add(vertex);
//			data[i++] = (long) vertex.getWeight();
//		}
//
//		// path
//		for (Vertex vertex1 : undirectedGraph.getUnredundantAdjacencyTable().keySet()) {
//			for (Vertex vertex2 : undirectedGraph.getUnredundantAdjacencyTable().get(vertex1).keySet()) {
//				int index1 = allVertices.indexOf(vertex1);
//				int index2 = allVertices.indexOf(vertex2);
//				Link link = new Link(1, 1, 1, 1, index1, index2, 0);
//				linkList.add(link);
//			}
//		}
//		Circos circos = new Circos(data, new ArcEventHandler(), new LinkEventHandler());
//		circos.setStrokeWidth(2);
//		circos.initialize();
//		try {
//			circos.addLink(linkList);
//		} catch (UnconsistentDataException e) {
//			e.printStackTrace();
//		}
//		Trials trails = new Trials(circos);
//
//		Tab tabC = null;
//		tabC = new Tab("Circos", trails);
//		ClusterApplication.tabPane.getTabs().add(tabC);
//		tabC = ClusterApplication.tabPane.getTabs().get(1);
//
//	}
//
//}
