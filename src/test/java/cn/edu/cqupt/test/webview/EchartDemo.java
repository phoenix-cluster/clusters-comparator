package cn.edu.cqupt.test.webview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class EchartDemo extends Application {
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        URL url = EchartDemo.class.getResource("/pie-nest.html");
        System.out.println(url.toExternalForm());
        webEngine.load(url.toExternalForm());

        Scene scene = new Scene(webView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
