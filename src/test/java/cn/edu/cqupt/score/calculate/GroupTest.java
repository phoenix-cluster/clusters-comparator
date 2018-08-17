package cn.edu.cqupt.score.calculate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupTest {

    public static void main(String[] args) {
        int[] limit = new int[]{0, 10, 20, 30};
        List<Integer> data = Arrays.asList(1, 3, 4, 1, 10, 29, 23);
        Map<Integer, List<Integer>> group =
                data.stream()
                        .collect(Collectors.groupingBy(
                                (Integer d) -> {
                                    for (int i = 1; i < limit.length; i++) {
                                        if (d.intValue() < limit[i]) {
                                            return i;
                                        }
                                    }
                                    return -1;
                                }
                        ));

        List<Integer> result = group.values().stream()
                .map(
                        (List<Integer> g) -> {
                            Collections.sort(g, (Integer d1, Integer d2) -> Integer.compare(d2, d1));
                            return g.size() > 3 ? g.subList(0, 3) : g;
                        }
                )
                .flatMap(
                        (List<Integer> g) -> g.stream()
                )
                .collect(Collectors.toList());

        System.out.println();
        result.stream().forEach(d -> System.out.print(d + "\t"));
        System.out.println();
    }
}
