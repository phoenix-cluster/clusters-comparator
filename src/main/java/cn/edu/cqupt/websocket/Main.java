package cn.edu.cqupt.websocket;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        int port = 57861;
        try {
            EchartsServer echartsServer = new EchartsServer(port);
            System.out.println(echartsServer.getAddress().getHostName());
            System.out.println(echartsServer.getAddress().getHostString());
            echartsServer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
