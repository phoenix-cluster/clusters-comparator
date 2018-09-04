package cn.edu.cqupt.main;

import cn.edu.cqupt.main.data.stage.ClusteringDataStage;
import cn.edu.cqupt.main.data.stage.MgfDataStage;
import cn.edu.cqupt.util.TabPaneExpansion;
import cn.edu.cqupt.websocket.EchartsServer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class Application extends javafx.application.Application {

    public static String releaseIName;
    public static String releaseIIName;
    public static SimpleIntegerProperty pageSize; //the number of each page
    public static TabPaneExpansion tabPaneExpansion;
    public static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getBounds();
    public static EchartsServer echartsServer;

    static {
        pageSize = new SimpleIntegerProperty(8);
        tabPaneExpansion = new TabPaneExpansion();
//        tabPaneExpansion.addTabs(new Tab("Introduction"),
//                new Tab("Cluster Selection"),
//                new Tab("Cluster Comparison"));

        // echart WebSocket
        int port = 57861;
        try {
            echartsServer = new EchartsServer(port);
            echartsServer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        // menu
        MenuBar menu = getApplicationMenu(primaryStage);

        // introduce
        WebView introductionPane = getIntroductionPane();
        Tab introductionTab = new Tab("Introduction");
        introductionTab.setContent(introductionPane);

        // set builds layout
        tabPaneExpansion.addTab(introductionTab);
        root.setCenter(tabPaneExpansion.getTabPane());
        root.setTop(menu);

        // scene
        Scene scene = new Scene(root);

        // set stage
        double width = SCREEN_BOUNDS.getWidth();
        double height = SCREEN_BOUNDS.getHeight() - 100;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cluster Comparer GUI");
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.setOnCloseRequest(event -> {
            try {
                echartsServer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


    private MenuBar getApplicationMenu(Window ownerWindow) {

        // create menu bar
        MenuBar menuBar = new MenuBar();

        /** first level menu: create File menu **/
        Menu fileMenu = new Menu("File");

        // second level menu: import data menu
        Menu importData = new Menu("Import Data");

        // second level menu: import data from different format
        MenuItem mgfMenuItem = new MenuItem("*.mgf");
        mgfMenuItem.setOnAction(e -> {
            MgfDataStage dataStage = new MgfDataStage();
            Stage subStage = new Stage();
            Scene scene = new Scene(dataStage.create(subStage));
            subStage.setScene(scene);
            subStage.showAndWait();
        });

        MenuItem clusteringMenuItem = new MenuItem("*.clustering");
        clusteringMenuItem.setOnAction(e -> {
            ClusteringDataStage dataStage = new ClusteringDataStage();
            Stage subStage = new Stage();
            Scene scene = new Scene(dataStage.create(subStage));
            subStage.setScene(scene);
            subStage.showAndWait();

            /** analysis and visualization **/
            // cluster selection pane

            ClusterSelection clusterSelection = new ClusterSelection(dataStage.getClusteringFileI(), dataStage.getClusteringFileII());
//            File file2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\cli_clustering.pxd000021.0.7_4.clustering");
//            File file1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\hdp_clustering.pxd000021.0.7_4.clustering");

//            //little data
//            File file1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_5.clustering");
//            File file2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_6.clustering");
//            // missing data example
//            File file1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_7.clustering");
//            File file2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\compare_8.clustering");

//            ClusterSelection clusterSelection = new ClusterSelection(file1, file2);
            GridPane clusterSelectionPane = null;
            try {
                clusterSelectionPane = clusterSelection.create();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Tab clusterSelectionTab = new Tab("Cluster Selection");
            clusterSelectionTab.setContent(clusterSelectionPane);
            tabPaneExpansion.removeTab("Introduction");
            tabPaneExpansion.addTab(clusterSelectionTab);
            tabPaneExpansion.getTabPane().getSelectionModel().select(clusterSelectionTab);

        });

        MenuItem restfulMenuItem = new MenuItem("restful");

        importData.getItems().addAll(mgfMenuItem, clusteringMenuItem, restfulMenuItem);


        // second level menu: exit
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(
                (ActionEvent e) -> {
                    try {
                        echartsServer.stop();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(0);
                }
        );

        // add menu item for file menu
        fileMenu.getItems().addAll(importData, exit);

        // create Tools menu
        Menu toolMenu = new Menu("Tools");

        // add menu items into menu bar
        menuBar.getMenus().addAll(fileMenu, toolMenu);
        return menuBar;
    }

    private WebView getIntroductionPane() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://github.com/phoenix-cluster/clusters-comparator/blob/master/README.md");
        return webView;
    }

}
