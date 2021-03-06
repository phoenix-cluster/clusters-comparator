package cn.edu.cqupt.score.view;

import cn.edu.cqupt.score.calculate.MS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTabPane {

    public static TabPane similarityScoreTabPane = new TabPane();
    public static HashMap<String, Tab> similarityScoreTabs = new HashMap<>();

    static {
        // initialize tabs
        similarityScoreTabs.put("Similarity Score", new Tab("Similarity Score"));
        similarityScoreTabs.put("Visualization Result", new Tab("Visualization Result"));
        similarityScoreTabs.put("Detail", new Tab("Detail"));
    }


    public static void setData(MS ms1, List<MS> msList, double fragmentTolerance, int threshold) {

        // get data from similarity score table service
        long time0 = System.currentTimeMillis();
        SimilarityScoreTableService service = new SimilarityScoreTableService(ms1, msList, fragmentTolerance, threshold);
        long time1 = System.currentTimeMillis();
        System.out.println("SimilarityScoreTabPane.class: similarity score service cost " + (time1 - time0) + "ms");

        // similarity score pane
        VBox simiScorePane = new VBox();

        // similarity score table
        TableView simiScoreTable = createSimilarityScoreTable(service);

        // create button to view visualization result
        Button mulPairsSpecReportBtn = createMulPairsSpecReportBtn("View Visualization Result", service);

        // set content for similarity score tab
        ScrollPane scrollPane = new ScrollPane(simiScoreTable);
        scrollPane.setFitToWidth(true);
        simiScorePane.getChildren().addAll(scrollPane, mulPairsSpecReportBtn);
        Tab simiScoreTab = similarityScoreTabs.get("Similarity Score");
        if (simiScoreTab.getContent() != null) {
            new HBox(simiScoreTab.getContent()); // clear tab content
        }
        simiScoreTab.setContent(simiScorePane);
        if (!similarityScoreTabPane.getTabs().contains(simiScoreTab)) {
            similarityScoreTabPane.getTabs().add(simiScoreTab);
        }
        similarityScoreTabPane.getSelectionModel().select(simiScoreTab);
    }

    private static TableView<SimilarityScoreTableData> createSimilarityScoreTable(SimilarityScoreTableService service) {

        // create table
        TableView<SimilarityScoreTableData> simiScoreTable = new TableView<>();

        // set columns of table
        TableColumn<SimilarityScoreTableData, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setMinWidth(150);
        idCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment:CENTER");

        TableColumn<SimilarityScoreTableData, Float> matchRateCol = new TableColumn<>("Match Rate");
        matchRateCol.setCellValueFactory(new PropertyValueFactory<>("matchRate"));
        matchRateCol.setMinWidth(80);
        matchRateCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment:CENTER");

        TableColumn<SimilarityScoreTableData, Double> simiScoreCol = new TableColumn<>("Similarity Score");
        simiScoreCol.setCellValueFactory(new PropertyValueFactory<>("similarityScore"));
        simiScoreCol.setMinWidth(150);
        simiScoreCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment:CENTER");

        TableColumn<SimilarityScoreTableData, String> viewBtnCol = new TableColumn<>("Detail");
        viewBtnCol.setCellValueFactory(new PropertyValueFactory<>("viewBtn"));
        viewBtnCol.setMinWidth(80);
        viewBtnCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment:CENTER");
        simiScoreTable.getColumns().addAll(idCol, matchRateCol, simiScoreCol, viewBtnCol);

        // set data
        long time6 = System.currentTimeMillis();
        ObservableList data = FXCollections.observableList(Arrays.asList(service.getSimiScoreTableData()));
        long time7 = System.currentTimeMillis();
        simiScoreTable.setItems(data);
        long time8 = System.currentTimeMillis();

        simiScoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        System.out.println("create data cost " + (time7 - time6) + "ms");
        System.out.println("1. set data cost " + (time8 - time7) + "ms");

        return simiScoreTable;
    }

    /**
     * the button to view multiple pairs spectra score report
     *
     * @param text A text string for button label.
     * @return
     */
    private static Button createMulPairsSpecReportBtn(String text, SimilarityScoreTableService service) {
        Button mulPairsSpecReportBtn = new Button(text);
        mulPairsSpecReportBtn.setOnAction(event -> {
            GridPane mulPairsSpecReport = service.getSimilarityScore().mulPairsSpecReport();
            Tab visuaRsTab = similarityScoreTabs.get("Visualization Result");
            if (visuaRsTab.getContent() != null) {
                new HBox(visuaRsTab.getContent());
            }
            visuaRsTab.setContent(new ScrollPane(mulPairsSpecReport));
            if (!similarityScoreTabPane.getTabs().contains(visuaRsTab)) {
                similarityScoreTabPane.getTabs().add(visuaRsTab);
            }
            similarityScoreTabPane.getSelectionModel().select(visuaRsTab);
        });
        return mulPairsSpecReportBtn;
    }
}
