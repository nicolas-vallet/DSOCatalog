package com.nzv.gwt.dsocatalog.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Observer implements Serializable {

	private double latitude;
	private double longitude;
	private double greenwichSiderealTime;
//	private double currentJulianDay;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getGreenwichSiderealTime() {
		return greenwichSiderealTime;
	}

	public void setGreenwichSiderealTime(double greenwichSiderealTime) {
		this.greenwichSiderealTime = greenwichSiderealTime;
	}

//	public double getCurrentJulianDay() {
//		return currentJulianDay;
//	}
//
//	public void setCurrentJulianDay(double currentJulianDay) {
//		this.currentJulianDay = currentJulianDay;
//	}

}
