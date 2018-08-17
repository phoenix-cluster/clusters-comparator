package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.mgf.MgfFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SimilarityScoreVarChuan {
    private List<MS> msList;
    private Path matrixFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\matrix.txt");
    private Path columnFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\column.txt");


    @Before
    public void startUp() throws IOException {
        Files.deleteIfExists(matrixFile);
        Files.deleteIfExists(columnFile);
        List<MS> allMSList = MgfFileReader.getAllSpectra(
                new File("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\qExHF01_02580.mgf"));
        msList = allMSList.subList(10000 - 1, 12000);
//        msList = MgfFileReader.getAllSpectra(new File("C:\\@code\\java\\clusters-comparator\\testdata\\mgf\\sample3.mgf"));

        System.out.println(msList.get(0));
        System.out.println(msList.get(msList.size() - 1));
    }

    @Test
    public void calSimilarityScoreVar() throws IOException {
        SimilarityScoreVariant simiScore = new SimilarityScoreVariant(10, 100, 0.5);
        Path matrixFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\matrix.txt");
        Path columnFile = Paths.get("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\data-dengchuan\\results\\column.txt");
        for (int i = 0; i < msList.size(); i++) {
            List<Double> rs = simiScore.calSimilarityScore(msList.get(i), msList);
            StringBuilder matrixStrBuilder = new StringBuilder();
            StringBuilder columnStrBuilder = new StringBuilder();
            for (int j = 0; j < rs.size(); j++) {
                if (j != rs.size() - 1) {
                    matrixStrBuilder.append(rs.get(j) + "\t");
                } else {
                    matrixStrBuilder.append(rs.get(j) + System.lineSeparator());
                }
                if (j > i) {
                    columnStrBuilder.append(rs.get(j) + System.lineSeparator());
                }
            }

            // write into files
            Files.write(matrixFile,
                    matrixStrBuilder.toString().getBytes(Charset.forName("UTF-8")),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.write(columnFile, columnStrBuilder.toString().getBytes(Charset.forName("UTF-8")),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }
}
