package cn.edu.cqupt.page;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangjs on 2018/4/6.
 */
public class TableViewWithPagination<T> {

    private Page<T> page;
    private TableView<T> tableView;
    private Pagination tableViewWithPaginationPane;

    // the head of column => order way
    private Map<Label, Order> orderMap;

    public Page<T> getPage() {
        return page;
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    public Pagination getTableViewWithPaginationPane() {
        return tableViewWithPaginationPane;
    }

    public TableViewWithPagination(Page<T> page, TableView<T> tableView) {
        this.page = page;
        this.tableView = tableView;
        orderMap = new HashMap<>();
        tableViewWithPaginationPane = new Pagination();
        tableViewWithPaginationPane.pageCountProperty().bindBidirectional(page.totalPageProperty());
        tableViewWithPaginationPane.currentPageIndexProperty().bindBidirectional(page.currentPageProperty());
        updatePagination();
    }

    private void updatePagination() {
        tableViewWithPaginationPane.setPageFactory(pageIndex -> {
            tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
            return tableView;
        });
    }

    public HBox createContoller() {

        // page size controller
        TextField pageSizeTF = new TextField(page.getPageSize() + "");
        pageSizeTF.setPrefWidth(40.0);
        pageSizeTF.setOnMouseClicked((MouseEvent event) ->
                pageSizeTF.clear()
        );
        Label pageSizeLabel = new Label("Per Page");

        // redirect page index controller
        Label redirectLabel1 = new Label("To");
        TextField redirectTF = new TextField(tableViewWithPaginationPane.getCurrentPageIndex() + 1 + "");
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
                        page.setPageSize(Integer.parseInt(pageSizeText));
                        updatePagination();
                    }
                }

                // set redirect page index controller
                if (inputText.length() != 0 && !inputText.equals(tableViewWithPaginationPane.getCurrentPageIndex() + 1 + "")) {
                    Matcher match = pattern.matcher(inputText);
                    if (match.find()) {
                        int currentPageIndex = Integer.parseInt(inputText) - 1;
                        tableViewWithPaginationPane.setCurrentPageIndex(currentPageIndex);
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

    public BorderPane getDefaultLayout() {

        HBox controller = createContoller();
        BorderPane pane = new BorderPane();
        pane.setCenter(tableViewWithPaginationPane);
        pane.setBottom(controller);
        return pane;
    }


    public void addGlobalOrdering(TableColumn<T, ?> column,
                                  Comparator<? super T> ascComparator,
                                  Comparator<? super T> descComparator) {
        System.out.println("column.getText() : " + column.getText());

        /** label setting **/
        Label label = new Label(column.getText());
        label.setMinWidth(column.getMinWidth());
        label.setMaxWidth(column.getMaxWidth());
        label.setPrefWidth(column.getPrefWidth());
        orderMap.put(label, Order.NO);

        /** column setting **/
        column.setText(null);
        column.setGraphic(label);

        // turn off built-in order in TableView
        column.setSortable(false);

        ImageView ascImg = new ImageView("/image-app/asc.png");
        ImageView descImg = new ImageView("/image-app/desc.png");
        ImageView noImg = new ImageView("/image-app/no.png");
        label.setOnMouseClicked(mouseEvent -> {
            orderMap.keySet().stream().forEach(lab -> lab.setGraphic(noImg));
            switch (orderMap.get(label)) {
                case NO:
                    orderMap.replace(label, Order.ASC);
                    label.setGraphic(ascImg);
                    order(ascComparator);
                    updatePagination();
                    break;
                case ASC:
                    orderMap.put(label, Order.DESC);
                    label.setGraphic(descImg);
                    order(descComparator);
                    updatePagination();
                    break;
                case DESC:
                    orderMap.put(label, Order.ASC);
                    label.setGraphic(ascImg);
                    order(ascComparator);
                    updatePagination();
                    break;
            }
        });
    }

    private void order(Comparator<? super T> comparator) {
        Collections.sort(page.getRowDataList(), comparator);
    }
}

enum Order {
    NO, ASC, DESC
}
