package com.nzv.gwt.dsocatalog.projection;

public class StereographicProjection implements Projection {
	
	public static double MIN_X = -2;
	public static double MAX_X = 2;
	public static double MIN_Y = -2;
	public static double MAX_Y = 2;

	@Override
	public Point2D project(double azimuthInRadians, double elevationInRadians) {
		double term1 = 2 * Math.cos(elevationInRadians) * Math.sin(azimuthInRadians);
		double term2 = 1 + Math.sin(elevationInRadians);
		double x = - (term1 / term2);

		term1 = 2 * Math.cos(elevationInRadians) * Math.cos(azimuthInRadians);
		double y = - (term1 / term2);
		
		return new Point2D(x,y);
	}
}
