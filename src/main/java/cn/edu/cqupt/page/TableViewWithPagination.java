package cn.edu.cqupt.page;

import cn.edu.cqupt.view.ClusterApplication;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangjs on 2018/4/6.
 */
public class TableViewWithPagination {

    public static Pagination create(Page page, TableView tableView) {
        Pagination pagination = new Pagination(page.getTotalPage(), 0);
        pagination.setPageFactory(pageIndex -> {
                tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
                return tableView;
        });
        return pagination;
    }

    public static HBox createContoller(Page page, TableView tableView, Pagination pagination){

        // page size controller
        TextField pageSizeTF = new TextField(page.getPageSize() + "");
        pageSizeTF.setPrefWidth(40.0);
        pageSizeTF.setOnMouseClicked((MouseEvent event) -> {
            pageSizeTF.clear();
        });
        Label pageSizeLabel = new Label("Per Page");

        // redirect page index controller
        Label redirectLabel1 = new Label("To");
        TextField redirectTF = new TextField(pagination.getCurrentPageIndex() + 1 + "");
        redirectTF.setOnMouseClicked((MouseEvent event) -> {
            redirectTF.setText("");
        });
        Label redirectLabel2 = new Label("Page");
        redirectTF.setPrefWidth(40.0);

        // submit
        Button submit = new Button("submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            Pattern pattern = Pattern.compile("\\b[1-9][0-9]*\\b");

            @Override
            public void handle(ActionEvent event) {
                String pageSizeText = pageSizeTF.getText().trim();
                String inputText = redirectTF.getText().trim();

                // set page size controller
                if (pageSizeText.length() != 0 && !pageSizeText.equals(page.getPageSize() + "")) {
                    Matcher matcher = pattern.matcher(pageSizeText);
                    if (matcher.find()) {
                        page.changePageSize(Integer.parseInt(pageSizeText));
                        pagination.setCurrentPageIndex(0);
                        pagination.setPageCount(page.getTotalPage());
                        pagination.setPageFactory(pageIndex -> {
                            tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
                            return tableView;
                        });
                    }
                }

                // set redirect page index controller
                if (inputText.length() != 0 && !inputText.equals(pagination.getCurrentPageIndex() + 1 + "")) {
                    Matcher match = pattern.matcher(inputText);
                    if (match.find()) {
                        int currentPageIndex = Integer.parseInt(inputText) - 1;
                        pagination.setCurrentPageIndex(currentPageIndex);
                    }
                }
            }
        });
        HBox controller = new HBox(pageSizeTF, pageSizeLabel, redirectLabel1, redirectTF, redirectLabel2, submit);
        controller.setAlignment(Pos.CENTER);
        controller.setSpacing(5.0);
        HBox.setMargin(pageSizeLabel, new Insets(0, 15, 0, 0));
        HBox.setMargin(submit, new Insets(0, 0, 0, 10));
        return controller;
    }

    public static BorderPane createByDefaultLayout(Page page, TableView tableView){
        Pagination pagination = create(page, tableView);
        HBox controller = createContoller(page, tableView, pagination);
        BorderPane pane = new BorderPane();
        pane.setCenter(pagination);
        pane.setBottom(controller);
        return pane;
    }
}
