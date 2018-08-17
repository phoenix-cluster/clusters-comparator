package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.mgf.MgfFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimilarityScoreVariantTest {
    private SimilarityScoreVariant simiScoreVar;
    private List<MS> msList;

    @Before
    public void startUp() throws IOException {

        // read a mgf file
        msList = MgfFileReader.getAllSpectra(new File("C:\\@code\\java\\clusters-comparator\\testdata\\mgf\\sample3.mgf"));
        simiScoreVar = new SimilarityScoreVariant(10, 100, 0.5);
    }

    @Test
    public void calSimilarityScore() {
        MS ms1 = msList.get(1);
        List<Double> simiScoreList = simiScoreVar.calSimilarityScore(ms1, msList);
        System.out.println();
        simiScoreList.stream().forEach(
                (Double d) -> System.out.print(d + "\t")
        );
    }
}