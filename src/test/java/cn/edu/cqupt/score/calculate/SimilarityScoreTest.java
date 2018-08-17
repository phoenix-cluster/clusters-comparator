package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.util.MathUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by huangjs on 2018/3/24.
 */
public class SimilarityScoreTest {
    private SimilarityScore simiScore;
    private List<MS> msList;

    @Before
    public void startUp() throws IOException {

        // read a mgf file
        msList = MgfFileReader.getAllSpectra(new File("C:\\@code\\java\\clusters-comparator\\testdata\\mgf\\sample3.mgf"));
        MS ms = msList.get(0);
        simiScore = new SimilarityScore(ms, msList, 0.5);
    }

    @Test
    public void testSplitAndFilter() {
        MS ms2 = msList.get(0);
        MS ms1 = msList.get(1);
        int q = 2;
        int windowSize = 100;
        List<Peak> sfPeakList1 = simiScore.splitAndFilter(ms1, q);
        List<Peak> sfPeakList2 = simiScore.splitAndFilter(ms2, q);

        // match
        Map<Peak, Peak> matchedPeaks = simiScore.searchMatchedPeaks(sfPeakList1,
                sfPeakList2, 0.5);

        // calculate probability-based score
        int n = sfPeakList1.size() > sfPeakList2.size()
                ? sfPeakList1.size()
                : sfPeakList2.size();
        int k = matchedPeaks.size();
        double p = (double) q / windowSize;
        double probabilityScore = simiScore.calProbabilityScore(n, k, p);

        // calculate intensity-based score
        double intensityScore = simiScore.calIntensityScore(sfPeakList1, sfPeakList2, matchedPeaks);

        // calculate a similarity score when retaining i high intensity peaks
        double currSimilarityScore = -10 * Math.log10(probabilityScore) * intensityScore;
        System.out.println("current similarity score is " + currSimilarityScore);
    }
}