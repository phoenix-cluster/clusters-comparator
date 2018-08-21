package cn.edu.cqupt.score.calculate.disms2;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;


public class DISMS2Test {

    @Test
    public void dataPreprocessing() throws IOException {
        String mgfFile = "E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\qExHF01_02580.mgf";
//        String mgfFile = "C:\\@code\\java\\clusters-comparator\\testdata\\mgf\\sample3.mgf";
        for (int time = 1; time < 100; time++) {
            List<MS> msList = MgfFileReader.getSpectraByRange(
                    new File(mgfFile),
                    0, 100 * time);
            int n = msList.size();
            System.out.print(n + "\t");
            double[] distances = new double[(n * (n - 1)) / 2];
            long t0 = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    distances[n * i + j - (i + 2) * (i + 1) / 2] = DISMS2.calCosineDistance(msList.get(i), msList.get(j), 100, 0.1);
                }
            }
            long t1 = System.currentTimeMillis();
            System.out.print((t1 - t0) / 1000.0 + "\t");

            long t2 = System.currentTimeMillis();
            IntStream.range(0, n).parallel()
                    .forEach((int i) ->
                            IntStream.range(i + 1, n).forEach(
                                    (int j) -> distances[n * i + j - (i + 2) * (i + 1) / 2] =
                                            DISMS2.calCosineDistance(msList.get(i), msList.get(j), 100, 0.1)
                            )
                    );
            long t3 = System.currentTimeMillis();
            System.out.print((t3 - t2) / 1000.0 + "\t");

            long t4 = System.currentTimeMillis();
            IntStream.range(0, n).parallel()
                    .forEach((int i) ->
                            IntStream.range(i + 1, n).parallel()
                                    .forEach(
                                            (int j) -> distances[n * i + j - (i + 2) * (i + 1) / 2] =
                                                    DISMS2.calCosineDistance(msList.get(i), msList.get(j), 100, 0.1)
                                    )
                    );
            long t5 = System.currentTimeMillis();
            System.out.println((t5 - t4) / 1000.0);
        }
    }

    @Test
    public void test(){
        IntStream.range(0, 1)
                .limit(10)
                .forEach(System.out::println);
    }
}