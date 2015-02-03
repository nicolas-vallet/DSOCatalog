package com.nzv.gwt.dsocatalog.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CatalogSearchOptions implements Serializable {

	public static final double DEFAULT_STAR_LIMIT_MAGNITUDE = 4.0d;
	public static final double DEFAULT_DSO_LIMIT_MAGNITUDE = 7.0d;

	public static CatalogSearchOptions createDefaultOptions() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setRestrictedToConstellationCode(null);
		options.setStarLimitMagnitude(DEFAULT_STAR_LIMIT_MAGNITUDE);
		options.setDsoLimitMagnitude(DEFAULT_DSO_LIMIT_MAGNITUDE);
		return options;
	}

	public static CatalogSearchOptions createOptionsEveryTypes() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setStarLimitMagnitude(DEFAULT_STAR_LIMIT_MAGNITUDE);
		options.setDsoLimitMagnitude(DEFAULT_DSO_LIMIT_MAGNITUDE);
		options.findPlanets = true;
		options.findStars = true;
		options.findAsterisms = true;
		options.findGalaxies = true;
		options.findGlobularClusters = true;
		options.findOpenClusters = true;
		options.findPlanetaryNebulas = true;
		options.findNebulas = true;
		options.findSupernovaRemnant = true;
		options.findQuasars = true;
		return options;
	}

	private boolean findPlanets = false;
	private String restrictedToConstellationCode = null;
	private boolean displayEquator = false;
	private boolean displayEcliptic = false;
	private boolean displayGalacticPlan = false;
	private boolean displayConstellationNames = false;
	private boolean displayConstellationBoundaries = false;
	private boolean displayConstellationShape = false;
	private double starLimitMagnitude = 0d;
	private double dsoLimitMagnitude = 0d;

	private boolean findStars = true;
	private boolean findAsterisms = false;
	private boolean findGalaxies = false;
	private boolean findGlobularClusters = false;
	private boolean findOpenClusters = false;
	private boolean findPlanetaryNebulas = false;
	private boolean findNebulas = false;
	private boolean findSupernovaRemnant = false;
	private boolean findQuasars = false;
	private String observerCurrentDateAsString;
	private String observerCurrentTimeAsString;
	private int observerGreenwhichHourOffset;
	private double observatoryLatitude = 0;
	private double observatoryLongitude = 0;
	
	public boolean isDisplayEquator() {
		return displayEquator;
	}

	public void setDisplayEquator(boolean displayEquator) {
		this.displayEquator = displayEquator;
	}

	public boolean isDisplayEcliptic() {
		return displayEcliptic;
	}

	public void setDisplayEcliptic(boolean displayEcliptic) {
		this.displayEcliptic = displayEcliptic;
	}

	public boolean isDisplayGalacticPlan() {
		return displayGalacticPlan;
	}

	public void setDisplayGalacticPlan(boolean displayGalacticPlan) {
		this.displayGalacticPlan = displayGalacticPlan;
	}

	public boolean isFindPlanets() {
		return findPlanets;
	}

	public void setFindPlanets(boolean findPlanets) {
		this.findPlanets = findPlanets;
	}

	public String getRestrictedToConstellationCode() {
		return restrictedToConstellationCode;
	}

	public void setRestrictedToConstellationCode(
			String restrictedToConstellationCode) {
		this.restrictedToConstellationCode = restrictedToConstellationCode;
	}
	
	public boolean isDisplayConstellationNames() {
		return displayConstellationNames;
	}

	public void setDisplayConstellationNames(boolean displayConstellationNames) {
		this.displayConstellationNames = displayConstellationNames;
	}

	public boolean isDisplayConstellationBoundaries() {
		return displayConstellationBoundaries;
	}

	public void setDisplayConstellationBoundaries(
			boolean displayConstellationBoundaries) {
		this.displayConstellationBoundaries = displayConstellationBoundaries;
	}
	
	public boolean isDisplayConstellationShape() {
		return displayConstellationShape;
	}

	public void setDisplayConstellationShape(boolean displayConstellationShape) {
		this.displayConstellationShape = displayConstellationShape;
	}

	public double getStarLimitMagnitude() {
		return starLimitMagnitude;
	}

	public void setStarLimitMagnitude(double starLimitMagnitude) {
		this.starLimitMagnitude = starLimitMagnitude;
	}

	public double getDsoLimitMagnitude() {
		return dsoLimitMagnitude;
	}

	public void setDsoLimitMagnitude(double dsoLimitMagnitude) {
		this.dsoLimitMagnitude = dsoLimitMagnitude;
	}

	public boolean isFindStars() {
		return findStars;
	}

	public void setFindStars(boolean findStars) {
		this.findStars = findStars;
	}

	public boolean isFindGalaxies() {
		return findGalaxies;
	}

	public void setFindGalaxies(boolean findGalaxies) {
		this.findGalaxies = findGalaxies;
	}

	public boolean isFindOpenClusters() {
		return findOpenClusters;
	}

	public void setFindOpenClusters(boolean findOpenClusters) {
		this.findOpenClusters = findOpenClusters;
	}

	public boolean isFindGlobularClusters() {
		return findGlobularClusters;
	}

	public void setFindGlobularClusters(boolean findGlobularClusters) {
		this.findGlobularClusters = findGlobularClusters;
	}

	public boolean isFindAsterisms() {
		return findAsterisms;
	}

	public void setFindAsterisms(boolean findAsterisms) {
		this.findAsterisms = findAsterisms;
	}

	public boolean isFindNebulas() {
		return findNebulas;
	}

	public void setFindNebulas(boolean findNebulas) {
		this.findNebulas = findNebulas;
	}

	public boolean isFindPlanetaryNebulas() {
		return findPlanetaryNebulas;
	}

	public void setFindPlanetaryNebulas(boolean findPlanetaryNebulas) {
		this.findPlanetaryNebulas = findPlanetaryNebulas;
	}

	public boolean isFindSupernovaRemnant() {
		return findSupernovaRemnant;
	}

	public void setFindSupernovaRemnant(boolean findSupernovaRemnant) {
		this.findSupernovaRemnant = findSupernovaRemnant;
	}

	public boolean isFindQuasars() {
		return findQuasars;
	}

	public void setFindQuasars(boolean findQuasars) {
		this.findQuasars = findQuasars;
	}
	
	public double getObservatoryLatitude() {
		return observatoryLatitude;
	}

	public String getObserverCurrentDateAsString() {
		return observerCurrentDateAsString;
	}

	public void setObserverCurrentDateAsString(String observerCurrentDateAsString) {
		this.observerCurrentDateAsString = observerCurrentDateAsString;
	}

	public String getObserverCurrentTimeAsString() {
		return observerCurrentTimeAsString;
	}

	public void setObserverCurrentTimeAsString(String observerCurrentTimeAsString) {
		this.observerCurrentTimeAsString = observerCurrentTimeAsString;
	}

	public int getObserverGreenwhichHourOffset() {
		return observerGreenwhichHourOffset;
	}

	public void setObserverGreenwhichHourOffset(int observerGreenwhichHourOffset) {
		this.observerGreenwhichHourOffset = observerGreenwhichHourOffset;
	}

	public void setObservatoryLatitude(double observatoryLatitude) {
		this.observatoryLatitude = observatoryLatitude;
	}

	public double getObservatoryLongitude() {
		return observatoryLongitude;
	}

	public void setObservatoryLongitude(double observatoryLongitude) {
		this.observatoryLongitude = observatoryLongitude;
	}

	@Override
	public String toString() {
		return "CatalogSearchOptions [star mag max=" + starLimitMagnitude
				+ ", dso mag max=" + dsoLimitMagnitude + " | planets=" + findPlanets
				+ ", stars=" + findStars + ", asterisms=" + findAsterisms + ", galaxies="
				+ findGalaxies + ", globular clusters=" + findGlobularClusters
				+ ", open clusters=" + findOpenClusters
				+ ", planetary nebulas=" + findPlanetaryNebulas + ", nebulas="
				+ findNebulas + ", SN remnant=" + findSupernovaRemnant
				+ ", quasars=" + findQuasars + ", limiting constellation="
				+ restrictedToConstellationCode + "]";
	}

}
