package cn.edu.cqupt.mgf.ncluster.model;

import cn.edu.cqupt.score.calculate.MS;

import java.util.List;

public class MSEdge {

    /**
     * the label of classification
     **/
    private int label1;
    private int label2;

    /**
     * overlap massage
     **/
    private List<MS> uniqueMSList1;
    private List<MS> uniqueMSList2;
    private List<MS> overlapMSList;

    /**
     * overlap count
     **/
    private int weight;

    public int getLabel1() {
        return label1;
    }

    public int getLabel2() {
        return label2;
    }

    public List<MS> getUniqueMSList1() {
        return uniqueMSList1;
    }

    public List<MS> getUniqueMSList2() {
        return uniqueMSList2;
    }

    public List<MS> getOverlapMSList() {
        return overlapMSList;
    }

    public int getWeight() {
        return weight;
    }

    public MSEdge(int label1, int label2, List<MS> uniqueMSList1, List<MS> uniqueMSList2, List<MS> overlapMSList, int weight) {
        this.label1 = label1;
        this.label2 = label2;
        this.uniqueMSList1 = uniqueMSList1;
        this.uniqueMSList2 = uniqueMSList2;
        this.overlapMSList = overlapMSList;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "MSEdge{" +
                "label1=" + label1 +
                ", label2=" + label2 +
                ", uniqueMSList1=" + uniqueMSList1 +
                ", uniqueMSList2=" + uniqueMSList2 +
                ", overlapMSList=" + overlapMSList +
                ", weight=" + weight +
                '}';
    }
}
