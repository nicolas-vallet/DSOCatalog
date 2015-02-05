package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

import com.nzv.astro.ephemeris.coordinate.GeographicCoordinates;
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.Observer;

@SuppressWarnings("serial")
public abstract class AstroObject implements Serializable {

	public abstract Double getVisualMagnitude();

	public abstract double getRightAscension();
	
	public abstract double getDeclinaison();

	public abstract String getIdentifier();

	public abstract String getObjectType();
	
	public abstract Double getBoundingBoxSizeInArcMinute();

	public double getXCoordinateForReferential(CoordinatesSystem system, Observer... observer) {
		if (CoordinatesSystem.EQ == system) {
			return getRightAscension();
		}
		EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
				new EquatorialCoordinates(getRightAscension(),
						getDeclinaison()));
		switch (system) {
		case ECL:
			return eca.getEcliptiqueLongitude();
		case GAL:
			return eca.getGalacticLongitude();
		case ALTAZ:
			if (observer == null || observer.length == 0) {
				throw new RuntimeException("Observer is not specified!");
			}
			return eca.getAzimuth(new GeographicCoordinates(observer[0].getLatitude(), 
					observer[0].getLongitude()), observer[0].getGreenwichSiderealTime());
		default:
			throw new RuntimeException("Not yet implemented!");
		}
	}

	public double getYCoordinateForReferential(CoordinatesSystem system, Observer... observer) {
		if (CoordinatesSystem.EQ == system) {
			return getDeclinaison();
		}
		EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
				new EquatorialCoordinates(getRightAscension(),
						getDeclinaison()));
		switch (system) {
		case ECL:
			return eca.getEcliptiqueLatitude();
		case GAL:
			return eca.getGalacticLatitude();
		case ALTAZ:
			if (observer == null || observer.length == 0) {
				throw new RuntimeException("Observer is not specified!");
			}
			return eca.getElevation(new GeographicCoordinates(observer[0].getLatitude(),
					observer[0].getLongitude()), observer[0].getGreenwichSiderealTime());
		default:
			throw new RuntimeException("Not yet implemented!");
		}
	}

}
