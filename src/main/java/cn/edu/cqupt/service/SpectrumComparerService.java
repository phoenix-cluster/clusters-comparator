package cn.edu.cqupt.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;

public class SpectrumComparerService {

	private Cluster cluster; // a cluster needed to find overlap
	private List<Cluster> releaseCluster; // all Clusters of a release
	private HashMap<Cluster, List<Spectrum>> intersection; // key: a cluster of releaseCluster, value: overlap spectrums
	private HashMap<Cluster, Float> rate; // key: a cluster of releaseCluster, value: the rate of overlap spectrums

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public List<Cluster> getReleaseCluster() {
		return releaseCluster;
	}

	public void setReleaseCluster(List<Cluster> releaseCluster) {
		this.releaseCluster = releaseCluster;
	}

	public HashMap<Cluster, List<Spectrum>> getIntersection() {
		return intersection;
	}

	public void setIntersection(HashMap<Cluster, List<Spectrum>> intersection) {
		this.intersection = intersection;
	}

	public HashMap<Cluster, Float> getRate() {
		return rate;
	}

	public void setRate(HashMap<Cluster, Float> rate) {
		this.rate = rate;
	}

	public SpectrumComparerService() {

	}

	public SpectrumComparerService(Cluster cluster, List<Cluster> releaseCluster) {
		super();
		this.cluster = cluster;
		this.releaseCluster = releaseCluster;
		doCompare();
	}

	public void doCompare() {
		this.intersection = new HashMap<>();
		this.rate = new HashMap<>();
		List<Spectrum> sourceSpectrums = this.cluster.getSpectrums();
		for (Cluster tmpCluster : this.releaseCluster) {
			List<Spectrum> tmpSpectrums = tmpCluster.getSpectrums();

			// get intersection of two different cluster: there need override
			// Spectrum.equals()
			tmpSpectrums.retainAll(sourceSpectrums);

			// only store the cluster existed overlap
			if (tmpSpectrums.size() > 0) {
				this.intersection.put(tmpCluster, tmpSpectrums);
				this.rate.put(tmpCluster, (float) tmpSpectrums.size() / sourceSpectrums.size());
			}
		}
	}

	public void printIntersection() {
		Iterator<Entry<Cluster, List<Spectrum>>> itr = intersection.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Cluster, List<Spectrum>> entry = itr.next();
			System.out.println(entry.getKey().getId() + "->");
			Iterator<Spectrum> specItr = entry.getValue().iterator();
			while (specItr.hasNext()) {
				System.out.println(specItr.next().toString());
			}
		}
	}

	public void printRate() {
		Iterator<Entry<Cluster, Float>> itr = rate.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<Cluster, Float> entry = itr.next();
			System.out.println(entry.getKey().getId() + "->" + entry.getValue());
		}
	}


	public static void main(String[] args) {
		ClusterTableService releaseI = new ClusterTableService("./compare/cli_clustering.pxd000021.0.7_4.clustering");
		ClusterTableService releaseII = new ClusterTableService("./compare/hdp_clustering.pxd000021.0.7_4.clustering");

		// ClusterTableService releaseI = new
		// ClusterTableService("./compare/compare_1.clustering");
		// ClusterTableService releaseII = new
		// ClusterTableService("./compare/compare_2.clustering");

		Cluster cluster = releaseI.getAllClusters().get(0);
		List<Cluster> releaseCluster = releaseII.getAllClusters();

		SpectrumComparerService spectrumComparerService = new SpectrumComparerService(cluster, releaseCluster);
		spectrumComparerService.printIntersection();
		spectrumComparerService.printRate();
	}
}
