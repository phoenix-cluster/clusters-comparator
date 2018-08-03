package cn.edu.cqupt.clustering.view;


import cn.edu.cqupt.websocket.EchartsServer;
import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.java_websocket.WebSocket;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class PeakMap {

    private final WebView webView;
    private boolean isFirstRun = true;

    public WebView getWebView() {
        return webView;
    }

    public PeakMap() {
        webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        URL url = PeakMap.class.getResource("/html/PeakMap.html");
        webEngine.load(url.toExternalForm());
    }


    public void updateData(String clusterId, List<Float> mzValue, List<Float> intensityValue) {
        Gson gson = new Gson();

        // format data
        PeakMapData[] peakMapData = formatData(clusterId, mzValue, intensityValue);
        String message = "{\"type\":\"PeakMap\", \"series\":" + gson.toJson(peakMapData) + "}";

        if (isFirstRun) {
            EchartsServer.webSocketMap.addListener(
                    (MapChangeListener.Change<? extends String, ? extends WebSocket> change) -> {
                        if (change.wasAdded()) {
                            if (change.getKey().matches("PeakMap")) {
                                System.out.println("send message to peak map: " + message);
                                change.getValueAdded().send(message);
                            }
                        }
                    });
            isFirstRun = false;
        } else {
            EchartsServer.webSocketMap.get("PeakMap").send(message);
        }
    }

    private PeakMapData[] formatData(String clusterId, List<Float> mzValue, List<Float> intensityValue) {
        PeakMapData[] peakMapData = new PeakMapData[mzValue.size()];
        IntStream.range(0, mzValue.size())
                .parallel()
                .forEach(index -> {
                    double[][] data = new double[][]{
                            new double[]{mzValue.get(index), 0},
                            new double[]{mzValue.get(index), intensityValue.get(index)}
                    };
                    peakMapData[index] = new PeakMapData(clusterId, "line",
                            false, data);
                });
        return peakMapData;
    }
}

class PeakMapData {
    private String name;
    private String type;
    private boolean showSymbol;
    private double[][] data;

    public PeakMapData(String name, String type, boolean showSymbol, double[][] data) {
        this.name = name;
        this.type = type;
        this.showSymbol = showSymbol;
        this.data = data;
    }
}

