package com.nzv.gwt.dsocatalog.projection;

public class Point2D {

	private double x;
	private double y;

	protected Point2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public Point2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
