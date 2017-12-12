package cn.edu.cqupt.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;

public class NetworkGraphData {

	private UndirectedGraph<Vertex, Edge> graph;
	private final String releaseIName;
	private final String releaseIIName;
	private ArrayList<Edge> visitedEdges;

	public UndirectedGraph<Vertex, Edge> getGraph() {
		return graph;
	}

	// public NetworkGraph() {
	// super();
	// this.graph = new UndirectedGraph();
	// }

	public NetworkGraphData(String searchClusterId, String releaseIName, String releaseIIName) {
		this.releaseIName = releaseIName;
		this.releaseIIName = releaseIIName;
		this.graph = new UndirectedGraph<>();
		this.visitedEdges = new ArrayList<>();
		createNetworkGraphData(releaseIName, searchClusterId, getSpectraBelongToCluster(searchClusterId));
	}
	
	/**
	 * read data from database get the cluster id that contains the spectrum
	 * 
	 * @param releaseName
	 * @param specId
	 *            the spectrum id
	 * @return
	 */
	private String getClusterIdBySpecId(String releaseName, String specId) {
		String clusterId = null;

		/************************************/
		HashMap<String, String> specToClu = new HashMap<>();
		if (releaseName.equals("releaseI")) {
			specToClu.put("1", "1.1");
			specToClu.put("2", "1.1");
			specToClu.put("3", "1.2");
			specToClu.put("7", "1.2");
			specToClu.put("4", "1.3");
			specToClu.put("5", "1.3");
			specToClu.put("6", "1.3");
		} else {
			specToClu.put("5", "2.1");
			specToClu.put("6", "2.1");
			specToClu.put("7", "2.1");
			specToClu.put("2", "2.2");
			specToClu.put("3", "2.2");
			specToClu.put("4", "2.2");
			specToClu.put("1", "2.3");

		}
		clusterId = specToClu.get(specId);
		/************************************/
		return clusterId;
	}

	/**
	 * get cluster message from database and assemble Cluster object
	 * 
	 * @param releaseName
	 * @param clusterId
	 * @return
	 */
	private Cluster getClusterById(String releaseName, String clusterId) {
		Cluster cluster = new Cluster();

		/************************************/
		Random random = new Random();
		cluster.setId(clusterId);
		cluster.setSpecCount(random.nextInt(10) + 10);
		/************************************/

		return cluster;
	}

	/**
	 * get spectrum message from database and assemble Spectrum object
	 * 
	 * @param releaseName
	 * @param clusterId
	 * @return
	 */
	private Spectrum getSpectrumById(String spectrumId) {
		Spectrum spectrum = new Spectrum();

		/************************************/
		spectrum.setId(spectrumId);
		/************************************/

		return spectrum;
	}

	/**
	 * read data from database and get all spectra id that the cluster contains
	 * 
	 * @param clusterId
	 * @return
	 */
	private ArrayList<String> getSpectraBelongToCluster(String clusterId) {
		ArrayList<String> spectra = new ArrayList<>();
		/************************************/
		HashMap<String, String[]> cluToSpec = new HashMap<>();
		cluToSpec.put("1.1", new String[] { "1", "2" });
		cluToSpec.put("1.2", new String[] { "3", "7" });
		cluToSpec.put("1.3", new String[] { "4", "5", "6" });
		cluToSpec.put("2.1", new String[] { "5", "6", "7" });
		cluToSpec.put("2.2", new String[] { "2", "3", "4" });
		cluToSpec.put("2.3", new String[] { "1" });

		for (String str : cluToSpec.get(clusterId)) {
			spectra.add(str);
		}
		
		/************************************/
		return spectra;
	}

	/**
	 * @param spectraId
	 * @param releaseName
	 * @return
	 */
	private HashMap<String, ArrayList<String>> getOverlapCluster(String releaseName, List<String> spectraId) {

		// key: overlap cluster id; value: overlap spectra
		HashMap<String, ArrayList<String>> overlapCluster = new HashMap<>();
		for (String specId : spectraId) {
			String clusterId = getClusterIdBySpecId(releaseName, specId);
			if (overlapCluster.containsKey(clusterId)) {
				overlapCluster.get(clusterId).add(specId);
			} else {
				ArrayList<String> overlapSpec = new ArrayList<String>();
				overlapSpec.add(specId);
				overlapCluster.put(clusterId, overlapSpec);
			}
		}

		return overlapCluster;
	}

	/**
	 * @param releaseName
	 * @param clusterId
	 * @return
	 */
	public Vertex createVertex(String releaseName, String clusterId) {

		// create a Cluster object
		Cluster cluster = getClusterById(releaseName, clusterId);
		return new Vertex(releaseName, cluster, cluster.getSpecCount());
	}

	public Edge createEdge(ArrayList<String> overlapSpecId) {
		List<Spectrum> overlapSpectra = new ArrayList<>();
		for (String specId : overlapSpecId) {
			overlapSpectra.add(getSpectrumById(specId));
		}
		return new Edge(overlapSpectra, overlapSpectra.size());
	}

	/**
	 * @param sourceReleaseName
	 * @param sourceClusterId
	 * @param spectraId
	 */
	public void createNetworkGraphData(String sourceReleaseName, String sourceClusterId, List<String> spectraId) {
		String releaseName = null;
		if ( sourceReleaseName.equals(releaseIName)) { // 
			releaseName = this.releaseIIName;
		} else {
			releaseName = this.releaseIName;
		}

		// get clusterId => overlap spectra
		HashMap<String, ArrayList<String>> overlapCluster = getOverlapCluster(releaseName, spectraId);

		for (String objectClusterId : overlapCluster.keySet()) {

			if (!visitedEdges.contains(createEdge(overlapCluster.get(objectClusterId)))) {

				// add edge
				Vertex vertex1 = createVertex(sourceReleaseName, sourceClusterId);
				Vertex vertex2 = createVertex(releaseName, objectClusterId);
				Edge edge = createEdge(overlapCluster.get(objectClusterId));
				graph.addEdge(vertex1, vertex2, edge);

				// set vertices visited
				visitedEdges.add(edge);

				// delete visited spectra
				ArrayList<String> spectraBelongToCluster = getSpectraBelongToCluster(objectClusterId);
				spectraBelongToCluster.removeAll(overlapCluster.get(objectClusterId));

				// recursion
				if (spectraBelongToCluster.size() != 0) {
					createNetworkGraphData(releaseName, objectClusterId, spectraBelongToCluster);
				}

			}
		}
	}
}