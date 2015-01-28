package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Planet extends AstroObject implements Serializable {
	
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
		this.rightAscension = ra;
		this.declinaison = dec;
	}

	@Override
	public Double getVisualMagnitude() {
		return this.visualMagnitude;
	}

	@Override
	public double getRightAscension() {
		return this.rightAscension;
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
