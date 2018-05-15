package cn.edu.cqupt.test.test.java.cn.edu.cqupt.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupingTest {
    public static void main(String[] args) {
        List<Cluster> clusters = new ArrayList<>();
        clusters.add(new Cluster("1", "0", "0"));
        clusters.add(new Cluster("0", "2", "0"));
        clusters.add(new Cluster("0", "0", "3"));

        Map<String, Map<String, Map<String, List<Cluster>>>> result = clusters.stream()
                .collect(
                        Collectors.groupingBy(Cluster::getLabel1,
                                Collectors.groupingBy(Cluster::getLabel2,
                                        Collectors.groupingBy(Cluster::getLabel3))));
        System.out.println(result);
    }
}


class Cluster{
    private String label1;
    private String label2;
    private String label3;

    public Cluster(String label1, String label2, String label3) {
        this.label1 = label1;
        this.label2 = label2;
        this.label3 = label3;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "label1='" + label1 + '\'' +
                ", label2='" + label2 + '\'' +
                ", label3='" + label3 + '\'' +
                '}';
    }
}