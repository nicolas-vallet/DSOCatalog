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
		options.displayConstellationBoundaries = false;
		options.displayConstellationShape = false;
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

	private String restrictedToConstellationCode = null;
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

	public String getRestrictedToConstellationCode() {
		return restrictedToConstellationCode;
	}

	public void setRestrictedToConstellationCode(
			String restrictedToConstellationCode) {
		this.restrictedToConstellationCode = restrictedToConstellationCode;
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

	@Override
	public String toString() {
		return "CatalogSearchOptions [star mag max=" + starLimitMagnitude
				+ ", dso mag max=" + dsoLimitMagnitude + " | stars="
				+ findStars + ", asterisms=" + findAsterisms + ", galaxies="
				+ findGalaxies + ", globular clusters=" + findGlobularClusters
				+ ", open clusters=" + findOpenClusters
				+ ", planetary nebulas=" + findPlanetaryNebulas + ", nebulas="
				+ findNebulas + ", SN remnant=" + findSupernovaRemnant
				+ ", quasars=" + findQuasars + ", limiting constellation="
				+ restrictedToConstellationCode + "]";
	}

}
