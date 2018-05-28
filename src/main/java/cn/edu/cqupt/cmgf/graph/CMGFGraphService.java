package cn.edu.cqupt.cmgf.graph;

import cn.edu.cqupt.cmgf.CMGF;
import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.sankey.Edge;
import cn.edu.cqupt.sankey.Vertex;
import cn.edu.cqupt.score.calculate.MS;

import java.util.*;
import java.util.stream.Collectors;

public class CMGFGraphService {
    private boolean[] isVisited;
    private Map<String, List<CMGF>> cmgfGroup1;
    private Map<String, List<CMGF>> cmgfGroup2;
    private List<UndirectedGraph<Vertex, Edge>> undirectedGraphList;

    public CMGFGraphService() {
        undirectedGraphList = new ArrayList<>();
    }

    public List<UndirectedGraph<Vertex, Edge>> getUndirectedGraphList() {
        return undirectedGraphList;
    }

    public void prepareData(CMGF[] cmgfArr) {
        isVisited = new boolean[cmgfArr.length];

        // group by spectra cluster label
        cmgfGroup1 = Arrays.stream(cmgfArr).collect(Collectors.groupingBy(CMGF::getLabel1));
        cmgfGroup2 = Arrays.stream(cmgfArr).collect(Collectors.groupingBy(CMGF::getLabel2));

        // seeking the intersection of each group
        for (List<CMGF> cmgfList : cmgfGroup1.values()) {
            List<MS> msCluster = cmgfList2msList(cmgfList);

            CMGFVertex vertex = new CMGFVertex(msCluster, msCluster.size(), 0);
            UndirectedGraph<Vertex, Edge> graph = new UndirectedGraph<>();
            undirectedGraphList.add(graph);

            link(graph, vertex, msCluster, cmgfGroup2);
        }


    }

    private void link(UndirectedGraph<Vertex, Edge> graph,
                      CMGFVertex sourceVertex, List<MS> differenceSet, Map<String, List<CMGF>> cmgfGroup) {

        for (List<CMGF> cmgfList : cmgfGroup.values()) {

            // backup differenceSet to seek intersection
            List<MS> intersection = backupMsList(differenceSet);

            // get spectrum
            List<MS> msList = cmgfList2msList(cmgfList);

            // intersection: size > 0 indicate there are some overlap spectra
            intersection.retainAll(msList);
            System.out.println("differenceSetBackup = " + intersection.size());
            if (intersection.size() > 0) {
                System.out.println("sourceVertex=" + sourceVertex);
                System.out.println("sourceVertex.getLayer() = " + (sourceVertex.getLayer()));

                CMGFVertex vertex = new CMGFVertex(msList, msList.size(), sourceVertex.getLayer() + 1);

                // backup differenceSetBackup is necessary
                graph.addEdge(sourceVertex, vertex,
                        new CMGFEdge(backupMsList(intersection), intersection.size()));

                // backup msList to seek difference set
                List<MS> msListBackup = backupMsList(msList);

                // difference set: size > 0 indicate it is intermediate node, size == 0 indicate it is leaf node
                msListBackup.removeAll(intersection);
                if (msListBackup.size() > 0) {
                    link(graph, vertex, msListBackup, cmgfGroup == cmgfGroup1 ? cmgfGroup2 : cmgfGroup1);
                }
            }


        }

    }

    private List<MS> cmgfList2msList(List<CMGF> cmgfList) {
        return cmgfList.stream()
                .map(CMGF::getMs)
                .collect(Collectors.toList());
    }

    private List<MS> backupMsList(List<MS> msList) {
        return msList.stream()
                .map(ms -> new MS(ms))
                .collect(Collectors.toList());
    }

    public void printGraphList() {
        for (UndirectedGraph<Vertex, Edge> graph : undirectedGraphList) {
            System.out.println(graph);
            graph.printUnredundantAdjacencyTable();
            System.out.println("-----------------------");
        }
    }

}
