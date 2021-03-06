package cn.edu.cqupt.mgf.view;

import cn.edu.cqupt.page.Page;
import cn.edu.cqupt.page.TableViewWithPagination;
import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.Peak;
import cn.edu.cqupt.score.view.SimilarityScoreTabPane;
import cn.edu.cqupt.view.PeakMap;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjs on 2018/4/6.
 */
public class MgfInfoDisplayPane {

    private String mgfName1;
    private String mgfName2;
    private List<MS> msList1;
    private List<MS> msList2;
    private SimpleDoubleProperty fragmentTolerance;
    private SimpleIntegerProperty threshold;
    private int pageSize;
    private GridPane mgfInfoDisplayPane;

    public List<MS> getMsList1() {
        return msList1;
    }

    public void setMsList1(List<MS> msList1) {
        this.msList1 = msList1;
    }

    public List<MS> getMsList2() {
        return msList2;
    }

    public void setMsList2(List<MS> msList2) {
        this.msList2 = msList2;
    }

    public double getFragmentTolerance() {
        return fragmentTolerance.get();
    }

    public SimpleDoubleProperty fragmentToleranceProperty() {
        return fragmentTolerance;
    }

    public void setFragmentTolerance(double fragmentTolerance) {
        this.fragmentTolerance.set(fragmentTolerance);
    }

    public int getThreshold() {
        return threshold.get();
    }

    public SimpleIntegerProperty thresholdProperty() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold.set(threshold);
    }

    public MgfInfoDisplayPane() {
        fragmentTolerance = new SimpleDoubleProperty(0.5);
        threshold = new SimpleIntegerProperty(10);
    }

    public MgfInfoDisplayPane(String mgfName1, String mgfName2, List<MS> msList1, List<MS> msList2, int pageSize) {
        this();
        this.mgfName1 = mgfName1;
        this.mgfName2 = mgfName2;
        this.msList1 = msList1;
        this.msList2 = msList2;
        this.pageSize = pageSize;
    }


    public GridPane getMgfInfoDisplayPane() {
        mgfInfoDisplayPane = new GridPane();

        // mgf name label
        Label mgfLabel = new Label("MGF: ");
        mgfLabel.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label mgfNameLabel = new Label(mgfName1);
        mgfNameLabel.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 16));
        mgfInfoDisplayPane.add(new HBox(mgfLabel, mgfNameLabel), 0, 0);

        // mgf table
        Page<MS> page = new Page<>(msList1, pageSize);
        TableView tableView = createMgfTable();
        TableViewWithPagination tableViewWithPagination = new TableViewWithPagination(page, tableView);
        mgfInfoDisplayPane.add(tableViewWithPagination.getDefaultLayout(), 0, 1);

        // similarity score table
        // create similarity score pane
        SimilarityScoreTabPane.setData(msList1.get(0), msList2, fragmentTolerance.doubleValue(), threshold.intValue());
        mgfInfoDisplayPane.add(SimilarityScoreTabPane.similarityScoreTabPane, 1, 1);

        return mgfInfoDisplayPane;
    }

    public TableView<MS> createMgfTable() {

        // create table
        TableView<MS> mgfTable = new TableView<>();

        // set columns
        TableColumn<MS, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(100);
        titleCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<MS, String> chargeCol = new TableColumn<>("Charge");
        chargeCol.setCellValueFactory(new PropertyValueFactory<>("charge"));
        chargeCol.setPrefWidth(80);
        chargeCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        mgfTable.getColumns().addAll(titleCol, chargeCol);

        // allows for one item to be selected at a time
        mgfTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // add event to mgf table : focus on the row of the mgf table to display
        // the corresponding similarity score pane
        mgfTable.getFocusModel().focusedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int row = newValue.intValue();
                if (row != -1 && mgfTable.getItems().get(row) != null) {

                    // current MS
                    MS currentMS = mgfTable.getItems().get(row);

                    // update similarity score table
                    // create similarity score pane
                    SimilarityScoreTabPane.setData(currentMS, msList2, fragmentTolerance.doubleValue(), threshold.intValue());

                    // create peak map
                    mgfInfoDisplayPane.add(createPeakMap(currentMS), 0, 2, 2, 1);
                }

            }
        });

        // initialize: make first row be selected
        mgfTable.getSelectionModel().focus(1);
        mgfTable.getSelectionModel().focus(0);

        // set table size
        mgfTable.setPrefSize(400, 400);

        // eliminate horizontal blank
        mgfTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return mgfTable;
    }

    private VBox createPeakMap(MS ms) {
        List<Float> mzList = new ArrayList<>();
        List<Float> intensityList = new ArrayList<>();
        for (Peak peak : ms.getPeakList()) {
            mzList.add((float) peak.getMz());
            intensityList.add((float) peak.getIntensity());
        }
        PeakMap peakMap = new PeakMap(mzList, intensityList);
        return peakMap.getVbox();
    }
}

