package cn.edu.cqupt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cqupt.dao.ClusterDaoFileImpl;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.OverlapCluster;
import cn.edu.cqupt.model.OverlapClusterComponent;
import cn.edu.cqupt.model.OverlapClusterComposite;
import cn.edu.cqupt.model.Spectrum;

public class ClusterComparerService {

	private OverlapClusterComponent root;
	private final Cluster cluster;
	private final List<Cluster> releaseI;
	private final List<Cluster> releaseII;
	// key: cluster id, value: tree node, to store cluster existed tree
	private List<List<Spectrum>> existedItems;

	public OverlapClusterComponent getRoot() {
		return root;
	}

	public ClusterComparerService(Cluster cluster, List<Cluster> releaseI, List<Cluster> releaseII)
			throws CloneNotSupportedException {
		super();
		this.cluster = cluster;
		this.releaseI = releaseI;
		this.releaseII = releaseII;
		this.existedItems = new ArrayList<>();
		this.root = new OverlapClusterComposite(this.cluster, this.cluster.getSpectrums());
		existedItems.add(this.cluster.getSpectrums());
		doCompare(this.cluster.getSpectrums(), 2);
	}

	/**
	 * compare sourceSpectrums and spectrums of cluster in releaseI or releaseII
	 * 
	 * @param sourceSpectrums
	 * @param releaseNum
	 *            release number(1 or 2)
	 * @return
	 * @throws CloneNotSupportedException
	 */
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
			List<Spectrum> tmpSpectrums1 = clusterClone1.getSpectrums(); // get spectrums of cluster in release
			List<Spectrum> tmpSpectrums2 = clusterClone2.getSpectrums(); // get spectrums clone
			tmpSpectrums1.retainAll(sourceSpectrums); // get overlap spectrum
			if (tmpSpectrums1.size() > 0) { // exist overlap

				// get different spectrums between child spectrums and overlap spectrum
				tmpSpectrums2.removeAll(tmpSpectrums1);

				/*
				 * true indicate child spectrums exist spectrums not belong to parent spectrums:
				 * the node is middle node no leaf node
				 */
				if (tmpSpectrums2.size() > 0) {
					OverlapClusterComposite middleNode = new OverlapClusterComposite(tmpCluster, tmpSpectrums1); // node
					if (existedItems.indexOf(tmpSpectrums1) == -1) {

						existedItems.add(tmpSpectrums1);

						OverlapClusterComponent node = doCompare(tmpSpectrums2, releaseNum); // middle node or leaf node
						if (node != null) {
							middleNode.add(node);
						}
						if (sourceSpectrums == this.cluster.getSpectrums()) {
							this.root.add(middleNode);
						} else {
							return middleNode;
						}
					}
				} else { // child spectrums belong to parent spectrums: leaf node

					// if (isRootChild) {
					// this.root.add(new OverlapCluster(tmpCluster, tmpSpectrums1));
					// isRootChild = false;
					// } else {
					// return new OverlapCluster(tmpCluster, tmpSpectrums1);
					// }
					// System.out.println(tmpCluster.getId() + ": ");
					// printSpectrums(tmpSpectrums1);
					OverlapCluster leafNode = new OverlapCluster(tmpCluster, tmpSpectrums1);
					existedItems.add(tmpSpectrums1);
					if (sourceSpectrums == this.cluster.getSpectrums()) {
						this.root.add(leafNode);
					} else {
						return leafNode;
					}

				}
			}
		}
		return null;
	}

	public void printCluster(Cluster cluster) {
		System.out.println("message-" + cluster.getId() + ":" + cluster.getSpecCount());
	}

	public static void main(String[] args) {

		// List<Cluster> releaseI = new ClusterDaoFileImpl(new
		// File("./compare/compare_1.clustering")).findAllClusters();
		// List<Cluster> releaseII = new ClusterDaoFileImpl(new
		// File("./compare/compare_2.clustering")).findAllClusters();
		List<Cluster> releaseI = new ClusterDaoFileImpl (new File("./compare/compare_5.clustering")).findAllClusters();
		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_6.clustering")).findAllClusters();
		Cluster cluster = releaseI.get(0);
		ClusterComparerService clusterComparer = null;
		try {
			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		clusterComparer.getRoot().print();
	}
}
