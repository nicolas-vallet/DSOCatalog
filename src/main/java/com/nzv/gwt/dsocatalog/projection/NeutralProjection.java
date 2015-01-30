package com.nzv.gwt.dsocatalog.projection;

public class NeutralProjection implements Projection {

	@Override
	public Point2D project(double longitudeInRadians, double latitudeInRadians) {
		return new Point2D(longitudeInRadians, latitudeInRadians);
	}
}
