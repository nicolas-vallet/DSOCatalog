package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Planet extends AstroObject implements Serializable {
	
	public static final int
    SUN=0, MERCURY=1, VENUS=2, EARTH=3, MARS=4,
    JUPITER=5, SATURN=6, URANUS=7, NEPTUNE=8, PLUTO=9, MOON=10;
	
	private double rightAscension;
	private double declinaison;
	private Double visualMagnitude = null;
	private String identifier;
	private int numericIdentifier;
	
	@SuppressWarnings("unused")
	private Planet(){}

	public Planet(int numId, String id, Double visualMag, double ra, double dec) {
		this.numericIdentifier = numId;
		this.identifier = id;
		this.visualMagnitude = visualMag;
		while (ra < 0) {
			ra += 360;
		}
		this.rightAscension = ra % 360;
		this.declinaison = dec;
	}

	@Override
	public Double getVisualMagnitude() {
		return this.visualMagnitude;
	}

	@Override
	public double getRightAscension() {
		while (this.rightAscension < 0) {
			this.rightAscension += 360;
		}
		return this.rightAscension % 360;
	}

	@Override
	public double getDeclinaison() {
		return this.declinaison;
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public int getNumericIdentifier() {
		return numericIdentifier;
	}

	@Override
	public String getObjectType() {
		return "PLANET";
	}
}
