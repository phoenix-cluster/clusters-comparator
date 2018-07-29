package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.clustering.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ClusterTableTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClusterTable clusterTable = new ClusterTable();
        File file = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\cli_clustering.pxd000021.0.7_4.clustering");
        List<Cluster> clusterList = ClusteringFileHandler.getAllClusters(file);
        BorderPane clusterTablePane = clusterTable.create(clusterList, 8);

        TableView<Cluster> clusterTableView = clusterTable.getClusterTableView();

        for(Cluster c : clusterTableView.getItems()){
            System.out.println(c);
        }

        Scene scene = new Scene(clusterTablePane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}