package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.main.Application;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.util.ColorUtil;
import cn.edu.cqupt.view.ClusterApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.Callback;

import java.util.List;

/**
 * Function:
 * 1. display a cluster's spectra
 * 2. display two cluster's spectra in two different tables and make overlap spectra highlight
 *
 * @author huangjs
 */
public class SpectrumTable {

    /**
     * only generate a table view framework and no data is filled
     */
    public TableView<Spectrum> createSpectrumTableView() {
        TableView<Spectrum> spectrumTableView = new TableView<>();

        /** set table: table head and table cell value type **/
        // id
        TableColumn<Spectrum, String> idCol = new TableColumn<>("Spectrum ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("id"));
        idCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTableView.getColumns().add(idCol);

        // sequence
        TableColumn<Spectrum, String> seqCol = new TableColumn<>("Sequence");
        seqCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("sequence"));
        // set event for column sequence: open unipept link using default system browser
        seqCol.setCellFactory(new Callback<TableColumn<Spectrum, String>, TableCell<Spectrum, String>>() {

            @Override
            public TableCell<Spectrum, String> call(TableColumn<Spectrum, String> param) {
                TableCell<Spectrum, String> cell = new TableCell<Spectrum, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (!empty) {
                            String[] seqs = item.split(",");
                            VBox seqsLink = new VBox();
                            for (int i = 0; i < seqs.length; i++) {
                                Hyperlink link = new Hyperlink(seqs[i]);
                                link.setOnAction((ActionEvent e) -> {
                                    ClusterApplication.hostServices.showDocument(
                                            "https://unipept.ugent.be/sequences/" + link.getText() + "/equateIL");
                                });
                                seqsLink.getChildren().add(link);
                            }
                            setGraphic(seqsLink);
                        }
                    }
                };
                return cell;
            }
        });
        seqCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTableView.getColumns().add(seqCol);

        // charge
        TableColumn<Spectrum, Float> chaCol = new TableColumn<>("Charge");
        chaCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("Charge"));
        chaCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTableView.getColumns().add(chaCol);

        // precursorMz
        TableColumn<Spectrum, Float> preCol = new TableColumn<>("PrecursorMz");
        preCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("precursorMz"));
        preCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTableView.getColumns().add(preCol);

        // species
        TableColumn<Spectrum, String> speCol = new TableColumn<>("Species");
        speCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("species"));
        speCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTableView.getColumns().add(speCol);

        // set size
        double width = Application.SCREEN_BOUNDS.getWidth() * 0.5;
        spectrumTableView.setMaxWidth(width);
        spectrumTableView.setMinWidth(width);
        spectrumTableView.setPrefWidth(width);

        // set column resize policy
        spectrumTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return spectrumTableView;
    }

    /**
     * create a table to show a cluster's spectra
     * @param spectrumTableView a spectrum table view framework with no data
     * @param spectra a cluster's spectra
     * @return spectrum table view after formatting
     */
    public TableView<Spectrum> setTableView(TableView<Spectrum> spectrumTableView, List<Spectrum> spectra) {

        // set data
        ObservableList<Spectrum> observableTableData = FXCollections.observableList(spectra);
        spectrumTableView.setItems(observableTableData);

        return spectrumTableView;
    }

    /**
     * display overlap spectra of two cluster
     * @param spectrumTableView1
     * @param spectrumTableView2
     * @param releaseIName
     * @param releaseIIName
     * @param cluster1
     * @param cluster2
     * @param overlapSpectra
     * @return
     */
    public GridPane setTableView(
            TableView<Spectrum> spectrumTableView1,
            TableView<Spectrum> spectrumTableView2,
            String releaseIName, String releaseIIName,
                    Cluster cluster1, Cluster cluster2,
                    List<Spectrum> overlapSpectra) {

        // the component of two table and some small builds
        GridPane overlapSpectraPane = new GridPane();
        overlapSpectraPane.setPadding(new Insets(10, 0, 0, 0));
//		this.overlapSpectraPane.setHgap(10);

        // spectrum table
        setTableView(spectrumTableView1, cluster1.getSpectra());
        setTableView(spectrumTableView2, cluster2.getSpectra());

        // cluster message
        Label clusterLabel1 = new Label("Cluster ID: " + cluster1.getId());
        clusterLabel1.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label releaseLabel1 = new Label("from " + releaseIName);
        releaseLabel1.setFont(Font.font("Arial", FontWeight.THIN, 10));
        Label specCountLab1 = new Label("Spectra Count: " + cluster1.getSpectra().size());
        specCountLab1.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        HBox hbox1 = new HBox(clusterLabel1, releaseLabel1);
        VBox vbox1 = new VBox(hbox1, specCountLab1);

        Label clusterLabel2 = new Label("Cluster ID: " + cluster2.getId());
        clusterLabel2.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label releaseLabel2 = new Label("from " + releaseIIName);
        releaseLabel2.setFont(Font.font("Arial", FontWeight.THIN, 10));
        Label specCountLab2 = new Label("Spectra Count: " + cluster2.getSpectra().size());
        specCountLab2.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        HBox hbox2 = new HBox(clusterLabel2, releaseLabel2);
        VBox vbox2 = new VBox(hbox2, specCountLab2);

        // add builds in grid pane
        overlapSpectraPane.add(vbox1, 0, 0);
        overlapSpectraPane.add(vbox2, 1, 0);
        overlapSpectraPane.add(spectrumTableView1, 0, 1);
        overlapSpectraPane.add(spectrumTableView2, 1, 1);

        // render row
        renderRow(spectrumTableView1, overlapSpectra, Color.BEIGE);
        renderRow(spectrumTableView2, overlapSpectra, Color.BEIGE);

        return overlapSpectraPane;
    }

    /**
     * render overlap row
     *
     * @param spectrumTable
     * @param overlapSpectrums
     * @param color
     */
    private void renderRow(TableView<Spectrum> spectrumTable, List<Spectrum> overlapSpectrums, Color color) {
        spectrumTable.setRowFactory(row -> new TableRow<Spectrum>() {

            @Override
            public void updateItem(Spectrum item, boolean empty) {
                if (item != null && !empty) {
                    if (overlapSpectrums.indexOf(item) != -1)
                        setStyle("-fx-background-color: " + ColorUtil.colorToHex(color));
                    else
                        setStyle("");
                }
            }
        });
    }
}
