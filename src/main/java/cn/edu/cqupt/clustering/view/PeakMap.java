package cn.edu.cqupt.clustering.view;

import com.google.gson.Gson;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;

public class PeakMap {

    private WebView webView;

    public WebView getWebView() {
        return webView;
    }

    public PeakMap() {
        webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        URL url = PeakMap.class.getResource("/html/PeakMap.html");
        webEngine.load(url.toExternalForm());
    }


    public void updateData(double[] mzValue, double[] intensityValue) {
        WebEngine webEngine = webView.getEngine();

        // convert java object to json string
        Gson gson = new Gson();
        String mzValueStr = gson.toJson(mzValue);
        String intensityValueStr = gson.toJson(intensityValue);

        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends State> ov, State oldState,
                 State newState) -> {
                    if (newState == State.SUCCEEDED) {
                        String test = (String) webEngine.executeScript("test()");
                        System.out.println(test);

                        webEngine.executeScript("updateSingleSpectrum(" + mzValueStr + "," + intensityValueStr + ")");
                    }
                });
//        webView.getEngine().executeScript("test()");
//        webView.getEngine().executeScript("option.xAxis[0].data=" + data);
//        webView.getEngine().executeScript("myChart.setOption(option, true)");
    }
}
