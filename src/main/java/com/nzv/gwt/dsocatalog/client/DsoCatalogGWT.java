package com.nzv.gwt.dsocatalog.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.coordinate.GeographicCoordinates;
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.date.DateComputation;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPoint;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.visualization.MyDataTable;
import com.nzv.gwt.dsocatalog.visualization.MyLineChartOptions;
import com.nzv.gwt.dsocatalog.visualization.MySeries;

public class DsoCatalogGWT implements EntryPoint {

	public static String DATE_FORMAT = "dd/MM/yyyy";
	public static String TIME_FORMAT = "HH:mm:ss";
	
	private static final DateTimeFormat dtfDateAndTime = DateTimeFormat.getFormat(DATE_FORMAT+" "+TIME_FORMAT);
	private static final DateTimeFormat dtfDate = DateTimeFormat.getFormat(DATE_FORMAT);
	private static final DateTimeFormat dtfTime = DateTimeFormat.getFormat(TIME_FORMAT);
	
	/* Styles */
	public static String STYLE_CHART_BACKGROUND_COLOR = "#110e3d";
	public static String AXIS_TITLE_TEXT_STYLE = "color: #ffffff;";
	public static String STYLE_CONSTELLATION_BORDER_COLOR = "#888888";
	public static String STYLE_CONSTELLATION_SHAPE_COLOR = "#ffffff";
	public static String STYLE_STARS = "point {shape-type: star;shape-dent: 0.2; size: 5; fill-color: #f29500;}";
	public static String STYLE_ASTERISMS = "point { shape-type: polygon; size: 4; fill-color: #110e3d; stroke-color: #f29500; stroke-width: 1;}";
	public static String STYLE_GALAXIES = "point { shape-type: circle; size: 4; fill-color: #110e3d; stroke-color: #f42929; stroke-width: 2;}";
	public static String STYLE_GLOBULAR_CLUSTERS = "point {shape-type: circle; size: 4; fill-color: #9bf2bd; stroke-color: #000000; stroke-width: 1;}";
	public static String STYLE_OPEN_CLUSTERS = "point { shape-type: circle; size: 4; fill-color: #110e3d; stroke-color: #04ea64; stroke-width: 1;}";
	public static String STYLE_PLANETARY_NEBULAS = "point { shape-type: star; shape-sides: 4; shape-dent: 0.2; size: 6; fill-color: #0099c6;}";
	public static String STYLE_NEBULAS = "point { shape-type: square; size: 6; fill-color: #f28787; stroke-color: #f42929; shape-rotation:45;}";
	public static String STYLE_SN_REMNANTS = "point { shape-type: square; size: 5; fill-color: #110e3d; stroke-color: #f42929; stroke-width: 1; shape-rotation:45;}";
	public static String STYLE_QUASARS = "point { shape-type: circle; size: 3; fill-color: #f42929;}";
	
	private static VerticalPanel filterPanel = new VerticalPanel();
	private static Label lbConstellation = new Label("Constellation : ");
	private static ListBox liConstellations = new ListBox();
	private static CheckBox chkDisplayConstellationShapes = new CheckBox("Afficher les formes des constellations");
	private static CheckBox chkDisplayConstellationBoundaries = new CheckBox("Afficher les limites des constellations");
	private static CheckBox chkDisplayStars = new CheckBox("Afficher les étoiles");
	private static Label lbStarLimitMagnitude = new Label(
			"Vmag. max étoiles :");
	private static TextBox txtBoxStarLimitMagnitude = new TextBox();

	private static CheckBox chkDisplayAsterisms = new CheckBox("Astérismes");
	private static CheckBox chkDisplayGalaxies = new CheckBox("Galaxies");
	private static CheckBox chkDisplayGlobularClusters = new CheckBox("Amas globulaires");
	private static CheckBox chkDisplayOpenClusters = new CheckBox("Amas ouverts");
	private static CheckBox chkDisplayPlanetaryNebulas = new CheckBox("Nébuleuses planétaires");
	private static CheckBox chkDisplayNebulas = new CheckBox("Nébuleuses");
	private static CheckBox chkDisplaySupernovaRemnants = new CheckBox("Rémanants du supernova");
	private static CheckBox chkDisplayQuasars = new CheckBox("Quasars");
	
	private static Label lbDsoLimitMagnitude = new Label(
			"Vmag. max ciel profond :");
	private static TextBox txtBoxDsoLimitMagnitude = new TextBox();
	
	private static Label lbCoordinatesMode = new Label("Système de coordonnées");
	private static ListBox liCoordinatesMode = new ListBox();
	private static CheckBox chkShowObjectsUnderHorizon = new CheckBox("Montrer les objets sous l'horizon");
	private static VerticalPanel coordinatesPanel = new VerticalPanel();
	
	private static Label lbObserverLatitude = new Label("Latitude de l'observateur");
	private static TextBox txtObserverLatitude = new TextBox();
	private static Label lbObserverLongitude = new Label("Longitude de l'observateur");
	private static TextBox txtObserverLongitude = new TextBox();
	private static Label lbObserverDate = new Label("Date pour l'observateur (jj/mm/yyyy)");
	private static TextBox txtObserverDate = new TextBox();
	private static CheckBox chkObserverUseComputerDateAndTime = new CheckBox("Utiliser l'heure système");
	private static Label lbObserverLocalTime = new Label("Heure locale de l'observateur (hh:mm:ss)");
	private static TextBox txtObserverLocalTime = new TextBox();
	private static PushButton btObserverSubstractOneWeek = new PushButton("-1w");
	private static PushButton btObserverSubstractOneDay = new PushButton("-1d");
	private static PushButton btObserverSubstractOneHour = new PushButton("-1h");
	private static PushButton btObserverAddOneHour = new PushButton("+1h");
	private static PushButton btObserverAddOneDay = new PushButton("+1d");
	private static PushButton btObserverAddOneWeek = new PushButton("+1w");
	private static HorizontalPanel timeExplorerPanel = new HorizontalPanel();
	private static Label lbGreenwichHourOffset = new Label("Décalage par rapport à l'heure de Greenwich");
	private static TextBox txtGreenwichHourOffset = new TextBox();
	private static VerticalPanel observerPanel = new VerticalPanel();

	private static Panel visualizationPanel = new SimplePanel();

	private static PushButton btUpdateMap = new PushButton("Update map");
	private static Label systemMessage = new Label("");
	
	private static StackLayoutPanel configurationPanel = new StackLayoutPanel(Unit.PX);
	private static VerticalPanel leftPanel = new VerticalPanel();
	private static VerticalPanel rightPanel = new VerticalPanel();
	private final SplitLayoutPanel appPanel = new SplitLayoutPanel();

	private static PublicCatalogServiceAsync catalogService = GWT
			.create(PublicCatalogService.class);
	
	private static HashMap<String, Constellation> constellationsList = new HashMap<String, Constellation>();
	
	@Override
	public void onModuleLoad() {
		// We set the size of the stack panel.
		configurationPanel.setPixelSize(280, 600);
		
		//filterPanel.setSize("270px", "250px");
		
		// We put the constellation select list in the filter panel.
		liConstellations.addChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(lbConstellation);
		filterPanel.add(liConstellations);
		chkDisplayConstellationShapes.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayConstellationShapes);
		chkDisplayConstellationBoundaries.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayConstellationBoundaries);

		// We put the star magnitude filter field
		chkDisplayStars.setValue(true);
		chkDisplayStars.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayStars);
		filterPanel.add(lbStarLimitMagnitude);
		txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		txtBoxStarLimitMagnitude.setWidth("100px");
		filterPanel.add(txtBoxStarLimitMagnitude);
		
		// We set the DSO filter fields
		chkDisplayAsterisms.setValue(true);
		chkDisplayAsterisms.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayAsterisms);
		
		chkDisplayGalaxies.setValue(false);
		chkDisplayGalaxies.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayGalaxies);
		
		chkDisplayGlobularClusters.setValue(false);
		chkDisplayGlobularClusters.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayGlobularClusters);
		
		chkDisplayOpenClusters.setValue(false);
		chkDisplayOpenClusters.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayOpenClusters);
		
		chkDisplayPlanetaryNebulas.setValue(false);
		chkDisplayPlanetaryNebulas.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayPlanetaryNebulas);
		
		chkDisplayNebulas.setValue(false);
		chkDisplayNebulas.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayNebulas);
		
		chkDisplaySupernovaRemnants.setValue(false);
		chkDisplaySupernovaRemnants.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplaySupernovaRemnants);
		
		chkDisplayQuasars.setValue(false);
		chkDisplayQuasars.addValueChangeHandler(new UpdateMapEventHandler());
		filterPanel.add(chkDisplayQuasars);
		
		filterPanel.add(lbDsoLimitMagnitude);
		txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		txtBoxDsoLimitMagnitude.setWidth("100px");
		filterPanel.add(txtBoxDsoLimitMagnitude);

		// We add the filter panel to configuration panel.
		configurationPanel.add(filterPanel, new Label("Filtres"), 28);
		
		// We add the coordinates panel.
		coordinatesPanel.setSize("270px", "30px");
		coordinatesPanel.add(lbCoordinatesMode);
		liCoordinatesMode.addItem("Equatoriaux", ""+CoordinatesSystem.EQ);
		liCoordinatesMode.addItem("Ecliptiques", ""+CoordinatesSystem.ECL);
		liCoordinatesMode.addItem("Galactiques", ""+CoordinatesSystem.GAL);
		liCoordinatesMode.addItem("Alt-Az", ""+CoordinatesSystem.ALTAZ);
		liCoordinatesMode.addChangeHandler(new UpdateMapEventHandler());
		coordinatesPanel.add(liCoordinatesMode);
		chkShowObjectsUnderHorizon.addClickHandler(new UpdateMapEventHandler());
		coordinatesPanel.add(chkShowObjectsUnderHorizon);

		// We add the coordinates panel to configuration panel.
		configurationPanel.add(coordinatesPanel, new Label("Projection"), 28);
		
		// We add the observer panel.
		observerPanel.setSize("270px", "250px");
		observerPanel.add(lbObserverLatitude);
		txtObserverLatitude.setText("48.833");
		observerPanel.add(txtObserverLatitude);
		observerPanel.add(lbObserverLongitude);
		txtObserverLongitude.setText("2.333");
		observerPanel.add(txtObserverLongitude);
		observerPanel.add(lbObserverDate);
		Date now = new Date();
		txtObserverDate.setText(dtfDate.format(now));
		txtObserverDate.addKeyUpHandler(new UpdateMapEventHandler());
		observerPanel.add(txtObserverDate);
		chkObserverUseComputerDateAndTime.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue() == true) {
					setObserverTimeFromComputerTime();
				}
			}
		});
		observerPanel.add(chkObserverUseComputerDateAndTime);
		observerPanel.add(lbObserverLocalTime);
		txtObserverLocalTime.setText(dtfTime.format(now));
		txtObserverDate.addKeyUpHandler(new UpdateMapEventHandler());
		observerPanel.add(txtObserverLocalTime);
		observerPanel.add(lbGreenwichHourOffset);
		txtGreenwichHourOffset.setText("+1");
		observerPanel.add(txtGreenwichHourOffset);
		
		// We set the time explorer panel.
		btObserverSubstractOneWeek.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverSubstractOneWeek);
		btObserverSubstractOneDay.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverSubstractOneDay);
		btObserverSubstractOneHour.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverSubstractOneHour);
		btObserverAddOneHour.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverAddOneHour);
		btObserverAddOneDay.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverAddOneDay);
		btObserverAddOneWeek.addClickHandler(new TimeExplorerClickHandler());
		timeExplorerPanel.add(btObserverAddOneWeek);
		observerPanel.add(timeExplorerPanel);
		
		// We add the observer panel to configuration panel.
		configurationPanel.add(observerPanel, new Label("Observateur"), 28);
		
		leftPanel.add(configurationPanel);

		// We set the button which will trigger the map's display.
		btUpdateMap.addClickHandler(new UpdateMapEventHandler());
		leftPanel.add(btUpdateMap);
		
		// We put a label that will be used to give feedback to the user.
		rightPanel.add(systemMessage);
		
		// We add the panel that will be used to display the chart.
		rightPanel.add(visualizationPanel);
		
		// We place the left and the right panels in the main one.
		appPanel.addWest(leftPanel, 280);
		appPanel.add(rightPanel);

		// We load the list of constellation.
		catalogService.findAllConstellations(new AsyncCallback<List<Constellation>>() {
			@Override
			public void onFailure(Throwable caught) {
				systemMessage.setText("Unable to load the list of constellations");
			}

			@Override
			public void onSuccess(final List<Constellation> result) {
				liConstellations.addItem("Non spécifiée", "");
				for (Constellation c : result) {
					liConstellations.addItem(c.getName(), c.getCode());
					constellationsList.put(c.getCode(), c);
				}
				
				// Once everything is ready, we add the application panel 
				// to the body of the page and we update the map. 
				RootLayoutPanel.get().add(appPanel);
				updateMap();
			}
		});
	}

	private static void updateMap() {
		final CatalogSearchOptions searchOptions = createSearchOptions();
		catalogService.findObjectBrighterThan(searchOptions, new AsyncCallback<Set<AstroObject>>() {

					@Override
					public void onSuccess(final Set<AstroObject> objects) {
						systemMessage.setText("Displaying " + objects.size()
								+ " object(s)...");
						visualizationPanel.clear();

						// Initialize the visualization...
						Runnable onVisuApiLoadCallback = new MyRunnable<Set<AstroObject>>(
								objects) {

							@Override
							public void run() {
								AbstractDataTable data = createDataTableOptimized(searchOptions);
								Options options = createLineChartOptions(searchOptions);
								final LineChart chart = new LineChart(data, options);
								visualizationPanel.add(chart);
							}

							private MyDataTable createDataTableOptimized(CatalogSearchOptions searchOptions) {
								MyDataTable optimizedData = MyDataTable.create();
								optimizedData = initializeDataTable(searchOptions, optimizedData, objects);
								optimizedData = fillDataTableWithValues(searchOptions, optimizedData, objects);
								return optimizedData;
							}
						};
						VisualizationUtils.loadVisualizationApi(
								onVisuApiLoadCallback, LineChart.PACKAGE);
					}

					public void onFailure(Throwable caught) {
						systemMessage.setText(caught.getMessage());
					}
				});
	}
	
	private static Observer initializeObserver() {
		Observer observer = new Observer();
		observer.setLatitude(Double.valueOf(txtObserverLatitude.getText()));
		observer.setLongitude(Double.valueOf(txtObserverLongitude.getText()));
		String[] date = txtObserverDate.getText().split("/");
		int j = Integer.valueOf(date[0]);
		int m = Integer.valueOf(date[1]);
		int a = Integer.valueOf(date[2]);
		String[] time = txtObserverLocalTime.getText().split(":");
		int H = Integer.valueOf(time[0]);
		int M = Integer.valueOf(time[1]);
		int S = Integer.valueOf(time[2]);
		double jd;
		if (H - Integer.valueOf(txtGreenwichHourOffset.getText()) < 0) {
			// We must consider the day before the one specified...
			Date dateInGreenwhich = dtfDate.parse(txtObserverDate.getText());
			CalendarUtil.addDaysToDate(dateInGreenwhich, -1);
			date = dtfDate.format(dateInGreenwhich).split("/");
			j = Integer.valueOf(date[0]);
			m = Integer.valueOf(date[1]);
			a = Integer.valueOf(date[2]);
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		} else if (H + Integer.valueOf(txtGreenwichHourOffset.getText()) > 23) {
			// We must consider the day after the one specified...
			Date dateInGreenwhich = dtfDate.parse(txtObserverDate.getText());
			CalendarUtil.addDaysToDate(dateInGreenwhich, +1);
			date = dtfDate.format(dateInGreenwhich).split("/");
			j = Integer.valueOf(date[0]);
			m = Integer.valueOf(date[1]);
			a = Integer.valueOf(date[2]);
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		} else {
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		}
		double gst = DateComputation.getMeanSiderealTimeAsHoursFromJulianDay(
				jd, new Sexagesimal(H-Integer.valueOf(txtGreenwichHourOffset.getText()), M, S));
		observer.setGreenwichSiderealTime(gst);
		return observer;
	}
	
	private static CatalogSearchOptions createSearchOptions() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setRestrictedToConstellationCode(liConstellations.getValue(liConstellations.getSelectedIndex()));
		options.setDisplayConstellationShape(chkDisplayConstellationShapes.getValue());
		options.setDisplayConstellationBoundaries(chkDisplayConstellationBoundaries.getValue());
		Double starLimitMagnitude = CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE;
		try {
			starLimitMagnitude = Double.parseDouble(txtBoxStarLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			systemMessage.setText("Vous devez indiquer une magnitude limite");
			txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			systemMessage
					.setText("Veuillez vérifier la valeur de magnitude limite. Format : \"xx.yy\"");
			txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setStarLimitMagnitude(starLimitMagnitude);

		Double dsoLimitMagnitude = CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE;
		try {
			dsoLimitMagnitude = Double.parseDouble(txtBoxDsoLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			systemMessage.setText("Vous devez indiquer une magnitude limite");
			txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			systemMessage
					.setText("Veuillez vérifier la valeur de magnitude limite. Format : \"xx.yy\"");
			txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setDsoLimitMagnitude(dsoLimitMagnitude);
		
		options.setFindStars(chkDisplayStars.getValue());
		options.setFindAsterisms(chkDisplayAsterisms.getValue());
		options.setFindGalaxies(chkDisplayGalaxies.getValue());
		options.setFindGlobularClusters(chkDisplayGlobularClusters.getValue());
		options.setFindOpenClusters(chkDisplayOpenClusters.getValue());
		options.setFindPlanetaryNebulas(chkDisplayPlanetaryNebulas.getValue());
		options.setFindNebulas(chkDisplayNebulas.getValue());
		options.setFindSupernovaRemnant(chkDisplaySupernovaRemnants.getValue());
		options.setFindQuasars(chkDisplayQuasars.getValue());
		return options;
	}
	
	private static class TimeExplorerClickHandler implements ClickHandler {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(ClickEvent event) {
			Widget source = (Widget) event.getSource();
			Date currentConfiguredDate = dtfDateAndTime.parse(txtObserverDate.getText()+" "+txtObserverLocalTime.getText());
			if (btObserverSubstractOneWeek == source) {
				CalendarUtil.addDaysToDate(currentConfiguredDate, -7);
			} else if (btObserverSubstractOneDay == source) {
				CalendarUtil.addDaysToDate(currentConfiguredDate, -1);
			} else if (btObserverAddOneDay == source) {
				CalendarUtil.addDaysToDate(currentConfiguredDate, +1);
			} else if (btObserverAddOneWeek == source) {
				CalendarUtil.addDaysToDate(currentConfiguredDate, +7);
			} else if (btObserverSubstractOneHour == source) {
				currentConfiguredDate.setHours(currentConfiguredDate.getHours()-1);
			} else if (btObserverAddOneHour == source) {
				currentConfiguredDate.setHours(currentConfiguredDate.getHours()+1);
			}
			txtObserverDate.setText(dtfDate.format(currentConfiguredDate));
			txtObserverLocalTime.setText(dtfTime.format(currentConfiguredDate));
			updateMap();
		}
		
	}
	
	private static class UpdateMapEventHandler implements ClickHandler, ValueChangeHandler<Boolean>, ChangeHandler, KeyUpHandler {
		@Override
		public void onClick(ClickEvent event) {
			updateMap();
		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			updateMap();
		}

		@Override
		public void onChange(ChangeEvent event) {
			updateMap();
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				updateMap();
			}
		}
	}
	
	private static MyDataTable fillRowWithNullValues(MyDataTable data, int rowIndex) {
		for (int i=0 ; i<data.getNumberOfColumns(); i++) {
			data.setValueNull(rowIndex, i);
		}
		return data;
	}
	
	private static MyDataTable initializeDataTable(CatalogSearchOptions searchOptions, MyDataTable optimizedData, Set<AstroObject> objects) {
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null && 
				!searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(liCoordinatesMode.getValue(liCoordinatesMode.getSelectedIndex()));
		switch(cs) {
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
		if (searchOptions.isFindStars()) {
			optimizedData.addColumn(ColumnType.NUMBER, "Etoiles");
			optimizedData.addStyleColumn(optimizedData);
			optimizedData.addTooltipColumn(optimizedData);
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
		
		ArrayList<String> codeOfConstellationsToDisplay = new ArrayList<String>();
		if (showOneConstellation) {
			codeOfConstellationsToDisplay.add(liConstellations.getValue(liConstellations.getSelectedIndex()));
		} else {
			codeOfConstellationsToDisplay.addAll(constellationsList.keySet());
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			for (String constellationCode : codeOfConstellationsToDisplay) {
				optimizedData.addColumn(ColumnType.NUMBER, "Limites constellation "+constellationCode);
			}
		}
		if (searchOptions.isDisplayConstellationShape()) {
			for (String constellationCode : codeOfConstellationsToDisplay) {
				for (ConstellationShapeLine shapeLine : constellationsList.get(constellationCode).getShapeLines()) {
					optimizedData.addColumn(ColumnType.NUMBER, "Forme de la constellation "+constellationCode);
				}
			}
		}
		
		optimizedData.addRows(objects.size());
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList.get(searchOptions.getRestrictedToConstellationCode()).getBoundaryPoints().size()+1);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getBoundaryPoints().size()+1);
				}
			}
		}
		if (searchOptions.isDisplayConstellationShape()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList.get(searchOptions.getRestrictedToConstellationCode()).getShapeLines().size()*2);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getShapeLines().size()*2);
				}
			}
		}
		return optimizedData;
	}
	
	private static MyDataTable fillDataTableWithValues(CatalogSearchOptions searchOptions, MyDataTable optimizedData, Set<AstroObject> objects) {
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null && 
				!searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(liCoordinatesMode.getValue(liCoordinatesMode.getSelectedIndex()));
		Observer observer = initializeObserver();
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		int i = 0;
		for (AstroObject o : objects) {
			// Should we show objects under horizon ?
			if (cs == CoordinatesSystem.ALTAZ && !chkShowObjectsUnderHorizon.getValue()) {
				if (o.getYCoordinateForReferential(cs, observer) < 0) {
					continue;
				}
			}
			
			optimizedData = fillRowWithNullValues(optimizedData, i);
			optimizedData.setValue(i, 0, o.getXCoordinateForReferential(cs, observer));
			if (o instanceof Star) {
				optimizedData.setValue(i, serieIndexes.getStarSerieIndex(), 
						o.getYCoordinateForReferential(cs, observer));
				optimizedData.setValue(i, serieIndexes.getStarSerieIndex()+1, STYLE_STARS);
				optimizedData.setValue(i, serieIndexes.getStarSerieIndex()+2, generateTooltip(o, observer));
			} else if (o instanceof DeepSkyObject) {
				DeepSkyObject dso = (DeepSkyObject) o;
				if (dso.isAsterism()) {
					optimizedData.setValue(i, serieIndexes.getAsterismSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getAsterismSerieIndex()+1, STYLE_ASTERISMS);
					optimizedData.setValue(i, serieIndexes.getAsterismSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isGalaxy()) {
					optimizedData.setValue(i, serieIndexes.getGalaxySerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getGalaxySerieIndex()+1, STYLE_GALAXIES);
					optimizedData.setValue(i, serieIndexes.getGalaxySerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isGlobularCluster()) {
					optimizedData.setValue(i, serieIndexes.getGlobularClusterSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getGlobularClusterSerieIndex()+1, STYLE_GLOBULAR_CLUSTERS);
					optimizedData.setValue(i, serieIndexes.getGlobularClusterSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isOpenCluster()) {
					optimizedData.setValue(i, serieIndexes.getOpenClusterSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getOpenClusterSerieIndex()+1, STYLE_OPEN_CLUSTERS);
					optimizedData.setValue(i, serieIndexes.getOpenClusterSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isPlanetaryNebula()) {
					optimizedData.setValue(i, serieIndexes.getPlanetaryNebulaSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getPlanetaryNebulaSerieIndex()+1, STYLE_PLANETARY_NEBULAS);
					optimizedData.setValue(i, serieIndexes.getPlanetaryNebulaSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isNebula()) {
					optimizedData.setValue(i, serieIndexes.getNebulaSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getNebulaSerieIndex()+1, STYLE_NEBULAS);
					optimizedData.setValue(i, serieIndexes.getNebulaSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isSupernovaRemnant()) {
					optimizedData.setValue(i, serieIndexes.getSnRemnantSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getSnRemnantSerieIndex()+1, STYLE_SN_REMNANTS);
					optimizedData.setValue(i, serieIndexes.getSnRemnantSerieIndex()+2, generateTooltip(o, observer));
				} else if (dso.isQuasar()) {
					optimizedData.setValue(i, serieIndexes.getQuasarSerieIndex(), 
							dso.getYCoordinateForReferential(cs, observer));
					optimizedData.setValue(i, serieIndexes.getQuasarSerieIndex()+1, STYLE_QUASARS);
					optimizedData.setValue(i, serieIndexes.getQuasarSerieIndex()+2, generateTooltip(o, observer));
				}
			}
			i++;
		}
		
		ArrayList<Constellation> constellationsToDisplay = new ArrayList<Constellation>();
		if (searchOptions.isDisplayConstellationBoundaries() || searchOptions.isDisplayConstellationShape()) {
			if (showOneConstellation) {
				constellationsToDisplay.add(constellationsList.get(liConstellations.getValue(liConstellations.getSelectedIndex())));
			} else {
				constellationsToDisplay.addAll(constellationsList.values());
			}
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			int constellationIndex = 0;
			for (Constellation constellation : constellationsToDisplay) {
				// Constellation(s) boundaries...
				for (ConstellationBoundaryPoint p : constellation.getBoundaryPoints()) {
					EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(p.getRightAscensionAsDecimalDegrees(), p.getDeclinaisonAsDecimalDegrees()));
					switch(cs) {
					case ALTAZ:
						optimizedData.setValue(i, 0, eca.getAzimuth(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getElevation(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						break;
					case ECL:
						optimizedData.setValue(i, 0, eca.getEcliptiqueLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getEcliptiqueLatitude());
						break;
					case GAL:
						optimizedData.setValue(i, 0, eca.getGalacticLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getGalacticLatitude());
						break;
					case EQ:
					default:
						optimizedData.setValue(i, 0, p.getRightAscensionAsDecimalDegrees());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, p.getDeclinaisonAsDecimalDegrees());
						break;
					}
					i++;
				}
				// We add a last point to close the boundary of the constellation...
				ConstellationBoundaryPoint lastPoint = constellation.getBoundaryPoints().get(0);
				EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
						new EquatorialCoordinates(lastPoint.getRightAscensionAsDecimalDegrees(), lastPoint.getDeclinaisonAsDecimalDegrees()));
				switch(cs) {
				case ALTAZ:
					optimizedData.setValue(i, 0, eca.getAzimuth(
							new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
					optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getElevation(
							new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
					break;
				case ECL:
					optimizedData.setValue(i, 0, eca.getEcliptiqueLongitude());
					optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getEcliptiqueLatitude());
					break;
				case GAL:
					optimizedData.setValue(i, 0, eca.getGalacticLongitude());
					optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, eca.getGalacticLatitude());
					break;
				case EQ:
				default:
					optimizedData.setValue(i, 0, lastPoint.getRightAscensionAsDecimalDegrees());
					optimizedData.setValue(i, serieIndexes.getFirstConstellationBoundarySerieIndex()+constellationIndex, lastPoint.getDeclinaisonAsDecimalDegrees());
					break;
				}
				i++;
				constellationIndex++;
			}
		}
			
		if (searchOptions.isDisplayConstellationShape()) {
			// Constellation shape lines...
			int constellationIndex = 0;
			for (Constellation constellation : constellationsToDisplay) {
				for (ConstellationShapeLine l : constellation.getShapeLines()) {
					// Start
					EquatorialCoordinatesAdapter ecaStart = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getStartRightAscension(), l.getStartDeclinaison()));
					switch(cs) {
					case ALTAZ:
						optimizedData.setValue(i, 0, ecaStart.getAzimuth(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaStart.getElevation(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						break;
					case ECL:
						optimizedData.setValue(i, 0, ecaStart.getEcliptiqueLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaStart.getEcliptiqueLatitude());
						break;
					case GAL:
						optimizedData.setValue(i, 0, ecaStart.getGalacticLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaStart.getGalacticLatitude());
						break;
					case EQ:
					default:
						optimizedData.setValue(i, 0, l.getStartRightAscension());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, l.getStartDeclinaison());
						break;
					}
					i++;
					
					// End
					EquatorialCoordinatesAdapter ecaEnd = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getEndRightAscension(), l.getEndDeclinaison()));
					switch(cs) {
					case ALTAZ:
						optimizedData.setValue(i, 0, ecaEnd.getAzimuth(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaEnd.getElevation(
								new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()), observer.getGreenwichSiderealTime()));
						break;
					case ECL:
						optimizedData.setValue(i, 0, ecaEnd.getEcliptiqueLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaEnd.getEcliptiqueLatitude());
						break;
					case GAL:
						optimizedData.setValue(i, 0, ecaEnd.getGalacticLongitude());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, ecaEnd.getGalacticLatitude());
						break;
					case EQ:
					default:
						optimizedData.setValue(i, 0, l.getEndRightAscension());
						optimizedData.setValue(i, serieIndexes.getFirstConstellationShapeSerieIndex()+constellationIndex, l.getEndDeclinaison());
						break;
					}
					i++;
				}
				constellationIndex++;
			}
		}
		// We remove unused rows if any.
		// It is the case when we do not display the objects under horizon in the AltAz projection.
		optimizedData.removeRows(i, (optimizedData.getNumberOfRows() - i));
		return optimizedData;
	}
	
	private static String generateTooltip(AstroObject o, Observer observer) {
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
				sb.append(" "+dso.getClasstype());
			}
			sb.append(" \n");
		}
		
		// Identifier and magnitude...
		sb.append(o.getIdentifier()+"\n");
		if (o instanceof Star && ((Star)o).getSaoNumber() != null) {
			sb.append("SAO " + ((Star)o).getSaoNumber() + "\n");
		}
		sb.append("Mag.="+o.getVisualMagnitude()+"\n");
		
		// Coordinates...
		if (o instanceof Star) {
			Star s = (Star) o;
			sb.append("RA="+s.getRightAscensionHour()+"h "+s.getRightAscensionMinute()+"m "+s.getRightAscensionSecond()+"s \n");
			sb.append("DEC="+s.getDeclinaisonSignus()+s.getDeclinaisonDegree()+"° "+s.getDeclinaisonMinute()+"m "+s.getDeclinaisonSecond()+"s \n");
		} else if (o instanceof DeepSkyObject) {
			DeepSkyObject dso = (DeepSkyObject) o;
			sb.append("RA="+dso.getRightAscensionHour()+"h "+dso.getRightAscensionMinute()+"m \n");
			sb.append("DEC="+dso.getDeclinaisonDegree()+"° "+dso.getDeclinaisonMinute()+"m \n");
		}
		
		CoordinatesSystem cs =
				CoordinatesSystem.instanceOf(liCoordinatesMode.getValue(liCoordinatesMode.getSelectedIndex())); 
		switch(cs) {
			case ECL:
				sb.append("Longitude ecl.="+o.getXCoordinateForReferential(cs)+"° \n");
				sb.append("Latitude ecl.="+o.getYCoordinateForReferential(cs)+"° \n");
				break;
			case GAL:
				sb.append("Longitude gal.="+o.getXCoordinateForReferential(cs)+"° \n");
				sb.append("Latitude gal.="+o.getYCoordinateForReferential(cs)+"° \n");
				break;
			case EQ:
				break;
			case ALTAZ:
				sb.append("Azimuth="+o.getXCoordinateForReferential(cs, observer)+"° \n");
				sb.append("Elevation="+o.getYCoordinateForReferential(cs, observer)+"° \n");
				break;
		}
		return sb.toString();
	}
	
	private static Options createLineChartOptions(CatalogSearchOptions searchOptions) {
		String coordinatesMode = liCoordinatesMode.getValue(liCoordinatesMode.getSelectedIndex());
		MyLineChartOptions options = MyLineChartOptions.create();
		options.setWidth(1000);
		options.setHeight(600);
		options.setBackgroundColor(STYLE_CHART_BACKGROUND_COLOR);
		AxisOptions hAxisOptions = AxisOptions.create();
		AxisOptions vAxisOptions = AxisOptions.create();
		TextStyle axisTitleTextStyle = TextStyle.create();
		axisTitleTextStyle.setColor("#ffffff");
		hAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		vAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		if (coordinatesMode.equals(""+CoordinatesSystem.ALTAZ) && chkShowObjectsUnderHorizon.getValue() == false) {
			vAxisOptions.setMinValue(0);
		} else {
			vAxisOptions.setMinValue(-90);
		}
		vAxisOptions.setMaxValue(90);
		if ((""+CoordinatesSystem.ECL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("LONGITUDE ECLIPTIQUE");
			vAxisOptions.setTitle("LATITUDE ECLIPTIQUE");
		} else if ((""+CoordinatesSystem.GAL).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(0);
			hAxisOptions.setMaxValue(360);
			hAxisOptions.setTitle("LONGITUDE GALACTIQUE");
			vAxisOptions.setTitle("LATITUDE GALACTIQUE");
		} else if ((""+CoordinatesSystem.ALTAZ).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("AZIMUTH");
			vAxisOptions.setTitle("ELEVATION");
		} else if ((""+CoordinatesSystem.EQ).equals(coordinatesMode)) {
			if (!liConstellations.getValue(liConstellations.getSelectedIndex()).isEmpty()) {
				// We set the limit of the map to the limit of the selected constellation...
				String constellation = liConstellations.getValue(liConstellations.getSelectedIndex());
				EquatorialCoordinates upperWesterMapLimit = constellationsList.get(constellation).getUpperWesternMapLimit(1);
				EquatorialCoordinates lowerEasterMapLimit = constellationsList.get(constellation).getLowerEasternMapLimit(1);
				hAxisOptions.setMinValue((int)upperWesterMapLimit.getRightAscension());
				hAxisOptions.setMaxValue((int)(lowerEasterMapLimit.getRightAscension()+1));
				vAxisOptions.setMinValue((int)lowerEasterMapLimit.getDeclinaison());
				vAxisOptions.setMaxValue((int)(upperWesterMapLimit.getDeclinaison()+1));
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
		
		// Trying to fine tune the series representation options...
		options = createSeriesOptions(options, searchOptions);
		return options;
	}
	
	private static MyLineChartOptions createSeriesOptions(MyLineChartOptions options, CatalogSearchOptions searchOptions) {
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		int i=0;
		for (i=0 ; i<serieIndexes.getSeriesCount() ; i++) {
			MySeries s = MySeries.create();
			s.setLineWidth(0);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (searchOptions.getRestrictedToConstellationCode() != null && 
					!searchOptions.getRestrictedToConstellationCode().isEmpty()) {
				MySeries s = MySeries.create();
				s.setLineWidth(1);
				s.setPointSize(0);
				s.setLineDashStyle(new int[]{5, 1, 3});
				s.setColor(STYLE_CONSTELLATION_BORDER_COLOR);
				s.setVisibleInLegend(false);
				options.setSeries(i, s);
				i++;
			} else {
				for (int constellationIndex = 0 ; constellationIndex<constellationsList.size() ; constellationIndex++) {
					MySeries s = MySeries.create();
					s.setLineWidth(1);
					s.setPointSize(0);
					s.setLineDashStyle(new int[]{5, 1, 3});
					s.setColor(STYLE_CONSTELLATION_BORDER_COLOR);
					s.setVisibleInLegend(false);
					options.setSeries(i, s);
					i++;
				}
			}
		}
		if (searchOptions.isDisplayConstellationShape()) {
			if (searchOptions.getRestrictedToConstellationCode() != null &&
					!searchOptions.getRestrictedToConstellationCode().isEmpty()) {
				for (ConstellationShapeLine shapeLine : constellationsList.get(searchOptions.getRestrictedToConstellationCode()).getShapeLines()) {
					MySeries s = MySeries.create();
					s.setLineWidth(1);
					s.setPointSize(0);
					s.setColor(STYLE_CONSTELLATION_SHAPE_COLOR);
					s.setVisibleInLegend(false);
					options.setSeries(i, s);
					i++;
				}
			} else {
				for (Constellation constellation : constellationsList.values()) {
					for (ConstellationShapeLine shapeLine : constellation.getShapeLines()) {
						MySeries s = MySeries.create();
						s.setLineWidth(1);
						s.setPointSize(0);
						s.setColor(STYLE_CONSTELLATION_SHAPE_COLOR);
						s.setVisibleInLegend(false);
						options.setSeries(i, s);
						i++;
					}
				}
			}
		}
		return options;
	}
	
	private static void setObserverTimeFromComputerTime() {
		Date now = new Date();
		txtObserverDate.setText(dtfDate.format(now));
		txtObserverLocalTime.setText(dtfTime.format(now));
		updateMap();
	}
}
