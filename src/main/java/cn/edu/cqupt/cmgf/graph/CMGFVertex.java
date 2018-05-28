package cn.edu.cqupt.cmgf.graph;

import cn.edu.cqupt.sankey.Vertex;
import cn.edu.cqupt.score.calculate.MS;

import java.util.List;

public class CMGFVertex implements Vertex {

    // the vertex save spectra with the same cluster label
    private List<MS> msCluster;

    // the number of spectra in the cluster
    private double weight;

    private int layer;

    public CMGFVertex(List<MS> msCluster, double weight, int layer) {
        this.msCluster = msCluster;
        this.weight = weight;
        this.layer = layer;
    }

    public List<MS> getMsCluster() {
        return msCluster;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public String toString() {
        return "HashCode=<" + this.hashCode() + ">" +
                "CMGFVertex{" +
                "msCluster=" + msCluster +
                ", weight=" + weight +
                ", layer=" + layer +
                '}';
    }
}
