package cn.edu.cqupt.view;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Page;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.view.SimilarityScoreTabPane;
import cn.edu.cqupt.service.ClusterTableService;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusterSelection {

    private StackPane spectrumStackPane; // hold spectrum tables
    private StackPane peakMapStackPane; // hold peak map
    public StackPane pieChartStackPane; // hold pie chart
    public static StackPane networkGraphStackPane; // hold overlap cluster map
    private GridPane gridPane; // hold cluster table, spectrum table, peak map, pie chart and network graph
    private Pagination pagination; // cluster table only with pagination
    private TableView<Cluster> clusterTable; // cluster table

    public ClusterSelection() {

    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public ClusterSelection(String releaseName, ClusterTableService clusterTableService, int pageSize, int pageCount) {
        this.spectrumStackPane = new StackPane(); // hold spectrum tables
        this.peakMapStackPane = new StackPane(); // hold peak map
        this.pieChartStackPane = new StackPane(); // hold pie chart
        networkGraphStackPane = new StackPane();
        this.pagination = new Pagination(ClusterApplication.pageCount.get());
        this.clusterTable = new TableView<>();
        this.gridPane = new GridPane();

        // set grid pane width
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50.0);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50.0);
        this.gridPane.getColumnConstraints().addAll(col1, col2);

        // add builds to grid pane
        createClusterTable();
        BorderPane clusterTablePane = getClusterTablePane(releaseName, clusterTableService, pageSize,
                pageCount);
        gridPane.add(clusterTablePane, 0, 0);
        gridPane.add(spectrumStackPane, 1, 0);

        // bottom pane
        GridPane bottomPane = new GridPane();
        ColumnConstraints btmCol1 = new ColumnConstraints();
        btmCol1.setPercentWidth(33);
        ColumnConstraints btmCol2 = new ColumnConstraints();
        btmCol2.setPercentWidth(33);
        ColumnConstraints btmCol3 = new ColumnConstraints();
        btmCol3.setPercentWidth(33);
        bottomPane.getColumnConstraints().addAll(btmCol1, btmCol2, btmCol3);
        bottomPane.add(peakMapStackPane, 0, 0);
        bottomPane.add(pieChartStackPane, 1, 0);
        bottomPane.add(new ScrollPane(networkGraphStackPane), 2, 0);
        gridPane.add(bottomPane, 0, 1, 2, 1);
        // gridPane.setGridLinesVisible(true);
        bottomPane.setGridLinesVisible(true);
    }

    /**
     * create a table frame to display clusters data for current page
     **/
    @SuppressWarnings("unchecked")
    private void createClusterTable() {

        // set columns
        TableColumn<Cluster, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Cluster, String>("id"));
        idCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<Cluster, Float> ratioCol = new TableColumn<>("Ratio");
        ratioCol.setCellValueFactory(new PropertyValueFactory<Cluster, Float>("ratio"));
        ratioCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<Cluster, Float> avPrecursorMzCol = new TableColumn<>("av_precursor_mz");
        avPrecursorMzCol.setCellValueFactory(new PropertyValueFactory<>("avPrecursorMz"));
        avPrecursorMzCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<Cluster, Float> avPrecursorIntensCol = new TableColumn<>("av_precursor_intens");
        avPrecursorIntensCol.setCellValueFactory(new PropertyValueFactory<>("avPrecursorIntens"));
        avPrecursorIntensCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<Cluster, Integer> specCountCol = new TableColumn<>("Spectrum Count");
        specCountCol.setCellValueFactory(new PropertyValueFactory<>("specCount"));
        specCountCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        // add columns
        clusterTable.getColumns().addAll(idCol, avPrecursorMzCol, avPrecursorIntensCol, specCountCol);

        // set table
        clusterTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // add event to cluster table : focus on the row of the cluster table to display
        // the corresponding table and charts
        clusterTable.getFocusModel().focusedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // System.out.println(oldValue + "->" + newValue);
                int row = newValue.intValue();
                if (row != -1 && clusterTable.getItems().get(row) != null) {

                    // current cluster
                    Cluster currentCluster = clusterTable.getItems().get(row);

                    // create spectrum table
                    SpectrumTable spectrumTable = new SpectrumTable(currentCluster.getSpectra());

                    // create peak map
                    PeakMap peakMap = new PeakMap(currentCluster.getMzValues(), currentCluster.getIntensValues());

                    // get networkGraphservice to create pie chart and network graph
                    NetworkGraphService networtGraphService = null;
                    try {
                        networtGraphService = new NetworkGraphService(
                                currentCluster, ClusterApplication.releaseIName, ClusterApplication.releaseIIName,
                                ClusterApplication.serviceReleaseI.getAllClusters(),
                                ClusterApplication.serviceReleaseII.getAllClusters());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    /** pie chart pane: pie chart and do similarity score button **/
                    VBox comparerPieChartPane = new VBox();
                    UndirectedGraph<Vertex, Edge> undirectedGraph = networtGraphService.getUndirectedGraph();
                    Vertex focusVertex = networtGraphService.getFocusVertex();
                    PieChart comparerPieChart = new ComparerPieChart(
                            ClusterApplication.releaseIName, ClusterApplication.releaseIIName,
                            undirectedGraph, focusVertex).getComparerPieChart();

                    // create similarity score button and set event
                    Button simiScoreBtn = new Button("Do Similarity Score");
                    simiScoreBtn.setOnAction(event -> {

                        // create similarity score tab pane
                        List<Cluster> adjacencyClusterList = new ArrayList<>();
                        for (Vertex vertex : undirectedGraph.getAdjacencyVertices(focusVertex)) {
                            adjacencyClusterList.add(vertex.getCluster());
                        }
                        MS currentMS = MS.clustering2MS(currentCluster);
                        ArrayList<MS> msList = new ArrayList<>(MS.clustering2MS(adjacencyClusterList));
                        SimilarityScoreTabPane.setData(currentMS, msList, 0.5, 10);

                        // open new stage
                        Stage stage = new Stage();
                        Scene scene = new Scene(new StackPane(SimilarityScoreTabPane.similarityScoreTabPane), 800, 500);
                        stage.setScene(scene);
                        stage.show();
                    });
                    comparerPieChartPane.getChildren().addAll(simiScoreBtn, comparerPieChart);
                    comparerPieChartPane.setSpacing(10);


                    // network graph
                    GridPane networkGraphPane = new NetworkGraph().create(networtGraphService);


                    // add builds into pane
                    StackPane trashCans = new StackPane();
                    trashCans.getChildren().addAll(spectrumStackPane.getChildren());
                    trashCans.getChildren().addAll(peakMapStackPane.getChildren());
                    trashCans.getChildren().addAll(pieChartStackPane.getChildren());
                    trashCans.getChildren().addAll(networkGraphStackPane.getChildren());
                    trashCans = null;

                    spectrumStackPane.getChildren().add(spectrumTable.getSpectrumTable());
                    peakMapStackPane.getChildren().add(peakMap.getVbox());
                    pieChartStackPane.getChildren().add(comparerPieChartPane);
                    networkGraphStackPane.getChildren().add(networkGraphPane);
                }

            }

        });

        // set table size
        Rectangle2D screen = ClusterApplication.screenBounds;
        clusterTable.setMinWidth(screen.getWidth() * 0.5);
        clusterTable.setMinHeight(screen.getHeight() * 0.35);

        // eliminate horizontal blank
        clusterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * @param clusterTableService
     * @param pageSize            the number of items displayed per page
     * @param pageCount           the total number of pages
     * @return Pagination object
     */
    private void createPagination(ClusterTableService clusterTableService, int pageSize, int pageCount) {

        // table with pagination
        this.pagination.setPageCount(ClusterApplication.pageCount.get());
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                Page<Cluster> currentPageClusters = clusterTableService.getCurrentPageClusters(pageIndex + 1, pageSize);
                clusterTable.setItems(FXCollections.observableList(currentPageClusters.getDataList()));
                return clusterTable;
            }
        });
        // return pagination;
    }

    public BorderPane getClusterTablePane(String releaseName, ClusterTableService clusterTableService, int pageSize,
                                          int pageCount) {
        BorderPane clusterTablePane = new BorderPane();

        // top
        Label rlsNameLabel = new Label("release: ");
        rlsNameLabel.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label rlsName = new Label(releaseName);
        rlsName.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 14));
        Button switchButton = new Button("switch");

        // switch release
        switchButton.setOnAction((ActionEvent event) -> {
            // exchange serviceRelease
            ClusterTableService tmp = ClusterApplication.serviceReleaseI;
            ClusterApplication.serviceReleaseI = ClusterApplication.serviceReleaseII;
            ClusterApplication.serviceReleaseII = tmp;
            tmp = null;

            // exchange release name
            String tmpName = ClusterApplication.releaseIName;
            ClusterApplication.releaseIName = ClusterApplication.releaseIIName;
            ClusterApplication.releaseIIName = tmpName;
            tmpName = null;

            ClusterApplication.pageCount.set(ClusterApplication.serviceReleaseI
                    .getCurrentPageClusters(1, ClusterApplication.pageSize.get()).getTotalPage());
            ClusterSelection clusterTable = new ClusterSelection(ClusterApplication.releaseIName,
                    ClusterApplication.serviceReleaseI, ClusterApplication.pageSize.get(),
                    ClusterApplication.pageCount.get());
            ClusterApplication.tabPane.getTabs().get(0).setContent(clusterTable.getGridPane());
        });
        HBox top = new HBox(rlsNameLabel, rlsName, switchButton);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10, 0, 0, 0));
        top.setSpacing(10);

        // center
        createPagination(clusterTableService, ClusterApplication.pageSize.get(), ClusterApplication.pageCount.get());

        // bottom
        // page size
        TextField pageSizeField = new TextField(ClusterApplication.pageSize.get() + "");
        pageSizeField.setPrefWidth(40.0);
        pageSizeField.setOnMouseClicked((MouseEvent event) -> {
            pageSizeField.clear();
        });
        Label pageSizeLabel = new Label("Per Page");

        // redirect page index
        Label redirectLabel1 = new Label("To");
        TextField input = new TextField(pagination.getCurrentPageIndex() + 1 + "");
        input.setOnMouseClicked((MouseEvent event) -> {
            input.setText("");
        });
        Label redirectLabel2 = new Label("Page");
        input.setPrefWidth(40.0);

        // submit
        Button submit = new Button("submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            Pattern pattern = Pattern.compile("\\b[1-9][0-9]*\\b");

            @Override
            public void handle(ActionEvent event) {
                String pageSizeText = pageSizeField.getText().trim();
                String inputText = input.getText().trim();

                // page size regulator
                if (pageSizeText.length() != 0 && !pageSizeText.equals(ClusterApplication.pageSize + "")) {
                    Matcher matcher = pattern.matcher(pageSizeText);
                    if (matcher.find()) {
                        ClusterApplication.pageSize.set(Integer.parseInt(pageSizeText));
                        createPagination(clusterTableService, ClusterApplication.pageSize.get(),
                                ClusterApplication.pageCount.get());
                        clusterTablePane.setCenter(pagination);
                    }
                }

                // current page index regulator
                if (inputText.length() != 0 && !inputText.equals(pagination.getCurrentPageIndex() + 1 + "")) {
                    Matcher match = pattern.matcher(inputText);
                    if (match.find()) {
                        int currentPageIndex = Integer.parseInt(inputText) - 1;
                        pagination.setCurrentPageIndex(currentPageIndex);
                    }
                }
            }
        });
        HBox bottom = new HBox(pageSizeField, pageSizeLabel, redirectLabel1, input, redirectLabel2, submit);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(5.0);
        HBox.setMargin(pageSizeLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(submit, new Insets(0, 0, 0, 10));

        clusterTablePane.setTop(top);
        clusterTablePane.setCenter(pagination);
        clusterTablePane.setBottom(bottom);

        return clusterTablePane;
    }
}