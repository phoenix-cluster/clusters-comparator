package cn.edu.cqupt.sankey;

import cn.edu.cqupt.graph.UndirectedGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SankeyDiagramTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UndirectedGraph<Vertex, Edge> undirectedGraph = new UndirectedGraph<>();
        VertexImpl v0 = new VertexImpl("A", 10.0, 0);
        VertexImpl v1 = new VertexImpl("B", 6.0, 1);
        VertexImpl v2 = new VertexImpl("C", 9.0, 1);
        VertexImpl v3 = new VertexImpl("D", 3.0, 2);
        VertexImpl v4 = new VertexImpl("E", 2.0, 2);
        undirectedGraph.addEdge(v0, v1, new EdgeImpl(6.0));
        undirectedGraph.addEdge(v0, v2, new EdgeImpl(4.0));
        undirectedGraph.addEdge(v2, v3, new EdgeImpl(3.0));
        undirectedGraph.addEdge(v2, v4, new EdgeImpl(2.0));
        SankeyDiagram sankey = new SankeyDiagram(undirectedGraph);
        sankey.initialize();


        // display sankey diagram
        Scene scene = new Scene(new StackPane(sankey.getGroup()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
