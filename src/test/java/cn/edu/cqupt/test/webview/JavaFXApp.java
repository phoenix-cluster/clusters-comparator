package cn.edu.cqupt.test.webview;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Map;

public class JavaFXApp extends Application {

    private WebView webView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        createBrowse();
        Scene scene = new Scene(webView, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String str;
    private Map<String, String> map;
    private void createBrowse() {

        str = "hello";
        map = new HashMap<>();
        map.put("A", "B");

        webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        JavaFXApp app = new JavaFXApp();
        webEngine.load(this.getClass().getResource("/book.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (Worker.State.SUCCEEDED == newValue) {
                        JSObject win = (JSObject) webEngine.executeScript("window");
                        win.setMember("app", app);
                    }
                }
        );

    }

    public void print() {
        System.out.println("str=" + str);
        System.out.println("map=" + map.get("A"));
    }

}



