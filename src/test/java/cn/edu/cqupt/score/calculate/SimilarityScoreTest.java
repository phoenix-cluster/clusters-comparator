package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangjs on 2018/3/24.
 */
public class SimilarityScoreTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        File clusterFile1 = new File("C:\\Users\\huangjs\\Desktop\\compare\\cli_clustering.pxd000021.0.7_4.clustering");
        File clusterFile2 = new File("C:\\Users\\huangjs\\Desktop\\compare\\hdp_clustering.pxd000021.0.7_4.clustering");
//        File clusterFile1 = new File("C:\\Users\\huangjs\\Desktop\\compare\\compare_1.clustering");
//        File clusterFile2 = new File("C:\\Users\\huangjs\\Desktop\\compare\\compare_2.clustering");
        ClusterTableService serviceReleaseI = new ClusterTableService(clusterFile1);
        ClusterTableService serviceReleaseII = new ClusterTableService(clusterFile2);
        List<Cluster> clusterList1 = serviceReleaseI.getAllClusters();
        List<Cluster> clusterList2 = serviceReleaseII.getAllClusters();

        // score
        MS ms1 = MS.clustering2MS(clusterList1.get(0));
        List<MS> msList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            msList.add(MS.clustering2MS(clusterList2.get(i)));
        }
        SimilarityScore score = new SimilarityScore(ms1, msList, 0.5);
        HashMap<MS, Double> result = score.calSimilarityScore();
        System.out.println("similarity score: " + result);
        Scene scene = new Scene(new ScrollPane(score.mulPairsSpecReport()), 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
