package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

public enum CoordinatesSystem implements Serializable {

	EQ("EQUATO"),
	ECL("ECLIPTIC"),
	GAL("GALACTIC"),
	ALTAZ("ALTAZ");
	
	private String comment;
	
	private CoordinatesSystem(String c) {
		this.comment = c;
	}

	public String getComment() {
		return comment;
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
