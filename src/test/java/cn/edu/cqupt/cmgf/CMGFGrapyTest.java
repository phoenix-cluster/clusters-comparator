package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.cmgf.graph.CMGFGraphService;
import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.sankey.Edge;
import cn.edu.cqupt.sankey.SankeyDiagram;
import cn.edu.cqupt.sankey.Vertex;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CMGFGrapyTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // read data
        try {
            CMGF[] cmgfArr = CMGFReader.readCMGF(new File("testdata/cmgf/sample.mgf"),
                    new File("testdata/cmgf/label1.txt"),
                    new File("testdata/cmgf/label2.txt"));

            CMGFGraphService service = new CMGFGraphService();
            service.prepareData(cmgfArr);
            service.printGraphList();
            UndirectedGraph<Vertex, Edge> graph = service.getUndirectedGraphList().get(0);

            // build graph
            SankeyDiagram sankey = new SankeyDiagram(graph);

            // display
            Scene scene = new Scene(sankey.getGroup());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
