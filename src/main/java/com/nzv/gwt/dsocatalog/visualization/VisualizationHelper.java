package com.nzv.gwt.dsocatalog.visualization;

import java.math.BigDecimal;
import java.math.MathContext;
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
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
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
import com.nzv.gwt.dsocatalog.model.PlanetEnum;
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
		if (searchOptions.isFindPlanets()) {
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
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayConstellationShape()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Forme constellation");
			optimizedData.addTooltipColumn(optimizedData);
		}

		optimizedData.addRows(objects.size());
		if (searchOptions.isDisplayConstellationNames()) {
			optimizedData.addRows(codeOfConstellationsToDisplay.size());
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList
						.get(searchOptions.getRestrictedToConstellationCode()).getBoundaryLines()
						.size() * 3);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getBoundaryPoints().size() * 3);
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
		MathContext mc = new MathContext(5);
		if (o instanceof Planet) {
			Planet p = (Planet) o;
			Sexagesimal ra = new Sexagesimal(BigDecimal.valueOf(p.getRightAscension() / 15)
					.round(mc).doubleValue());
			Sexagesimal dec = new Sexagesimal(BigDecimal.valueOf(p.getDeclinaison()).round(mc)
					.doubleValue());
			sb.append("RA=" + ra.toString(SexagesimalType.HOURS) + "\n");
			sb.append("DEC=" + dec.toString(SexagesimalType.DEGREES) + "\n");
		}

		CoordinatesSystem cs = CoordinatesSystem.instanceOf(appPanel.getLiCoordinatesMode()
				.getValue(appPanel.getLiCoordinatesMode().getSelectedIndex()));
		Sexagesimal x = new Sexagesimal(BigDecimal
				.valueOf(o.getXCoordinateForReferential(cs, observer)).round(mc).doubleValue());
		Sexagesimal y = new Sexagesimal(BigDecimal
				.valueOf(o.getYCoordinateForReferential(cs, observer)).round(mc).doubleValue());
		switch (cs) {
		case ECL:
			sb.append("Longitude ecl.=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append("Latitude ecl.=" + y.toString(SexagesimalType.DEGREES) + " \n");
			break;
		case GAL:
			sb.append("Longitude gal.=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append("Latitude gal.=" + y.toString(SexagesimalType.DEGREES) + " \n");
			break;
		case EQ:
			break;
		case ALTAZ:
			sb.append("Azimuth=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append("Elevation=" + y.toString(SexagesimalType.DEGREES) + " \n");
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
				- ApplicationBoard.LEFT_PANEL_WIDTH;
		int chartHeight = Window.getClientHeight() - 25 - ApplicationBoard.SOUTH_PANEL_HEIGHT;
		// We use a ration 2:1 on chart width:height
		if (chartHeight > chartWidth / 2) {
			chartHeight = chartWidth / 2;
		} else {
			chartWidth = 2 * chartHeight;
		}
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
			// hAxisOptions.setMinValue(-180);
			// hAxisOptions.setMaxValue(180);
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
	public static void displayObjectDetails(AstroObject ao, FlexTable objectDetailsTableIdentifier,
			FlexTable objectDetailsTableCoordinates, FlexTable objectDetailsTableBrightness,
			FlexTable objectDetailsTableSpecificities, FlexTable objectDetailsTableExternalResources) {

		objectDetailsTableIdentifier.removeAllRows();
		objectDetailsTableCoordinates.removeAllRows();
		objectDetailsTableBrightness.removeAllRows();
		objectDetailsTableSpecificities.removeAllRows();
		objectDetailsTableExternalResources.removeAllRows();

		// Identifiers
		int row = 0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			objectDetailsTableIdentifier.setHTML(row, 0, "PLANETE");
			objectDetailsTableIdentifier.setHTML(row++, 1, p.getIdentifier());
		} else if (ao instanceof Star) {
			Star s = (Star) ao;
			objectDetailsTableIdentifier.setHTML(row, 0, "NUMERO HR");
			objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getHrNumber());

			objectDetailsTableIdentifier.setHTML(row, 0, "NUMERO HD");
			objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getHdNumber());

			objectDetailsTableIdentifier.setHTML(row, 0, "NUMERO SAO");
			objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getSaoNumber());

			objectDetailsTableIdentifier.setHTML(row, 0, "NOM");
			objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getName());

			objectDetailsTableIdentifier.setHTML(row, 0, "NUMERO FK5");
			objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getFk5Number());

			objectDetailsTableIdentifier.setHTML(row, 0, "ID DURCHMUSTERUNG");
			objectDetailsTableIdentifier
					.setHTML(row++, 1, "" + s.getDurchmusterungIdentification());

			if (s.getAdsNumber() != null) {
				objectDetailsTableIdentifier.setHTML(row, 0, "NUMERO ADS");
				objectDetailsTableIdentifier.setHTML(row++, 1, "" + s.getAdsNumber());
			}

			if (s.getVariableStarIdentification() != null) {
				objectDetailsTableIdentifier.setHTML(row, 0, "IDENTIFICATION VARIABLE");
				objectDetailsTableIdentifier.setHTML(row++, 1,
						"" + s.getVariableStarIdentification());
			}

		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			objectDetailsTableIdentifier.setHTML(row, 0, "NOM");
			objectDetailsTableIdentifier.setHTML(row++, 1, dso.getName());

			objectDetailsTableIdentifier.setHTML(row, 0, "AUTRE NOM");
			objectDetailsTableIdentifier.setHTML(row++, 1, dso.getOtherName());
		}

		// Coordinates
		EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
				new EquatorialCoordinates(ao.getRightAscension(), ao.getDeclinaison()));
		MathContext mc = new MathContext(5);
		row = 0;
		objectDetailsTableCoordinates.setHTML(row, 0, "ASCENSION DROITE");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEquatorialCoordinates().getRightAscension() / 15)
								.round(mc).doubleValue()).toString(SexagesimalType.HOURS));

		objectDetailsTableCoordinates.setHTML(row, 0, "DECLINAISON");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEquatorialCoordinates().getDeclinaison())
								.round(mc).doubleValue()).toString(SexagesimalType.DEGREES));

		objectDetailsTableCoordinates.setHTML(row, 0, "LONGITUDE ECLIPTIQUE");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEcliptiqueLongitude()).round(mc).doubleValue())
						.toString(SexagesimalType.DEGREES));

		objectDetailsTableCoordinates.setHTML(row, 0, "LATITUDE ECLIPTIQUE");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEcliptiqueLatitude()).round(mc).doubleValue())
						.toString(SexagesimalType.DEGREES));

		objectDetailsTableCoordinates.setHTML(row, 0, "LONGITUDE GALACTIQUE");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getGalacticLongitude()).round(mc).doubleValue())
						.toString(SexagesimalType.DEGREES));

		objectDetailsTableCoordinates.setHTML(row, 0, "LATITUDE GALACTIQUE");
		objectDetailsTableCoordinates.setHTML(
				row++,
				1,
				Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getGalacticLatitude()).round(mc).doubleValue())
						.toString(SexagesimalType.DEGREES));
		if (ao instanceof DeepSkyObject) {
			objectDetailsTableCoordinates.setHTML(row, 0, "CONSTELLATION");
			objectDetailsTableCoordinates.setHTML(row++, 1, ((DeepSkyObject) ao).getConstellation()
					.getName());
		}

		// Brightness
		row = 0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			objectDetailsTableBrightness.setText(row, 0, "ALBEDO");
			objectDetailsTableBrightness.setText(row++, 1, "" + PlanetEnum.forId(p.getNumericIdentifier()).getMeanAlbedo());
			
			// TODO : Compute the magnitude of the planet...
		} else if (ao instanceof Star) {
			Star s = (Star) ao;
			if (s.getSpectralType() != null) {
				objectDetailsTableBrightness.setText(row, 0, "TYPE SPECTRAL");
				objectDetailsTableBrightness.setText(row++, 1, "" + s.getSpectralType());
			}
			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE");
			objectDetailsTableBrightness.setText(row++, 1, "" + s.getVisualMagnitude());

			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE B-V");
			objectDetailsTableBrightness.setText(row++, 1,
					"" + s.getBvMag() + (s.isUncertainBvMag() ? " (uncertain) " : ""));

			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE U-B");
			objectDetailsTableBrightness.setText(row++, 1,
					"" + s.getUbMag() + (s.isUncertainUbMag() ? " (uncertain) " : ""));

			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE R-I");
			objectDetailsTableBrightness.setText(row++, 1, "" + s.getRiMag());

			objectDetailsTableBrightness.setText(row, 0, "SOURCE IR ?");
			objectDetailsTableBrightness.setText(row++, 1, "" + s.isIrSource());

		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE");
			objectDetailsTableBrightness.setText(row++, 1, "" + dso.getMagnitude());

			objectDetailsTableBrightness.setText(row, 0, "MAGNITUDE SURFACIQUE");
			objectDetailsTableBrightness.setText(row++, 1, "" + dso.getSurfaceBrightness());

			objectDetailsTableBrightness.setText(row, 0, "DIMENSION");
			objectDetailsTableBrightness.setText(row++, 1, "" + dso.getSizeHumanReadable());

			if (dso.getPositionAngle() != null) {
				objectDetailsTableBrightness.setText(row, 0, "ORIENTATION");
				objectDetailsTableBrightness.setText(row++, 1, "" + dso.getPositionAngle() + "°");
			}

		}

		// Specificities
		row = 0;
		if (ao instanceof Star) {
			Star s = (Star) ao;
			if (s.getProperMotionRa() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "MOUVEMENT PROPRE EN RA");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getProperMotionRa());
			}
			if (s.getProperMotionDec() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "MOUVEMENT PROPRE EN DEC");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getProperMotionDec());
			}
			if (s.getRadialVelocity() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "VITESSE RADIALE");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getRadialVelocity());
			}
			if (s.getParallax() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "PARALLAXE");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getParallax());
			}
			if (s.getParallaxCode() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "CODE PARALLAXE");
				objectDetailsTableSpecificities.setText(row++, 1, ""
						+ s.getParallaxCode().getComment());
			}
			if (s.getCompanionCount() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "NOMBRE DE COMPAGNIONS");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getCompanionCount());
			}
			if (s.getMultipleStarCode() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "CODE ETOILE MULTIPLE");
				objectDetailsTableSpecificities.setText(row++, 1, s.getMultipleStarCode()
						.getComment());
			}
			if (s.getCompanionIdentification() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "ID ETOILE COMPAGNION");
				objectDetailsTableSpecificities.setText(row++, 1, s.getCompanionIdentification());
			}
			if (s.getCompanionSeparation() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "SEPARATION");
				objectDetailsTableSpecificities.setText(row++, 1, "" + s.getCompanionSeparation());
			}
			if (s.getCompanionMagnitudeDifference() != null) {
				objectDetailsTableSpecificities.setText(row, 0,
						"DIFFERENCE DE MAGNITUDE AVEC LE COMPAGNION");
				objectDetailsTableSpecificities.setText(row++, 1,
						"" + s.getCompanionMagnitudeDifference());
			}

		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			objectDetailsTableSpecificities.setText(row, 0, "TYPE");
			objectDetailsTableSpecificities.setText(row++, 1, dso.getType().getComment());

			if (dso.getClasstype() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "SOUS-TYPE");
				objectDetailsTableSpecificities.setText(row++, 1, dso.getClasstype());
			}

			if (dso.getStarsCount() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "NOMBRE D'ETOILES");
				objectDetailsTableSpecificities.setText(row++, 1, "" + dso.getStarsCount());
			}

			if (dso.getBrightestStarMagnitude() != null) {
				objectDetailsTableSpecificities.setText(row, 0,
						"MAGNITUDE DE L'ETOILE LA PLUS LUMINEUSE");
				objectDetailsTableSpecificities.setText(row++, 1,
						"" + dso.getBrightestStarMagnitude());
			}

			objectDetailsTableSpecificities.setText(row, 0,
					"PRESENT DANS LE CATALOGUE \"BEST NGC\"");
			objectDetailsTableSpecificities.setText(row++, 1, dso.isInBestNgcCatalog() ? "OUI"
					: "NON");

			objectDetailsTableSpecificities.setText(row, 0,
					"PRESENT DANS LE CATALOGUE \"CALDWELL\"");
			objectDetailsTableSpecificities.setText(row++, 1, dso.isInCaldwellCalatalog() ? "OUI"
					: "NON");

			objectDetailsTableSpecificities.setText(row, 0,
					"PRESENT DANS LE CATALOGUE \"HERSCHEL\"");
			objectDetailsTableSpecificities.setText(row++, 1, dso.isInHerschelCatalog() ? "OUI"
					: "NON");

			objectDetailsTableSpecificities
					.setText(row, 0, "PRESENT DANS LE CATALOGUE \"MESSIER\"");
			objectDetailsTableSpecificities.setText(row++, 1, dso.isInMessierCatalog() ? "OUI"
					: "NON");

			if (dso.getNgcDescription() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "DESCRIPTION NGC");
				objectDetailsTableSpecificities.setText(row++, 1, dso.getNgcDescription());
			}

			if (dso.getNotes() != null) {
				objectDetailsTableSpecificities.setText(row, 0, "NOTES");
				objectDetailsTableSpecificities.setText(row++, 1, dso.getNotes());
			}
		}
		
		// External resources
		row = 0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			objectDetailsTableExternalResources.setHTML(row++, 0, "<a href='http://en.wikipedia.org/wiki/"+p.getIdentifier().toLowerCase()+"' target='_blank'>Wikipedia</a>");
			objectDetailsTableExternalResources.setHTML(row++, 0,
					"<a href='http://www.astrobin.com/search/?search_type=0"
					+ "&license=0&license=1&license=2&license=3&license=4&license=5&license=6"
					+ "&telescope_type=any&telescope_type=0&telescope_type=1&telescope_type=2&telescope_type=3"
					+ "&telescope_type=4&telescope_type=5&telescope_type=6&telescope_type=7&telescope_type=8"
					+ "&telescope_type=9&telescope_type=10&telescope_type=11&telescope_type=12&telescope_type=13"
					+ "&telescope_type=14&telescope_type=15&telescope_type=16&telescope_type=17&telescope_type=18"
					+ "&telescope_type=19&telescope_type=20&telescope_type=21&telescope_type=22"
					+ "&camera_type=any&camera_type=0&camera_type=1&camera_type=2&camera_type=3&camera_type=4&camera_type=5&q="
					+ p.getIdentifier()+"' target='_blank'>Astrobin</a>");
			
		} else if (ao instanceof Star) {
			Star s = (Star) ao;
			objectDetailsTableExternalResources.setHTML(row++, 0, 
					"<a href='http://simbad.u-strasbg.fr/simbad/sim-id?Ident=hr"+s.getHrNumber()+"' target='_blank'>Simbad</a>");
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			objectDetailsTableExternalResources.setHTML(row++, 0,
					"<a href='http://www.astrobin.com/search/?search_type=0"
					+ "&license=0&license=1&license=2&license=3&license=4&license=5&license=6"
					+ "&telescope_type=any&telescope_type=0&telescope_type=1&telescope_type=2&telescope_type=3"
					+ "&telescope_type=4&telescope_type=5&telescope_type=6&telescope_type=7&telescope_type=8"
					+ "&telescope_type=9&telescope_type=10&telescope_type=11&telescope_type=12&telescope_type=13"
					+ "&telescope_type=14&telescope_type=15&telescope_type=16&telescope_type=17&telescope_type=18"
					+ "&telescope_type=19&telescope_type=20&telescope_type=21&telescope_type=22"
					+ "&camera_type=any&camera_type=0&camera_type=1&camera_type=2&camera_type=3&camera_type=4&camera_type=5&q="
					+ dso.getName()+"' target='_blank'>Astrobin</a>");
			objectDetailsTableExternalResources.setHTML(row++, 0, 
					"<a href='http://fr.wikipedia.org/w/index.php?fulltext=Search&searchengineselect=mediawiki&search="+dso.getName()+"' target='_blank'>Wikipedia</a>");
			objectDetailsTableExternalResources.setHTML(row++, 0, 
					"<a href='http://deepskypedia.com/w/index.php?search="+dso.getName()+"' target='_blank'>DeepSkyPedia</a>");
			objectDetailsTableExternalResources.setHTML(row++, 0, 
					"<a href='http://simbad.u-strasbg.fr/simbad/sim-id?Ident="+dso.getName()+"' target='_blank'>Simbad</a>");
		}
	}
}
