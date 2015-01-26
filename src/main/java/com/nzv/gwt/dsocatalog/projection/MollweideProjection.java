package com.nzv.gwt.dsocatalog.projection;

public class MollweideProjection {

	private static final int MAX_ITER = 10;
	private static final double TOLERANCE = 1e-7;
	private double cx, cy, cp;

	public MollweideProjection() {
		double p = Math.PI / 2;
		double r, sp, p2 = p + p;

		sp = Math.sin(p);
		r = Math.sqrt(Math.PI * 2.0 * sp / (p2 + Math.sin(p2)));
		cx = 2. * r / Math.PI;
		cy = r / sp;
		cp = p2 + Math.sin(p2);
	}

	public Point project(double longitudeInRadians, double latitudeInRadian) {
		double k, v;
		int i;

		k = cp * Math.sin(latitudeInRadian);
		for (i = MAX_ITER; i != 0; i--) {
			latitudeInRadian -= v = (latitudeInRadian + Math.sin(latitudeInRadian) - k) / (1. + Math.cos(latitudeInRadian));
			if (Math.abs(v) < TOLERANCE) {
				break;
			}
		}
		if (i == 0) {
			latitudeInRadian = (latitudeInRadian < 0.) ? -Math.PI / 2 : Math.PI / 2;
		} else {
			latitudeInRadian *= 0.5;
		}
		Point xy = new Point();
		xy.setX(cx * longitudeInRadians * Math.cos(latitudeInRadian));
		xy.setY(cy * Math.sin(latitudeInRadian));
		return xy;
	}

}
