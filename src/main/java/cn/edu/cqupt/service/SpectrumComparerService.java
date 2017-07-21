package cn.edu.cqupt.service;

import java.util.HashMap;
import java.util.List;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;

public class SpectrumComparerService {

	// key: cluster's id existed overlap; value: the count of overlap spectrum
	private HashMap<String, Integer> overlapSpectrumCount; 
	// key: cluster's id existed overlap; value: cluster existed overlap
	private HashMap<String, Cluster> overlapCluster; 
	// key: cluster's id existed overlap; value: overlap spectrums of corresponding cluster, intersection of two clusters
	private HashMap<String, List<Spectrum>> overlapSpectrumOfCluster; // key: a cluster id of releaseCluster, value:
																		// overlap cluster's spectrums

	public HashMap<String, Integer> getOverlapSpectrumCount() {
		return overlapSpectrumCount;
	}

	public void setOverlapSpectrumCount(HashMap<String, Integer> overlapSpectrumCount) {
		this.overlapSpectrumCount = overlapSpectrumCount;
	}

	public HashMap<String, Cluster> getOverlapCluster() {
		return overlapCluster;
	}

	public void setOverlapCluster(HashMap<String, Cluster> overlapCluster) {
		this.overlapCluster = overlapCluster;
	}

	public HashMap<String, List<Spectrum>> getOverlapSpectrumOfCluster() {
		return overlapSpectrumOfCluster;
	}

	public void setOverlapSpectrumOfCluster(HashMap<String, List<Spectrum>> overlapSpectrumOfCluster) {
		this.overlapSpectrumOfCluster = overlapSpectrumOfCluster;
	}

	public SpectrumComparerService() {

	}

	/**
	 * @param cluster
	 *            a cluster needed to find overlap
	 * @param releaseCluster
	 *            all Clusters of a release
	 * @throws CloneNotSupportedException
	 */
	
	public SpectrumComparerService(Cluster cluster, List<Cluster> releaseCluster) throws CloneNotSupportedException {
		doCompare(cluster, releaseCluster);
	}
	
	public void doCompare(Cluster cluster, List<Cluster> releaseCluster) throws CloneNotSupportedException {
		this.overlapCluster = new HashMap<>();
		this.overlapSpectrumOfCluster = new HashMap<>();
		this.overlapSpectrumCount = new HashMap<>();

		List<Spectrum> sourceSpectrums = cluster.getSpectrums();
		for (Cluster tmpCluster : releaseCluster) {

			// releaseCluster will change if directly use tmpCluster: there need to override
			// Cluster.clone()
			Cluster cloneCluster = tmpCluster.clone();
			List<Spectrum> tmpSpectrums = cloneCluster.getSpectrums();

			// get intersection of two different cluster: there need override
			// Spectrum.equals()
			tmpSpectrums.retainAll(sourceSpectrums);

			// only store the cluster existed overlap
			if (tmpSpectrums.size() > 0) {
				this.overlapCluster.put(tmpCluster.getId(), tmpCluster);
				this.overlapSpectrumOfCluster.put(tmpCluster.getId(), tmpSpectrums);
				this.overlapSpectrumCount.put(tmpCluster.getId(), tmpSpectrums.size());
			}
		}
	}
}
