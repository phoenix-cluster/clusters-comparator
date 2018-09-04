package cn.edu.cqupt.mgf.ncluster.model;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.score.calculate.MS;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NetworkGraphService {
    private Map<Integer, List<Integer>> group1;
    private Map<Integer, List<Integer>> group2;
    private String releaseIName;
    private String releaseIIName;
    private List<MS> msList;
    private UndirectedGraph<MSVertex, MSEdge> graph;
    private MSVertex focusVertex;

    public UndirectedGraph<MSVertex, MSEdge> getGraph() {
        return graph;
    }

    public MSVertex getFocusVertex() {
        return focusVertex;
    }

    public NetworkGraphService(NetworkGraphData data, MSVertex focusVertex){
        this.group1 = data.getGroup1();
        this.group2 = data.getGroup2();
        this.releaseIName = data.getReleaseIName();
        this.releaseIIName = data.getReleaseIIName();
        this.msList = data.getMsList();
        this.graph = new UndirectedGraph<>();
        this.focusVertex = focusVertex;
    }

    public NetworkGraphService(Map<Integer, List<Integer>> group1,
                               Map<Integer, List<Integer>> group2,
                               String releaseIName, String releaseIIName,
                               List<MS> msList,
                               MSVertex focusVertex) {
        this.group1 = group1;
        this.group2 = group2;
        this.releaseIName = releaseIName;
        this.releaseIIName = releaseIIName;
        this.msList = msList;
        this.graph = new UndirectedGraph<>();
        this.focusVertex = focusVertex;
    }

    public void doCompare(int classLabel,
                          Map<Integer, List<Integer>> sourceGroup,
                          List<Integer> differenceSet) {
        Map<Integer, List<Integer>> targetGroup;
        String sourceRelease;
        String targetRelease;

        if (sourceGroup == group1) {
            targetGroup = group2;
            sourceRelease = releaseIName;
            targetRelease = releaseIIName;
        } else {
            targetGroup = group1;
            sourceRelease = releaseIIName;
            targetRelease = releaseIName;
        }

        for (Integer key : targetGroup.keySet()) {
            List<Integer> targetBackup1 = targetGroup.get(key).stream().collect(Collectors.toList());
            List<Integer> targetBackup2 = targetGroup.get(key).stream().collect(Collectors.toList());

            // if intersection's size gather than 0, there is overlap between two classes
            targetBackup1.retainAll(differenceSet);
            if (targetBackup1.size() > 0) {

                // obtain difference set for get unique ms list
                List<Integer> sourceBackup = sourceGroup.get(classLabel).stream().collect(Collectors.toList());
                sourceBackup.removeAll(targetBackup1);

                // if difference set's size gather than 0, there is child node
                targetBackup2.removeAll(targetBackup1);

                // obtain unique ms list of two classes
                List<MS> uniqueMSList1 = sourceBackup
                        .stream()
                        .map((Integer c) -> msList.get(c))
                        .collect(Collectors.toList());
                List<MS> uniqueMSList2 = targetBackup2
                        .stream()
                        .map((Integer c) -> msList.get(c))
                        .collect(Collectors.toList());
                List<MS> overlapMSList = targetBackup1
                        .stream()
                        .map((Integer c) -> msList.get(c))
                        .collect(Collectors.toList());

                // create vertexes and edge, then add edge into graph
                MSVertex vertex1 = new MSVertex(sourceRelease, classLabel, sourceGroup.get(classLabel).size());
                MSVertex vertex2 = new MSVertex(targetRelease, key, targetGroup.get(key).size());
                if (!graph.hasEdge(vertex1, vertex2)) {
                    MSEdge edge = new MSEdge(classLabel, key, uniqueMSList1, uniqueMSList2,
                            overlapMSList, overlapMSList.size());
                    graph.addEdge(vertex1, vertex2, edge);

                    if (targetBackup2.size() > 0) {
                        doCompare(key, targetGroup, targetBackup2);
                    }
                }

            }
        }
    }
}