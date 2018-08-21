package cn.edu.cqupt.websocket;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class EchartsServer extends WebSocketServer {

//    private ObservableMap<String, WebSocket> messagesMap = FXCollections.observableMap(new HashMap<>());

    //    public ObservableMap<String, WebSocket> getMessagesMap() {
//        return messagesMap;
//    }
    public static ObservableMap<String, WebSocket> webSocketMap =
            FXCollections.observableMap(new HashMap<>());
    private Gson gson = new Gson();

    public EchartsServer(int port) throws UnknownHostException {
        super(new InetSocketAddress("localhost", port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("1. " + webSocket + " came!!!");
        System.out.println("2. " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " came!!!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println(webSocket + " left");

    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        System.out.println("message from " + webSocket + ": " + message);
        Parameters params = gson.fromJson(message, Parameters.class);
//        // it suggests WebView has been loaded if get the message "id:xx"
//        Pattern p = Pattern.compile("\\{\"type\"\\s*:\\s*\"(\\w+)\"}");
//        Matcher m = p.matcher(message);
//        if (m.find()) {
//            System.out.println("id = " + m.group(1));
//            switch (m.group(1)) {
//                case "PieChart":
//                    webSocketMap.put("PieChart", webSocket);
//                    break;
//                case "PieChartClickEvent":
//                    PieChart.createClusterComparison()
//            }
//        }

        switch (params.getType()) {
            case "PieChart":
                webSocketMap.put("PieChart", webSocket);
                break;
            case "PeakMap":
                webSocketMap.put("PeakMap", webSocket);
                break;
            case "PieChartClickEvent":
//                System.out.println("PieChart.createClusterComparison():" + params.getData());
//                PieChart.createClusterComparison(params.getData());
                webSocketMap.put(message, webSocket);
                break;
        }
    }


    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    public void sendMessage(WebSocket webSocket, String message) {
        webSocket.send(message);
    }
}

