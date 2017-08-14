package cn.edu.cqupt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * @author huangjs
 * find all clusters connected with the cluster
 */
public class ClusterComparerService {

	private final Cluster cluster;
	private final List<Cluster> releaseI;
	private final List<Cluster> releaseII;
	private UndirectedSparseGraph<Vertex, Edge> graph;
	private ArrayList<List<Spectrum>> visited;
	private HashMap<String, Vertex> node;

	public UndirectedSparseGraph<Vertex, Edge> getGraph() {
		return graph;
	}

	public ClusterComparerService(Cluster cluster, List<Cluster> releaseI, List<Cluster> releaseII)
			throws CloneNotSupportedException {
		super();
		this.cluster = cluster;
		this.releaseI = releaseI;
		this.releaseII = releaseII;
		this.graph = new UndirectedSparseGraph<>();
		this.visited = new ArrayList<>();
		this.node = new HashMap<>();
		doCompare(this.cluster, this.cluster.getSpectra(), 2);
	}

	/**
	 * compare sourceSpectrums and spectrums of cluster in releaseI or releaseII
	 * 
	 * @param sourceSpectrums
	 * @param releaseNum
	 *            release number(1 or 2)
	 * @return
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public void doCompare(Cluster sourceCluster, List<Spectrum> sourceSpectra, int releaseNum)
			throws CloneNotSupportedException {
		List<Cluster> release = null;
		if (releaseNum == 1) {
			release = releaseI;
			releaseNum = 2;
		} else {
			release = releaseII;
			releaseNum = 1;
		}
		for (Cluster tmpCluster : release) {
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
					if (visited.indexOf(tmpSpectra1) == -1) {
						System.out.println(sourceCluster.getId() + " vs " + tmpCluster.getId());
						System.out.println(sourceCluster + " vs " + tmpCluster);
						
						Vertex vertex1, vertex2;
						if(node.containsKey(sourceCluster.getId()))
							 vertex1 = node.get(sourceCluster.getId());
						else {
							vertex1 = new Vertex(sourceCluster);
							node.put(sourceCluster.getId(), vertex1);
						}
						if(node.containsKey(tmpCluster.getId()))
							 vertex2 = node.get(tmpCluster.getId());
						else {
							vertex2 = new Vertex(tmpCluster);
							node.put(tmpCluster.getId(), vertex2);
						}
						
						graph.addEdge(new Edge(tmpSpectra1), vertex1, vertex2);
						visited.add(tmpSpectra1);
						
						doCompare(tmpCluster, tmpSpectra2, releaseNum);
					}
				} else { // child spectrums belong to parent spectrums: leaf node
					Vertex vertex1 = new Vertex(sourceCluster);
					Vertex vertex2 = new Vertex(tmpCluster);
					graph.addEdge(new Edge(tmpSpectra1), vertex1, vertex2);
					System.out.println(sourceCluster.getId() + " vs " + tmpCluster.getId());
					visited.add(tmpSpectra1);
				}
			}
		}
	}

//	public static void main(String[] args) {
//		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_5.clustering")).findAllClusters();
//		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_6.clustering")).findAllClusters();
//		Cluster cluster = releaseI.get(0);
//		ClusterComparerService clusterComparer = null;
//		try {
//			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//	}
}
