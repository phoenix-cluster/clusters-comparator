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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ClusterTable {
	private String[] colName = { "ID", "av_precursor_mz", "av_precursor_intens", "Spectrum Count" }; // 列名
	private String[] property = { "id", "avPrecursorMz", "avPrecursorIntens", "specCount" }; // 列属性
	private HBox hBox;
	private StackPane stackPane;

	public HBox gethBox() {
		return hBox;
	}

	public void sethBox(HBox hBox) {
		this.hBox = hBox;
	}

	public ClusterTable() {

	}

	public ClusterTable(ClusterTableService clusterTableService, int pageSize) {
		this.hBox = new HBox();
		this.stackPane = new StackPane(); // receive spectrum tables

		// initial parameter
		Page<Cluster> iniPara = clusterTableService.getCurrentPageClusters(1, pageSize);

		// create cluster table with pagination
		int pageCount = iniPara.getTotalPage(); // get total page count
		Pagination pagination = createPagination(clusterTableService, pageSize, pageCount);
		hBox.getChildren().add(pagination);

		// create spectrum table of cluster 1
		SpectrumTable spectrumTable = new SpectrumTable(iniPara.getDataList().get(0).getSpectrums());
		stackPane.getChildren().add(spectrumTable.getSpectrumTable());
		hBox.getChildren().add(stackPane);
	}

	private TableView<Cluster> createClusterTable(List<Cluster> tableData) {
		TableView<Cluster> clusterTable = new TableView<>();
		ObservableList<Cluster> observableTableData = FXCollections.observableList(tableData);

		// 设置表格：表头和单元格值类型
		for (int i = 0; i < colName.length; i++) {
			TableColumn<Cluster, String> tmpCol = new TableColumn<>(colName[i]); // 这里是存疑的，因为有一些数据不是String，后期如果有排序等操作会出错
			tmpCol.setCellValueFactory(new PropertyValueFactory<>(property[i]));
			clusterTable.getColumns().add(tmpCol);
		}

		// add event to cluster table
		clusterTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				TablePosition<Cluster, String> pos = clusterTable.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				int col = pos.getColumn();
				System.out.println("it is " + row + " row and " + col + " column");
				if (clusterTable.getItems().get(row) != null) {
					SpectrumTable spectrumTable = new SpectrumTable(tableData.get(row).getSpectrums());
					stackPane.getChildren().add(spectrumTable.getSpectrumTable());
					hBox.getChildren().remove(1);
					hBox.getChildren().add(stackPane);
				}
			}
		});

		// 设置表格大小
		clusterTable.setPrefSize(800, 500);

		// 设置多余部分分散在列之间
		clusterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// 填充数据
		clusterTable.setItems(observableTableData);

		return clusterTable;
	}

	private Pagination createPagination(ClusterTableService clusterTableService, int pageSize, int pageCount) {
		Pagination pagination = new Pagination(pageCount);
		pagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				Page<Cluster> currentPageClusters = clusterTableService.getCurrentPageClusters(pageIndex + 1, pageSize);

				System.out.println("pageIndex:" + pageIndex);
				System.out.println("getPageSize: " + currentPageClusters.getPageSize());
				System.out.println("getTotalRecord: " + currentPageClusters.getTotalRecord());
				System.out.println("getTotalPage: " + currentPageClusters.getTotalPage());

				return createClusterTable(currentPageClusters.getDataList());
			}
		});
		return pagination;
	}
}