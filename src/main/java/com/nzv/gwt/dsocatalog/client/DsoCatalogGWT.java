package com.nzv.gwt.dsocatalog.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
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
import com.nzv.gwt.dsocatalog.projection.MollweideProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.visualization.MyDataTable;
import com.nzv.gwt.dsocatalog.visualization.MyLineChartOptions;
import com.nzv.gwt.dsocatalog.visualization.MySeries;

public class DsoCatalogGWT implements EntryPoint {

	public static String DATE_FORMAT = "dd/MM/yyyy";
	public static String TIME_FORMAT = "HH:mm:ss";
	
//	private static final DateTimeFormat dtfDateAndTime = DateTimeFormat.getFormat(DATE_FORMAT+" "+TIME_FORMAT);
//	private static final DateTimeFormat dtfDate = DateTimeFormat.getFormat(DATE_FORMAT);
//	private static final DateTimeFormat dtfTime = DateTimeFormat.getFormat(TIME_FORMAT);
	
	/* Styles */
	public static String STYLE_CHART_BACKGROUND_COLOR = "#110e3d";
	public static String AXIS_TITLE_TEXT_STYLE = "color: #ffffff;";
	public static String STYLE_CONSTELLATION_BORDER_COLOR = "#888888";
	public static String STYLE_CONSTELLATION_SHAPE_COLOR = "#32baf0";
	public static String STYLE_STARS = "point {shape-type: star;shape-dent: 0.2; size: SIZE_STAR; fill-color: #f29500;}";
	public static int STYLE_STARS_SIZE_MAX_POINT = 7;
	public static String STYLE_ASTERISMS = "point { shape-type: polygon; size: 4; fill-color: #110e3d; stroke-color: #f29500; stroke-width: 1;}";
	public static String STYLE_GALAXIES = "point { shape-type: circle; size: 4; fill-color: #110e3d; stroke-color: #f42929; stroke-width: 2;}";
	public static String STYLE_GLOBULAR_CLUSTERS = "point {shape-type: circle; size: 4; fill-color: #9bf2bd; stroke-color: #000000; stroke-width: 1;}";
	public static String STYLE_OPEN_CLUSTERS = "point { shape-type: circle; size: 4; fill-color: #110e3d; stroke-color: #04ea64; stroke-width: 1;}";
	public static String STYLE_PLANETARY_NEBULAS = "point { shape-type: star; shape-sides: 4; shape-dent: 0.2; size: 6; fill-color: #0099c6;}";
	public static String STYLE_NEBULAS = "point { shape-type: square; size: 6; fill-color: #f28787; stroke-color: #f42929; shape-rotation:45;}";
	public static String STYLE_SN_REMNANTS = "point { shape-type: square; size: 5; fill-color: #110e3d; stroke-color: #f42929; stroke-width: 1; shape-rotation:45;}";
	public static String STYLE_QUASARS = "point { shape-type: circle; size: 3; fill-color: #f42929;}";
	

	private static PublicCatalogServiceAsync catalogService = GWT
			.create(PublicCatalogService.class);
	
	private static HashMap<String, Constellation> constellationsList = new HashMap<String, Constellation>();
	
	private ApplicationBoard appPanel = ApplicationBoard.buildApplicationBoard(this);
	
	@Override
	public void onModuleLoad() {
		
		
		// We load the list of constellation.
		catalogService.findAllConstellations(new AsyncCallback<List<Constellation>>() {
			@Override
			public void onFailure(Throwable caught) {
				appPanel.systemMessage.setText("Unable to load the list of constellations");
			}

			@Override
			public void onSuccess(final List<Constellation> result) {
				appPanel.liConstellations.addItem("Non spécifiée", "");
				for (Constellation c : result) {
					appPanel.liConstellations.addItem(c.getName(), c.getCode());
					constellationsList.put(c.getCode(), c);
				}
				
				// Once everything is ready, we add the application panel 
				// to the body of the page and we update the map. 
				RootLayoutPanel.get().add(appPanel);
				updateMap();
			}
		});
	}

	protected void updateMap() {
		// We remove every data in details table
		appPanel.objectDetailsTable.removeAllRows();
		final CatalogSearchOptions searchOptions = createSearchOptions();
		catalogService.findObjectBrighterThan(searchOptions, new AsyncCallback<Set<AstroObject>>() {

					@Override
					public void onSuccess(final Set<AstroObject> objects) {
//						systemMessage.setText("Displaying " + objects.size() + " object(s)...");
						appPanel.systemMessage.setText("Rendering map...");
						appPanel.visualizationPanel.clear();

						// Initialize the visualization...
						Runnable onVisuApiLoadCallback = new MyRunnable<Set<AstroObject>>(
								objects) {

							@Override
							public void run() {
								final Map<ObjectReferenceAddressInTable, ObjectReference> displayedObjectReferences = 
										new HashMap<ObjectReferenceAddressInTable, ObjectReference>();
								AbstractDataTable data = createDataTableOptimized(searchOptions, displayedObjectReferences);
								Options options = createLineChartOptions(searchOptions);
								final LineChart chart = new LineChart(data, options);
								chart.addSelectHandler(new SelectHandler() {
									@Override
									public void onSelect(SelectEvent event) {
										JsArray<Selection> selections = chart.getSelections();
										for (int i=0 ; i<selections.length() ; i++) {
											Selection selection = selections.get(i);
											if (displayedObjectReferences.containsKey(new ObjectReferenceAddressInTable(selection.getRow(), selection.getColumn()))) {
												// The user clicked on a AstroObject (star or dso)
												fetchObjectDetails(displayedObjectReferences.get(
														new ObjectReferenceAddressInTable(selection.getRow(), selection.getColumn())));
											} else {
												// The user selected a constellation boundary point or a shape line limit...
												appPanel.objectDetailsTable.removeAllRows();
											}
										}
									}
								});
								appPanel.systemMessage.setText(displayedObjectReferences.keySet().size() + " object(s) displayed.");
								appPanel.visualizationPanel.add(chart);
							}

							private MyDataTable createDataTableOptimized(CatalogSearchOptions searchOptions,
									Map<ObjectReferenceAddressInTable, ObjectReference> displayedObjectReferences) {
								MyDataTable optimizedData = MyDataTable.create();
								optimizedData = initializeDataTable(searchOptions, optimizedData, objects);
								optimizedData = fillDataTableWithValues(searchOptions, optimizedData, displayedObjectReferences, objects);
								return optimizedData;
							}
						};
						VisualizationUtils.loadVisualizationApi(
								onVisuApiLoadCallback, LineChart.PACKAGE);
					}

					public void onFailure(Throwable caught) {
						appPanel.systemMessage.setText(caught.getMessage());
					}
				});
	}
	
	private Observer initializeObserver() {
		Observer observer = new Observer();
		observer.setLatitude(Double.valueOf(appPanel.txtObserverLatitude.getText()));
		observer.setLongitude(Double.valueOf(appPanel.txtObserverLongitude.getText()));
		String[] date = appPanel.txtObserverDate.getText().split("/");
		int j = Integer.valueOf(date[0]);
		int m = Integer.valueOf(date[1]);
		int a = Integer.valueOf(date[2]);
		String[] time = appPanel.txtObserverLocalTime.getText().split(":");
		int H = Integer.valueOf(time[0]);
		int M = Integer.valueOf(time[1]);
		int S = Integer.valueOf(time[2]);
		double jd;
		if (H - Integer.valueOf(appPanel.txtGreenwichHourOffset.getText()) < 0) {
			// We must consider the day before the one specified...
			Date dateInGreenwhich = ApplicationBoard.dtfDate.parse(appPanel.txtObserverDate.getText());
			CalendarUtil.addDaysToDate(dateInGreenwhich, -1);
			date = ApplicationBoard.dtfDate.format(dateInGreenwhich).split("/");
			j = Integer.valueOf(date[0]);
			m = Integer.valueOf(date[1]);
			a = Integer.valueOf(date[2]);
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		} else if (H + Integer.valueOf(appPanel.txtGreenwichHourOffset.getText()) > 23) {
			// We must consider the day after the one specified...
			Date dateInGreenwhich = ApplicationBoard.dtfDate.parse(appPanel.txtObserverDate.getText());
			CalendarUtil.addDaysToDate(dateInGreenwhich, +1);
			date = ApplicationBoard.dtfDate.format(dateInGreenwhich).split("/");
			j = Integer.valueOf(date[0]);
			m = Integer.valueOf(date[1]);
			a = Integer.valueOf(date[2]);
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		} else {
			jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		}
		double gst = DateComputation.getMeanSiderealTimeAsHoursFromJulianDay(
				jd, new Sexagesimal(H-Integer.valueOf(appPanel.txtGreenwichHourOffset.getText()), M, S));
		observer.setGreenwichSiderealTime(gst);
		return observer;
	}
	
	private CatalogSearchOptions createSearchOptions() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setRestrictedToConstellationCode(appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex()));
		options.setDisplayConstellationShape(appPanel.chkDisplayConstellationShapes.getValue());
		options.setDisplayConstellationBoundaries(appPanel.chkDisplayConstellationBoundaries.getValue());
		Double starLimitMagnitude = CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE;
		try {
			starLimitMagnitude = Double.parseDouble(appPanel.txtBoxStarLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			appPanel.systemMessage.setText("Vous devez indiquer une magnitude limite");
			appPanel.txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			appPanel.systemMessage
					.setText("Veuillez vérifier la valeur de magnitude limite. Format : \"xx.yy\"");
			appPanel.txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setStarLimitMagnitude(starLimitMagnitude);

		Double dsoLimitMagnitude = CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE;
		try {
			dsoLimitMagnitude = Double.parseDouble(appPanel.txtBoxDsoLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			appPanel.systemMessage.setText("Vous devez indiquer une magnitude limite");
			appPanel.txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			appPanel.systemMessage
					.setText("Veuillez vérifier la valeur de magnitude limite. Format : \"xx.yy\"");
			appPanel.txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setDsoLimitMagnitude(dsoLimitMagnitude);
		
		options.setFindStars(appPanel.chkDisplayStars.getValue());
		options.setFindAsterisms(appPanel.chkDisplayAsterisms.getValue());
		options.setFindGalaxies(appPanel.chkDisplayGalaxies.getValue());
		options.setFindGlobularClusters(appPanel.chkDisplayGlobularClusters.getValue());
		options.setFindOpenClusters(appPanel.chkDisplayOpenClusters.getValue());
		options.setFindPlanetaryNebulas(appPanel.chkDisplayPlanetaryNebulas.getValue());
		options.setFindNebulas(appPanel.chkDisplayNebulas.getValue());
		options.setFindSupernovaRemnant(appPanel.chkDisplaySupernovaRemnants.getValue());
		options.setFindQuasars(appPanel.chkDisplayQuasars.getValue());
		return options;
	}
	
	private static MyDataTable fillRowWithNullValues(MyDataTable data, int rowIndex) {
		for (int i=0 ; i<data.getNumberOfColumns(); i++) {
			data.setValueNull(rowIndex, i);
		}
		return data;
	}
	
	private MyDataTable initializeDataTable(CatalogSearchOptions searchOptions, MyDataTable optimizedData, Set<AstroObject> objects) {
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null && 
				!searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(appPanel.liCoordinatesMode.getValue(appPanel.liCoordinatesMode.getSelectedIndex()));
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
			codeOfConstellationsToDisplay.add(appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex()));
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
		if (searchOptions.isDisplayConstellationBoundaries()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList.get(searchOptions.getRestrictedToConstellationCode()).getBoundaryPoints().size()+2);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getBoundaryPoints().size()+2);
				}
			}
		}
		if (searchOptions.isDisplayConstellationShape()) {
			if (showOneConstellation) {
				optimizedData.addRows(constellationsList.get(searchOptions.getRestrictedToConstellationCode()).getShapeLines().size()*3);
			} else {
				for (Constellation constellation : constellationsList.values()) {
					optimizedData.addRows(constellation.getShapeLines().size()*3);
				}
			}
		}
		return optimizedData;
	}
	
	private MyDataTable fillDataTableWithValues(CatalogSearchOptions searchOptions, MyDataTable optimizedData, 
			Map<ObjectReferenceAddressInTable, ObjectReference> displayedObjectReferences, Set<AstroObject> objects) {
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null && 
				!searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(appPanel.liCoordinatesMode.getValue(appPanel.liCoordinatesMode.getSelectedIndex()));
		Observer observer = initializeObserver();
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		int i = 0;
		for (AstroObject o : objects) {
			// Should we show objects under horizon ?
			if (cs == CoordinatesSystem.ALTAZ && !appPanel.chkShowObjectsUnderHorizon.getValue()) {
				if (o.getYCoordinateForReferential(cs, observer) < 0) {
					continue;
				}
			}
			
			optimizedData = fillRowWithNullValues(optimizedData, i);
			int serieIndexToUse = 0;
			String styleToUse = new String();
			ObjectReference objectReference = null;
			if (o instanceof Star) {
				serieIndexToUse = serieIndexes.getStarSerieIndex();
				
				// We compute the size of point to use given star magnitude...
				double mag = o.getVisualMagnitude();
				double tmp = 10 / Math.exp(0.15 * mag);
				int sizePoint = ((int)(Math.ceil(tmp))) * STYLE_STARS_SIZE_MAX_POINT / 12;
				styleToUse = STYLE_STARS.replaceAll("SIZE_STAR", ""+sizePoint);
				objectReference = new ObjectReference(true, false, ((Star) o).getHrNumber());
			} else if (o instanceof DeepSkyObject) {
				DeepSkyObject dso = (DeepSkyObject) o;
				if (dso.isAsterism()) {
					serieIndexToUse = serieIndexes.getAsterismSerieIndex();
					styleToUse = STYLE_ASTERISMS;
				} else if (dso.isGalaxy()) {
					serieIndexToUse = serieIndexes.getGalaxySerieIndex();
					styleToUse = STYLE_GALAXIES;
				} else if (dso.isGlobularCluster()) {
					serieIndexToUse = serieIndexes.getGlobularClusterSerieIndex();
					styleToUse = STYLE_GLOBULAR_CLUSTERS;
				} else if (dso.isOpenCluster()) {
					serieIndexToUse = serieIndexes.getOpenClusterSerieIndex();
					styleToUse = STYLE_OPEN_CLUSTERS;
				} else if (dso.isPlanetaryNebula()) {
					serieIndexToUse = serieIndexes.getPlanetaryNebulaSerieIndex();
					styleToUse = STYLE_PLANETARY_NEBULAS;
				} else if (dso.isNebula()) {
					serieIndexToUse = serieIndexes.getNebulaSerieIndex();
					styleToUse = STYLE_NEBULAS;
				} else if (dso.isSupernovaRemnant()) {
					serieIndexToUse = serieIndexes.getSnRemnantSerieIndex();
					styleToUse = STYLE_SN_REMNANTS;
				} else if (dso.isQuasar()) {
					serieIndexToUse = serieIndexes.getQuasarSerieIndex();
					styleToUse = STYLE_QUASARS;
				}
				objectReference = new ObjectReference(false, true, dso.getId());
			}
			if (cs == CoordinatesSystem.GAL) {
				MollweideProjection projection = new MollweideProjection();
				EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(o.getRightAscension(), o.getDeclinaison()));
				Point2D p = projection.project(Math.toRadians(eca.getGalacticLongitude()>=180?eca.getGalacticLongitude()-360:eca.getGalacticLongitude()), Math.toRadians(eca.getGalacticLatitude()));
				optimizedData.setValue(i, 0, Math.toDegrees(p.getX()));
				optimizedData.setValue(i, serieIndexToUse, Math.toDegrees(p.getY()));
			} else {
				optimizedData.setValue(i, 0, o.getXCoordinateForReferential(cs, observer));
				optimizedData.setValue(i, serieIndexToUse, o.getYCoordinateForReferential(cs, observer));
			}
			optimizedData.setValue(i, serieIndexToUse+1, styleToUse);
			optimizedData.setValue(i, serieIndexToUse+2, generateTooltip(o, observer));
			displayedObjectReferences.put(new ObjectReferenceAddressInTable(i, serieIndexToUse), objectReference);
			i++;
		}
		
		ArrayList<Constellation> constellationsToDisplay = new ArrayList<Constellation>();
		if (searchOptions.isDisplayConstellationBoundaries() || searchOptions.isDisplayConstellationShape()) {
			if (showOneConstellation) {
				constellationsToDisplay.add(constellationsList.get(appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex())));
			} else {
				constellationsToDisplay.addAll(constellationsList.values());
			}
		}
		// Constellation(s) boundaries...
		if (searchOptions.isDisplayConstellationBoundaries()) {
			for (Constellation constellation : constellationsToDisplay) {
				double X;
				double Y;
				for (ConstellationBoundaryPoint p : constellation.getBoundaryPoints()) {
					EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(p.getRightAscensionAsDecimalDegrees(), p.getDeclinaisonAsDecimalDegrees()));
					switch(cs) {
					case ALTAZ:
						GeographicCoordinates observatory = new GeographicCoordinates(observer.getLatitude(), observer.getLongitude());
						X = eca.getAzimuth(observatory, observer.getGreenwichSiderealTime());
						Y = eca.getElevation(observatory, observer.getGreenwichSiderealTime());
						break;
					case ECL:
						X = eca.getEcliptiqueLongitude();
						Y = eca.getEcliptiqueLatitude();
						break;
					case GAL:
						X = eca.getGalacticLongitude();
						Y = eca.getGalacticLatitude();
						MollweideProjection projection = new MollweideProjection();
						Point2D mp = projection.project(Math.toRadians(X>=180?X-360:X), Math.toRadians(Y));
						X = Math.toDegrees(mp.getX());
						Y = Math.toDegrees(mp.getY());
						break;
					case EQ:
					default:
						X = p.getRightAscensionAsDecimalDegrees();
						Y = p.getDeclinaisonAsDecimalDegrees();
						break;
					}
					optimizedData.setValue(i, 0, X);
					optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex(), Y);
					i++;
				}
				// We put the first point again to close the boundary of the constellation...
				ConstellationBoundaryPoint lastPoint = constellation.getBoundaryPoints().get(0);
				EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
						new EquatorialCoordinates(lastPoint.getRightAscensionAsDecimalDegrees(), lastPoint.getDeclinaisonAsDecimalDegrees()));
				switch(cs) {
				case ALTAZ:
					GeographicCoordinates observatory = new GeographicCoordinates(observer.getLatitude(), observer.getLongitude());
					X = eca.getAzimuth(observatory, observer.getGreenwichSiderealTime());
					Y = eca.getElevation(observatory, observer.getGreenwichSiderealTime());
					break;
				case ECL:
					X = eca.getEcliptiqueLongitude();
					Y = eca.getEcliptiqueLatitude();
					break;
				case GAL:
					X = eca.getGalacticLongitude();
					Y = eca.getGalacticLatitude();
					MollweideProjection projection = new MollweideProjection();
					Point2D mp = projection.project(Math.toRadians(X>=180?X-360:X), Math.toRadians(Y));
					X = Math.toDegrees(mp.getX());
					Y = Math.toDegrees(mp.getY());
					break;
				case EQ:
				default:
					X = lastPoint.getRightAscensionAsDecimalDegrees();
					Y = lastPoint.getDeclinaisonAsDecimalDegrees();
					break;
				}
				optimizedData.setValue(i, 0, X);
				optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex(), Y);
				i++;
				
				// We finally put a null value as separator in the serie to avoid two constellations to have their boundaries linked together
				optimizedData.setValue(i, 0, X);
				optimizedData.setValueNull(i, serieIndexes.getConstellationBoundarySerieIndex());
				i++;
			}
		}
			
		// Constellation shape lines...
		if (searchOptions.isDisplayConstellationShape()) {
			for (Constellation constellation : constellationsToDisplay) {
				for (ConstellationShapeLine l : constellation.getShapeLines()) {
					// Start
					EquatorialCoordinatesAdapter lineStart = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getStartRightAscension(), l.getStartDeclinaison()));
					// End
					EquatorialCoordinatesAdapter lineEnd = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getEndRightAscension(), l.getEndDeclinaison()));
					
					double startX = 0; 
					double startY = 0;
					double endX = 0;
					double endY = 0;
					switch(cs) {
					case ALTAZ:
						GeographicCoordinates observatory = new GeographicCoordinates(observer.getLatitude(), observer.getLongitude()); 
						startX = lineStart.getAzimuth(observatory, observer.getGreenwichSiderealTime());
						startY = lineStart.getElevation(observatory, observer.getGreenwichSiderealTime());
						endX = lineEnd.getAzimuth(observatory, observer.getGreenwichSiderealTime());
						endY = lineEnd.getElevation(observatory, observer.getGreenwichSiderealTime());
						break;
					case ECL:
						startX = lineStart.getEcliptiqueLongitude();
						startY = lineStart.getEcliptiqueLatitude();
						endX = lineEnd.getEcliptiqueLongitude();
						endY = lineEnd.getEcliptiqueLatitude();
						break;
					case GAL:
						MollweideProjection projection = new MollweideProjection();
						
						startX = lineStart.getGalacticLongitude();
						startY = lineStart.getGalacticLatitude();
						Point2D pStart = projection.project(Math.toRadians(startX>=180?startX-360:startX), Math.toRadians(startY));
						startX = Math.toDegrees(pStart.getX());
						startY = Math.toDegrees(pStart.getY());

						endX = lineEnd.getGalacticLongitude();
						endY = lineEnd.getGalacticLatitude();
						Point2D pEnd = projection.project(Math.toRadians(endX>=180?endX-360:endX), Math.toRadians(endY));
						endX = Math.toDegrees(pEnd.getX());
						endY = Math.toDegrees(pEnd.getY());
						break;
					case EQ:
					default:
						startX = l.getStartRightAscension();
						startY = l.getStartDeclinaison();
						endX = l.getEndRightAscension();
						endY = l.getEndDeclinaison();
						break;
					}
					
					
					optimizedData.setValue(i, 0, startX);
					optimizedData.setValue(i, serieIndexes.getConstellationShapeSerieIndex(), startY);
					optimizedData.setValue(i+1, 0, endX);
					optimizedData.setValue(i+1, serieIndexes.getConstellationShapeSerieIndex(), endY);
					optimizedData.setValue(i+2, 0, endX);
					optimizedData.setValueNull(i+2, serieIndexes.getConstellationShapeSerieIndex());
					
					i += 3;
				}
			}
		}
		// We remove unused rows if any.
		// It is the case when we do not display the objects under horizon in the AltAz projection.
		optimizedData.removeRows(i, (optimizedData.getNumberOfRows() - i));
		return optimizedData;
	}
	
	private String generateTooltip(AstroObject o, Observer observer) {
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
				CoordinatesSystem.instanceOf(appPanel.liCoordinatesMode.getValue(appPanel.liCoordinatesMode.getSelectedIndex())); 
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
	
	private Options createLineChartOptions(CatalogSearchOptions searchOptions) {
		String coordinatesMode = appPanel.liCoordinatesMode.getValue(appPanel.liCoordinatesMode.getSelectedIndex());
		MyLineChartOptions options = MyLineChartOptions.create();
		int chartWidth = Window.getClientWidth() - (2 * ApplicationBoard.PANEL_SPLITTER_WIDTH) - ApplicationBoard.LEFT_PANEL_WIDTH - ApplicationBoard.RIGHT_PANEL_WIDTH;
		int chartHeight = Window.getClientHeight() - 25;
		options.setWidth(chartWidth);
		options.setHeight(chartHeight);
		ChartArea area = ChartArea.create();
		area.setLeft(25);
		area.setTop(25);
		area.setWidth("85%");
		area.setHeight("85%");
		options.setChartArea(area);
		options.setBackgroundColor(STYLE_CHART_BACKGROUND_COLOR);
		AxisOptions hAxisOptions = AxisOptions.create();
		AxisOptions vAxisOptions = AxisOptions.create();
		TextStyle axisTitleTextStyle = TextStyle.create();
		axisTitleTextStyle.setColor("#ffffff");
		hAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		vAxisOptions.setTitleTextStyle(axisTitleTextStyle);
		if (coordinatesMode.equals(""+CoordinatesSystem.ALTAZ) && appPanel.chkShowObjectsUnderHorizon.getValue() == false) {
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
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("LONGITUDE GALACTIQUE");
			vAxisOptions.setTitle("LATITUDE GALACTIQUE");
		} else if ((""+CoordinatesSystem.ALTAZ).equals(coordinatesMode)) {
			hAxisOptions.setMinValue(-180);
			hAxisOptions.setMaxValue(180);
			hAxisOptions.setTitle("AZIMUTH");
			vAxisOptions.setTitle("ELEVATION");
		} else if ((""+CoordinatesSystem.EQ).equals(coordinatesMode)) {
			if (!appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex()).isEmpty()) {
				// We set the limit of the map to the limit of the selected constellation...
				String constellation = appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex());
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
		
		// Trying to fine-tune the series representation options...
		options = createSeriesOptions(options, searchOptions);
		return options;
	}
	
	private static MyLineChartOptions createSeriesOptions(MyLineChartOptions options, CatalogSearchOptions searchOptions) {
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		int i=0;
		for (i=0 ; i<serieIndexes.getObjectSeriesCount() ; i++) {
			MySeries s = MySeries.create();
			s.setLineWidth(0);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
		}
		if (searchOptions.isDisplayConstellationBoundaries()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointSize(0);
			s.setLineDashStyle(new int[]{5, 1, 3});
			s.setColor(STYLE_CONSTELLATION_BORDER_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		if (searchOptions.isDisplayConstellationShape()) {
			MySeries s = MySeries.create();
			s.setLineWidth(1);
			s.setPointSize(0);
			s.setColor(STYLE_CONSTELLATION_SHAPE_COLOR);
			s.setVisibleInLegend(false);
			options.setSeries(i, s);
			i++;
		}
		return options;
	}
	
	private void fetchObjectDetails(final ObjectReference objectReference) {
		if (objectReference.isStar()) {
			catalogService.findStarByHrNumber(objectReference.getId(), new AsyncCallback<Star>() {
				@Override
				public void onFailure(Throwable caught) {
					appPanel.systemMessage.setText("Unable to fetch details about star ["+objectReference.getId()+"]");
				}

				@Override
				public void onSuccess(Star result) {
					displayObjectDetails(result);
				}
			});
		} else if (objectReference.isDeepSkyObject()) {
			catalogService.findObjectById(objectReference.getId(), new AsyncCallback<DeepSkyObject>() {
				@Override
				public void onFailure(Throwable caught) {
					appPanel.systemMessage.setText("Unable to fetch details about object ["+objectReference.getId()+"]");
				}
				
				@Override
				public void onSuccess(DeepSkyObject result) {
					displayObjectDetails(result);
				}
			});
		}
	}
	
	private void displayObjectDetails(AstroObject ao) {
		appPanel.objectDetailsTable.removeAllRows();
		int row = 0;
		Sexagesimal ra = new Sexagesimal(ao.getRightAscension() / 15);
		appPanel.objectDetailsTable.setText(row, 0, "RIGHT ASCENSION (J2000)");
		appPanel.objectDetailsTable.setText(row++, 1, ra.toString(SexagesimalType.HOURS));
		
		Sexagesimal dec = new Sexagesimal(ao.getDeclinaison());
		appPanel.objectDetailsTable.setText(row, 0, "DECLINAISON (J2000)");
		appPanel.objectDetailsTable.setText(row++, 1, dec.toString(SexagesimalType.DEGREES));
		
		if (ao instanceof Star) {
			Star o = (Star) ao;
			appPanel.objectDetailsTable.setText(row, 0, "NAME");
			appPanel.objectDetailsTable.setText(row++, 1, o.getName());
			
			appPanel.objectDetailsTable.setText(row, 0, "HR NUMBER");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getHrNumber());
			
			appPanel.objectDetailsTable.setText(row, 0, "HD NUMBER");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getHdNumber());
			
			appPanel.objectDetailsTable.setText(row, 0, "SAO NUMBER");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getSaoNumber());
			
			appPanel.objectDetailsTable.setText(row, 0, "MAGNITUDE");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getVisualMagnitude());
			
			appPanel.objectDetailsTable.setText(row, 0, "MAGNITUDE B-V");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getBvMag()+(o.isUncertainBvMag() ? " (uncertain) " : ""));
			
			appPanel.objectDetailsTable.setText(row, 0, "MAGNITUDE U-B");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getUbMag()+(o.isUncertainUbMag() ? " (uncertain) " : ""));
			
			appPanel.objectDetailsTable.setText(row, 0, "MAGNITUDE R-I");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getRiMag());
			
			appPanel.objectDetailsTable.setText(row, 0,  "SPECTRAL TYPE");
			appPanel.objectDetailsTable.setText(row++, 1, o.getSpectralType());
			
			appPanel.objectDetailsTable.setText(row, 0, "IR SOURCE ?");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.isIrSource());
			
		} else if (ao instanceof DeepSkyObject) {
			DeepSkyObject o = (DeepSkyObject) ao;
			appPanel.objectDetailsTable.setText(row, 0, "NAME");
			appPanel.objectDetailsTable.setText(row++, 1, o.getName());
			
			appPanel.objectDetailsTable.setText(row, 0, "OTHER NAME");
			appPanel.objectDetailsTable.setText(row++, 1, o.getOtherName());
			
			appPanel.objectDetailsTable.setText(row, 0, "OBJECT TYPE");
			appPanel.objectDetailsTable.setText(row++, 1, o.getObjectType());
			
			appPanel.objectDetailsTable.setText(row, 0, "CONSTELLATION");
			appPanel.objectDetailsTable.setText(row++, 1, o.getConstellation().getName());
			
			appPanel.objectDetailsTable.setText(row, 0, "MAGNITUDE");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getMagnitude());
			
			appPanel.objectDetailsTable.setText(row, 0, "SURFACE BRIGHTNESS");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getSurfaceBrightness());
			
			appPanel.objectDetailsTable.setText(row, 0, "SIZE");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.getSizeHumanReadable());
			
			appPanel.objectDetailsTable.setText(row, 0, "IN BEST NGC CATALOG");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.isInBestNgcCatalog());
			
			appPanel.objectDetailsTable.setText(row, 0, "IN CALDWELL CATALOG");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.isInCaldwellCalatalog());
			
			appPanel.objectDetailsTable.setText(row, 0, "IN HERSCHEL CATALOG");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.isInHerschelCatalog());
			
			appPanel.objectDetailsTable.setText(row, 0, "IN MESSIER CATALOG");
			appPanel.objectDetailsTable.setText(row++, 1, ""+o.isInMessierCatalog());
		}
	}
	
}
