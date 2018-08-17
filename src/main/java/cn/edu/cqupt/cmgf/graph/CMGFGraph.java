package cn.edu.cqupt.cmgf.graph;

import cn.edu.cqupt.cmgf.CMGF;
import cn.edu.cqupt.graph.UndirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

public class CMGFGraph {
    public UndirectedGraph<CMGFVertex, CMGFEdge> create(List<CMGF> cmgfList1, List<CMGF> cmgfList2) {
        UndirectedGraph<CMGFVertex, CMGFEdge> graph = new UndirectedGraph<>();

        // group
        Map<String, List<CMGF>> cmgfGroup1 = groupByClusterLabel(cmgfList1);
        Map<String, List<CMGF>> cmgfGroup2 = groupByClusterLabel(cmgfList2);

        // seeking the intersection of each group
        for (String label1 : cmgfGroup1.keySet()) {

            // backup group 1
            List<CMGF> backup = new ArrayList<>(cmgfGroup1.get(label1).size());
            for (CMGF cmgf : cmgfGroup1.get(label1)) {
                backup.add(new CMGF(cmgf));
            }

            // intersection
            for (String label2 : cmgfGroup2.keySet()) {
                backup.retainAll(cmgfGroup2.get(label2));
                if (backup.size() != 0) {
//                    graph.addEdge(new CMGFVertex())
                }
            }
        }

        return graph;
    }


    /**
     * group data by attribute clusterLabel
     *
     * @param cmgfList
     * @return
     */
    private Map<String, List<CMGF>> groupByClusterLabel(List<CMGF> cmgfList) {
        return cmgfList.parallelStream().collect(groupingBy(CMGF::getClusterLabel));
    }

    public void printGroup(List<CMGF> cmgfList) {
        Map<String, List<CMGF>> groups = groupByClusterLabel(cmgfList);
        groups.keySet().stream()
                .sorted(comparing(Integer::parseInt))
                .forEach(label -> System.out.println(label + " has " + groups.get(label).size() + " elements"));

    }
}
