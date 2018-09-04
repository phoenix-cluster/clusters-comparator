package cn.edu.cqupt.mgf.ncluster.model;

import cn.edu.cqupt.score.calculate.MS;

import java.util.List;

/**
 * clustered mass spectrum
 */
public class CMS {
    private int clusterLabel;
    private List<MS> msCluster;

    public int getClusterLabel() {
        return clusterLabel;
    }

    public void setClusterLabel(int clusterLabel) {
        this.clusterLabel = clusterLabel;
    }

    public List<MS> getMsCluster() {
        return msCluster;
    }

    public void setMsCluster(List<MS> msCluster) {
        this.msCluster = msCluster;
    }

    public CMS() {
    }

    public CMS(int clusterLabel, List<MS> msCluster) {
        this.clusterLabel = clusterLabel;
        this.msCluster = msCluster;
    }
}
