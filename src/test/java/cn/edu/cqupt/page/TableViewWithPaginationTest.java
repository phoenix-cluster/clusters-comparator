package cn.edu.cqupt.page;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjs on 2018/4/6.
 */
public class TableViewWithPaginationTest extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        // data
        List<People> rowData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rowData.add(new People("No." + i, i));
        }

        // create object Page
        Page<People> page = new Page<>(rowData, 4);

        // create table
        TableView<People> table = new TableView<>();

        TableColumn<People, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<People, Integer> ageCol = new TableColumn("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        table.getColumns().addAll(nameCol, ageCol);


        // layout
        TableViewWithPagination tableViewWithPagination = new TableViewWithPagination(page, table);

        Scene scene = new Scene(tableViewWithPagination.getDefaultLayout(), 800, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}