package com.nzv.gwt.dsocatalog.visualization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.ChartArea;
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
import com.nzv.gwt.dsocatalog.client.DetailsPanelSelectionHandler;
import com.nzv.gwt.dsocatalog.client.DsoCatalogGWT;
import com.nzv.gwt.dsocatalog.client.Observer;
import com.nzv.gwt.dsocatalog.client.UpdateDssImageHandler;
import com.nzv.gwt.dsocatalog.i18n.DsoCatalogMessages;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Planet;
import com.nzv.gwt.dsocatalog.model.PlanetEnum;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.OrthographicProjection;

public class VisualizationHelper {
	
	private static DsoCatalogMessages msg = GWT.create(DsoCatalogMessages.class);

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
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapEclHaxisTitle());
			break;
		case GAL:
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapGalHaxisTitle());
			break;
		case ALTAZ:
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapAltazHaxisTitle());
			break;
		case EQ:
		default:
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapEqHaxisTitle());
			break;
		}
		if (searchOptions.getCoordinatesSystem() == CoordinatesSystem.ALTAZ) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonHorizon());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
			optimizedData.addAnnotationColumn(optimizedData);
		}
		if (searchOptions.isDisplayEcliptic()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonEcliptic());
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayEquator()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonEquator());
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayGalacticPlan()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonGalacticPlan());
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayConstellationNames()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonConstellation());
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
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapConstellationBoundaries());
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isDisplayConstellationShape()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.mapConstellationShapes());
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindAsterisms()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayAsterisms());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindGalaxies()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayGalaxies());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindGlobularClusters()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayGlobularCluster());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindOpenClusters()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayOpenCluster());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindPlanetaryNebulas()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayPlanetaryNebulas());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindNebulas()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayNebulas());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindSupernovaRemnant()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplaySnRemnants());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindQuasars()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.tabFiltersDisplayQuasars());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindStars()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonStars());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}
		if (searchOptions.isFindPlanets()) {
			optimizedData.addColumn(ColumnType.NUMBER, msg.commonPlanets());
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
		}

		optimizedData.addRows(objects.size());
		if (searchOptions.getCoordinatesSystem() == CoordinatesSystem.ALTAZ) {
			// We create a row for each point of the horizon (one each 1 degree)
			optimizedData.addRows(361);
		}
		if (searchOptions.isDisplayEcliptic()) {
			optimizedData.addRows(360*2);
		}
		if (searchOptions.isDisplayEquator()) {
			optimizedData.addRows(360*2);
		}
		if (searchOptions.isDisplayGalacticPlan()) {
			optimizedData.addRows(360*2);
		}
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
			sb.append(msg.mapTooltipRightAscension()+"=" + ra.toString(SexagesimalType.HOURS) + "\n");
			sb.append(msg.mapTooltipDeclinaison()+"=" + dec.toString(SexagesimalType.DEGREES) + "\n");
		}

		CoordinatesSystem cs = CoordinatesSystem.instanceOf(appPanel.getLiCoordinatesMode()
				.getValue(appPanel.getLiCoordinatesMode().getSelectedIndex()));
		Sexagesimal x = new Sexagesimal(BigDecimal
				.valueOf(o.getXCoordinateForReferential(cs, observer)).round(mc).doubleValue());
		Sexagesimal y = new Sexagesimal(BigDecimal
				.valueOf(o.getYCoordinateForReferential(cs, observer)).round(mc).doubleValue());
		switch (cs) {
		case ECL:
			sb.append(msg.mapTooltipEclipticLongitude()+"=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append(msg.mapTooltipEclipticLatitude()+"=" + y.toString(SexagesimalType.DEGREES) + " \n");
			break;
		case GAL:
			sb.append(msg.mapTooltipGalacticLongitude()+"=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append(msg.mapTooltipGalacticLatitude()+"=" + y.toString(SexagesimalType.DEGREES) + " \n");
			break;
		case EQ:
			break;
		case ALTAZ:
			sb.append(msg.mapTooltipAzimuth()+"=" + x.toString(SexagesimalType.DEGREES) + " \n");
			sb.append(msg.mapTooltipElevation()+"=" + y.toString(SexagesimalType.DEGREES) + " \n");
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
		CoordinatesSystem cs = 
				CoordinatesSystem.valueOf(appPanel.getLiCoordinatesMode().getValue(appPanel.getLiCoordinatesMode().getSelectedIndex()));
		boolean displayOnConstellation = 
				searchOptions.getRestrictedToConstellationCode() != null && !searchOptions.getRestrictedToConstellationCode().isEmpty();
		int chartWidth = Window.getClientWidth() - (2 * ApplicationBoard.PANEL_SPLITTER_WIDTH)
				- ApplicationBoard.LEFT_PANEL_WIDTH;
		int chartHeight = Window.getClientHeight() - 25 - ApplicationBoard.SOUTH_PANEL_HEIGHT;
		if (cs == CoordinatesSystem.ALTAZ) {
			// We use a ratio 1:1 on chart width:height
			if (chartHeight > chartWidth) {
				chartHeight = chartWidth;
			} else {
				chartWidth = chartHeight;
			}
		} else if (cs == CoordinatesSystem.ECL || cs == CoordinatesSystem.GAL || (cs == CoordinatesSystem.EQ && !displayOnConstellation)) {
			// We use a ration 2:1 on chart width:height
			if (chartHeight > chartWidth / 2) {
				chartHeight = chartWidth / 2;
			} else {
				chartWidth = 2 * chartHeight;
			}
		} else {
			// We use the ratio of the constellation width:height 
			Constellation displayedConstellation = constellationsList.get(searchOptions.getRestrictedToConstellationCode());
			EquatorialCoordinates uw = displayedConstellation.getUpperWesternMapLimit();
			EquatorialCoordinates le = displayedConstellation.getLowerEasternMapLimit();
			double constellationWidth = 
					GeometryUtils.getAngleDifference(uw.getRightAscension(), le.getRightAscension());
			double constellationHeight =
					GeometryUtils.getAngleDifference(uw.getDeclinaison(), le.getDeclinaison());
			double chartWidthHeightRatio = constellationWidth / constellationHeight;
			double windowWidthHeightRatio = (1.0 * chartWidth) / (1.0 * chartHeight);
			if (chartWidthHeightRatio > windowWidthHeightRatio) {
				chartHeight = (int) (chartWidth / chartWidthHeightRatio);
			} else {
				chartWidth = (int) (chartHeight * chartWidthHeightRatio);
			}
		}
		options.setWidth(chartWidth);
		options.setHeight(chartHeight);
		ChartArea area = ChartArea.create();
		area.setLeft(45);
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
		if (("" + CoordinatesSystem.ECL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle(msg.mapEclHaxisTitle());
			vAxisOptions.setMinValue(-90);
			vAxisOptions.setMaxValue(90);
			vAxisOptions.setTitle(msg.mapEclVaxisTitle());
		} else if (("" + CoordinatesSystem.GAL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle(msg.mapGalHaxisTitle());
			vAxisOptions.setMinValue(-90);
			vAxisOptions.setMaxValue(90);
			vAxisOptions.setTitle(msg.mapGalVaxisTitle());
		} else if (("" + CoordinatesSystem.ALTAZ).equals(coordinatesMode)) {
			hAxisOptions.setTitle(msg.mapAltazHaxisTitle());
			vAxisOptions.setTitle(msg.mapAltazVaxisTitle());
			hAxisOptions.setMinValue(Math.toDegrees(OrthographicProjection.MIN_X));
			hAxisOptions.setMaxValue(Math.toDegrees(OrthographicProjection.MAX_X));
			vAxisOptions.setMinValue(Math.toDegrees(OrthographicProjection.MIN_Y));
			vAxisOptions.setMaxValue(Math.toDegrees(OrthographicProjection.MAX_Y));
		} else if (("" + CoordinatesSystem.EQ).equals(coordinatesMode)) {
			if (!appPanel.getLiConstellations()
					.getValue(appPanel.getLiConstellations().getSelectedIndex()).isEmpty()) {
				// We set the limit of the map to the limit of the selected
				// constellation...
				Constellation constellation = constellationsList.get(appPanel.getLiConstellations().getValue(
						appPanel.getLiConstellations().getSelectedIndex()));
				EquatorialCoordinates upperWesterMapLimit = constellation.getUpperWesternMapLimit();
				EquatorialCoordinates lowerEasterMapLimit = constellation.getLowerEasternMapLimit();
				hAxisOptions.setMinValue((int) upperWesterMapLimit.getRightAscension());
				hAxisOptions.setMaxValue((int) (lowerEasterMapLimit.getRightAscension()));
				vAxisOptions.setMinValue((int) lowerEasterMapLimit.getDeclinaison());
				vAxisOptions.setMaxValue((int) (upperWesterMapLimit.getDeclinaison()));
			} else {
				hAxisOptions.setMinValue(0);
				hAxisOptions.setMaxValue(360);
				vAxisOptions.setMinValue(-90);
				vAxisOptions.setMaxValue(90);
			}
			hAxisOptions.setTitle(msg.mapEqHaxisTitle());
			vAxisOptions.setTitle(msg.mapEqVaxisTitle());
		}
		options.set("pointShape", "star");
		options.setPointSize(5);
		options.setLineWidth(0);
		TextStyle textStyle = TextStyle.create();
		textStyle.setColor("#ffffff");
		if (!("" + CoordinatesSystem.ALTAZ).equals(coordinatesMode)) {
			hAxisOptions.setDirection(-1);
		}
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
		
		// Configure the zoom
		Explorer explorer = Explorer.create();
		explorer.setMaxZoomIn(0.001);
		explorer.setMaxZoomOut(1);
		explorer.setZoomDelta(1.1);
		options.setExplorer(explorer);
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
		if (searchOptions.getCoordinatesSystem() == CoordinatesSystem.ALTAZ) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointShape(Shape.circle);
			s.setPointSize(0);
			s.setColor(DsoCatalogGWT.STYLE_HORIZON_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayEcliptic()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointShape(Shape.circle);
			s.setPointSize(0);
			s.setColor(DsoCatalogGWT.STYLE_ECLIPTIC_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayEquator()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointShape(Shape.circle);
			s.setPointSize(0);
			s.setColor(DsoCatalogGWT.STYLE_EQUATOR_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayGalacticPlan()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointShape(Shape.circle);
			s.setPointSize(0);
			s.setColor(DsoCatalogGWT.STYLE_GALACTIC_PLAN_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
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
		for (int j = 0; j < serieIndexes.getObjectSeriesCount(); j++) {
			MySeries s = MySeries.create();
			s.setLineWidth(0);
			s.setVisibleInLegend(false);
			options.setSeries((i+j), s);
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
	public static void displayObjectDetails(AstroObject ao, TabLayoutPanel objectDetailsPanel) {
		objectDetailsPanel.clear();
		MathContext precision = new MathContext(5);
		String[] availableTabs = null;
		if (ao instanceof Planet) {
			objectDetailsPanel.add(generateIdentifiersTable(ao), new Label(msg.detailsTabIdentifiersTitle()));
			objectDetailsPanel.add(generateCoordinatesTable(ao, precision), new Label(msg.detailsTabCoordinatesTitle()));
			objectDetailsPanel.add(generateAspectTable(ao), new Label(msg.detailsTabAspectTitle()));
			objectDetailsPanel.add(generateExternalResourcesTable(ao), new Label(msg.detailsTabExternalResourcesTitle()));
			availableTabs = new String[]{msg.detailsTabIdentifiersTitle(), msg.detailsTabCoordinatesTitle(),
					msg.detailsTabAspectTitle(), msg.detailsTabExternalResourcesTitle()};
			
		} else if (ao instanceof Star) {
			objectDetailsPanel.add(generateIdentifiersTable(ao), new Label(msg.detailsTabIdentifiersTitle()));
			objectDetailsPanel.add(generateCoordinatesTable(ao, precision), new Label(msg.detailsTabCoordinatesTitle()));
			objectDetailsPanel.add(generateAspectTable(ao), new Label(msg.detailsTabAspectTitle()));
			objectDetailsPanel.add(generateSpecificitiesTable(ao), new Label(msg.detailsTabSpecificitiesTitle()));
			objectDetailsPanel.add(generateExternalResourcesTable(ao), new Label(msg.detailsTabExternalResourcesTitle()));
			objectDetailsPanel.add(generateImageTable(ao), new Label(msg.detailsTabImagesTitle()));
			availableTabs = new String[]{msg.detailsTabIdentifiersTitle(), msg.detailsTabCoordinatesTitle(),
					msg.detailsTabAspectTitle(), msg.detailsTabSpecificitiesTitle(),
					msg.detailsTabExternalResourcesTitle(), msg.detailsTabImagesTitle()};
			
		} else if (ao instanceof DeepSkyObject) {
			objectDetailsPanel.add(generateIdentifiersTable(ao), new Label(msg.detailsTabIdentifiersTitle()));
			objectDetailsPanel.add(generateCoordinatesTable(ao, precision), new Label(msg.detailsTabCoordinatesTitle()));
			objectDetailsPanel.add(generateAspectTable(ao), new Label(msg.detailsTabAspectTitle()));
			objectDetailsPanel.add(generateSpecificitiesTable(ao), new Label(msg.detailsTabSpecificitiesTitle()));
			objectDetailsPanel.add(generateExternalResourcesTable(ao), new Label(msg.detailsTabExternalResourcesTitle()));
			objectDetailsPanel.add(generateImageTable(ao), new Label(msg.detailsTabImagesTitle()));
			availableTabs = new String[]{msg.detailsTabIdentifiersTitle(), msg.detailsTabCoordinatesTitle(),
					msg.detailsTabAspectTitle(), msg.detailsTabSpecificitiesTitle(),
					msg.detailsTabExternalResourcesTitle(), msg.detailsTabImagesTitle()};
		}
		objectDetailsPanel.addSelectionHandler(new DetailsPanelSelectionHandler(availableTabs));
	}
	
	private static HorizontalPanel generateImageTable(AstroObject ao) {
		HorizontalPanel imagePanel = new HorizontalPanel(); 
		SimplePanel leftPart = new SimplePanel();
		leftPart.setPixelSize(ApplicationBoard.SOUTH_PANEL_HEIGHT - 30, ApplicationBoard.SOUTH_PANEL_HEIGHT - 30);
		final Image spinner = new Image("images/spinner.gif");
		final Image img = new Image(UpdateDssImageHandler.getDssImageUrl("phase2_gsc1", ao));
		img.setSize(""+(ApplicationBoard.SOUTH_PANEL_HEIGHT - 30), ""+(ApplicationBoard.SOUTH_PANEL_HEIGHT - 30));
		img.setAltText("From Digital Sky Survey");
		img.setVisible(false);
		spinner.setVisible(true);
		img.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				spinner.setVisible(false);
				img.setVisible(true);
			}
		});
		leftPart.add(img);
		imagePanel.add(leftPart);
		
		// We create a list of available surveys...
		VerticalPanel surveyPanel = new VerticalPanel();
		surveyPanel.add(new Label(msg.detailsTabImagesSurveys()+" :"));
		ListBox liSurveys = new ListBox();
		liSurveys.addItem(msg.detailsTabImagesSurveysPoss2ukstuRed(), "poss2ukstu_red");
		liSurveys.addItem(msg.detailsTabImagesSurveysPoss2ukstuBlue(), "poss2ukstu_blue");
		liSurveys.addItem(msg.detailsTabImagesSurveysPoss2ukstuIr(), "poss2ukstu_ir");
		liSurveys.addItem(msg.detailsTabImagesSurveysPoss1Red(), "poss1_red");
		liSurveys.addItem(msg.detailsTabImagesSurveysPoss1Blue(), "poss1_blue");
		liSurveys.addItem(msg.detailsTabImagesSurveysQuickv(), "quickv");
		liSurveys.addItem(msg.detailsTabImagesSurveysPhase2Gsc2(), "phase2_gsc2");
		liSurveys.addItem(msg.detailsTabImagesSurveysPhase2Gsc1(), "phase2_gsc1");
		liSurveys.addItem(msg.detailsTabImagesSurveysSloan(), "sloan");
		liSurveys.addItem(msg.detailsTabImagesSurveysSloanInverted(), "sloan_inverted");
		liSurveys.setSelectedIndex(liSurveys.getItemCount()-4);
		liSurveys.addChangeHandler(new UpdateDssImageHandler(liSurveys, img, spinner, ao));
		surveyPanel.add(liSurveys);
		spinner.getElement().setAttribute("alignement", "center");
		surveyPanel.add(spinner);
		imagePanel.add(surveyPanel);
		return imagePanel;
	}
	
	private static FlexTable generateIdentifiersTable(AstroObject ao) {
		FlexTable ftIdentifier = new FlexTable();
		int row=0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersPlanet());
			ftIdentifier.setHTML(row++, 1, p.getIdentifier());
		} else if (ao instanceof Star) {
			Star s = (Star) ao;
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersHrNumber());
			ftIdentifier.setHTML(row++, 1, "" + s.getHrNumber());
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersHdNumber());
			ftIdentifier.setHTML(row++, 1, "" + s.getHdNumber());
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersSaoNumber());
			ftIdentifier.setHTML(row++, 1, "" + s.getSaoNumber());
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersName());
			ftIdentifier.setHTML(row++, 1, "" + s.getName());
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersFk5Number());
			ftIdentifier.setHTML(row++, 1, "" + s.getFk5Number());
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersDurchmusterungId());
			ftIdentifier.setHTML(row++, 1, "" + s.getDurchmusterungIdentification());
			if (s.getAdsNumber() != null) {
				ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersAdsNumber());
				ftIdentifier.setHTML(row++, 1, "" + s.getAdsNumber());
			}
			if (s.getVariableStarIdentification() != null) {
				ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersVariableId());
				ftIdentifier.setHTML(row++, 1,
						"" + s.getVariableStarIdentification());
			}
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersName());
			ftIdentifier.setHTML(row++, 1, dso.getName());

			ftIdentifier.setHTML(row, 0, msg.detailsTabIdentifiersOtherName());
			ftIdentifier.setHTML(row++, 1, dso.getOtherName());
		}
		
		return ftIdentifier;
	}
	
	private static FlexTable generateAspectTable(AstroObject ao) {
		FlexTable ftAspect = new FlexTable();
		int row=0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			ftAspect.setText(row, 0, msg.detailsTabAspectAlbedo());
			ftAspect.setText(row++, 1, "" + PlanetEnum.forId(p.getNumericIdentifier()).getMeanAlbedo());
			
		} else if (ao instanceof Star) {
			Star s = (Star) ao;
			if (s.getSpectralType() != null) {
				ftAspect.setText(row, 0, msg.detailsTabAspectSpectralType());
				ftAspect.setText(row++, 1, "" + s.getSpectralType());
			}
			ftAspect.setText(row, 0, msg.detailsTabAspectMagnitude());
			ftAspect.setText(row++, 1, "" + s.getVisualMagnitude());
			ftAspect.setText(row, 0, msg.detailsTabAspectMagnitudeBV());
			ftAspect.setText(row++, 1, "" + s.getBvMag() + (s.isUncertainBvMag() ? " ("+msg.detailsTabAspectMagnitudeUncertain()+") " : ""));
			ftAspect.setText(row, 0, msg.detailsTabAspectMagnitudeUB());
			ftAspect.setText(row++, 1, "" + s.getUbMag() + (s.isUncertainUbMag() ? " ("+msg.detailsTabAspectMagnitudeUncertain()+") " : ""));
			ftAspect.setText(row, 0, msg.detailsTabAspectMagnitudeRI());
			ftAspect.setText(row++, 1, "" + s.getRiMag());
			ftAspect.setText(row, 0, msg.detailsTabAspectIrSource());
			ftAspect.setText(row++, 1, "" + s.isIrSource());
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			ftAspect.setText(row, 0, msg.detailsTabAspectMagnitude());
			ftAspect.setText(row++, 1, "" + dso.getMagnitude());
			ftAspect.setText(row, 0, msg.detailsTabAspectSurfaceBrightness());
			ftAspect.setText(row++, 1, "" + dso.getSurfaceBrightness());
			ftAspect.setText(row, 0, msg.detailsTabAspectDimension());
			ftAspect.setText(row++, 1, "" + dso.getSizeHumanReadable());
			if (dso.getPositionAngle() != null) {
				ftAspect.setText(row, 0, msg.detailsTabAspectOrientation());
				ftAspect.setText(row++, 1, "" + dso.getPositionAngle() + "°");
			}
		}
			 
		return ftAspect;
	}
	
	private static FlexTable generateSpecificitiesTable(AstroObject ao) {
		FlexTable ftSpec = new FlexTable();
		int row = 0;
		if (ao instanceof Star) {
			Star s = (Star) ao;
			if (s.getSpectralType() != null) {
				ftSpec.setText(row, 0, msg.detailsTabAspectSpectralType());
				ftSpec.setText(row++, 1, "" + s.getSpectralType());
			}
			if (s.getProperMotionRa() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesProperMotionRa());
				ftSpec.setText(row++, 1, "" + s.getProperMotionRa());
			}
			if (s.getProperMotionDec() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesProperMotionDec());
				ftSpec.setText(row++, 1, "" + s.getProperMotionDec());
			}
			if (s.getRadialVelocity() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesRadialSpeed());
				ftSpec.setText(row++, 1, "" + s.getRadialVelocity());
			}
			if (s.getParallax() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesParallaxe());
				ftSpec.setText(row++, 1, "" + s.getParallax());
			}
			if (s.getParallaxCode() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesParallaxeCode());
				ftSpec.setText(row++, 1, "" + s.getParallaxCode().getComment());
			}
			if (s.getCompanionCount() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesCompanionCount());
				ftSpec.setText(row++, 1, "" + s.getCompanionCount());
			}
			if (s.getMultipleStarCode() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesMultipleStarCode());
				ftSpec.setText(row++, 1, s.getMultipleStarCode()
						.getComment());
			}
			if (s.getCompanionIdentification() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesIdCompanionStar());
				ftSpec.setText(row++, 1, s.getCompanionIdentification());
			}
			if (s.getCompanionSeparation() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesSeparation());
				ftSpec.setText(row++, 1, "" + s.getCompanionSeparation());
			}
			if (s.getCompanionMagnitudeDifference() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesMagnitudeDifferenceWithCompanion());
				ftSpec.setText(row++, 1, "" + s.getCompanionMagnitudeDifference());
			}
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			ftSpec.setText(row, 0, msg.detailsTabSpecificitiesType());
			ftSpec.setText(row++, 1, dso.getType().getComment());
			if (dso.getClasstype() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesSubtype());
				ftSpec.setText(row++, 1, dso.getClasstype());
			}
			if (dso.getStarsCount() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesStarCount());
				ftSpec.setText(row++, 1, "" + dso.getStarsCount());
			}
			if (dso.getBrightestStarMagnitude() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesBrightestStarMagnitude());
				ftSpec.setText(row++, 1, "" + dso.getBrightestStarMagnitude());
			}
			ftSpec.setText(row, 0, msg.detailsTabSpecificitiesInBestofNgcCatalog());
			ftSpec.setText(row++, 1, dso.isInBestNgcCatalog() ? msg.commonYes() : msg.commonNo());
			ftSpec.setText(row, 0, msg.detailsTabSpecificitiesInCaldwellCatalog());
			ftSpec.setText(row++, 1, dso.isInCaldwellCalatalog() ? msg.commonYes() : msg.commonNo());
			ftSpec.setText(row, 0, msg.detailsTabSpecificitiesInHerschelCatalog());
			ftSpec.setText(row++, 1, dso.isInHerschelCatalog() ? msg.commonYes() : msg.commonNo());
			ftSpec.setText(row, 0, msg.detailsTabSpecificitiesInMessierCatalog());
			ftSpec.setText(row++, 1, dso.isInMessierCatalog() ? msg.commonYes() : msg.commonNo());
			if (dso.getNgcDescription() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesNgcDescription());
				ftSpec.setText(row++, 1, dso.getNgcDescription());
			}
			if (dso.getNotes() != null) {
				ftSpec.setText(row, 0, msg.detailsTabSpecificitiesNotes());
				ftSpec.setText(row++, 1, dso.getNotes());
			}
		}
		return ftSpec;
	}
	
	private static FlexTable generateExternalResourcesTable(AstroObject ao) {
		FlexTable ftExtResources = new FlexTable();
		int row=0;
		if (ao instanceof Planet) {
			Planet p = (Planet) ao;
			ftExtResources.setHTML(row++, 0, "<a href='http://en.wikipedia.org/wiki/"+p.getIdentifier().toLowerCase()+"' target='_blank'>Wikipedia</a>");
			ftExtResources.setHTML(row++, 0,
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
			ftExtResources.setHTML(row++, 0, 
					"<a href='http://simbad.u-strasbg.fr/simbad/sim-id?Ident=hr"+s.getHrNumber()+"' target='_blank'>Simbad</a>");
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) ao;
			ftExtResources.setHTML(row++, 0,
					"<a href='http://www.astrobin.com/search/?search_type=0"
					+ "&license=0&license=1&license=2&license=3&license=4&license=5&license=6"
					+ "&telescope_type=any&telescope_type=0&telescope_type=1&telescope_type=2&telescope_type=3"
					+ "&telescope_type=4&telescope_type=5&telescope_type=6&telescope_type=7&telescope_type=8"
					+ "&telescope_type=9&telescope_type=10&telescope_type=11&telescope_type=12&telescope_type=13"
					+ "&telescope_type=14&telescope_type=15&telescope_type=16&telescope_type=17&telescope_type=18"
					+ "&telescope_type=19&telescope_type=20&telescope_type=21&telescope_type=22"
					+ "&camera_type=any&camera_type=0&camera_type=1&camera_type=2&camera_type=3&camera_type=4&camera_type=5&q="
					+ dso.getName()+"' target='_blank'>Astrobin</a>");
			ftExtResources.setHTML(row++, 0, 
					"<a href='http://fr.wikipedia.org/w/index.php?fulltext=Search&searchengineselect=mediawiki&search="+dso.getName()+"' target='_blank'>Wikipedia</a>");
			ftExtResources.setHTML(row++, 0, 
					"<a href='http://deepskypedia.com/w/index.php?search="+dso.getName()+"' target='_blank'>DeepSkyPedia</a>");
			ftExtResources.setHTML(row++, 0, 
					"<a href='http://simbad.u-strasbg.fr/simbad/sim-id?Ident="+dso.getName()+"' target='_blank'>Simbad</a>");
		}
		return ftExtResources;
	}
	
	private static FlexTable generateCoordinatesTable(AstroObject ao, MathContext precision) {
		FlexTable table = new FlexTable();
		int row=0;
		EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(ao.getRightAscension(), ao.getDeclinaison()));
		
		table.setHTML(row, 0, msg.detailsTabCoordinatesRightAscension());
		table.setHTML(row++, 1,Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEquatorialCoordinates().getRightAscension() / 15)
								.round(precision).doubleValue()).toString(SexagesimalType.HOURS));
		table.setHTML(row, 0, msg.detailsTabCoordinatesDeclinaison());
		table.setHTML(row++, 1, Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEquatorialCoordinates().getDeclinaison())
								.round(precision).doubleValue()).toString(SexagesimalType.DEGREES));
		table.setHTML(row, 0, msg.detailsTabCoordinatesEclipticLongitude());
		table.setHTML(row++, 1, Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEcliptiqueLongitude()).round(precision).doubleValue())
						.toString(SexagesimalType.DEGREES));
		table.setHTML(row, 0, msg.detailsTabCoordinatesEclipticLatitude());
		table.setHTML(row++, 1, Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getEcliptiqueLatitude()).round(precision).doubleValue())
						.toString(SexagesimalType.DEGREES));
		table.setHTML(row, 0, msg.detailsTabCoordinatesGalacticLongitude());
		table.setHTML(row++, 1, Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getGalacticLongitude()).round(precision).doubleValue())
						.toString(SexagesimalType.DEGREES));
		table.setHTML(row, 0, msg.detailsTabCoordinatesGalacticLatitude());
		table.setHTML(row++, 1, Sexagesimal.decimalToSexagesimal(
						BigDecimal.valueOf(eca.getGalacticLatitude()).round(precision).doubleValue())
						.toString(SexagesimalType.DEGREES));
		if (ao instanceof DeepSkyObject) {
			table.setHTML(row, 0, msg.detailsTabCoordinatesConstellation());
			table.setHTML(row++, 1, ((DeepSkyObject) ao).getConstellation().getName());
		}
		return table;
	}
}
