package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.main.Application;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.page.Page;
import cn.edu.cqupt.page.TableViewWithPagination;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class ClusterTable {

    private TableView<Cluster> clusterTableView;

    /**
     * Note: must call method create() before calling this method
     *
     * @return
     */
    public TableView<Cluster> getClusterTableView() {
        return clusterTableView;
    }

    public ClusterTable() {
        clusterTableView = createClusterTableView();
    }

    /**
     * create cluster table with pagination pane
     *
     * @param clusterList cluster list need to be displayed
     * @param pageSize    the number of data in per page
     * @return
     */
    public BorderPane create(List<Cluster> clusterList, int pageSize) {

        // create object Page
        Page<Cluster> page = new Page<>(clusterList, pageSize);

        // set data
        clusterTableView.setItems(FXCollections.observableList(clusterList));

        // create table with pagination
        return TableViewWithPagination.createByDefaultLayout(page, clusterTableView);
    }

    /**
     * create a cluster table view
     * note: there is no data being filled and only to make a framework
     *
     * @return
     */
    private TableView<Cluster> createClusterTableView() {
        TableView<Cluster> clusterTable = new TableView<>();

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
        double width = Application.SCREEN_BOUNDS.getWidth() * 0.45;
        clusterTable.setMaxWidth(width);
        clusterTable.setMinWidth(width );
        clusterTable.setPrefWidth(width);
        clusterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return clusterTable;
    }
}
