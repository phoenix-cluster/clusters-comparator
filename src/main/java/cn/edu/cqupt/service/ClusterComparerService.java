package cn.edu.cqupt.service;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.dao.ClusterDaoFileImpl;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.OverlapCluster;
import cn.edu.cqupt.model.OverlapClusterComponent;
import cn.edu.cqupt.model.OverlapClusterComposite;
import cn.edu.cqupt.model.Spectrum;

public class ClusterComparerService {

	private OverlapClusterComponent overlapClusterComponent;
	private final Cluster cluster;
	private final List<Cluster> releaseI;
	private final List<Cluster> releaseII;
	private boolean first = true;

	public OverlapClusterComponent getOverlapClusterComponent() {
		return overlapClusterComponent;
	}

	public ClusterComparerService(Cluster cluster, List<Cluster> releaseI, List<Cluster> releaseII)
			throws CloneNotSupportedException {
		super();
		this.cluster = cluster;
		this.releaseI = releaseI;
		this.releaseII = releaseII;

		this.overlapClusterComponent = new OverlapClusterComposite(this.cluster, this.cluster.getSpectrums());
		OverlapClusterComponent middleNode = (OverlapClusterComponent) doCompare(this.cluster.getSpectrums(), 2);
		this.overlapClusterComponent.add(middleNode);
	}

	public OverlapClusterComponent doCompare(List<Spectrum> sourceSpectrums, int releaseNum)
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
			List<Spectrum> tmpSpectrums1 = clusterClone1.getSpectrums(); // get spectrums of release cluster
			List<Spectrum> tmpSpectrums2 = clusterClone2.getSpectrums(); // get spectrums clone
			tmpSpectrums1.retainAll(sourceSpectrums); // get overlap spectrum
			if (tmpSpectrums1.size() > 0) { // exist overlap
				// System.out.println("******sourceSpectrums****************");
				// printSpectrums(sourceSpectrums);
				// System.out.println("************tmpCluster.getSpectrums()***********");
				// printSpectrums(tmpCluster.getSpectrums());
				// System.out.println("************tmpSpectrums1***********");
				// printSpectrums(tmpSpectrums1);
				// System.out.println("***********************");
				// get different spectrums between child spectrums and overlap spectrum
				tmpSpectrums2.removeAll(tmpSpectrums1);
				if (tmpSpectrums2.size() > 0) { // child spectrums exist spectrums not belong to parent spectrums
					OverlapClusterComposite middleNode = new OverlapClusterComposite(tmpCluster, tmpSpectrums1); // node
					OverlapClusterComponent node = doCompare(tmpSpectrums2, releaseNum); // moddle node or leaf node
					middleNode.add(node);
					return middleNode;
					// System.out.println(tmpCluster.getId() + ": ");
					// printSpectrums(tmpSpectrums1);
					// return middleNode;
				} else { // child spectrums belong to parent spectrums: leaf node
					if (!first) {
						return new OverlapCluster(tmpCluster, tmpSpectrums1);
					}
					else {
						this.overlapClusterComponent.add(new OverlapCluster(tmpCluster, tmpSpectrums1));
						first = false;
					}
					// System.out.println(tmpCluster.getId() + ": ");
					// printSpectrums(tmpSpectrums1);
				}
			}
		}
		return null;
	}

	public void printSpectrums(List<Spectrum> spectrums) {
		for (Spectrum tmp : spectrums) {
			System.out.println(tmp.getSpectrumId());
		}
	}

	public static void main(String[] args) {

//		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_1.clustering")).findAllClusters();
//		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_2.clustering")).findAllClusters();
		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_4.clustering")).findAllClusters();
		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_3.clustering")).findAllClusters();
		Cluster cluster = releaseI.get(0);
		ClusterComparerService clusterComparer = null;
		try {
			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		 clusterComparer.getOverlapClusterComponent().print();
	}
}
