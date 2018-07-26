package cn.edu.cqupt.main;

import cn.edu.cqupt.clustering.ClusteringFileHandler;
import cn.edu.cqupt.clustering.view.ClusterTable;
import cn.edu.cqupt.clustering.view.SpectrumTable;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.List;

/**
 * 显示clustering文件的内容，始终以clusteringFileI指向的文件作为显示的对象；
 * 交换显示时，交换变量clusteringFileI和clusteringFileII，然后重新调用create方法。
 * <p>
 * Display the contents of the clustering file,
 * always make the file pointed to by clusteringFileI as the display target;
 * when swapping the display, exchange the variables clusteringFileI and clusteringFileII,
 * and then call the create method.
 */
public class ClusterSelection {
    private File clusteringFileI;
    private File clusteringFileII;
    private GridPane clusterSelectionPane;
    private int pageSize = 8;

    public File getClusteringFileI() {
        return clusteringFileI;
    }

    public File getClusteringFileII() {
        return clusteringFileII;
    }

    public GridPane getClusterSelection() {
        return clusterSelectionPane;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ClusterSelection(File clusteringFileI, File clusteringFileII) {
        this.clusteringFileI = clusteringFileI;
        this.clusteringFileII = clusteringFileII;
        this.clusterSelectionPane = new GridPane();
    }

    public ClusterSelection(File clusteringFileI, File clusteringFileII, int pageSize) {
        this.clusteringFileI = clusteringFileI;
        this.clusteringFileII = clusteringFileII;
        this.clusterSelectionPane = new GridPane();
        this.pageSize = pageSize;
    }

    /**
     * Note: all tables and charts only are created once and then filled by different data
     * @return
     * @throws Exception
     */
    public GridPane create() throws Exception {

        // make clusteringFileI as the display target
        List<Cluster> clusterList = ClusteringFileHandler.getAllClusters(clusteringFileI);

        // cluster table
        ClusterTable clusterTable = new ClusterTable();
        BorderPane clusterTablePane = clusterTable.create(clusterList, pageSize);
        clusterSelectionPane.add(clusterTablePane, 0, 0);

        // spectrum table
        SpectrumTable spectrumTable = new SpectrumTable();
        TableView<Spectrum> spectrumTableView = spectrumTable.createSpectrumTableView(); // create a framework
        clusterSelectionPane.add(new ScrollPane(spectrumTableView), 1, 0);

        // add event to cluster table: focus on the row of the cluster table to display
        // the corresponding table and charts
        clusterTable.getClusterTableView().getFocusModel().focusedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

                    TableView<Cluster> clusterTableView = clusterTable.getClusterTableView();

                    // acquire the focused row number
                    int row = newValue.intValue();
                    if (row != -1 && clusterTableView.getItems().get(row) != null) {

                        // obtain current cluster
                        Cluster currentCluster = clusterTableView.getItems().get(row);

                        // set spectrum table view
                        spectrumTable.setTableView(spectrumTableView, currentCluster.getSpectra());

                        // peak map

                        // network graph

                    }
                }
        );

        // trigger cluster table view's event to display chars and tables
        clusterTable.getClusterTableView().getFocusModel().focus(1);
        clusterTable.getClusterTableView().getFocusModel().focus(0);

        return clusterSelectionPane;
    }
}
