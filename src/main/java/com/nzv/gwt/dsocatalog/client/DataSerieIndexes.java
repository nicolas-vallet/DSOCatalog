package com.nzv.gwt.dsocatalog.client;

import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;


public class DataSerieIndexes {
	private int horizonSerieIndex = 0;
	private int planetSerieIndex = 0;
	private int starSerieIndex = 0;
	private int asterismSerieIndex = 0;
	private int galaxySerieIndex = 0;
	private int globularClusterSerieIndex = 0;
	private int openClusterSerieIndex = 0;
	private int planetaryNebulaSerieIndex = 0;
	private int nebulaSerieIndex = 0;
	private int snRemnantSerieIndex = 0;
	private int quasarSerieIndex = 0;
	private int eclipticSerieIndex = 0;
	private int equatorSerieIndex = 0;
	private int galacticPlanSerieIndex = 0;
	private int constellationNameSerieIndex = 0;
	private int constellationBoundarySerieIndex = 0;
	private int constellationShapeSerieIndex = 0;
	private int objectSeriesCount = 0;

	public DataSerieIndexes(CatalogSearchOptions searchOptions) {
		int serieIndex = 0;
		
		if (searchOptions.getCoordinatesSystem() == CoordinatesSystem.ALTAZ) {
			horizonSerieIndex = ++serieIndex;
			serieIndex += 3;
		}

		if (searchOptions.isDisplayEcliptic()) {
			eclipticSerieIndex = ++serieIndex;
			serieIndex++;
		}
		if (searchOptions.isDisplayEquator()) {
			equatorSerieIndex = ++serieIndex;
			serieIndex++;
		}
		if (searchOptions.isDisplayGalacticPlan()) {
			galacticPlanSerieIndex = ++serieIndex;
			serieIndex++;
		}
		if (searchOptions.isDisplayConstellationNames()) {
			// Constellation name display requires columns for RA center, DEC center, Style, Annotation (will contain the code), AnnotationText (will contain the name) 
			constellationNameSerieIndex = ++serieIndex;
			serieIndex += 3;
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			constellationBoundarySerieIndex = ++serieIndex;
			serieIndex++;
		}
		
		if (searchOptions.isDisplayConstellationShape()) {
			constellationShapeSerieIndex = ++serieIndex;
			serieIndex++;
		}
		if (searchOptions.isFindAsterisms()) {
			asterismSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindGalaxies()) {
			galaxySerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindGlobularClusters()) {
			globularClusterSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindOpenClusters()) {
			openClusterSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindPlanetaryNebulas()) {
			planetaryNebulaSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindNebulas()) {
			nebulaSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindSupernovaRemnant()) {
			snRemnantSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindQuasars()) {
			quasarSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindStars()) {
			starSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
		if (searchOptions.isFindPlanets()) {
			planetSerieIndex = ++serieIndex;
			serieIndex += 2;
			objectSeriesCount++;
		}
	}
	
	public int getPlanetSerieIndex() {
		return planetSerieIndex;
	}

	public int getStarSerieIndex() {
		return starSerieIndex;
	}

	public int getAsterismSerieIndex() {
		return asterismSerieIndex;
	}

	public int getGalaxySerieIndex() {
		return galaxySerieIndex;
	}

	public int getGlobularClusterSerieIndex() {
		return globularClusterSerieIndex;
	}

	public int getOpenClusterSerieIndex() {
		return openClusterSerieIndex;
	}

	public int getPlanetaryNebulaSerieIndex() {
		return planetaryNebulaSerieIndex;
	}

	public int getNebulaSerieIndex() {
		return nebulaSerieIndex;
	}

	public int getSnRemnantSerieIndex() {
		return snRemnantSerieIndex;
	}

	public int getQuasarSerieIndex() {
		return quasarSerieIndex;
	}

	public int getConstellationNameSerieIndex() {
		return constellationNameSerieIndex;
	}

	public int getConstellationBoundarySerieIndex() {
		return constellationBoundarySerieIndex;
	}

	public int getConstellationShapeSerieIndex() {
		return constellationShapeSerieIndex;
	}
	
	public int getHorizonSerieIndex() {
		return horizonSerieIndex;
	}

	public int getEclipticSerieIndex() {
		return eclipticSerieIndex;
	}

	public int getEquatorSerieIndex() {
		return equatorSerieIndex;
	}

	public int getGalacticPlanSerieIndex() {
		return galacticPlanSerieIndex;
	}

	public int getObjectSeriesCount() {
		return objectSeriesCount;
	}

	@Override
	public String toString() {
		return "DataSerieIndexes [planetSerieIndex="+planetSerieIndex
				+ ", \n starSerieIndex=" + starSerieIndex
				+ ", \n asterismSerieIndex=" + asterismSerieIndex
				+ ", \n galaxySerieIndex=" + galaxySerieIndex
				+ ", \n globularClusterSerieIndex=" + globularClusterSerieIndex
				+ ", \n openClusterSerieIndex=" + openClusterSerieIndex
				+ ", \n planetaryNebulaSerieIndex=" + planetaryNebulaSerieIndex
				+ ", \n nebulaSerieIndex=" + nebulaSerieIndex
				+ ", \n snRemnantSerieIndex=" + snRemnantSerieIndex
				+ ", \n quasarSerieIndex=" + quasarSerieIndex
				+ ", \n horizonSerieIndex=" + horizonSerieIndex
				+ ", \n eclipticSerieIndex=" + eclipticSerieIndex
				+ ", \n equatorSerieIndex=" + equatorSerieIndex
				+ ", \n galacticPlanSerieIndex=" + galacticPlanSerieIndex
				+ ", \n constellationNameSerieIndex="+constellationNameSerieIndex
				+ ", \n constellationBoundarySerieIndex="+constellationBoundarySerieIndex 
				+ ", \n constellationShapeSerieIndex="+constellationShapeSerieIndex
				+ ", \n objectSeriesCount=" + objectSeriesCount + "]";
	}
}
