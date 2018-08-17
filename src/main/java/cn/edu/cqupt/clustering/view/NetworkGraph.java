package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.sankey.ClusteringSankeyDiagram;
import cn.edu.cqupt.service.NetworkGraphService;
import cn.edu.cqupt.view.ClusterApplication;
import cn.edu.cqupt.view.ClusterSelection;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class NetworkGraph {

    private boolean isZoomed;
    private Tab backupTab;  //TabA's backup
    private GridPane networkGraphPane;

    public GridPane getNetworkGraphPane() {
        return networkGraphPane;
    }

    public NetworkGraph() {
        isZoomed = false;
        backupTab = new Tab();
        networkGraphPane = new GridPane();
    }

    public GridPane create(String releaseIName, String releaseIIName,
                           UndirectedGraph<Vertex, Edge> graph, Vertex focusedVertex) {

        // obtain sankey diagram
        ClusteringSankeyDiagram sankey = new ClusteringSankeyDiagram(releaseIName, releaseIIName, graph, focusedVertex);
        GridPane sankeyontroller = sankey.getController();
        Group sankeyGroup = sankey.getGroup();

        // add button
        Button zoomButton = new Button("Zoom");
        VBox controller = new VBox(zoomButton, sankeyontroller);
        controller.setPadding(new Insets(5, 0, 5, 5));

        // add builds into network graph pane
        VBox garbageCollection = new VBox();
        garbageCollection.getChildren().addAll(networkGraphPane.getChildren());
        garbageCollection = null;
        networkGraphPane.add(controller, 0, 0);
        networkGraphPane.add(sankeyGroup, 0, 1);


        // add event for button
//        zoomButton.setOnAction((ActionEvent event) -> {
//            if (isZoomed) {
//                ClusterSelection.networkGraphStackPane.getChildren().add(new ScrollPane(networkGraphPane));
//                ClusterApplication.tabPane.getTabs().get(0).setContent(backupTab.getContent());
//                isZoomed = false;
//            } else {
//                backupTab.setContent(ClusterApplication.tabPane.getTabs().get(0).getContent()); // backup
//                ClusterApplication.tabPane.getTabs().get(0).setContent(new ScrollPane(networkGraphPane)); // replace
//                isZoomed = true;
//            }
//        });
        return networkGraphPane;
    }
}
