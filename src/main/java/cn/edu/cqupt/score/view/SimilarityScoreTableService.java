package cn.edu.cqupt.score.view;


import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.SimilarityScore;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTableService {

    private MS ms1;
    private ArrayList<MS> msList;
    private double fragmentTolerance;
    private double threshold;
    private SimilarityScore similarityScore;

    public SimilarityScore getSimilarityScore() {
        return similarityScore;
    }

    public SimilarityScoreTableService(MS ms1, ArrayList<MS> msList, double fragmentTolerance, double threshold) {
        this.ms1 = ms1;
        this.msList = msList;
        this.fragmentTolerance = fragmentTolerance;
        this.threshold = threshold;

        // calculate similarity score
        this.similarityScore = new SimilarityScore(ms1, msList, fragmentTolerance);
        similarityScore.calSimilarityScore();
    }

    public List<SimilarityScoreTableData> getSimiScoreTableDataList(){


        // get ms array in descending order of similarity score
        MS[] msArr = similarityScore.getKeysDescByMaxSimScoreValues();

        // organize data of similarity score table
        List<SimilarityScoreTableData> dataList = new ArrayList<>();
        for(int i = 0; i < msArr.length && i < threshold; i++){

            // calculate match rate( matched peaks count divided by the number of peaks in processed spectrum with the most peaks)
            int index = similarityScore.getMaxSimilarityScoreIndex().get(msArr[i]);
            int n1 = similarityScore.getProcessedPeaks1().get(index).size();
            int n2 = similarityScore.getProcessedPeaks2().get(msArr[i]).get(index).size();
            int totalPeaks = n1 > n2 ? n1 : n2;
            int matchedPeaks = similarityScore.getAllMatchedPeaks().get(msArr[i]).get(index).size();
            float rate = (float)matchedPeaks / totalPeaks;

            // create button and add event for button
            GridPane singlePairSpecReport = similarityScore.singlePairSpecReport(ms1, msArr[i], similarityScore.getSimilarityScore().get(msArr[i]),
                    similarityScore.getProcessedPeaks1(), similarityScore.getProcessedPeaks2().get(msArr[i]));
            Button viewBtn = createAndSetBtn(singlePairSpecReport, "Show");
            SimilarityScoreTableData data = new SimilarityScoreTableData(msArr[i].getTitle(), rate, similarityScore.getMaxSimilarityScore().get(msArr[i]), viewBtn);
            dataList.add(data);
        }
        return dataList;
    }

    private static Button createAndSetBtn(GridPane singlePairSpecReport, String text){
        Button viewBtn = new Button(text);
        viewBtn.setOnAction(event -> {
            Tab detailTab = SimilarityScoreTabPane.similarityScoreTabs.get("Detail");
            if(detailTab.getContent() != null){
                new HBox(detailTab.getContent());
            }
            detailTab.setContent(new ScrollPane(singlePairSpecReport));
            if(! SimilarityScoreTabPane.similarityScoreTabPane.getTabs().contains(detailTab)) {
                SimilarityScoreTabPane.similarityScoreTabPane.getTabs().add(detailTab);
            }
            SimilarityScoreTabPane.similarityScoreTabPane.getSelectionModel().select(detailTab);
        });
        return viewBtn;
    }
}
