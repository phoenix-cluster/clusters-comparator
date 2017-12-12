package cn.edu.cqupt.graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class UndirectedGraph<V, E> {

	private HashMap<V, HashMap<V, E>> adjacencyTable; // adjacency table
	private int edgeCount; // the number of edge
	private HashMap<V, HashMap<V, E>> unredundantAdjacencyTable;
	private HashMap<V, Boolean> visited;

	public HashMap<V, HashMap<V, E>> getAdjacencyTable() {
		return adjacencyTable;
	}

	public int getEdgeCount() {
		return edgeCount;
	}

	public int getVerticesCount() {
		return adjacencyTable.keySet().size();
	}

	public Set<V> getAdjacencyVertices(V vertex) {
		return adjacencyTable.get(vertex).keySet();
	}

	public Collection<E> getAdjacencyEdge(V vertex) {
		return adjacencyTable.get(vertex).values();
	}

	public HashMap<V, E> getAdjacencyTableOfVertex(V vertex) {
		return adjacencyTable.get(vertex);
	}

	public HashMap<V, HashMap<V, E>> getUnredundantAdjacencyTable() {
		return unredundantAdjacencyTable;
	}

	public Set<V> getAllVertices() {
		return adjacencyTable.keySet();
	}

	public ArrayList<E> getAllEdges() {
		ArrayList<E> allEdges = new ArrayList<>();
		for (V vertex1 : unredundantAdjacencyTable.keySet()) {
			for (V vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
				allEdges.add(unredundantAdjacencyTable.get(vertex1).get(vertex2));
			}
		}
		return allEdges;
	}

	public UndirectedGraph() {
		super();
		this.adjacencyTable = new HashMap<>();
		this.edgeCount = 0;
		this.unredundantAdjacencyTable = new HashMap<>();
		this.visited = new HashMap<>();
	}

	/**
	 * add a edge
	 * 
	 * @param vertex1
	 * @param vertex2
	 * @param edge
	 */
	public void addEdge(V vertex1, V vertex2, E edge) {

		// add unredundant adjacency table
		if (!hasEdge(vertex1, vertex2)) {
			if (unredundantAdjacencyTable.containsKey(vertex1)) {
				unredundantAdjacencyTable.get(vertex1).put(vertex2, edge);
			} else {
				HashMap<V, E> link = new HashMap<>();
				link.put(vertex2, edge);
				unredundantAdjacencyTable.put(vertex1, link);
			}
		}

		// add edge into adjacency table: exist redundant data
		if (adjacencyTable.containsKey(vertex1)) {
			adjacencyTable.get(vertex1).put(vertex2, edge);
			// System.out.println("add method: " + vertex1 + "$" + vertex1.hashCode() + "->"
			// + vertex2 + "$" + vertex2.hashCode());

		} else {
			HashMap<V, E> link = new HashMap<>();
			link.put(vertex2, edge);
			adjacencyTable.put(vertex1, link);
			// System.out.println("connect method: " + vertex1 + "$" + vertex1.hashCode() +
			// "->" + vertex2 + "$" + vertex2.hashCode());
		}

		if (adjacencyTable.containsKey(vertex2)) {
			adjacencyTable.get(vertex2).put(vertex1, edge);
			// System.out.println("add method: " + vertex2 + "$" + vertex2.hashCode() + "->"
			// + vertex1 + "$" + vertex1.hashCode());

		} else {
			HashMap<V, E> link = new HashMap<>();
			link.put(vertex1, edge);
			adjacencyTable.put(vertex2, link);
			// System.out.println("connect method: " + vertex2 + "$" + vertex2.hashCode() +
			// "->" + vertex1 + "$" + vertex1.hashCode());
		}

		// set visited as false for traversal
		visited.put(vertex1, false);
		visited.put(vertex2, false);
		edgeCount++;
	}

	/**
	 * reset the edge between vertex1 and vertex2
	 * 
	 * @param vertex1
	 * @param vertex2
	 * @param edge
	 */
	public boolean setEdge(V vertex1, V vertex2, E edge) {
		try {
			adjacencyTable.get(vertex1).replace(vertex2, edge);
			adjacencyTable.get(vertex2).replace(vertex1, edge);
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public E getEdge(V vertex1, V vertex2) {
		return adjacencyTable.get(vertex1).get(vertex2);
	}

	public boolean hasEdge(V vertex1, V vertex2) {
		if (!adjacencyTable.containsKey(vertex1) || !adjacencyTable.containsKey(vertex2)) {
			return false;
		} else {
			return adjacencyTable.get(vertex1).get(vertex2) == null ? false : true;
		}
	}

	/**
	 * @param edge
	 * @return
	 */
	public Entry<V, V> getFirstVerticesOfEdge(E edge) {
		Entry<V, V> verticesOfEdge = null;
		if (getAllEdges().contains(edge)) {
			for (V vertex1 : unredundantAdjacencyTable.keySet()) {
				for (V vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
					if (unredundantAdjacencyTable.get(vertex1).get(vertex2).equals(edge)) {
						verticesOfEdge = new AbstractMap.SimpleEntry<V, V>(vertex1, vertex2);
						return verticesOfEdge;
					}
				}
			}
		}
		return verticesOfEdge;
	}

	/**
	 * @param edge
	 * @return
	 */
	public List<HashMap<V, V>> getAllVerticesOfEdge(E edge) {
		List<HashMap<V, V>> verticesOfEdge = null;
		if (getAllEdges().contains(edge)) {
			verticesOfEdge = new ArrayList<>();
			for (V vertex1 : unredundantAdjacencyTable.keySet()) {
				for (V vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
					if (unredundantAdjacencyTable.get(vertex1).get(vertex2).equals(edge)) {
						HashMap<V, V> vertices = new HashMap<>();
						vertices.put(vertex1, vertex2);
						verticesOfEdge.add(vertices);
					}
				}
			}
		}
		return verticesOfEdge;
	}

	/**
	 * judge whether the vertex is contained
	 * 
	 * @param vertex
	 * @return
	 */
	public boolean containsVertex(V vertex) {
		return adjacencyTable.containsKey(vertex);
	}

	// deep first search
	public void dfs(V vertex) {
		dfsRecursive(vertex);
		refresh();
	}

	private void dfsRecursive(V vertex) {
		Iterator<V> adjItr = getAdjacencyVertices(vertex).iterator();
		while (adjItr.hasNext()) {
			V adjVer = adjItr.next();
			visited.replace(vertex, true); // set the vertex visited
			System.out.println(vertex + "->" + adjVer + ": " + getEdge(vertex, adjVer));
			if (!visited.get(adjVer)) {
				dfsRecursive(adjVer);
			}
		}
	}

	// breadth first search
	public void bfs(V vertex) {
		LinkedList<V> queue = new LinkedList<>();
		visited.replace(vertex, true);
		queue.addLast(vertex);
		while (!queue.isEmpty()) {
			V tmpVer = queue.removeFirst();
			for (V adjVer : getAdjacencyVertices(tmpVer)) {
				if (!visited.get(adjVer)) {
					System.out.println(tmpVer + "->" + adjVer + ": " + getEdge(tmpVer, adjVer));
					visited.replace(adjVer, true);
					queue.addLast(adjVer);
				}
			}
		}
		refresh();
	}

	/**
	 * get the order of vertices access during breadth first search
	 * 
	 * @param vertex
	 * @return
	 */
	public ArrayList<V> getBfsVerticesOrder(V vertex) {
		ArrayList<V> bfsVerticesOrder = new ArrayList<>();

		// do bfs
		LinkedList<V> queue = new LinkedList<>();
		queue.addLast(vertex);
		visited.replace(vertex, true);
		bfsVerticesOrder.add(vertex);
		while (!queue.isEmpty()) {
			V tmpVer = queue.removeFirst();
			for (V adjVer : getAdjacencyVertices(tmpVer)) {
				if (!visited.get(adjVer)) {
					queue.addLast(adjVer);
					visited.replace(adjVer, true);
					bfsVerticesOrder.add(adjVer);
				}
			}
		}
		refresh();
		return bfsVerticesOrder;
	}

	/**
	 * reset all vertices to no visit
	 */
	public void refresh() {
		for (V tmpVer : adjacencyTable.keySet()) {
			visited.replace(tmpVer, false);
		}
	}

	public void printAdjacencyTable() {
		for (V tmpVer : adjacencyTable.keySet()) {
			System.out.print(tmpVer);
			for (V adjVer : adjacencyTable.get(tmpVer).keySet()) {
				System.out.print(" - " + adjVer + ": " + getEdge(tmpVer, adjVer));
			}
			System.out.println();
		}
	}

	public void printUnredundantAdjacencyTable() {
		for (V tmpVer : unredundantAdjacencyTable.keySet()) {
			System.out.print(tmpVer);
			for (V adjVer : unredundantAdjacencyTable.get(tmpVer).keySet()) {
				System.out.print(" - " + adjVer + ": " + getEdge(tmpVer, adjVer));
			}
			System.out.println();
		}
	}
}

// class Link<V, E> {
// private V vertex;
// private E edge;
//
// public V getVertex() {
// return vertex;
// }
//
// public E getEdge() {
// return edge;
// }
//
// public Link(V vertex, E edge) {
// super();
// this.vertex = vertex;
// this.edge = edge;
// }
// }
