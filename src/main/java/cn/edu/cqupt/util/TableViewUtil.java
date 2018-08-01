package cn.edu.cqupt.util;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.*;

import java.text.NumberFormat;

/**
 * refer to: https://gist.github.com/Roland09/6fb31781a64d9cb62179
 */
public class TableViewUtil {
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     * add copy feature for table view
     * CTRL + C <=> copy to clipboard
     *
     * @param tableView
     */
    public static void installCopyHandler(TableView<?> tableView) {
        tableView.setOnKeyPressed(new TableKeyEventHandler());
    }

    private static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C,
                KeyCodeCombination.CONTROL_ANY);

        @Override
        public void handle(KeyEvent event) {
            if (copyKeyCodeCombination.match(event)) {
                if (event.getSource() instanceof TableView) {
                    copySelectionToClipboard((TableView<?>) event.getSource());
                }
            }

        }
    }

    /**
     * get table selection and copy to the clipboard
     *
     * @param tableView
     */
    private static void copySelectionToClipboard(TableView<?> tableView) {
        StringBuilder clipboardString = new StringBuilder();
        ObservableList<TablePosition> positionList = tableView.getSelectionModel().getSelectedCells();
        int preRow = -1;
        for (TablePosition pos : positionList) {
            int row = pos.getRow();
            int col = pos.getColumn();

            if (preRow == row) {
                clipboardString.append("\t");
            } else if (preRow != -1) {
                clipboardString.append("\n");
            }

            // create string from cell
            String text = null;

            Object observableValue = tableView.getColumns().get(col).getCellObservableValue(row);

            if (observableValue == null) {
                text = "\t";
            } else if (observableValue instanceof DoubleProperty) {
                text = numberFormat.format(((DoubleProperty) observableValue).get());
            } else if (observableValue instanceof IntegerProperty) {
                text = numberFormat.format(((IntegerProperty) observableValue).get());
            } else if (observableValue instanceof StringProperty) {
                text = numberFormat.format(((StringProperty) observableValue).get());
            } else if (observableValue instanceof BooleanProperty) {
                text = ((BooleanProperty) observableValue).get() + "";
            } else if (observableValue instanceof ObjectProperty) {
                text = ((ObjectProperty) observableValue).get() + "";
            } else {
                System.out.println("Unsupported observable value: " + observableValue);
            }
            clipboardString.append(text);
            preRow = row;
        }

        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

}
