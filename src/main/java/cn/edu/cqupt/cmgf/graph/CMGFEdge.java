package cn.edu.cqupt.cmgf.graph;

import cn.edu.cqupt.sankey.Edge;
import cn.edu.cqupt.score.calculate.MS;

import java.util.List;

public class CMGFEdge implements Edge {

    private List<MS> overlapMsList;
    private double weight;

    public CMGFEdge(List<MS> overlapMsList, double weight) {
        this.overlapMsList = overlapMsList;
        this.weight = weight;
    }

    public List<MS> getOverlapMsList() {
        return overlapMsList;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "CMGFEdge{" +
                "overlapMsList=" + overlapMsList +
                ", weight=" + weight +
                '}';
    }
}
