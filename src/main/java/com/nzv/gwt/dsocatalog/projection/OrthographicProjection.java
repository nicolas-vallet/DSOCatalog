package com.nzv.gwt.dsocatalog.projection;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class OrthographicProjection implements Projection {
	
	public static double MIN_X = -1;
	public static double MAX_X = 1;
	public static double MIN_Y = -1;
	public static double MAX_Y = 1;

	@Override
	public Point2D project(double longitudeInRadians, double latitudeInRadians) {
		double x = cos(latitudeInRadians) * sin(longitudeInRadians);
		double y = - (cos(latitudeInRadians) * cos(longitudeInRadians));
		return new Point2D(x, y);
	}
}
