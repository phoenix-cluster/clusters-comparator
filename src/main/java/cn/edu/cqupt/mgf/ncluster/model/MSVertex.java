package cn.edu.cqupt.mgf.ncluster.model;

import java.util.Objects;

public class MSVertex {
    private String releaseName;
    private int label;
    private int weight;

    public String getReleaseName() {
        return releaseName;
    }

    public int getLabel() {
        return label;
    }

    public int getWeight() {
        return weight;
    }

    public MSVertex(String releaseName, int label, int weight) {
        this.releaseName = releaseName;
        this.label = label;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "MSVertex{" +
                "releaseName='" + releaseName + '\'' +
                ", label=" + label +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSVertex msVertex = (MSVertex) o;
        return label == msVertex.label &&
                weight == msVertex.weight &&
                Objects.equals(releaseName, msVertex.releaseName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(releaseName, label, weight);
    }
}
