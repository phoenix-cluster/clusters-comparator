package cn.edu.cqupt.view;

import java.util.List;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Page;
import cn.edu.cqupt.service.ClusterTableService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ClusterSelection {

	private StackPane spectrumStackPane; // hold spectrum tables
	private StackPane peakMapStackPane; // hold peak map
	private StackPane pieStackPane; // hold pie chart
	private StackPane overlapClusterPane; // hold overlap cluster map
	private GridPane gridPane; // hold cluster table, spectrum table, peak map and pie chart
	private List<Cluster> releaseCluster;

	public ClusterSelection() {

	}

	public GridPane getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}

	public ClusterSelection(ClusterTableService clusterTableService, int pageSize, List<Cluster> releaseCluster) {
		this.spectrumStackPane = new StackPane(); // hold spectrum tables
		this.peakMapStackPane = new StackPane(); // hold peak map
		this.pieStackPane = new StackPane(); // hold pie chart
		this.overlapClusterPane = new StackPane();
		this.gridPane = new GridPane();
		this.releaseCluster = releaseCluster;

		// get data of the default display page
		Page<Cluster> iniPara = clusterTableService.getCurrentPageClusters(1, pageSize);
		Cluster firstCluster = iniPara.getDataList().get(0);

		// create cluster table with pagination
		int pageCount = iniPara.getTotalPage(); // get total page count
		Pagination pagination = createPagination(clusterTableService, pageSize, pageCount);

		// create spectrum table of cluster 1
		SpectrumTable spectrumTable = new SpectrumTable(firstCluster.getSpectra());
		spectrumStackPane.getChildren().add(spectrumTable.getSpectrumTable());

		// create peak map of cluster 1
		PeakMap peakMap = new PeakMap(firstCluster.getMzValues(),
				firstCluster.getIntensValues());
		peakMapStackPane.getChildren().add(peakMap.getVbox());

		// create pie chart of cluster 1
		ComparerPieChart comparerPieChart = new ComparerPieChart(firstCluster, releaseCluster);
		pieStackPane.getChildren().add(comparerPieChart.getComparerPieChart());

		// create overlap cluster map
		OverlapClusterMap overlapClusterMap = new OverlapClusterMap(firstCluster, 
				ClusterApplication.serviceReleaseI.getAllClusters(),ClusterApplication.serviceReleaseII.getAllClusters());
		overlapClusterPane.getChildren().add(overlapClusterMap.getOverlapClusterMap());

		// add builds to grid pane
		gridPane.add(pagination, 0, 0);
		gridPane.add(spectrumStackPane, 1, 0);
		gridPane.add(peakMapStackPane, 0, 1);
		gridPane.add(pieStackPane, 1, 1);
		gridPane.add(overlapClusterPane, 2, 1);
	}

	private TableView<Cluster> createClusterTable(List<Cluster> tableData) {
		TableView<Cluster> clusterTable = new TableView<>();
		ObservableList<Cluster> observableTableData = FXCollections.observableList(tableData);

		TableColumn<Cluster, String> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<Cluster, String>("id"));
		clusterTable.getColumns().add(idCol);
		TableColumn<Cluster, Float> avPrecursorMzCol = new TableColumn<>("av_precursor_mz");
		avPrecursorMzCol.setCellValueFactory(new PropertyValueFactory<>("avPrecursorMz"));
		clusterTable.getColumns().add(avPrecursorMzCol);
		TableColumn<Cluster, Float> avPrecursorIntensCol = new TableColumn<>("av_precursor_intens");
		avPrecursorIntensCol.setCellValueFactory(new PropertyValueFactory<>("avPrecursorIntens"));
		clusterTable.getColumns().add(avPrecursorIntensCol);
		TableColumn<Cluster, Integer> specCount = new TableColumn<>("Spectrum Count");
		specCount.setCellValueFactory(new PropertyValueFactory<>("specCount"));
		clusterTable.getColumns().add(specCount);

		// add event to cluster table : click on the row of the cluster table to display
		// the corresponding spectrums
		clusterTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {

				// obtain position
				TablePosition<Cluster, String> pos = clusterTable.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				// System.out.println("you have clicked : it is " + row + " row and " + col + "
				// column");

				if (clusterTable.getItems().get(row) != null) {
					
					// current cluster
					Cluster currentCluster = tableData.get(row);

					// create spectrum table
					SpectrumTable spectrumTable = new SpectrumTable(currentCluster.getSpectra());

					// create peak map
					PeakMap peakMap = new PeakMap(currentCluster.getMzValues(),
							currentCluster.getIntensValues());

					// create pie chart
					ComparerPieChart comparerPieChart = new ComparerPieChart(currentCluster, releaseCluster);

					// create overlap cluster map
					OverlapClusterMap overlapClusterMap = new OverlapClusterMap(currentCluster, 
							ClusterApplication.serviceReleaseI.getAllClusters(), 
							ClusterApplication.serviceReleaseII.getAllClusters());;
					
					// add builds into pane
					spectrumStackPane.getChildren().remove(0);
					spectrumStackPane.getChildren().add(spectrumTable.getSpectrumTable());
					peakMapStackPane.getChildren().remove(0);
					peakMapStackPane.getChildren().add(peakMap.getVbox());
					pieStackPane.getChildren().remove(0);
					pieStackPane.getChildren().add(comparerPieChart.getComparerPieChart());
					overlapClusterPane.getChildren().add(overlapClusterMap.getOverlapClusterMap());
				}
			}
		});

		// set table size
		clusterTable.setPrefSize(800, 350);

		// 设置多余部分分散在列之间
		clusterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// fill data
		clusterTable.setItems(observableTableData);

		return clusterTable;
	}

	/**
	 * @param clusterTableService
	 * @param pageSize
	 *            the number of items displayed per page
	 * @param pageCount
	 *            the total number of pages
	 * @return Pagination object
	 */
	private Pagination createPagination(ClusterTableService clusterTableService, int pageSize, int pageCount) {
		Pagination pagination = new Pagination(pageCount);
		pagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				Page<Cluster> currentPageClusters = clusterTableService.getCurrentPageClusters(pageIndex + 1, pageSize);

				// System.out.println("pageIndex:" + pageIndex);
				// System.out.println("getPageSize: " + currentPageClusters.getPageSize());
				// System.out.println("getTotalRecord: " +
				// currentPageClusters.getTotalRecord());
				// System.out.println("getTotalPage: " + currentPageClusters.getTotalPage());

				return createClusterTable(currentPageClusters.getDataList());
			}
		});
		return pagination;
	}
}