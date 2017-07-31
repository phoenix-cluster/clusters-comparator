package cn.edu.cqupt.dao;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Page;
import cn.edu.cqupt.util.FileUtil;

public class ClusterDaoFileImpl implements IClusterDao {
	private FileUtil fileUtil;
	
	public ClusterDaoFileImpl() {

	}

	public ClusterDaoFileImpl(File clusteringFile) {
		this.fileUtil = new FileUtil(clusteringFile);
	}

	@Override
	public List<Cluster> findAllClusters() {
		return fileUtil.findAllClusters();
	}

	@Override
	public Page<Cluster> findCurrentPageClusters(int currentPage, int pageSize) {
		Page<Cluster> result = null; // return value

		// get Page object
		int totalRecord = fileUtil.getTotalRecord(); // total record number
		int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
		int fromIndex = currentPage > 0 ? 1 + (currentPage - 1) * pageSize : 1;
		int endIndex = currentPage * pageSize > totalRecord ? totalRecord : currentPage * pageSize;
		List<Cluster> currentPageClusters = fileUtil.findClusterByScope(fromIndex, endIndex); // clusters data needed to display
		result = new Page<Cluster>(totalRecord, pageSize, totalPage, currentPage, currentPageClusters);

		return result;
	}
}
