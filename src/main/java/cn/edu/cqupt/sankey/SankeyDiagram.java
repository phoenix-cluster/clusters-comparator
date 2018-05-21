package cn.edu.cqupt.sankey;

import cn.edu.cqupt.graph.UndirectedGraph;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class SankeyDiagram {

    /* input parameters */
    private UndirectedGraph<Vertex, Edge> undirectedGraph;

    /* layout setting */
    // scale factor for all components
    private SimpleDoubleProperty componentsFactor;

    // the vertical distance of two vertices
    private SimpleDoubleProperty vGap;

    // the horizontal distance of two layers
    private SimpleDoubleProperty hGap;

    // the width of vertex
    private SimpleDoubleProperty vertexWidth;

    // vertex => shape
    private HashMap<Vertex, Rectangle> vertexShape;

    // store all shapes of edges into a list
    private ArrayList<Path> edgesShapes;

    /* intermediate calculation value */
    // vertex => the layer number of the vertex
    private HashMap<Vertex, Integer> layers;

    // initial value: the Y coordinate of the upper-left corner of each vertex
    private HashMap<Vertex, Double> preVertexEndY;

    // total layer number
    private int totalLayerNumber;

    // something will not need run when adjusting controller(not run first)
    private boolean firstRunFlag;

    /* result */
    private Group group;
    private GridPane controller;

    public Group getGroup() {
        return group;
    }

    public GridPane getController() {
        return controller;
    }

    public SankeyDiagram(UndirectedGraph<Vertex, Edge> undirectedGraph) {
        this.undirectedGraph = undirectedGraph;

        // set default value
        componentsFactor = new SimpleDoubleProperty(1.0);
        vGap = new SimpleDoubleProperty(5.0);
        hGap = new SimpleDoubleProperty(100.0);
        vertexWidth = new SimpleDoubleProperty(10.0);
        firstRunFlag = true;

        // initialize
        vertexShape = new HashMap<>();
        preVertexEndY = new HashMap<>();
        edgesShapes = new ArrayList<>();
        layers = new HashMap<>();

        // return value
        group = new Group();
        controller = new GridPane();
        firstRunFlag = true;
    }

    public void initialize() {

        // arrange layer and calculate the total length of each layer
        Map<Integer, Double> layersLength = undirectedGraph.getAllVertices()
                .stream()
                .collect(groupingBy(Vertex::getLayer,
                        Collectors.reducing(-vGap.get(), Vertex::getWeight, (w1, w2) -> w1 + w2 + vGap.get())));

        // get the maximum length in layers for layout
        double maxLayersLength = layersLength.values().stream()
                .mapToDouble(v -> v.doubleValue())
                .max()
                .orElseThrow(NoSuchElementException::new);

        /* arrange the location to vertices */
        double[] layersPreEndY = new double[layersLength.size()];
        for (Vertex vertex : undirectedGraph.getAllVertices()) {
            int layer = vertex.getLayer();
            double startX = layer * (vertexWidth.get() + hGap.get());
            double startY;
            if (layersPreEndY[layer] == 0.0) {
                startY = (maxLayersLength - layersLength.get(new Integer(layer))) / 2;
            } else {
                startY = layersPreEndY[layer] + vGap.get();
            }
            layersPreEndY[layer] = startY + vertex.getWeight();

            // create vertex shape
            vertexShape.put(vertex, new Rectangle(startX, startY, vertexWidth.get(), vertex.getWeight()));
            preVertexEndY.put(vertex, startY);
        }


        // add vertices into canvas
        group.getChildren().addAll(vertexShape.values());

        /* arrange the location to edges */
        HashMap<Vertex, HashMap<Vertex, Edge>> unredundantAdjacencyTable = undirectedGraph.getUnredundantAdjacencyTable();
        for (Vertex vertex1 : unredundantAdjacencyTable.keySet()) {
            for (Vertex vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
                // set start and end point coordinate
                double startX1, startY1, endX1, endY1, startX2, startY2, endX2, endY2;
                double edgeWeight = unredundantAdjacencyTable.get(vertex1).get(vertex2).getWeight();
                if (vertex1.getLayer() < vertex2.getLayer()) {

                    // start point coordinate
                    startX1 = vertexShape.get(vertex1).getX() + vertexWidth.get();
                    startY1 = preVertexEndY.get(vertex1);

                    // end point coordinate
                    startX2 = vertexShape.get(vertex2).getX();
                    startY2 = preVertexEndY.get(vertex2);
                } else {

                    // start point coordinate
                    startX1 = vertexShape.get(vertex2).getX() + vertexWidth.get();
                    startY1 = preVertexEndY.get(vertex2);

                    // end point coordinate
                    startX2 = vertexShape.get(vertex1).getX();
                    startY2 = preVertexEndY.get(vertex1);
                }
                endX1 = startX1;
                endY1 = startY1 + edgeWeight;
                endX2 = startX2;
                endY2 = startY2 + edgeWeight;

                // save Y coordinate of previous vertex
                preVertexEndY.replace(vertex1, endY1);
                preVertexEndY.replace(vertex2, endY2);

                /* create edges and add into canvas */
                if (firstRunFlag) {
                    Path path = new Path();
                    MoveTo moveTo = new MoveTo();
                    moveTo.setX(startX1);
                    moveTo.setY(startY1);

                    // line1
                    LineTo lineTo1 = new LineTo();
                    lineTo1.setX(startX2);
                    lineTo1.setY(startY2);

                    // line2
                    LineTo lineTo2 = new LineTo();
                    lineTo2.setX(endX2);
                    lineTo2.setY(endY2);

                    // line3
                    LineTo lineTo3 = new LineTo();
                    lineTo3.setX(endX1);
                    lineTo3.setY(endY1);

                    // path setting
                    path.setStrokeWidth(0);
                    path.setStrokeType(StrokeType.OUTSIDE);
                    path.setFill(Color.web("#808080", 0.4));

                    // add elements
                    path.getElements().addAll(moveTo, lineTo1, lineTo2, lineTo3);

                    // add edge into canvas
                    group.getChildren().add(path);
                }
            }
        }
    }
}
