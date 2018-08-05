package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.page.Page;
import cn.edu.cqupt.page.TableViewWithPagination;
import cn.edu.cqupt.util.TableViewUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Collections;
import java.util.Comparator;
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
        TableViewWithPagination<Cluster> tableViewWithPagination = new TableViewWithPagination<>(page, clusterTableView);

        // add global ordering for specified
        tableViewWithPagination.addGlobalOrdering(
                clusterTableView.getColumns().get(1),
                (c1, c2) -> Float.compare(c1.getRatio(), c2.getRatio()),
                (c1, c2) -> Float.compare(c2.getRatio(), c1.getRatio())
        );
        tableViewWithPagination.addGlobalOrdering(
                clusterTableView.getColumns().get(2),
                (c1, c2) -> Float.compare(c1.getAvPrecursorMz(), c2.getAvPrecursorMz()),
                (c1, c2) -> Float.compare(c2.getAvPrecursorMz(), c1.getAvPrecursorMz())
        );
        tableViewWithPagination.addGlobalOrdering(
                clusterTableView.getColumns().get(3),
                (c1, c2) -> Float.compare(c1.getAvPrecursorIntens(), c2.getAvPrecursorIntens()),
                (c1, c2) -> Float.compare(c2.getAvPrecursorIntens(), c1.getAvPrecursorIntens())
        );
        tableViewWithPagination.addGlobalOrdering(
                clusterTableView.getColumns().get(4),
                (c1, c2) -> Float.compare(c1.getSpecCount(), c2.getSpecCount()),
                (c1, c2) -> Float.compare(c2.getSpecCount(), c1.getSpecCount())
        );
        return tableViewWithPagination.getDefaultLayout();
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
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        idCol.setStyle("-fx-font-family:'Arial'; -fx-font-size: 14; -fx-alignment: CENTER;");

        TableColumn<Cluster, Float> ratioCol = new TableColumn<>("Ratio");
        ratioCol.setCellValueFactory(new PropertyValueFactory("ratio"));
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
        clusterTable.getColumns().addAll(idCol, ratioCol, avPrecursorMzCol, avPrecursorIntensCol, specCountCol);

        // set table
        clusterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clusterTable.getSelectionModel().setCellSelectionEnabled(true);
        clusterTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // make cluster table can copy
        TableViewUtil.installCopyHandler(clusterTable);

        return clusterTable;
    }

}
