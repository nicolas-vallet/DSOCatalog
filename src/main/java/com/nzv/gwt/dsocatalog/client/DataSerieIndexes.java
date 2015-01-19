package com.nzv.gwt.dsocatalog.client;

import com.nzv.gwt.dsocatalog.model.Constellation;

public class DataSerieIndexes {
	private int starSerieIndex = 0;
	private int asterismSerieIndex = 0;
	private int galaxySerieIndex = 0;
	private int globularClusterSerieIndex = 0;
	private int openClusterSerieIndex = 0;
	private int planetaryNebulaSerieIndex = 0;
	private int nebulaSerieIndex = 0;
	private int snRemnantSerieIndex = 0;
	private int quasarSerieIndex = 0;
	private int firstConstellationBoundarySerieIndex = 0;
	private int firstConstellationShapeSerieIndex = 0;
	private int seriesCount = 0;

	public DataSerieIndexes(CatalogSearchOptions searchOptions) {
		int serieIndex = 0;

		if (searchOptions.isFindStars()) {
			starSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindAsterisms()) {
			asterismSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindGalaxies()) {
			galaxySerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindGlobularClusters()) {
			globularClusterSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindOpenClusters()) {
			openClusterSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindPlanetaryNebulas()) {
			planetaryNebulaSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindNebulas()) {
			nebulaSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindSupernovaRemnant()) {
			snRemnantSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isFindQuasars()) {
			quasarSerieIndex = ++serieIndex;
			serieIndex += 2;
			seriesCount++;
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (searchOptions.getRestrictedToConstellationCode() != null
					&& !searchOptions.getRestrictedToConstellationCode()
							.isEmpty()) {
				firstConstellationBoundarySerieIndex = ++serieIndex;
			} else {
				for (int i = 0; i < Constellation.NB_CONSTELLATION; i++) {
					if (i == 0) {
						firstConstellationBoundarySerieIndex = ++serieIndex;
						continue;
					}
					++serieIndex;
				}
			}
		}
		
		if (searchOptions.isDisplayConstellationShape()) {
			if (searchOptions.getRestrictedToConstellationCode() != null
					&& !searchOptions.getRestrictedToConstellationCode()
							.isEmpty()) {
				firstConstellationShapeSerieIndex = ++serieIndex;
			} else {
				for (int i = 0; i < Constellation.NB_CONSTELLATION; i++) {
					if (i == 0) {
						firstConstellationShapeSerieIndex = ++serieIndex;
						continue;
					}
					++serieIndex;
				}
			}
		}
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

	public int getFirstConstellationBoundarySerieIndex() {
		return firstConstellationBoundarySerieIndex;
	}

	public int getFirstConstellationShapeSerieIndex() {
		return firstConstellationShapeSerieIndex;
	}
	
	public int getSeriesCount() {
		return seriesCount;
	}

	@Override
	public String toString() {
		return "DataSerieIndexes [starSerieIndex=" + starSerieIndex
				+ ", asterismSerieIndex=" + asterismSerieIndex
				+ ", galaxySerieIndex=" + galaxySerieIndex
				+ ", globularClusterSerieIndex=" + globularClusterSerieIndex
				+ ", openClusterSerieIndex=" + openClusterSerieIndex
				+ ", planetaryNebulaSerieIndex=" + planetaryNebulaSerieIndex
				+ ", nebulaSerieIndex=" + nebulaSerieIndex
				+ ", snRemnantSerieIndex=" + snRemnantSerieIndex
				+ ", quasarSerieIndex=" + quasarSerieIndex
				+ ", firstConstellationBoundarySerieIndex="
				+ firstConstellationBoundarySerieIndex 
				+ ", firstConstellationShapeSerieIndex="
				+ firstConstellationShapeSerieIndex
				+ ", seriesCount=" + seriesCount + "]";
	}
}
