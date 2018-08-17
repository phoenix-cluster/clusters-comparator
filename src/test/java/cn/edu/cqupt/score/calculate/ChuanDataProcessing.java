package cn.edu.cqupt.score.calculate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class ChuanDataProcessing {
    @Before
    public void startup() throws IOException {
        Path distanceFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\distances.txt");
        Files.deleteIfExists(distanceFile);
    }

    @Test
    public void dataProcessing() throws IOException {
        Stream<String> data = Files.lines(Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\matrix.txt"));
        List<double[]> rs = data
                .map(
                        (String s) -> {
                            String[] line = s.split("\t");
                            return Arrays.stream(line)
                                    .mapToDouble(Double::parseDouble)
                                    .toArray();
                        }
                )
                .collect(Collectors.toList());
        double[][] matrix = new double[2000][2000];
        for (int i = 1; i < rs.size(); i++) {
            for (int j = 1; j < rs.get(i).length; j++) {

                matrix[i - 1][j - 1] = rs.get(i)[j];

            }
            System.out.println("row: " + (i - 1) + "->" + matrix[i - 1].length);
        }

        // matrix->distance
        Path distanceFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\distances.txt");
        StringBuilder disStr = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            double max = Arrays.stream(matrix[i]).max().getAsDouble();
            double min = Arrays.stream(matrix[i]).min().getAsDouble();
            double difference = max - min;
            for (int j = 0; j < matrix[i].length; j++) {
                if (j > i) {
                    double normalization = (matrix[i][j] - min) / difference;
                    disStr.append(normalization + System.lineSeparator());
                }
            }
        }
        Files.write(distanceFile,
                disStr.toString().getBytes(Charset.forName("UTF-8")),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
}
