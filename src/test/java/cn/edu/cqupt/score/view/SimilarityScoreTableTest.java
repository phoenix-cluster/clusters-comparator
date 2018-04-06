package cn.edu.cqupt.score.view;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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
        ArrayList<MS> msList1 = MgfFileReader.getAllSpectra(new File("C:\\Users\\huangjs\\Desktop\\mgf\\header_test.mgf"));
        ArrayList<MS> msList2 = MgfFileReader.getAllSpectra(new File("C:\\Users\\huangjs\\Desktop\\mgf\\header_test.mgf"));

        MS ms1 = msList1.get(0);
        TabPane tabPane = SimilarityScoreTabPane.create(ms1, msList2, 0.5, 10);
        Scene scene = new Scene(tabPane, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
