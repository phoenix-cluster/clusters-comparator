package cn.edu.cqupt.mgf.view;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by huangjs on 2018/4/6.
 */
public class MgfInfoDisplayPaneTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<MS> msList1 = MgfFileReader.getAllSpectra(new File("C:\\Users\\huangjs\\Desktop\\mgf\\header_test.mgf"));
        List<MS> msList2 = MgfFileReader.getAllSpectra(new File("C:\\Users\\huangjs\\Desktop\\mgf\\header_test.mgf"));
        MgfInfoDisplayPane pane = new MgfInfoDisplayPane("header_test.mgf", "header_test.mgf", msList1, msList2, 8);

        Scene scene = new Scene(pane.getMgfInfoDisplayPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
