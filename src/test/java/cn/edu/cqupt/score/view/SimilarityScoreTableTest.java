package cn.edu.cqupt.score.view;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by huangjs on 2018/4/3.
 */
public class SimilarityScoreTableTest extends Application {

    private int i = 0;
    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // read data
        List<MS> msList1 = MgfFileReader.getAllSpectra(
                new File("D:\\@project\\program\\coding\\java\\cluster-comparer\\testdata\\mgf\\sample1.mgf"));
        List<MS> msList2 = MgfFileReader.getAllSpectra(
                new File("D:\\@project\\program\\coding\\java\\cluster-comparer\\testdata\\mgf\\sample2.mgf"));

        Button btn = new Button("next");
        btn.setOnAction(event -> {
            MS ms1 = msList1.get(i++);
            SimilarityScoreTabPane.setData(ms1, msList2, 0.5, 10);
            tabPane = SimilarityScoreTabPane.similarityScoreTabPane;
        });
        btn.fire();
        VBox vbox = new VBox(tabPane, btn);


        Scene scene = new Scene(vbox, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
