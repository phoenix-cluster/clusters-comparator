package cn.edu.cqupt.sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Collision extends Application {

@Override
public void start(Stage stage) {

    Group root = new Group();
    Scene scene = new Scene(root, 600, 600, Color.CADETBLUE);

// Nodes
    Rectangle bounds = new Rectangle(500,500, Color.TRANSPARENT);
    bounds.setStroke(Color.WHITE);
    bounds.setStrokeWidth(5);
    bounds.setTranslateX(50);
    bounds.setTranslateY(50);

    Circle c1 = new Circle(30, Color.WHITE);
    c1.setTranslateX(300);
    c1.setTranslateY(300);

// Events

    // Collision Detection and c1 movement
    EventHandler<MouseEvent> handle = new EventHandler<MouseEvent>() {      

        double difX;    // Circle to mouse difference
        double difY;

        double reloX;   // Circle touches bounds
        double reloY;

        Bounds nodeBounds = c1.getBoundsInLocal();  // Bound Objects for Node and Rect
        Bounds bound = bounds.getBoundsInLocal();

        public void handle(MouseEvent event) {

            // If Pressed save Coordinates
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {

                difX = event.getX();
                difY = event.getY();      

            }
            // If Dragged check Collision and relocate
            else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

                c1.setTranslateX( event.getSceneX() - difX );
                c1.setTranslateY( event.getSceneY() - difY );


                if( c1.localToScene(nodeBounds).getMinX() < bounds.localToScene(bound).getMinX() ) {
                    bounds.setStroke(Color.RED);
                    c1.setTranslateX( bound.getMinX() - nodeBounds.getMinX() + 50 );
                }
                else if( c1.localToScene(nodeBounds).getMinY() < bounds.localToScene(bound).getMinY() ) { 
                    bounds.setStroke(Color.RED);
                    c1.setTranslateY( bound.getMinY() - nodeBounds.getMinY() + 50 );
                }
                else if( c1.localToScene(nodeBounds).getMaxX() > bounds.localToScene(bound).getMaxX() ) { 
                    bounds.setStroke(Color.RED);
                    c1.setTranslateX( bound.getMaxX() - nodeBounds.getMaxX() + 50 );
                }
                else if( c1.localToScene(nodeBounds).getMaxY() > bounds.localToScene(bound).getMaxY() ) { 
                    bounds.setStroke(Color.RED);
                    c1.setTranslateY( bound.getMaxY() - nodeBounds.getMaxY() + 50 );
                }                   
                else {
                    bounds.setStroke(Color.WHITE);
                }

            }       


        }
    };

    c1.addEventHandler(MouseEvent.ANY, handle);

//Stage is setUp
    root.getChildren().addAll(bounds, c1);
    stage.setTitle("omfg, srsly !?");
    stage.setScene(scene);
    stage.show();
}

public static void main(String[] args) {
    launch(args);}}