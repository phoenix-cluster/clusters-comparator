package cn.edu.cqupt.view;

import java.util.List;

import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.util.ColorUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * Function:
 * 1. display a cluster's spectra
 * 2. display two cluster's spectra in two different tables and make overlap spectra highlight
 *
 * @author huangjs
 */
public class SpectrumTable {

    private TableView<Spectrum> spectrumTable; // display a cluster's spectra
    private GridPane overlapSpectraPane; // display overlap spectra of two cluster

    public TableView<Spectrum> getSpectrumTable() {
        return spectrumTable;
    }

    public GridPane getOverlapSpectraPane() {
        return overlapSpectraPane;
    }

    public SpectrumTable() {

    }

    public SpectrumTable(List<Spectrum> spectra) {
        spectrumTable = new TableView<>();
        ObservableList<Spectrum> observableTableData = FXCollections.observableList(spectra);

        // set table: table head and table cell value type
        // id
        TableColumn<Spectrum, String> idCol = new TableColumn<>("Spectrum ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("id"));
        idCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTable.getColumns().add(idCol);

        // sequence
        TableColumn<Spectrum, String> seqCol = new TableColumn<>("Sequence");
        seqCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("sequence"));
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

                                // open unipept link using default system browser
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
        spectrumTable.getColumns().add(seqCol);

        // charge
        TableColumn<Spectrum, Float> chaCol = new TableColumn<>("Charge");
        chaCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("Charge"));
        chaCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTable.getColumns().add(chaCol);

        // precursorMz
        TableColumn<Spectrum, Float> preCol = new TableColumn<>("PrecursorMz");
        preCol.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("precursorMz"));
        preCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTable.getColumns().add(preCol);

        // species
        TableColumn<Spectrum, String> speCol = new TableColumn<>("Species");
        speCol.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("species"));
        speCol.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14; -fx-alignment: CENTER");
        spectrumTable.getColumns().add(speCol);

        // set size
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        spectrumTable.setMinSize(bounds.getWidth() * 0.5, bounds.getHeight() * 0.45);

        // set column resize policy
        spectrumTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // set data
        spectrumTable.setItems(observableTableData);
    }

    public SpectrumTable(String releaseIName, String clusterId1, List<Spectrum> spectra1,
                         String releaseIIName, String clusterId2, List<Spectrum> spectra2, List<Spectrum> overlapSpectrums) {

        // the component of two table and some small builds
        this.overlapSpectraPane = new GridPane();
        this.overlapSpectraPane.setPadding(new Insets(10, 0, 0, 0));
//		this.overlapSpectraPane.setHgap(10);

        // spectrum table
        SpectrumTable spectrumTable1 = new SpectrumTable(spectra1);
        SpectrumTable spectrumTable2 = new SpectrumTable(spectra2);

        // cluster message
        Label clusterLabel1 = new Label("Cluster ID: " + clusterId1);
        clusterLabel1.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label releaseLabel1 = new Label("from " + releaseIName);
        releaseLabel1.setFont(Font.font("Arial", FontWeight.THIN, 10));
        Label specCountLab1 = new Label("Spectra Count: " + spectra1.size());
        specCountLab1.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        HBox hbox1 = new HBox(clusterLabel1, releaseLabel1);
        VBox vbox1 = new VBox(hbox1, specCountLab1);

        Label clusterLabel2 = new Label("Cluster ID: " + clusterId2);
        clusterLabel2.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        Label releaseLabel2 = new Label("from " + releaseIIName);
        releaseLabel2.setFont(Font.font("Arial", FontWeight.THIN, 10));
        Label specCountLab2 = new Label("Spectra Count: " + spectra2.size());
        specCountLab2.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        HBox hbox2 = new HBox(clusterLabel2, releaseLabel2);
        VBox vbox2 = new VBox(hbox2, specCountLab2);

        // add builds in grid pane
        overlapSpectraPane.add(vbox1, 0, 0);
        overlapSpectraPane.add(vbox2, 1, 0);
        overlapSpectraPane.add(spectrumTable1.getSpectrumTable(), 0, 1);
        overlapSpectraPane.add(spectrumTable2.getSpectrumTable(), 1, 1);

        // render row
        spectrumTable1.renderRow(spectrumTable1, overlapSpectrums, Color.BEIGE);
        spectrumTable2.renderRow(spectrumTable2, overlapSpectrums, Color.BEIGE);
    }

    private void renderRow(SpectrumTable spectrumTable, List<Spectrum> overlapSpectrums, Color color) {

        // render overlap row
        spectrumTable.getSpectrumTable().setRowFactory(row -> new TableRow<Spectrum>() {
            @Override
            public void updateItem(Spectrum item, boolean empty) {
                if (item != null && !empty) {
                    if (overlapSpectrums.indexOf(item) != -1)
                        setStyle("-fx-background-color: " + ColorUtils.colorToHex(color));
                    else
                        setStyle("");
                }
            }
        });
    }
}
