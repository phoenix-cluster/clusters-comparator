package cn.edu.cqupt.dao;

import cn.edu.cqupt.clustering.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;

import java.io.File;
import java.util.List;

public class ClusteringFileImportDaoImpl implements ImportDao<File, List<Cluster>> {

    @Override
    public List<Cluster> getResult(File clusteringFile) throws Exception {
        return ClusteringFileHandler.getAllClusters(clusteringFile);
    }
}
