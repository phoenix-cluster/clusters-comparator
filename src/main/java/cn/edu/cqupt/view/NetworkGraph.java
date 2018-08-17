package cn.edu.cqupt.view;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.sankey.ClusteringSankeyDiagram;
import cn.edu.cqupt.service.NetworkGraphService;
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

    public NetworkGraph() {
        this.isZoomed = false;
        this.backupTab = new Tab();
    }

    public GridPane create(NetworkGraphService networkGraphService) {

        GridPane networkGraphPane = new GridPane();

        // obtain network graph(data)
        Vertex focusVertex = networkGraphService.getFocusVertex();
        UndirectedGraph<Vertex, Edge> graph = networkGraphService.getUndirectedGraph();
        String releaseIName = networkGraphService.getReleaseIName();
        String releaseIIName = networkGraphService.getReleaseIIName();

        // obtain sankey diagram
        ClusteringSankeyDiagram sankey = new ClusteringSankeyDiagram(releaseIName, releaseIIName, graph, focusVertex);
        GridPane sankeyontroller = sankey.getController();
        Group sankeyGroup = sankey.getGroup();

        // add button
        Button zoomButton = new Button("Zoom");
        VBox controller = new VBox(zoomButton, sankeyontroller);
        controller.setPadding(new Insets(5, 0, 5, 5));

        // add builds into network graph pane
        networkGraphPane.add(controller, 0, 0);
        networkGraphPane.add(sankeyGroup, 0, 1);


        // add event for button
        zoomButton.setOnAction((ActionEvent event) -> {
            if (isZoomed) {
                ClusterSelection.networkGraphStackPane.getChildren().add(new ScrollPane(networkGraphPane));
                ClusterApplication.tabPane.getTabs().get(0).setContent(backupTab.getContent());
                isZoomed = false;
            } else {
                backupTab.setContent(ClusterApplication.tabPane.getTabs().get(0).getContent()); // backup
                ClusterApplication.tabPane.getTabs().get(0).setContent(new ScrollPane(networkGraphPane)); // replace
                isZoomed = true;
            }
        });
        return networkGraphPane;
    }
}
