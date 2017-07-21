package cn.edu.cqupt.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.service.SpectrumComparerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ComparerPieChart {
	private PieChart comparerPieChart;

	public PieChart getComparerPieChart() {
		return comparerPieChart;
	}

	public void setComparerPieChart(PieChart comparerPieChart) {
		this.comparerPieChart = comparerPieChart;
	}

	public ComparerPieChart() {

	}

	public void createComparerPieChart(Cluster cluster, List<Cluster> releaseCluster) {

		// get source data
		SpectrumComparerService comparer = null;
		try {
			comparer = new SpectrumComparerService(cluster, releaseCluster);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		HashMap<String, Integer> overlapSpectrumCount = comparer.getOverlapSpectrumCount();
		HashMap<String, Cluster> overlapCluster = comparer.getOverlapCluster();
		HashMap<String, List<Spectrum>> overlapSpectrumOfCluster = comparer.getOverlapSpectrumOfCluster();

		// organize pie data
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		Iterator<Entry<String, Integer>> interItr = overlapSpectrumCount.entrySet().iterator();
		while (interItr.hasNext()) {
			Entry<String, Integer> entry = interItr.next();
			String clusterId = entry.getKey();
			BigDecimal bd = new BigDecimal((double) entry.getValue() / cluster.getSpecCount() * 100);
			double rate = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
			pieChartData.add(new PieChart.Data(
					clusterId + "\ncount : " + entry.getValue() + "\npercentage : " + rate + "%", entry.getValue()));
		}

		// paint pie chart
		this.comparerPieChart = new PieChart(pieChartData);

		// set pie chart : title and legend
		this.comparerPieChart.setTitle(cluster.getId() + "'s spectrum composition");
		this.comparerPieChart.setLegendVisible(false);
		this.comparerPieChart.setMinSize(500, 400);
		;
		// this.comparerPieChart.setStyle("-fx-border-color: black;");

		// add event : print spectrum tables of two overlap clusters
		for (PieChart.Data data : this.comparerPieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					// redirect to tabB
					ClusterApplication.tabPane.getSelectionModel().select(1);

					// get tabB
					Tab tabB = null;
					if (ClusterApplication.tabPane.getTabs().size() == 2)
						tabB = ClusterApplication.tabPane.getTabs().get(1);
					else {
						tabB = new Tab("Tab II");
						ClusterApplication.tabPane.getTabs().add(tabB);
						ClusterApplication.tabPane.getSelectionModel().select(1);
					}
					// add spectrum tables to tabB
					HBox hBox = new HBox();
					hBox.setMaxSize(1000, 500);
					String clusterId = data.getName().split("\\n")[0];

					// source
					SpectrumTable sourceSpectrumTable = new SpectrumTable(cluster.getSpectrums(),
							overlapSpectrumOfCluster.get(clusterId));

					// object
					SpectrumTable objectSpectrumTable = new SpectrumTable(overlapCluster.get(clusterId).getSpectrums(),
							overlapSpectrumOfCluster.get(clusterId));
					// System.out.println(cluster.getId() + " : " + cluster.getSpecCount());
					// System.out.println("overlap spectrum counts" + " : " +
					// overlapSpectrumOfCluster.get(clusterId).size());
					// System.out.println(overlapCluster.get(clusterId).getId() + " : " +
					// overlapCluster.get(clusterId).getSpecCount());
					hBox.getChildren().addAll(sourceSpectrumTable.getSpectrumTable(),
							objectSpectrumTable.getSpectrumTable());
					tabB.setContent(hBox);
				}
			});
		}
	}
}