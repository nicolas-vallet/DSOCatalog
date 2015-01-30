package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

public enum CoordinatesSystem implements Serializable {

	EQ("EQUATO", 0, 360, -90, 90),
	ECL("ECLIPTIC", -180, 180, -90, 90),
	GAL("GALACTIC", -180, 180, -90, 90),
	ALTAZ("ALTAZ", 0, 0, 0, 0);
	
	private String comment;
	private double chartMinValueX;
	private double chartMaxValueX;
	private double chartMinValueY;
	private double chartMaxValueY;
	
	private CoordinatesSystem(String c, double minX, double maxX, double minY, double maxY) {
		this.comment = c;
		chartMinValueX = minX;
		chartMaxValueX = maxX;
		chartMinValueY = minY;
		chartMaxValueY = maxY;
	}

	public String getComment() {
		return comment;
	}
	
	public double getChartMinValueX() {
		return chartMinValueX;
	}

	public double getChartMaxValueX() {
		return chartMaxValueX;
	}

	public double getChartMinValueY() {
		return chartMinValueY;
	}

	public double getChartMaxValueY() {
		return chartMaxValueY;
	}

	public static CoordinatesSystem instanceOf(String coordinatesSystem) {
		for (CoordinatesSystem c : values()) {
			if ((""+c).equals(coordinatesSystem)) {
				return c;
			}
		}
		return null;
	}
}
