package cn.edu.cqupt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.view.ClusterApplication;

/**
 * @author huangjs find all clusters connected with the cluster
 */
public class NetworkGraphService{

	private final Cluster cluster;
	private final String releaseIName;
	private final String releaseIIName;
	private final List<Cluster> releaseI;
	private final List<Cluster> releaseII;
	private UndirectedGraph<Vertex, Edge> undirectedGraph;
	private ArrayList<List<Spectrum>> visitedEdges;
	private HashMap<String, Vertex> visitedVertices;
	private  Vertex focusVertex;

	public String getReleaseIName() {
		return releaseIName;
	}

	public String getReleaseIIName() {
		return releaseIIName;
	}

	public List<Cluster> getReleaseI() {
		return releaseI;
	}

	public List<Cluster> getReleaseII() {
		return releaseII;
	}

	public Vertex getFocusVertex() {
		return focusVertex;
	}

	public UndirectedGraph<Vertex, Edge> getUndirectedGraph() {
		return undirectedGraph;
	}

	public NetworkGraphService(Cluster cluster, String releaseIName, String releaseIIName, List<Cluster> releaseI, List<Cluster> releaseII)
			throws CloneNotSupportedException {
		super();
		this.cluster = cluster;
		this.releaseIName = releaseIName;
		this.releaseIIName = releaseIIName;
		this.releaseI = releaseI;
		this.releaseII = releaseII;
		this.undirectedGraph = new UndirectedGraph<Vertex, Edge>();
		this.visitedEdges = new ArrayList<>();
		this.visitedVertices = new HashMap<>();
		focusVertex = new Vertex(this.releaseIName, this.cluster, this.cluster.getSpecCount());
		doCompare(this.cluster, this.releaseIName, this.cluster.getSpectra());
		
	}


	/**
	 * compare source cluster's spectra and spectra of cluster in releaseI or releaseII
	 * @param releaseName sourceCluster's release name
	 * @param sourceCluster
	 * @param sourceSpectra
	 * @throws CloneNotSupportedException
	 */
	public void doCompare(Cluster sourceCluster, String sourceReleaseName, List<Spectrum> sourceSpectra)
			throws CloneNotSupportedException {
		List<Cluster> objectRelease = null;
		String objectReleaseName = null;
		if (sourceReleaseName.equals(releaseIName)) {
			objectRelease = releaseII;
			objectReleaseName = releaseIIName;
		} else {
			objectRelease = releaseI;
			objectReleaseName = releaseIName;
		}
		for (Cluster tmpCluster : objectRelease) {
			Cluster clusterClone1 = tmpCluster.clone();
			Cluster clusterClone2 = tmpCluster.clone();
			List<Spectrum> tmpSpectra1 = clusterClone1.getSpectra(); // get spectrums of cluster in release
			List<Spectrum> tmpSpectra2 = clusterClone2.getSpectra(); // get spectrums clone
			tmpSpectra1.retainAll(sourceSpectra); // get overlap spectrum
			if (tmpSpectra1.size() > 0) { // exist overlap

				// get different spectrums between child spectrums and overlap spectrum
				tmpSpectra2.removeAll(tmpSpectra1);

				/*
				 * true indicate child spectrums exist spectrums not belong to parent spectrums:
				 * the node is middle node no leaf node
				 */
				if (tmpSpectra2.size() > 0) {

					if (visitedEdges.indexOf(tmpSpectra1) == -1) {
						Vertex vertex1, vertex2;
						if (visitedVertices.containsKey(sourceCluster.getId()))
							vertex1 = visitedVertices.get(sourceCluster.getId());
						else {
							vertex1 = new Vertex(sourceReleaseName, sourceCluster, sourceCluster.getSpecCount());
							visitedVertices.put(sourceCluster.getId(), vertex1);
						}
						if (visitedVertices.containsKey(tmpCluster.getId()))
							vertex2 = visitedVertices.get(tmpCluster.getId());
						else {
							vertex2 = new Vertex(objectReleaseName, tmpCluster, tmpCluster.getSpecCount());
							visitedVertices.put(tmpCluster.getId(), vertex2);
						}
						undirectedGraph.addEdge(vertex1, vertex2, new Edge(tmpSpectra1, tmpSpectra1.size()));
						visitedEdges.add(tmpSpectra1);

						doCompare(tmpCluster, objectReleaseName, tmpSpectra2);
					}
				} else { // child spectrums belong to parent spectrums: leaf node
					Vertex vertex1 = new Vertex(sourceReleaseName, sourceCluster, sourceCluster.getSpecCount());
					Vertex vertex2 = new Vertex(objectReleaseName, tmpCluster, tmpCluster.getSpecCount());
					undirectedGraph.addEdge(vertex1, vertex2, new Edge(tmpSpectra1, tmpSpectra1.size()));
					visitedEdges.add(tmpSpectra1);
				}
			}
		}
	}
}
