package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

import com.nzv.gwt.dsocatalog.projection.MollweideProjection;
import com.nzv.gwt.dsocatalog.projection.NeutralProjection;
import com.nzv.gwt.dsocatalog.projection.OrthographicProjection;
import com.nzv.gwt.dsocatalog.projection.Projection;

public enum CoordinatesSystem implements Serializable {

	EQ("EQUATO", new NeutralProjection()),
	ECL("ECLIPTIC", new NeutralProjection()),
	GAL("GALACTIC", new MollweideProjection()),
	ALTAZ("ALTAZ", new OrthographicProjection());
	
	private String comment;
	private Projection projection; 
	
	private CoordinatesSystem(String c, Projection proj) {
		this.comment = c;
		projection = proj;
	}

	public String getComment() {
		return comment;
	}
	
	public Projection getProjection() {
		return projection;
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
