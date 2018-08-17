package cn.edu.cqupt.dao;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Page;
import cn.edu.cqupt.restful.RestfulClient;

import java.util.List;

public class ClusterDaoRestfulImpl implements IClusterDao {

    private String releaseName;
    private String orderKey;
    private String orderDirection;
    private int startIndex;
    private int endIndex;


    public ClusterDaoRestfulImpl() {

    }

    public ClusterDaoRestfulImpl(String releaseName, String orderKey,
                                 String orderDirection, int startIndex,
                                 int endIndex) {
        this.releaseName = releaseName;
        this.orderKey = orderKey;
        this.orderDirection = orderDirection;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public List<Cluster> findAllClusters() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Cluster> findCurrentPageClusters(int currentPage, int pageSize) {
        Page<Cluster> result = null; // return value

        // get Page object
        int totalRecord = endIndex - startIndex + 1; // total record number
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;

        // the range of current page(1-base system)
        int start = currentPage > 0 ? startIndex + (currentPage - 1) * pageSize : startIndex;
        int end = currentPage * pageSize > totalRecord ? endIndex : currentPage * pageSize + startIndex - 1;
        RestfulClient restfulClient = new RestfulClient();
        List<Cluster> currentPageClusters = restfulClient.getClusterByRange(releaseName, orderKey,
                orderDirection, start, end); // clusters data needed to display
        result = new Page<>(totalRecord, pageSize, totalPage, currentPage, currentPageClusters);
        System.out.println("ClusterDaoRestfulImpl-currentPageClusters:\n" + currentPageClusters);
        return result;
    }
}
