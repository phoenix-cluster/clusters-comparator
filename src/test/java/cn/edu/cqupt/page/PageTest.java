package cn.edu.cqupt.page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjs on 2018/4/6.
 */
public class PageTest {
    public static void main(String[] args) {
        // data
        List<Integer> rowData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rowData.add(i);
        }

        Page<Integer> page = new Page<>(rowData, 4);
        for (int i = 0; i < page.getTotalPage(); i++) {
            List<Integer> currDataList = page.getCurrentPageDataList(i);
            for (Integer tmp : currDataList) {
                System.out.print(i + ": " + tmp + "\t");
            }
            System.out.println();
        }

    }
}
