package cn.edu.cqupt.sankey;

import java.util.Objects;

public class VertexImpl implements Vertex{

    private String id;
    private double weight;
    private int layer;

    public VertexImpl(String id, double weight, int layer) {
        this.id = id;
        this.weight = weight;
        this.layer = layer;
    }

    public String getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexImpl vertex = (VertexImpl) o;
        return Double.compare(vertex.weight, weight) == 0 &&
                layer == vertex.layer &&
                Objects.equals(id, vertex.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, weight, layer);
    }

    @Override
    public String toString() {
        return "VertexImpl{" +
                "id='" + id + '\'' +
                ", weight=" + weight +
                ", layer=" + layer +
                '}';
    }
}
