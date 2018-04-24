package cn.edu.cqupt.score.view;


import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.SimilarityScore;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTableService {

    private MS ms1;
    private ArrayList<MS> msList;
    private double fragmentTolerance;
    private int threshold;
    private SimilarityScore similarityScore;

    public SimilarityScore getSimilarityScore() {
        return similarityScore;
    }

    public SimilarityScoreTableService(MS ms1, ArrayList<MS> msList, double fragmentTolerance, int threshold) {
        this.ms1 = ms1;
        this.msList = msList;
        this.fragmentTolerance = fragmentTolerance;
        this.threshold = threshold;

        // calculate similarity score
        this.similarityScore = new SimilarityScore(ms1, msList, fragmentTolerance);
        similarityScore.calSimilarityScore();
    }

    public SimilarityScoreTableData[] getSimiScoreTableData() {

        // get ms array in descending order of similarity score
        MS[] msArr = similarityScore.getKeysDescByMaxSimScoreValues();

        // organize data of similarity score table
        int size = msArr.length <= threshold ? msArr.length : threshold;
        SimilarityScoreTableData[] dataArr = new SimilarityScoreTableData[size];

        // concurrency
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            final int subscript = i;
            new Thread(() -> {

                // calculate match rate( matched peaks count divided by the number of peaks in processed spectrum with the most peaks)
                int index = similarityScore.getMaxSimilarityScoreIndex().get(msArr[subscript]);
                int n1 = similarityScore.getProcessedPeaks1().get(index).size();
                int n2 = similarityScore.getProcessedPeaks2().get(msArr[subscript]).get(index).size();
                int totalPeaks = n1 > n2 ? n1 : n2;
                int matchedPeaks = similarityScore.getAllMatchedPeaks().get(msArr[subscript]).get(index).size();
                float rate = (float) matchedPeaks / totalPeaks;

                // create single pair spectra report
                GridPane singlePairSpecReport = similarityScore.singlePairSpecReport(
                        ms1, msArr[subscript], similarityScore.getSimilarityScore().get(msArr[subscript]),
                        similarityScore.getProcessedPeaks1(), similarityScore.getProcessedPeaks2().get(msArr[subscript]));

                // create button and add event(open single pair spectra report) for button
                Button viewBtn = createAndSetBtn(singlePairSpecReport, "Show");
                SimilarityScoreTableData data = new SimilarityScoreTableData(msArr[subscript].getTitle(), rate,
                        similarityScore.getMaxSimilarityScore().get(msArr[subscript]), viewBtn);
                dataArr[subscript] = data;
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataArr;
    }

    private static Button createAndSetBtn(GridPane singlePairSpecReport, String text) {
        Button viewBtn = new Button(text);
        viewBtn.setOnAction(event -> {
            Tab detailTab = SimilarityScoreTabPane.similarityScoreTabs.get("Detail");
            if (detailTab.getContent() != null) {
                new HBox(detailTab.getContent());
            }
            detailTab.setContent(new ScrollPane(singlePairSpecReport));
            if (!SimilarityScoreTabPane.similarityScoreTabPane.getTabs().contains(detailTab)) {
                SimilarityScoreTabPane.similarityScoreTabPane.getTabs().add(detailTab);
            }
            SimilarityScoreTabPane.similarityScoreTabPane.getSelectionModel().select(detailTab);
        });
        return viewBtn;
    }
}
