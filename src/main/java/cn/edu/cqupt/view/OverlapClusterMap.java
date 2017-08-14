package cn.edu.cqupt.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;

import com.google.common.base.Function;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.ClusterComparerService;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.embed.swing.SwingNode;

public class OverlapClusterMap{
	private float factor = 3F;
	private Cluster cluster;
	private List<Cluster> allClusterReleaseI;
	private List<Cluster> allClusterReleaseII;

	public OverlapClusterMap(Cluster cluster, List<Cluster> releaseI, List<Cluster> releaseII) {
		this.cluster = cluster;
		this.allClusterReleaseI = releaseI;
		this.allClusterReleaseII = releaseII;
	}

//	@Override
//	public void start(Stage primaryStage) {
//		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_5.clustering")).findAllClusters();
//		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_6.clustering")).findAllClusters();
//		Cluster cluster = releaseI.get(0);
//		ClusterComparerService clusterComparer = null;
//		try {
//			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		SwingNode swingNode = getSwingNode(clusterComparer.getGraph());
//		StackPane pane = new StackPane();
//		pane.getChildren().add(swingNode);
//		Scene scene = new Scene(pane);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}

	public SwingNode getOverlapClusterMap() {
		SwingNode swingNode = new SwingNode();

		// get graph
		ClusterComparerService clusterComparer = null;
		try {
		clusterComparer = new ClusterComparerService(cluster, allClusterReleaseI, allClusterReleaseII);
	} catch (CloneNotSupportedException e) {
		throw new RuntimeException(e);
	}
		
		Layout<Vertex, Edge> layout = new CircleLayout<>(clusterComparer.getGraph());
		layout.setSize(new Dimension(200, 200));
		VisualizationViewer<Vertex, Edge> vv = new VisualizationViewer<>(layout);
		vv.setPreferredSize(new Dimension(350, 350));

		// label
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		// vertex size
		vv.getRenderContext().setVertexShapeTransformer(new Function<Vertex, Shape>() {
			@Override
			public Shape apply(Vertex vertex) {
				double radius = vertex.getCluster().getSpecCount() * factor;
				Shape circle = new Ellipse2D.Double(-factor, -factor, radius, radius);
				return circle;
			}
		});

		// vertex color
		vv.getRenderContext().setVertexFillPaintTransformer(new Function<Vertex, Paint>() {
			@Override
			public Paint apply(Vertex arg0) {
				return new Color(141, 24, 219);
			}

		});

		// edge size
		vv.getRenderContext().setEdgeStrokeTransformer(new Function<Edge, Stroke>() {
			public Stroke apply(Edge e) {
				BasicStroke bs = new BasicStroke(e.getOverlapSpectra().size() * factor);
				return  bs;
			}
		});
		
		// edge color
		vv.getRenderContext().setEdgeDrawPaintTransformer(new Function<Edge, Paint>(){

			@Override
			public Paint apply(Edge e) {
				return new Color(255, 182, 193);
			}
			
		});

		// Create a graph mouse and add it to the visualization component
		DefaultModalGraphMouse<Vertex, Edge> gm = new DefaultModalGraphMouse<>();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.addKeyListener(gm.getModeKeyListener());
		vv.setGraphMouse(gm);

		vv.addGraphMouseListener(new GraphMouseListener<Vertex>() {

			@Override
			public void graphClicked(Vertex arg0, MouseEvent arg1) {
				System.out.println(arg0);

			}

			@Override
			public void graphPressed(Vertex arg0, MouseEvent arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void graphReleased(Vertex arg0, MouseEvent arg1) {
				// TODO Auto-generated method stub

			}

		});

		// edge Listener
		vv.getPickedEdgeState().addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("item: " + e.getItem());
				System.out.println("getStateChange: " + e.getStateChange());
				System.out.println("id: " + e.getID());
				System.out.println("getItemSelectable: " + e.getItemSelectable());

			}

		});
		swingNode.setContent(vv);
		return swingNode;
	}

//	public static void main(String[] args) {
//		launch(args);
//	}
}
