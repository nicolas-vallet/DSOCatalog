package com.nzv.gwt.dsocatalog.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.ApplicationBoard;
import com.nzv.gwt.dsocatalog.client.CatalogSearchOptions;
import com.nzv.gwt.dsocatalog.client.DataSerieIndexes;
import com.nzv.gwt.dsocatalog.client.DsoCatalogGWT;
import com.nzv.gwt.dsocatalog.client.Observer;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Planet;
import com.nzv.gwt.dsocatalog.model.Star;

public class VisualizationHelper {

	public static MyDataTable initializeDataTable(ApplicationBoard appPanel,
			MyDataTable optimizedData, Set<AstroObject> objects,
			HashMap<String, Constellation> constellationsList) {
		CatalogSearchOptions searchOptions = appPanel.getCatalogSearchOptions();
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null
				&& !searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(appPanel.getLiCoordinatesMode().getValue(
				appPanel.getLiCoordinatesMode().getSelectedIndex()));
		switch (cs) {
		case ECL:
		case GAL:
			optimizedData.addColumn(ColumnType.NUMBER, "LONGITUDE");
			break;
		case ALTAZ:
			optimizedData.addColumn(ColumnType.NUMBER, "AZIMUTH");
			break;
		case EQ:
		default:
			optimizedData.addColumn(ColumnType.NUMBER, "RA");
			break;
		}
		if (searchOptions.isFindAsterisms()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Asterismes");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindGalaxies()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Galaxies");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindGlobularClusters()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Amas globulaires");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindOpenClusters()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Amas ouverts");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindPlanetaryNebulas()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Nébuleuses planétaires");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindNebulas()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Nébuleuses");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindSupernovaRemnant()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Rémanants de supernova");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindQuasars()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Quasars");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindStars()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Etoiles");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayPlanets()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Planetes");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayConstellationNames()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Constellation");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addAnnotationColumn(optimizedData);
			optimizedData.addAnnotationTextColumn(optimizedData);
		}

		ArrayList<String> codeOfConstellationsToDisplay = new ArrayList<String>();
		if (showOneConstellation) {
			codeOfConstellationsToDisplay.add(appPanel.getLiConstellations().getValue(
					appPanel.getLiConstellations().getSelectedIndex()));
		} else {
			codeOfConstellationsToDisplay.addAll(constellationsList.keySet());
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Limites constellation");
		}
		if (searchOptions.isDisplayConstellationShape()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Forme constellation");
		}

		optimizedData.addRows(objects.size());
		if (searchOptions.isDisplayConstellationNames()) {
			optimizedData.addRows(codeOfConstellationsToDisplay.size());
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList
						.get(searchOptions.getRestrictedToConstellationCode()).getBoundaryPoints()
						.size() + 2);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getBoundaryPoints().size() + 2);
				}
			}
		}
		if (searchOptions.isDisplayConstellationShape()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList
						.get(searchOptions.getRestrictedToConstellationCode()).getShapeLines()
						.size() * 3);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getShapeLines().size() * 3);
				}
			}
		}
		return optimizedData;
	}

	public static MyDataTable fillRowWithNullValues(MyDataTable data, int rowIndex) {
		for (int i = 0; i < data.getNumberOfColumns(); i++) {
			data.setValueNull(rowIndex, i);
		}
		return data;
	}

	/**
	 * Generate the tooltip box content for a given AstroObject.
	 * 
	 * @param appPanel
	 * @param o
	 * @param observer
	 * @return
	 */
	public static String generateTooltip(ApplicationBoard appPanel, AstroObject o, Observer observer) {
		StringBuffer sb = new StringBuffer();
		// Object type...
		if (o instanceof Star) {
			Star s = (Star) o;
			sb.append("Star ");
			if (s.getSpectralType() != null) {
				sb.append(s.getSpectralType());
			}
			sb.append(" \n");
		} else if (o instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) o;
			sb.append(dso.getType().getComment());
			if (dso.getClasstype() != null) {
				sb.append(" " + dso.getClasstype());
			}
			sb.append(" \n");
		}

		// Identifier and magnitude...
		sb.append(o.getIdentifier() + "\n");
		if (o instanceof Star && ((Star) o).getSaoNumber() != null) {
			sb.append("SAO " + ((Star) o).getSaoNumber() + "\n");
		}
		if (!(o instanceof Planet)) {
			sb.append("Mag.=" + o.getVisualMagnitude() + "\n");
		}

		// Coordinates...
		if (o instanceof Planet) {
			Planet p = (Planet) o;
			Sexagesimal ra = new Sexagesimal(p.getRightAscension() / 15);
			Sexagesimal dec = new Sexagesimal(p.getDeclinaison());
			sb.append("RA="+ra.toString(SexagesimalType.HOURS)+"\n");
			sb.append("DEC="+dec.toString(SexagesimalType.DEGREES)+"\n");
		} else if (o instanceof Star) {
			Star s = (Star) o;
			sb.append("RA=" + s.getRightAscensionHour() + "h " + s.getRightAscensionMinute() + "m "
					+ s.getRightAscensionSecond() + "s \n");
			sb.append("DEC=" + s.getDeclinaisonSignus() + s.getDeclinaisonDegree() + "° "
					+ s.getDeclinaisonMinute() + "m " + s.getDeclinaisonSecond() + "s \n");
		} else if (o instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) o;
			sb.append("RA=" + dso.getRightAscensionHour() + "h " + dso.getRightAscensionMinute()
					+ "m \n");
			sb.append("DEC=" + dso.getDeclinaisonDegree() + "° " + dso.getDeclinaisonMinute()
					+ "m \n");
		}

		CoordinatesSystem cs = CoordinatesSystem.instanceOf(appPanel.getLiCoordinatesMode()
				.getValue(appPanel.getLiCoordinatesMode().getSelectedIndex()));
		switch (cs) {
		case ECL:
			sb.append("Longitude ecl.=" + o.getXCoordinateForReferential(cs) + "° \n");
			sb.append("Latitude ecl.=" + o.getYCoordinateForReferential(cs) + "° \n");
			break;
		case GAL:
			sb.append("Longitude gal.=" + o.getXCoordinateForReferential(cs) + "° \n");
			sb.append("Latitude gal.=" + o.getYCoordinateForReferential(cs) + "° \n");
			break;
		case EQ:
			break;
		case ALTAZ:
			sb.append("Azimuth=" + o.getXCoordinateForReferential(cs, observer) + "° \n");
			sb.append("Elevation=" + o.getYCoordinateForReferential(cs, observer) + "° \n");
			break;
		}
		return sb.toString();
	}

	/**
	 * Generates the best Google chart options given the CatalogSearchOption
	 * passed as parameter.
	 * 
	 * @param searchOptions
	 * @return
	 */
	public static Options createLineChartOptions(CatalogSearchOptions searchOptions,
			ApplicationBoard appPanel, HashMap<String, Constellation> constellationsList) {
		String coordinatesMode = appPanel.getLiCoordinatesMode().getValue(
				appPanel.getLiCoordinatesMode().getSelectedIndex());
		MyLineChartOptions options = MyLineChartOptions.create();
		int chartWidth = Window.getClientWidth() - (2 * ApplicationBoard.PANEL_SPLITTER_WIDTH)
				- ApplicationBoard.LEFT_PANEL_WIDTH - ApplicationBoard.RIGHT_PANEL_WIDTH;
		int chartHeight = Window.getClientHeight() - 25;
		options.setWidth(chartWidth);
		options.setHeight(chartHeight);
		ChartArea area = ChartArea.create();
		area.setLeft(25);
		area.setTop(25);
		area.setWidth("85%");
		area.setHeight("85%");
		options.setChartArea(area);
		options.setBackgroundColor(DsoCatalogGWT.STYLE_CHART_BACKGROUND_COLOR);
		AxisOptions hAxisOptions = AxisOptions.create();
		AxisOptions vAxisOptions = AxisOptions.create();
		TextStyle axisTitleTextStyle = TextStyle.create();
		axisTitleTextStyle.setColor("#ffffff");
		hAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		vAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		if (coordinatesMode.equals("" + CoordinatesSystem.ALTAZ)
				&& appPanel.getChkShowObjectsUnderHorizon().getValue() == false) {
			vAxisOptions.setMinValue(0);
		} else {
			vAxisOptions.setMinValue(-90);
		}
		vAxisOptions.setMaxValue(90);
		if (("" + CoordinatesSystem.ECL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("LONGITUDE ECLIPTIQUE");
			vAxisOptions.setTitle("LATITUDE ECLIPTIQUE");
		} else if (("" + CoordinatesSystem.GAL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("LONGITUDE GALACTIQUE");
			vAxisOptions.setTitle("LATITUDE GALACTIQUE");
		} else if (("" + CoordinatesSystem.ALTAZ).equals(coordinatesMode)) {
//			hAxisOptions.setMinValue(-180);
//			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("AZIMUTH");
			vAxisOptions.setTitle("ELEVATION");
		} else if (("" + CoordinatesSystem.EQ).equals(coordinatesMode)) {
			if (!appPanel.getLiConstellations()
					.getValue(appPanel.getLiConstellations().getSelectedIndex()).isEmpty()) {
				// We set the limit of the map to the limit of the selected
				// constellation...
				String constellation = appPanel.getLiConstellations().getValue(
						appPanel.getLiConstellations().getSelectedIndex());
				EquatorialCoordinates upperWesterMapLimit = constellationsList.get(constellation)
						.getUpperWesternMapLimit(1);
				EquatorialCoordinates lowerEasterMapLimit = constellationsList.get(constellation)
						.getLowerEasternMapLimit(1);
				hAxisOptions.setMinValue((int) upperWesterMapLimit.getRightAscension());
				hAxisOptions.setMaxValue((int) (lowerEasterMapLimit.getRightAscension() + 1));
				vAxisOptions.setMinValue((int) lowerEasterMapLimit.getDeclinaison());
				vAxisOptions.setMaxValue((int) (upperWesterMapLimit.getDeclinaison() + 1));
			} else {
				hAxisOptions.setMinValue(0);
				hAxisOptions.setMaxValue(360);
			}
			hAxisOptions.setTitle("ASCENSION DROITE");
			vAxisOptions.setTitle("DECLINAISON");
		}
		options.set("pointShape", "star");
		options.setPointSize(5);
		options.setLineWidth(0);
		TextStyle textStyle = TextStyle.create();
		textStyle.setColor("#ffffff");
		hAxisOptions.setDirection(-1);
		hAxisOptions.setTextStyle(textStyle);
		vAxisOptions.setTextStyle(textStyle);
		options.setHAxisOptions(hAxisOptions);
		options.setVAxisOptions(vAxisOptions);
		options.setLegendTextStyle(textStyle);
		if (searchOptions.isDisplayConstellationNames()) {
			TextStyle annotationTextStyle = TextStyle.create();
			annotationTextStyle.setColor(DsoCatalogGWT.STYLE_CONSTELLATION_NAME_TEXT_COLOR);
			options.setAnnotationTextStyle(annotationTextStyle);
		}
		// Trying to fine-tune the series representation options...
		options = VisualizationHelper.createSeriesOptions(options, searchOptions);
		return options;
	}

	/**
	 * Generate Google Chart Series options, based on the CatalogSearchOptions
	 * passed as parameter.
	 * 
	 * @param options
	 * @param searchOptions
	 * @return
	 */
	public static MyLineChartOptions createSeriesOptions(MyLineChartOptions options,
			CatalogSearchOptions searchOptions) {
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		int i = 0;
		for (i = 0; i < serieIndexes.getObjectSeriesCount(); i++) {
			MySeries s = MySeries.create();
			s.setLineWidth(0);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
		}
		if (searchOptions.isDisplayConstellationNames()) {
			MySeries s = MySeries.create();
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointSize(0);
			s.setLineDashStyle(new int[] { 5, 1, 3 });
			s.setColor(DsoCatalogGWT.STYLE_CONSTELLATION_BORDER_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayConstellationShape()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointSize(0);
			s.setColor(DsoCatalogGWT.STYLE_CONSTELLATION_SHAPE_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		return options;
	}

	/**
	 * Displays details about the AstroObject passed as parameter into a GWT
	 * FlexTable also passed as parameter.
	 * 
	 * @param ao
	 * @param objectDetailsTable
	 */
	public static void displayObjectDetails(AstroObject ao, FlexTable objectDetailsTable) {
		objectDetailsTable.removeAllRows();
		int row = 0;

		objectDetailsTable.setHTML(row, 0, "<b>" + ao.getIdentifier() + "</b>");
		objectDetailsTable.getFlexCellFormatter().setColSpan(row, 0, 2);
		row++;

		Sexagesimal ra = new Sexagesimal(ao.getRightAscension() / 15);
		objectDetailsTable.setText(row, 0, "RIGHT ASCENSION (J2000)");
		objectDetailsTable.setText(row++, 1, ra.toString(SexagesimalType.HOURS));

		Sexagesimal dec = new Sexagesimal(ao.getDeclinaison());
		objectDetailsTable.setText(row, 0, "DECLINAISON (J2000)");
		objectDetailsTable.setText(row++, 1, dec.toString(SexagesimalType.DEGREES));

		if (ao instanceof Star) {
			Star o = (Star) ao;

			objectDetailsTable.setText(row, 0, "HR NUMBER");
			objectDetailsTable.setText(row++, 1, "" + o.getHrNumber());

			objectDetailsTable.setText(row, 0, "HD NUMBER");
			objectDetailsTable.setText(row++, 1, "" + o.getHdNumber());

			objectDetailsTable.setText(row, 0, "SAO NUMBER");
			objectDetailsTable.setText(row++, 1, "" + o.getSaoNumber());

			objectDetailsTable.setText(row, 0, "MAGNITUDE");
			objectDetailsTable.setText(row++, 1, "" + o.getVisualMagnitude());

			objectDetailsTable.setText(row, 0, "MAGNITUDE B-V");
			objectDetailsTable.setText(row++, 1, "" + o.getBvMag()
					+ (o.isUncertainBvMag() ? " (uncertain) " : ""));

			objectDetailsTable.setText(row, 0, "MAGNITUDE U-B");
			objectDetailsTable.setText(row++, 1, "" + o.getUbMag()
					+ (o.isUncertainUbMag() ? " (uncertain) " : ""));

			objectDetailsTable.setText(row, 0, "MAGNITUDE R-I");
			objectDetailsTable.setText(row++, 1, "" + o.getRiMag());

			objectDetailsTable.setText(row, 0, "SPECTRAL TYPE");
			objectDetailsTable.setText(row++, 1, o.getSpectralType());

			objectDetailsTable.setText(row, 0, "IR SOURCE ?");
			objectDetailsTable.setText(row++, 1, "" + o.isIrSource());

		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject o = (DeepSkyObject) ao;

			objectDetailsTable.setText(row, 0, "OTHER NAME");
			objectDetailsTable.setText(row++, 1, o.getOtherName());

			objectDetailsTable.setText(row, 0, "OBJECT TYPE");
			objectDetailsTable.setText(row++, 1, o.getObjectType());

			objectDetailsTable.setText(row, 0, "CONSTELLATION");
			objectDetailsTable.setText(row++, 1, o.getConstellation().getName());

			objectDetailsTable.setText(row, 0, "MAGNITUDE");
			objectDetailsTable.setText(row++, 1, "" + o.getMagnitude());

			objectDetailsTable.setText(row, 0, "SURFACE BRIGHTNESS");
			objectDetailsTable.setText(row++, 1, "" + o.getSurfaceBrightness());

			objectDetailsTable.setText(row, 0, "SIZE");
			objectDetailsTable.setText(row++, 1, "" + o.getSizeHumanReadable());

			objectDetailsTable.setText(row, 0, "IN BEST NGC CATALOG");
			objectDetailsTable.setText(row++, 1, "" + o.isInBestNgcCatalog());

			objectDetailsTable.setText(row, 0, "IN CALDWELL CATALOG");
			objectDetailsTable.setText(row++, 1, "" + o.isInCaldwellCalatalog());

			objectDetailsTable.setText(row, 0, "IN HERSCHEL CATALOG");
			objectDetailsTable.setText(row++, 1, "" + o.isInHerschelCatalog());

			objectDetailsTable.setText(row, 0, "IN MESSIER CATALOG");
			objectDetailsTable.setText(row++, 1, "" + o.isInMessierCatalog());

			objectDetailsTable.setHTML(row, 0,
					"<a href='http://deepskypedia.com/w/index.php?search=" + o.getName()
							+ "' target='_blank'>[+]</a>");
			objectDetailsTable.getFlexCellFormatter().setColSpan(row, 0, 2);
			row++;
		}
	}
}
