package cn.edu.cqupt.service;

import java.util.List;

import cn.edu.cqupt.dao.ClusterDaoFileImpl;
import cn.edu.cqupt.dao.IClusterDao;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Page;

public class ClusterTableService {
	private IClusterDao clusterDao;

	public IClusterDao getClusters() {
		return clusterDao;
	}

	public void setClusters(IClusterDao clusterDao) {
		this.clusterDao = clusterDao;
	}

	public ClusterTableService() {

	}

	// read clustering file
	public ClusterTableService(String clusteringFilePath) {
		this.clusterDao = new ClusterDaoFileImpl(clusteringFilePath);
	}

	// get the clustering result of a release
	public List<Cluster> getAllClusters() {
		List<Cluster> allClusters = clusterDao.findAllClusters();
		return allClusters;
	}

	/**
	 * get data displayed on the current page by data displayed on the current page
	 * and the number of items per page
	 * 
	 * @param currentPage
	 *            data displayed on the current page
	 * @param pageSize
	 *            the number of items per page
	 * @return data displayed on the current page
	 */
	public Page<Cluster> getCurrentPageClusters(int currentPage, int pageSize) {

		// get Page object, extract the data we need
		Page<Cluster> currentPageClusters = this.clusterDao.findCurrentPageClusters(currentPage, pageSize);
		return currentPageClusters;
	}
}
