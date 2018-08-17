package cn.edu.cqupt.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangjs on 2018/4/1.
 */
public class TempTest {
    public static void main(String[] args) {
        Set<Double> set = new HashSet<>();
        set.add(1.0);
        set.add(2.0);
        set.add(4.0);
        Double[] arr = new Double[set.size() + 1];
        arr[0] = new Double(12);
        set.toArray(arr);
        arr[3] = new Double(9);
        for (Double e : arr) {
            System.out.println(e.doubleValue());
        }

        List<Integer> re = Arrays.asList(new Integer[]{1, 2, 3});
        System.out.println(re.subList(0, 3));

        // 向上取整
        System.out.println(Math.ceil((double) -7 / 2));
    }
}
