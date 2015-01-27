package com.nzv.gwt.dsocatalog.projection;

public class MercatorProjection implements Projection {

	@Override
	public Point2D project(double longitudeInRadians, double latitudeInRadians) {
		double x = longitudeInRadians;
		
		double term1 = 1.0 + Math.sin(latitudeInRadians);
		double term2 = 1.0 - Math.sin(latitudeInRadians);
		double term3 = Math.log(term1 / term2);
		double y = term3 / 2;
		
		return new Point2D(x, y);
	}
}
