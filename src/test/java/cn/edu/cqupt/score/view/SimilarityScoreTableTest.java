package cn.edu.cqupt.score.view;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTableTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // read data
        ArrayList<MS> msList1 = MgfFileReader.getAllSpectra(
                new File("D:\\workspace\\coding\\java\\cluster-comparer\\testdata\\mgf\\sample1.mgf"));
        ArrayList<MS> msList2 = MgfFileReader.getAllSpectra(
                new File("D:\\workspace\\coding\\java\\cluster-comparer\\testdata\\mgf\\sample2.mgf"));

        MS ms1 = msList1.get(0);
        SimilarityScoreTabPane.setData(ms1, msList2, 0.5, 10);
        TabPane tabPane = SimilarityScoreTabPane.similarityScoreTabPane;
        Scene scene = new Scene(tabPane, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
