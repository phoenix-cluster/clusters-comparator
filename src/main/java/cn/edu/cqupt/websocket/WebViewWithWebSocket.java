package cn.edu.cqupt.websocket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebViewWithWebSocket extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(this.getClass().getResource("/html/echarts-messages-deliver.html").toExternalForm());
        System.out.println(this.getClass().getResource("/html/echarts-messages-deliver.html").toExternalForm());

        Scene scene = new Scene(webView, 1000, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
