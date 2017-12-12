package cn.edu.cqupt.graph;

public class UndirectedGraphTest {
	public static void main(String[] args) {
		UndirectedGraph<String, Integer> graph = new UndirectedGraph<>();
		graph.addEdge("A", "B", 10);
		graph.addEdge("A", "C", 1);
		graph.addEdge("C", "B", 12);
		graph.addEdge("A", "D", 2);
		graph.addEdge("A", "K", 10);
		graph.addEdge("B", "M", 3);
		graph.addEdge("C", "M", 5);
		System.out.println("*************dfs*********************");
		graph.dfs("A");

		System.out.println("*************bfs*********************");
		graph.bfs("A");

		System.out.println("*************printAdjacencyTable*********************");
		graph.printAdjacencyTable();

		System.out.println("*************printUnredundantAdjacencyTable*********************");
		graph.printUnredundantAdjacencyTable();

		System.out.println("*************getEdgeCount*********************");
		System.out.println(graph.getEdgeCount());

		System.out.println("*************getEdge*********************");
		System.out.println(graph.getEdge("A", "B"));
		System.out.println(graph.getEdge("A", "E"));

		System.out.println("*************setEdge*********************");
		System.out.println("before : " + graph.getEdge("A", "B"));
		boolean flag = graph.setEdge("A", "B", 1000);
		System.out.println("successd: " + flag);
		System.out.println("after : " + graph.getEdge("A", "B"));

		flag = graph.setEdge("A", "E", 1000);
		System.out.println("successd: " + flag);

		System.out.println("*************getVerticesOfEdge*********************");
		System.out.println(graph.getAllVerticesOfEdge(1000));
		System.out.println(graph.getAllVerticesOfEdge(10));
		System.out.println(graph.getFirstVerticesOfEdge(10));
		
		System.out.println("*************getBfsVerticesOrder*********************");
		System.out.println(graph.getBfsVerticesOrder("A"));
		
		System.out.println("*************dfs*********************");

	}
}
