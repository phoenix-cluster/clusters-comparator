package cn.edu.cqupt.score.view;

import javafx.scene.control.Button;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTableData {
    private String id;
    private float matchRate; // matched peaks counts divided by the number of peaks in the processed spectrum with the most peaks
    private double similarityScore;
    private Button viewBtn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(float matchRate) {
        this.matchRate = matchRate;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public Button getViewBtn() {
        return viewBtn;
    }

    public void setViewBtn(Button viewBtn) {
        this.viewBtn = viewBtn;
    }

    public SimilarityScoreTableData() {

    }

    public SimilarityScoreTableData(String id, float matchRate, double similarityScore, Button viewBtn) {
        this.id = id;
        this.matchRate = matchRate;
        this.similarityScore = similarityScore;
        this.viewBtn = viewBtn;
    }
}
