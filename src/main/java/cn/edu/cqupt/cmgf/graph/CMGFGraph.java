package cn.edu.cqupt.cmgf;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

public class CMGFGraph {
    public UndirectedGraph<CMGFVerteex, CMGFEdge>

    /**
     * group data by attribute clusterLabel
     * @param cmgfList
     * @return
     */
    private Map<String, List<CMGF>> groupByClusterLabel(List<CMGF> cmgfList){
        return cmgfList.parallelStream().collect(groupingBy(CMGF::getClusterLabel));
    }

    public void printGroup(List<CMGF> cmgfList){
        Map<String, List<CMGF>> groups = groupByClusterLabel(cmgfList);
        groups.keySet().stream()
                .sorted(comparing(Integer::parseInt))
                .forEach(label -> System.out.println(label + " has " + groups.get(label).size() + " elements"));

    }
}
