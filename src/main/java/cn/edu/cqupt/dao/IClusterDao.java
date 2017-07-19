package cn.edu.cqupt.dao;

import java.util.List;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Page;

public interface IClusterDao {

	/**
	 * @return the clustering result of a release
	 */
	public List<Cluster> findAllClusters();

	/**
	 * find the clusters data needed to display the current page
	 * 
	 * @param currentPage
	 *            the current page number
	 * @param pageSize
	 *            the number needed to display of per page
	 * @return clusters data needed to display
	 */
	public Page<Cluster> findCurrentPageClusters(int currentPage, int pageSize);
}
