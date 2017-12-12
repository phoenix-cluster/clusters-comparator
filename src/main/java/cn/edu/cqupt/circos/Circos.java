package cn.edu.cqupt.circos;

import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

public class Circos {
	
	// the gap of two arc
	private SimpleDoubleProperty leftGapPercProperty = new SimpleDoubleProperty(); // the gap percentage of left arc
	private SimpleDoubleProperty rightGapPercProperty = new SimpleDoubleProperty(); // the gap percentage of right arc

	// original data
	private double[] cluster1;
	private double[] cluster2;

	// common divisor
	private double total;

	// cluster percentage
	private double[] cluster1Percentage;
	private double[] cluster2Percentage;

	// cluster arc
	private Arc[] cluster1Arc;
	private Arc[] cluster2Arc;

	// start angle of each arc in degree
	private double[] cluster1StartDegree;
	private double[] cluster2StartDegree;

	// track circle
	private Circle trackCircle;

	// group
	private Group group;

	public Group getGroup() {
		return group;
	}

	public Circos(List<Double> cluster1List, List<Double> cluster2List) {
		double[] cluster1Arr = new double[cluster1List.size()];
		double[] cluster2Arr = new double[cluster2List.size()];
		int i = 0;
		for(double c1 : cluster1List) {
			cluster1Arr[i++] = c1;
		}
		i = 0;
		for(double c2 : cluster2List) {
			cluster2Arr[i++] = c2;
 		}
		this.cluster1 = cluster1Arr;
		this.cluster2 = cluster2Arr;

		this.cluster1StartDegree = new double[cluster1.length];
		this.cluster2StartDegree = new double[cluster2.length];
		this.group = new Group();

		// set builds(arc and gap) percentage
		setBuildsPercentage();

		// set track circle
		setTrackCircle(350, 350, 600);

		// set arc
		setArc();

		// add builds
		addTrackCircle();
		addArc();
	}
	public Circos(double[] cluster1Arr, double[] cluster2Arr) {
		this.cluster1 = cluster1Arr;
		this.cluster2 = cluster2Arr;

		this.cluster1StartDegree = new double[cluster1.length];
		this.cluster2StartDegree = new double[cluster2.length];
		this.group = new Group();

		// set builds(arc and gap) percentage
		setBuildsPercentage();

		// set track circle
		setTrackCircle(350, 350, 300);

		// set arc
		setArc();

		// add builds
		addTrackCircle();
		addArc();
	}

	public void setBuildsPercentage() {
		double leftLen = 0.0;
		double rightLen = 0.0;
		for (double element : cluster1) {
			leftLen += element;
		}

		for (double element : cluster2) {
			rightLen += element;
		}

		// calculate the gap of the two arc
		// 首先确定弧总长最大的在左边还是右边，例如在左边，则左边的空白为左边总长的平均值，两边同除的数子定为左边总长与空白总长的和，然后同除的数减去右边总长得到右边空白量
		double leftGap, rightGap;
		if (leftLen > rightLen) {
			leftGap = leftLen / cluster1.length;
			total = leftGap * cluster1.length + leftLen;
			rightGap = (total - rightLen) / cluster2.length;

		} else {
			rightGap = rightLen / cluster2.length;
			total = rightGap * cluster2.length + rightLen;
			leftGap = (total - leftLen) / cluster1.length;
		}

		// get percentage of clusters
		cluster1Percentage = new double[cluster1.length];
		cluster2Percentage = new double[cluster2.length];
		for (int i = 0; i < cluster1.length; i++) {
			cluster1Percentage[i] = cluster1[i] / total;
		}

		for (int i = 0; i < cluster2.length; i++) {
			cluster2Percentage[i] = cluster2[i] / total;
		}

		// get percentage of gap
		leftGapPercProperty.set(leftGap / total);
		rightGapPercProperty.set(rightGap / total);
		
		System.out.println("leftGap:" + leftGap);
		System.out.println("rightGap: " + rightGap );
		System.out.println("total: " + total);
	}

	/**
	 * create a track circle
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 */
	public void setTrackCircle(double centerX, double centerY, double radius) {
		trackCircle = new Circle(centerX, centerY, radius);
		trackCircle.setFill(null);
		trackCircle.setStroke(Color.web("#808080", 0.2));
		trackCircle.setStrokeWidth(15);
	}

	public void addTrackCircle() {
		this.group.getChildren().add(trackCircle);
	}

	public void setArc() {
		cluster1Arc = new Arc[cluster1.length];
		cluster2Arc = new Arc[cluster2.length];
		double startAngle = 90; // start angle of the left cluster

		// cluster1 percentage
		for (int i = 0; i < cluster1Percentage.length; i++) {
			Arc arc = new Arc();

			// coordinate
			arc.centerXProperty().bind(trackCircle.centerXProperty());
			arc.centerYProperty().bind(trackCircle.centerYProperty());
			arc.radiusXProperty().bind(trackCircle.radiusProperty());
			arc.radiusYProperty().bind(trackCircle.radiusProperty());
			arc.setStartAngle(startAngle); // start angle
			arc.setLength(180 * cluster1Percentage[i]); // the angular extent of the arc

			 System.out.println("************************arc1****************");
			 System.out.println("leftGapProperty" + (leftGapPercProperty.get() * 180));
			 System.out.println("startAngle: " + startAngle + " ~ endAngle: " +
			 (startAngle + 180 * cluster1Percentage[i]));

			// next time: the angular extent of the arc and the gap
			startAngle += leftGapPercProperty.get() * 180;
			startAngle += 180 * cluster1Percentage[i];

			// style
//			arc.setStroke(Color.web("#9400D3", 0.6));
			arc.setStroke(Color.web("#9400D3", 1));
			arc.setFill(null);
			arc.setStrokeWidth(15);
			arc.setType(ArcType.OPEN);

			// add the arc
			cluster1Arc[i] = arc;
		}

		// cluster2 percentage
		startAngle = 270; // start angle of the right cluster
		for (int i = 0; i < cluster2Percentage.length; i++) {
			Arc arc = new Arc();
			arc.centerXProperty().bind(trackCircle.centerXProperty());
			arc.centerYProperty().bind(trackCircle.centerYProperty());
			arc.radiusXProperty().bind(trackCircle.radiusProperty());
			arc.radiusYProperty().bind(trackCircle.radiusProperty());
			arc.setStartAngle(startAngle); // start angle
			arc.setLength(180 * cluster2Percentage[i]);// // the angular extent of the arc

			// System.out.println("************************arc2****************");
			// System.out.println("rightGapPercProperty" + (rightGapPercProperty.get() *
			// 180));
			// System.out.println("startAngle: " + startAngle + " ~ endAngle: " +
			// (startAngle + 180 * cluster2Percentage[i]));

			// next time
			startAngle += rightGapPercProperty.get() * 180;
			startAngle += 180 * cluster2Percentage[i];

			// style
			arc.setStroke(Color.web("#008000", 0.6));
			arc.setFill(null);
			arc.setStrokeWidth(15);
			arc.setType(ArcType.OPEN);

			// add the arc
			cluster2Arc[i] = arc;
		}

		// save the start angle of each arc in degree
		// warning: Arc(Counterclockwise) and ArcTo(Clockwise) have different
		// coordinate,
		for (int i = 0; i < cluster1Arc.length; i++) {
			cluster1StartDegree[i] = cluster1Arc[i].getStartAngle() + cluster1Arc[i].getLength();
		}

		for (int i = 0; i < cluster2Arc.length; i++) {
			cluster2StartDegree[i] = cluster2Arc[i].getStartAngle() + cluster2Arc[i].getLength();
		}

	}

	public void addArc() {
		this.group.getChildren().addAll(cluster1Arc);
		this.group.getChildren().addAll(cluster2Arc);

	}

	public Path createPath(int fromIndex, int toIndex, double weight) {
		double gap = 18; // 重新做
		Path path = new Path();
		double fromArcStartDegree = cluster1StartDegree[fromIndex];
		double toArcStartDegree = cluster2StartDegree[toIndex];

		// calculate end angle
		double fromArcEndDegree = fromArcStartDegree - weight / total * 180;
		double toArcEndDegree = toArcStartDegree - weight / total * 180;

		// calculate coordinate
		double arcRadius = trackCircle.getRadius() - gap;
		double fromArcStartX = trackCircle.getCenterX()
				+ arcRadius * Math.cos(-Math.toRadians(fromArcStartDegree));
		double fromArcStartY = trackCircle.getCenterY()
				+ arcRadius * Math.sin(-Math.toRadians(fromArcStartDegree));
		double toArcStartX = trackCircle.getCenterX()
				+ arcRadius * Math.cos(-Math.toRadians(toArcStartDegree));
		double toArcStartY = trackCircle.getCenterY()
				+ arcRadius * Math.sin(-Math.toRadians(toArcStartDegree));

		double fromArcEndX = trackCircle.getCenterX()
				+ arcRadius * Math.cos(-Math.toRadians(fromArcEndDegree));
		double fromArcEndY = trackCircle.getCenterY()
				+ arcRadius * Math.sin(-Math.toRadians(fromArcEndDegree));
		double toArcEndX = trackCircle.getCenterX()
				+ arcRadius * Math.cos(-Math.toRadians(toArcEndDegree));
		double toArcEndY = trackCircle.getCenterY()
				+ arcRadius * Math.sin(-Math.toRadians(toArcEndDegree));

		// build path
		MoveTo moveTo = new MoveTo(fromArcStartX, fromArcStartY);

		// arc
		ArcTo arcTo1 = new ArcTo();
		arcTo1.setX(fromArcEndX);
		arcTo1.setY(fromArcEndY);
		arcTo1.setRadiusX(trackCircle.getRadius());
		arcTo1.setRadiusY(trackCircle.getRadius());
		arcTo1.setSweepFlag(true);
		arcTo1.setLargeArcFlag(false);
		
		// line
		LineTo lineTo1 = new LineTo();
		lineTo1.setX(toArcStartX);
		lineTo1.setY(toArcStartY);
		
		// arc
		ArcTo arcTo2 = new ArcTo();
		arcTo2.setX(toArcEndX);
		arcTo2.setY(toArcEndY);
		arcTo2.setRadiusX(trackCircle.getRadius());
		arcTo2.setRadiusY(trackCircle.getRadius());
		arcTo2.setSweepFlag(true);
		arcTo2.setLargeArcFlag(false);
		
		// line
		LineTo lineTo2 = new LineTo();
		lineTo2.setX(fromArcStartX);
		lineTo2.setY(fromArcStartY);
		
		// path set
		path.setStroke(Color.web("#EE82EE", 0.4));
		
		path.setFill(Color.web("#EE82EE", 0.4));
		path.setStrokeLineCap(StrokeLineCap.ROUND);
		path.setStrokeType(StrokeType.OUTSIDE);
		path.setStrokeWidth(0);

		// add builds into path
		path.getElements().addAll(moveTo, arcTo1, lineTo1, arcTo2, lineTo2);

		// update start angle
		cluster1StartDegree[fromIndex] = fromArcEndDegree;
		cluster2StartDegree[toIndex] = toArcEndDegree;

		group.getChildren().add(path);
		return path;
	}

}
