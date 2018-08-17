package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.util.MathUtil;
import cn.edu.cqupt.view.PeakMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * Created by huangjs on 2018/3/22.
 */
public class SimilarityScore {
    private final double massWindow = 100.0;
    private final int cycleTimes = 10;
    private MS ms1;
    private List<MS> msList;
    private double fragmentTolerance;

    // subscript: the number of cycles; element: filtered data of each step
    private List<List<Peak>> processedPeaks1;

    // key: ms2 from msList; value: filtered data of each step
    private Map<MS, List<List<Peak>>> processedPeaks2;

    // key: ms2 from msList; value: matched peaks of each step
    private Map<MS, List<Map<Peak, Peak>>> allMatchedPeaks;

    // key: ms2 from msList; value: similarity score of each step
    private Map<MS, List<Double>> similarityScore;

    // key: ms2 from msList; value: the finial similarity score between ms1 and ms2
    private Map<MS, Double> maxSimilarityScore;

    // key: ms2 from msList; value: the cycle steps of the finial similarity score between ms1 and ms2
    private Map<MS, Integer> maxSimilarityScoreIndex;

    public int getCycleTimes() {
        return cycleTimes;
    }

    public List<List<Peak>> getProcessedPeaks1() {
        return processedPeaks1;
    }

    public MS getMs1() {
        return ms1;
    }

    public void setMs1(MS ms1) {
        this.ms1 = ms1;
    }

    public List<MS> getMsList() {
        return msList;
    }

    public void setMsList(List<MS> msList) {
        this.msList = msList;
    }

    public double getFragmentTolerance() {
        return fragmentTolerance;
    }

    public void setFragmentTolerance(double fragmentTolerance) {
        this.fragmentTolerance = fragmentTolerance;
    }

    public double getMassWindow() {
        return massWindow;
    }

    public Map<MS, List<List<Peak>>> getProcessedPeaks2() {
        return processedPeaks2;
    }

    public Map<MS, List<Map<Peak, Peak>>> getAllMatchedPeaks() {
        return allMatchedPeaks;
    }

    public Map<MS, List<Double>> getSimilarityScore() {
        return similarityScore;
    }

    public Map<MS, Double> getMaxSimilarityScore() {
        return maxSimilarityScore;
    }

    public Map<MS, Integer> getMaxSimilarityScoreIndex() {
        return maxSimilarityScoreIndex;
    }

    public SimilarityScore(MS ms1, List<MS> msList, double fragmentTolerance) {
        this.fragmentTolerance = fragmentTolerance;
        this.ms1 = ms1;
        this.msList = msList;
    }

    /**
     * calculate similarity score
     *
     * @return maximum similarity score of each alignment
     */
    public Map<MS, Double> calSimilarityScore() {
        processedPeaks1 = new ArrayList<>();
        processedPeaks2 = new HashMap<>();
        allMatchedPeaks = new HashMap<>();
        similarityScore = new HashMap<>();
        maxSimilarityScore = new HashMap<>();
        maxSimilarityScoreIndex = new HashMap<>();
        for (MS ms2 : msList) {

            // the result of a similarity score(subscripts represent the number of cycles)
            List<List<Peak>> tmpProcessedPeaks2 = new ArrayList<>();
            List<Map<Peak, Peak>> tmpAllMatchedPeaks = new ArrayList<>();
            List<Double> tmpSimilarityScore = new ArrayList<>();
            double tmpMaxSimilarityScore = Double.NEGATIVE_INFINITY;
            int tmpMaxSimilarityScoreIndex = -1;
            for (int i = 0; i < cycleTimes; i++) {

                // split and filter spectrum
                if (processedPeaks1.size() <= cycleTimes) {
                    ArrayList<Peak> sfPeakList1 = splitAndFilter(ms1, i + 1);
                    processedPeaks1.add(sfPeakList1);
                }
                ArrayList<Peak> sfPeakList2 = splitAndFilter(ms2, i + 1);
                tmpProcessedPeaks2.add(sfPeakList2);

                // match: peak1(from ms1) => peak2(from ms2)
                Map<Peak, Peak> matchedPeaks = searchMatchedPeaks(processedPeaks1.get(i), sfPeakList2,
                        fragmentTolerance);
                tmpAllMatchedPeaks.add(matchedPeaks);

                // calculate probability-based score
                int n = processedPeaks1.get(i).size() > sfPeakList2.size()
                        ? processedPeaks1.get(i).size()
                        : sfPeakList2.size();
                int k = matchedPeaks.size();
                double p = (double) (i + 1) / massWindow;
                double probabilityScore = calProbabilityScore(n, k, p);

                // calculate intensity-based score
                double intensityScore = calIntensityScore(processedPeaks1.get(i), sfPeakList2, matchedPeaks);

                // calculate a similarity score when retaining i high intensity peaks
                double currSimilarityScore = -10 * Math.log10(probabilityScore) * intensityScore;
                currSimilarityScore = currSimilarityScore == 0.0 ? 0.0 : currSimilarityScore;
                tmpSimilarityScore.add(currSimilarityScore);

                // record the maximum similarity score
                if (currSimilarityScore > tmpMaxSimilarityScore) {
                    tmpMaxSimilarityScore = currSimilarityScore;
                    tmpMaxSimilarityScoreIndex = i;
                }
            }
            processedPeaks2.put(ms2, tmpProcessedPeaks2);
            allMatchedPeaks.put(ms2, tmpAllMatchedPeaks);
            similarityScore.put(ms2, tmpSimilarityScore);
            maxSimilarityScore.put(ms2, tmpMaxSimilarityScore);
            maxSimilarityScoreIndex.put(ms2, tmpMaxSimilarityScoreIndex);
        }
        return maxSimilarityScore;
    }

    /**
     * only calculating similarity score, so other parameters will not be assigned
     *
     * @return maximum similarity score of each alignment
     */
    public Map<MS, Double> onlyCalSimilarityScore() {
        processedPeaks1 = new ArrayList<>();
        maxSimilarityScore = new HashMap<>();
        for (MS ms2 : msList) {

            // the result of a similarity score(subscripts represent the number of cycles)
            ArrayList<ArrayList<Peak>> tmpProcessedPeaks2 = new ArrayList<>();
            ArrayList<Map<Peak, Peak>> tmpAllMatchedPeaks = new ArrayList<>();
            ArrayList<Double> tmpSimilarityScore = new ArrayList<>();
            double tmpMaxSimilarityScore = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < cycleTimes; i++) {

                // split and filter spectrum
                if (processedPeaks1.size() <= cycleTimes) {
                    ArrayList<Peak> sfPeakList1 = splitAndFilter(ms1, i + 1);
                    processedPeaks1.add(sfPeakList1);
                }
                ArrayList<Peak> sfPeakList2 = splitAndFilter(ms2, i + 1);
                tmpProcessedPeaks2.add(sfPeakList2);

                // match: peak1(from ms1) => peak2(from ms2)
                Map<Peak, Peak> matchedPeaks = searchMatchedPeaks(processedPeaks1.get(i),
                        sfPeakList2, fragmentTolerance);
                tmpAllMatchedPeaks.add(matchedPeaks);

                // calculate probability-based score
                int n = processedPeaks1.get(i).size() > sfPeakList2.size() ? processedPeaks1.get(i).size() : sfPeakList2.size();
                int k = matchedPeaks.size();
                double p = (double) (i + 1) / massWindow;
                double probabilityScore = calProbabilityScore(n, k, p);

                // calculate intensity-based score
                double intensityScore = calIntensityScore(processedPeaks1.get(i), sfPeakList2, matchedPeaks);

                // calculate a similarity score when retaining i high intensity peaks
                double currSimilarityScore = -10 * Math.log10(probabilityScore) * intensityScore;
                currSimilarityScore = currSimilarityScore == 0.0 ? 0.0 : currSimilarityScore;
                tmpSimilarityScore.add(currSimilarityScore);

                // record the maximum similarity score
                if (currSimilarityScore > tmpMaxSimilarityScore) {
                    tmpMaxSimilarityScore = currSimilarityScore;
                }
            }
            maxSimilarityScore.put(ms2, tmpMaxSimilarityScore);
        }
        return maxSimilarityScore;
    }

    /**
     * split ms data by window size and filter the top q peaks
     *
     * @param ms
     * @param q
     * @return
     */
    public ArrayList<Peak> splitAndFilter(MS ms, int q) {
        ArrayList<Peak> sfPeakList = new ArrayList<>(); // peaks list after splitting and filtering
        ArrayList<Peak> peakListAscMz = ms.getPeakListAscMz();
        double limitMz = peakListAscMz.get(0).getMz(); // minimal mz value
        ArrayList<Peak> tmpPeakList = new ArrayList<>(); // peaks list in a window
        for (int i = 0; i < peakListAscMz.size(); i++) {
            if (peakListAscMz.get(i).getMz() < limitMz + 100) {
                tmpPeakList.add(peakListAscMz.get(i));
            } else {
                // filter peaks list
                Collections.sort(tmpPeakList, Peak.DescIntensityComparator);
                for (int j = 0; j < q && j < tmpPeakList.size(); j++) {
                    sfPeakList.add(tmpPeakList.get(j));
                }
                // assign parameters
                i -= 1;
                tmpPeakList.clear();
                limitMz += 100;
            }
        }
        // record data of the last cycle
        Collections.sort(tmpPeakList, Peak.DescIntensityComparator);
        for (int j = 0; j < q && j < tmpPeakList.size(); j++) {
            sfPeakList.add(tmpPeakList.get(j));
        }
        return sfPeakList;
    }

    /**
     * search matched peaks between two peak lists
     * taking into account a given fragment tolerance
     *
     * @param sfPeakList1
     * @param sfPeakList2
     * @param fragmentTolerance
     * @return
     */
    public Map<Peak, Peak> searchMatchedPeaks(List<Peak> sfPeakList1,
                                              List<Peak> sfPeakList2,
                                              double fragmentTolerance) {

        // record matched peaks, peak belongs to filtered peak list 1 => peak belongs to filtered peak list 2
        Map<Peak, Peak> matchedPeaks = new HashMap<>();
        Collections.sort(sfPeakList1, Peak.AscMzComparator);
        Collections.sort(sfPeakList2, Peak.AscMzComparator);
        double minDistance = fragmentTolerance;
        for (Peak peak1 : sfPeakList1) {

            // the best matched peak is the peak with the closest mz value
            Peak bestMatchedPeak = null;
            for (Peak peak2 : sfPeakList2) {
                double distance = peak1.getMz() - peak2.getMz();
                if (Math.abs(distance) > fragmentTolerance) {
                    if (distance < 0) {
                        break;
                    }
                } else {

                    /**
                     * if there are multiple peaks in list 2 match the same peak in list 1,
                     * the best matched peak is the peak with the closest mz value
                     */
                    if (Math.abs(distance) <= minDistance) {
                        bestMatchedPeak = peak2;
                        minDistance = Math.abs(distance);
                    }
                }
            }

            /**
             * if there are multiple peaks in list 1 match the same peak in list 2,
             * we only record the first discovered peak in list 1 and its matched peak in list 2.
             */
            if (bestMatchedPeak != null && !matchedPeaks.containsValue(bestMatchedPeak)) {
                matchedPeaks.put(peak1, bestMatchedPeak);
            }
        }
        return matchedPeaks;
    }

    /**
     * calculate the probability-based score
     *
     * @param n represents the number of peaks in processed spectrum with the most peaks
     * @param k is the number of matched peaks taking into account a given fragment tolerance
     * @param p is the probability of finding a single matched peak by chance
     *          and is calculated by dividing the number of retained high intensity peaks
     *          by the mass window size
     * @return the probability-based score
     */
    public double calProbabilityScore(int n, int k, double p) {
        double probabilityScore = 0.0;
        for (int i = k; i <= n; i++) {
            try {
                double combination = MathUtil.calCombination(n, i);
                probabilityScore += combination * Math.pow(p, i) * Math.pow(1 - p, n - i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return probabilityScore;
    }

    public double calIntensityScore(List<Peak> sfPeakList1, List<Peak> sfPeakList2, Map<Peak, Peak> matchedPeaks) {

        // intensity accumulation of peaks in precessed spectrum
        double sfPeakList1Acc = calIntensityAccumulation(sfPeakList1);
        double sfPeakList2Acc = calIntensityAccumulation(sfPeakList2);
        double matchedPeaksKeysAcc = calIntensityAccumulation(new ArrayList<>(matchedPeaks.keySet()));
        double matchedPeaksValuesAcc = calIntensityAccumulation(new ArrayList<>(matchedPeaks.values()));

        // calculate the intensity-based score
        double intensityScore = matchedPeaksKeysAcc / sfPeakList1Acc * (matchedPeaksValuesAcc / sfPeakList2Acc);
        return intensityScore;
    }

    private double calIntensityAccumulation(List<Peak> peakList) {
        double result = 0.0;
        for (Peak peak : peakList) {
            result += peak.getIntensity();
        }
        return result;
    }

    /**
     * get keys in descending order of maxSimilarityScore's values
     *
     * @return
     */
    public MS[] getKeysDescByMaxSimScoreValues() {

        int size = maxSimilarityScore.size();
        MS[] keysDescByMaxSimScoreValues = new MS[size];

        // get array of keys
        MS[] keys = new MS[size];
        maxSimilarityScore.keySet().toArray(keys);

        // get values contained in the map
        List<Double> values = new ArrayList<>(size);
        for (MS ms : keys) {
            values.add(maxSimilarityScore.get(ms));
        }
        int[] descOrderIndex = getDescOrderIndex(values);
        for (int i = 0; i < descOrderIndex.length; i++) {
            keysDescByMaxSimScoreValues[i] = keys[descOrderIndex[i]];
        }
        return keysDescByMaxSimScoreValues;
    }

    public GridPane mulPairsSpecReport() {

        // set column
        ColumnConstraints noCol = new ColumnConstraints(20, 20, 20);
        ColumnConstraints titleCol = new ColumnConstraints(100, 100, 100);

        GridPane root = new GridPane();
        root.getColumnConstraints().addAll(noCol, titleCol);

        MS[] msArr = getKeysDescByMaxSimScoreValues();
        for (int i = 0, row = 2; i < msArr.length; i++, row += 2) {
            MS ms2 = msArr[i];

            // ms2's title
            TextField ms2Title = new TextField(ms2.getTitle());

            // chart of the maximum similarity score
            int currMaxSimilarityScoreIndex = maxSimilarityScoreIndex.get(ms2);
            XYChart<Number, Number> peakMap = chart(ms1, ms2, processedPeaks1.get(currMaxSimilarityScoreIndex),
                    processedPeaks2.get(ms2).get(currMaxSimilarityScoreIndex));

            // remark
            TextArea remarkTextArea = new TextArea(remark(currMaxSimilarityScoreIndex, processedPeaks1, processedPeaks2.get(ms2),
                    allMatchedPeaks.get(ms2), similarityScore.get(ms2)));
            GridPane.setValignment(remarkTextArea, VPos.CENTER);
            remarkTextArea.setEditable(false);
            remarkTextArea.setPrefHeight(70);
            remarkTextArea.setMaxHeight(70);
            remarkTextArea.setMinHeight(70);

            // add components
            root.add(new Label((i + 1) + ""), 0, row, 1, 2);
            root.add(ms2Title, 1, row, 1, 2);
            root.add(peakMap, 2, row);
            root.add(remarkTextArea, 2, row + 1);
        }

        return root;
    }

    /**
     * the detailed report between ms1 and ms2
     *
     * @param ms1
     * @param ms2
     * @param similarityScoreList
     * @param processedPeaksList1
     * @param processedPeaksList2
     * @return
     */
    public GridPane singlePairSpecReport(MS ms1, MS ms2,
                                         List<Double> similarityScoreList,
                                         List<List<Peak>> processedPeaksList1,
                                         List<List<Peak>> processedPeaksList2) {

        // root pane setting
        GridPane report = new GridPane();
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(20);
        c1.setPrefWidth(20);
        c1.setMaxWidth(20);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setMinWidth(900);
        c2.setPrefWidth(900);
        c2.setMaxWidth(900);
        report.getColumnConstraints().addAll(c1, c2);
        report.setGridLinesVisible(false);
        report.setVgap(10);

        // header
        Label headerTitle = new Label(ms1.getTitle() + "\n"
                + "vs\n"
                + ms2.getTitle());
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        report.add(headerTitle, 0, 0, 3, 1);
        GridPane.setHalignment(headerTitle, HPos.CENTER);

        // body: peak map and remark
        CountDownLatch latch = new CountDownLatch(cycleTimes); // do concurrency
        int[] descOrderIndex = getDescOrderIndex(similarityScoreList);
        for (int i = 0, row = 1; i < cycleTimes; i++, row += 2) {
            final int id = i;
            final int rowNum = row;

            new Thread(() -> {

                // sequence number field
                Label indexLabel = new Label((id + 1) + "");

                // chart
                int index = descOrderIndex[id];
                XYChart<Number, Number> peakMap = chart(ms1, ms2, processedPeaksList1.get(index), processedPeaksList2.get(index));

                // remark
                TextArea remarkTextArea = new TextArea(remark(index, processedPeaksList1, processedPeaksList2,
                        allMatchedPeaks.get(ms2), similarityScore.get(ms2)));
                remarkTextArea.setEditable(false);
                remarkTextArea.setPrefHeight(70);
                remarkTextArea.setMaxHeight(70);
                remarkTextArea.setMinHeight(70);

                // organize components
                report.add(indexLabel, 0, rowNum, 1, 2);
                report.add(peakMap, 1, rowNum);
                report.add(remarkTextArea, 1, rowNum + 1);
                report.setAlignment(Pos.CENTER);
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return report;
    }

    /**
     * @param list
     * @return the subscripts of descending order list
     */
    private int[] getDescOrderIndex(List<Double> list) {

        // initial
        int[] result = new int[list.size()];
        List<Double> listBackup = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result[i] = i;
            listBackup.add(list.get(i));
        }

        // order
        for (int i = 0; i < listBackup.size(); i++) {
            double max = listBackup.get(i);
            for (int j = i + 1; j < listBackup.size(); j++) {
                if (listBackup.get(j) > max) {
                    max = listBackup.get(j);
                    // exchange
                    double tmp = listBackup.get(j);
                    listBackup.set(j, listBackup.get(i));
                    listBackup.set(i, tmp);
                    int tmpIndex = result[j];
                    result[j] = result[i];
                    result[i] = tmpIndex;
                }
            }
        }
        return result;
    }

    private XYChart<Number, Number> chart(MS ms1, MS ms2, List<Peak> currProcessedPeakList1,
                                          List<Peak> currProcessedPeakList2) {
        ArrayList<Peak> peakList1 = ms1.getPeakList();
        ArrayList<Peak> peakList2 = ms2.getPeakList();

        List<Double> mzValues1 = ms1.getMzList();
        List<Double> intensityValues1 = ms1.getIntensityList();
        List<Double> mzValues2 = ms2.getMzList();
        List<Double> intensityValues2 = ms2.getIntensityList();
        List<Double> bestMzValues1 = new ArrayList<>();
        List<Double> bestIntensityValues1 = new ArrayList<>();
        List<Double> bestMzValues2 = new ArrayList<>();
        List<Double> bestIntensityValues2 = new ArrayList<>();

        for (Peak peak : currProcessedPeakList1) {
            bestMzValues1.add(peak.getMz());
            bestIntensityValues1.add(peak.getIntensity());
        }
        for (Peak peak : currProcessedPeakList2) {
            bestMzValues2.add(peak.getMz());
            bestIntensityValues2.add(peak.getIntensity());
        }

        // create chart
        return PeakMap.filteredPeakMap(mzValues1, intensityValues1, mzValues2, intensityValues2,
                bestMzValues1, bestIntensityValues1, bestMzValues2, bestIntensityValues2);
    }

    /**
     * @param cycleIndex          which is the cycle between ms1 and ms2
     * @param processedPeaksList1
     * @param processedPeaksList2
     * @param matchedPeaksList
     * @param similarityScoreList
     * @return
     */
    private String remark(int cycleIndex, List<List<Peak>> processedPeaksList1,
                          List<List<Peak>> processedPeaksList2, List<Map<Peak, Peak>> matchedPeaksList,
                          List<Double> similarityScoreList) {
        String remark = "The highest-intensity peaks are retained in each mass window is " + (cycleIndex + 1) + "\n";
        remark += "The filtered data size: " + "spectrum I is " + processedPeaksList1.get(cycleIndex).size() +
                " and spectrum II is " + processedPeaksList2.get(cycleIndex).size() + "\n";
        remark += "The matched peaks size is " + matchedPeaksList.get(cycleIndex).size() + "\n";
        remark += "The similarity score is " + similarityScoreList.get(cycleIndex);
        return remark;
    }
}
