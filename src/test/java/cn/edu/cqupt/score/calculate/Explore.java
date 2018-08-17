package cn.edu.cqupt.score.calculate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Explore extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String id = "No." + i;
            double weight = 99.8 + i;
            Button btn = new Button("Click");
            btn.setOnAction(e -> {
                System.out.println("a button was clicked!");
            });
            itemList.add(new Item(id, weight, btn));
        }

        long time0 = System.currentTimeMillis();
        FXCollections.observableList(itemList);
        long time1 = System.currentTimeMillis();
        System.out.println("transform data cost " + (time1 - time0) + "ms");
    }
}

class Item {
    private String id;
    private double weight;
    private Button btn;

    public Item(String id, double weight, Button btn) {
        this.id = id;
        this.weight = weight;
        this.btn = btn;
    }
}
