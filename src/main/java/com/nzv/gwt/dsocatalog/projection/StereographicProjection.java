package com.nzv.gwt.dsocatalog.projection;

public class StereographicProjection implements Projection {

	@Override
	public Point2D project(double longitudeInRadians, double latitudeInRadians) {
		double term1 = 2 * Math.cos(latitudeInRadians) * Math.sin(longitudeInRadians);
		double term2 = 1 + Math.sin(latitudeInRadians);
		double x = - (term1 / term2);

		term1 = 2 * Math.cos(latitudeInRadians) * Math.cos(longitudeInRadians);
		double y = - (term1 / term2);
		
		return new Point2D(x,y);
	}

}
