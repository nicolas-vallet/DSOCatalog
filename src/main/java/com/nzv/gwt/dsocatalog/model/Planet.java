package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

import com.nzv.astro.ephemeris.planetary.Planets;

@SuppressWarnings("serial")
public class Planet extends AstroObject implements Serializable {
	
	public static final int SUN = Planets.SUN;
	public static final int MERCURY = Planets.MERCURY;
	public static final int VENUS = Planets.VENUS;
	public static final int EARTH = Planets.EARTH;
	public static final int MARS = Planets.MARS;
	public static final int JUPITER = Planets.JUPITER;
	public static final int SATURN = Planets.SATURN;
	public static final int URANUS = Planets.URANUS;
	public static final int NEPTUNE = Planets.NEPTUNE;
	public static final int PLUTO = Planets.PLUTO;
	public static final int MOON = Planets.LUNA;
	
	private double rightAscension;
	private double declinaison;
	private Double visualMagnitude = null;
	private String identifier;
	private int numericIdentifier;
	
	@SuppressWarnings("unused")
	private Planet(){}
	
	@Override
	public Double getBoundingBoxSizeInArcMinute() {
		throw new UnsupportedOperationException();
	}

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
