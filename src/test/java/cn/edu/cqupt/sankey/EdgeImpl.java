package cn.edu.cqupt.sankey;

public class EdgeImpl implements Edge {

    private double weight;

    public EdgeImpl(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "EdgeImpl{" +
                "weight=" + weight +
                '}';
    }
}
