package com.nzv.gwt.dsocatalog.projection;

public interface Projection {
	
	/**
	 * Returns a point with Cartesian coordinates, which is the projection of the point which we passed the coordinates as parameters.
	 * @param longitudeInRadians a longitude expressed as radians
	 * @param latitudeInRadians latitude expressed as radians
	 * @return a point with Cartesian coordinates.
	 */
	public Point2D project(double longitudeInRadians, double latitudeInRadians);
}
