package cn.edu.cqupt.view;

import java.util.List;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.sankey.SankeyDiagram;
import cn.edu.cqupt.service.NetworkGraphService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class NetworkGraph {

	private GridPane networkGraphPane;
	private boolean isZoomed;
	private Tab backupTab;  //TabA's backup
	
	public GridPane getNetworkGraphPane() {
		return networkGraphPane;
	}

	public NetworkGraph(Cluster cluster, String releaseIName, String releaseIIName, List<Cluster> releaseI, List<Cluster> releaseII) {
		this.networkGraphPane = new GridPane();
		this.isZoomed = false;
		this.backupTab = new Tab();

		NetworkGraphService networtGraphService = null;
		try {
			networtGraphService = new NetworkGraphService(cluster, releaseIName, releaseIIName, releaseI, releaseII);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		// obtain network graph(data)
		Vertex focusVertex = networtGraphService.getFocusVertex();
		UndirectedGraph<Vertex, Edge> graph = networtGraphService.getUndirectedGraph();

		// obtain sankey diagram
		SankeyDiagram sankey= new SankeyDiagram(releaseIName, releaseIIName, graph, focusVertex);
		GridPane sankeyontroller = sankey.getController();
		Group sankeyGroup = sankey.getGroup();
		
		// add button
		Button zoomButton = new Button("Zoom");
		VBox controller = new VBox(zoomButton, sankeyontroller);
		controller.setPadding(new Insets(5, 0, 5, 5));

		// add builds into network graph pane
		networkGraphPane.add(controller, 0, 0);
		networkGraphPane.add(sankeyGroup, 0, 1);


		// add event for button
		zoomButton.setOnAction((ActionEvent event) -> {
			if (isZoomed) {
				ClusterSelection.networkGraphStackPane.getChildren().add(new ScrollPane(networkGraphPane));
				ClusterApplication.tabPane.getTabs().get(0).setContent(backupTab.getContent());
				isZoomed = false;
			} else {
				backupTab.setContent(ClusterApplication.tabPane.getTabs().get(0).getContent()); // backup
				ClusterApplication.tabPane.getTabs().get(0).setContent(new ScrollPane(networkGraphPane)); // replace
				isZoomed = true;
			}
		});

		// add event for network graph pane
		networkGraphPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.SECONDARY)) {
					// System.out.println("SECONDARY CLICK!!");
					// saveAsPng(sankeyGroup);
				}
			}
		});
	}

	// public void captureAndSavePane(StackPane pane) {
	//
	// int width = (int) pane.getBoundsInLocal().getWidth();
	// int height = (int) pane.getBoundsInLocal().getHeight();
	//
	// FileChooser fileChooser = new FileChooser();
	// File file = fileChooser.showOpenDialog(null);
	// if(file != null) {
	// WritableImage writableImage = new WritableImage(width + 20, height + 20);
	// pane.snapshot(null, writableImage);
	// RenderedImage renderedImage =
	// }
	// }

//	public void saveAsPng(Group pane) {
//		WritableImage image = pane.snapshot(new SnapshotParameters(), null);
//
//		// TODO: probably use a file chooser here
//		File file = new File("C:\\Users\\huangjs\\Desktop\\clustering\\chart.png");
//
//		try {
//			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//		} catch (IOException e) {
//			// TODO: handle exception here
//		}
//	}
}
