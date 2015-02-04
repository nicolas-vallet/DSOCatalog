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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.coordinate.GeographicCoordinates;
import com.nzv.astro.ephemeris.coordinate.adapter.EclipticCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.adapter.GalacticCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EclipticCoordinates;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.astro.ephemeris.coordinate.impl.GalacticCoordinates;
import com.nzv.gwt.dsocatalog.date.DateComputation;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryLine;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Planet;
import com.nzv.gwt.dsocatalog.model.PlanetEnum;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.MollweideProjection;
import com.nzv.gwt.dsocatalog.projection.NeutralProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;
import com.nzv.gwt.dsocatalog.projection.StereographicProjection;
import com.nzv.gwt.dsocatalog.visualization.MyDataTable;
import com.nzv.gwt.dsocatalog.visualization.VisualizationHelper;

public class DsoCatalogGWT implements EntryPoint {

	public static String DATE_FORMAT = "dd/MM/yyyy";
	public static String TIME_FORMAT = "HH:mm:ss";

	/* Styles */
	public static String STYLE_CHART_BACKGROUND_COLOR = "#132345";
	public static String AXIS_TITLE_TEXT_STYLE = "color: #ffffff;";
	public static String STYLE_CONSTELLATION_NAME = "point { size: 0; }";
	public static String STYLE_ECLIPTIC_COLOR = "#e5e8c5";
	public static String STYLE_EQUATOR_COLOR = "#70d1e0";
	public static String STYLE_GALACTIC_PLAN_COLOR = "#f5c773";
	public static String STYLE_CONSTELLATION_NAME_TEXT_COLOR = "#90AEF0";
	public static String STYLE_CONSTELLATION_BORDER_COLOR = "#888888";
	public static String STYLE_CONSTELLATION_SHAPE_COLOR = "#90AEF0";
	public static Map<Integer, String> STYLE_PLANETS = new HashMap<Integer, String>(); 
	static {
		STYLE_PLANETS.put(Planet.MERCURY, "point {shape-type: circle; size: 3; fill-color: #b8b8b8; }");
		STYLE_PLANETS.put(Planet.VENUS, "point {shape-type: circle; size: 4; fill-color: #d1d1c2; }");
		STYLE_PLANETS.put(Planet.MARS, "point {shape-type: circle; size: 3; fill-color: #fc9935; }");
		STYLE_PLANETS.put(Planet.JUPITER, "point {shape-type: circle; size: 6; fill-color: #f7be6d; }");
		STYLE_PLANETS.put(Planet.SATURN, "point {shape-type: circle; size: 5; fill-color: #ebdbc5; }");
		STYLE_PLANETS.put(Planet.URANUS, "point {shape-type: circle; size: 4; fill-color: #7aebe9; }");
		STYLE_PLANETS.put(Planet.NEPTUNE, "point {shape-type: circle; size: 4; fill-color: #107fe0; }");
		STYLE_PLANETS.put(Planet.PLUTO, "point {shape-type: circle; size: 2; fill-color: #c4bcaf; }");
		STYLE_PLANETS.put(Planet.SUN, "point {shape-type: circle; size: 10; fill-color: #ffff30; }");
		STYLE_PLANETS.put(Planet.MOON, "point {shape-type: circle; size: 8; fill-color: #dbdbdb; }");
	}
	public static String STYLE_STARS = "point {shape-type: star;shape-dent: 0.2; size: SIZE_STAR; fill-color: #f29500;}";
	public static int STYLE_STARS_POINT_SIZE_MAX = 7;
	public static String STYLE_ASTERISMS = "point { shape-type: polygon; size: 4; fill-color: #132345; stroke-color: #f29500; stroke-width: 1;}";
	public static String STYLE_GALAXIES = "point { shape-type: circle; size: 4; fill-color: #132345; stroke-color: #f42929; stroke-width: 2;}";
	public static int STYLE_GALAXIES_POINT_SIZE_MAX = 5;
	public static String STYLE_GLOBULAR_CLUSTERS = "point {shape-type: circle; size: 4; fill-color: #9bf2bd; stroke-color: #000000; stroke-width: 1;}";
	public static String STYLE_OPEN_CLUSTERS = "point { shape-type: circle; size: 4; fill-color: #132345; stroke-color: #04ea64; stroke-width: 1;}";
	public static String STYLE_PLANETARY_NEBULAS = "point { shape-type: star; shape-sides: 4; shape-dent: 0.2; size: 6; fill-color: #46EFF2;}";
	public static String STYLE_NEBULAS = "point { shape-type: square; size: 6; fill-color: #f28787; stroke-color: #f42929; shape-rotation:45;}";
	public static String STYLE_SN_REMNANTS = "point { shape-type: square; size: 5; fill-color: #132345; stroke-color: #f42929; stroke-width: 1; shape-rotation:45;}";
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
		//appPanel.objectDetailsTable.removeAllRows();
		appPanel.removeObjectDetails();
		final CatalogSearchOptions searchOptions = appPanel.getCatalogSearchOptions();
		catalogService.findObjectBrighterThan(searchOptions, new AsyncCallback<Set<AstroObject>>() {

					@Override
					public void onSuccess(final Set<AstroObject> objects) {
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
								Options options = VisualizationHelper.createLineChartOptions(searchOptions, appPanel, constellationsList);
								final LineChart chart = new LineChart(data, options);
								chart.addSelectHandler(new SelectHandler() {
									@Override
									public void onSelect(SelectEvent event) {
										JsArray<Selection> selections = chart.getSelections();
										for (int i=0 ; i<selections.length() ; i++) {
											Selection selection = selections.get(i);
											if (displayedObjectReferences.containsKey(new ObjectReferenceAddressInTable(selection.getRow(), selection.getColumn()))) {
												// The user clicked on a AstroObject (planet, star or dso)
												fetchObjectDetails(searchOptions, displayedObjectReferences.get(
														new ObjectReferenceAddressInTable(selection.getRow(), selection.getColumn())));
											} else {
												// The user selected a constellation boundary point or a shape line limit...
												appPanel.removeObjectDetails();
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
								optimizedData = VisualizationHelper.initializeDataTable(appPanel, optimizedData, objects, constellationsList);
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
	
	protected Observer initializeObserver() {
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
		} else if (H + Integer.valueOf(appPanel.txtGreenwichHourOffset.getText()) > 23) {
			// We must consider the day after the one specified...
			Date dateInGreenwhich = ApplicationBoard.dtfDate.parse(appPanel.txtObserverDate.getText());
			CalendarUtil.addDaysToDate(dateInGreenwhich, +1);
			date = ApplicationBoard.dtfDate.format(dateInGreenwhich).split("/");
			j = Integer.valueOf(date[0]);
			m = Integer.valueOf(date[1]);
			a = Integer.valueOf(date[2]);
		}
		jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(a+"."+m+j));
		Sexagesimal T = new Sexagesimal(H-Integer.valueOf(appPanel.txtGreenwichHourOffset.getText()), M, S);
		double gst = DateComputation.getMeanSiderealTimeAsHoursFromJulianDay(jd, T);
		observer.setGreenwichSiderealTime(gst);
		return observer;
	}
	
	private MyDataTable fillDataTableWithValues(CatalogSearchOptions searchOptions, MyDataTable optimizedData, 
			Map<ObjectReferenceAddressInTable, ObjectReference> displayedObjectReferences, Set<AstroObject> objects) {
		boolean showOneConstellation = searchOptions.getRestrictedToConstellationCode() != null && 
				!searchOptions.getRestrictedToConstellationCode().isEmpty();
		CoordinatesSystem cs = CoordinatesSystem.valueOf(appPanel.liCoordinatesMode.getValue(appPanel.liCoordinatesMode.getSelectedIndex()));
		Observer observer = initializeObserver();
		GeographicCoordinates observatory = new GeographicCoordinates(observer.getLatitude(), observer.getLongitude());
		DataSerieIndexes serieIndexes = new DataSerieIndexes(searchOptions);
		//Window.alert(serieIndexes.toString());
		int i = 0;
		
		
		// Ecliptic, Equator and Galactic plan...
		{
			Projection projectionToUse = new NeutralProjection();
			if (cs == CoordinatesSystem.ALTAZ) {
				projectionToUse = new StereographicProjection();
			} else if (cs == CoordinatesSystem.GAL) {
				projectionToUse = new MollweideProjection();
			}
			for (int j=0 ; j<360 ; j++) {
				EclipticCoordinatesAdapter eclipticAdapter = new EclipticCoordinatesAdapter(new EclipticCoordinates(j, 0));
				EquatorialCoordinatesAdapter equatorAdapter = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(j, 0));
				GalacticCoordinatesAdapter galacticAdapter = new GalacticCoordinatesAdapter(new GalacticCoordinates(j, 0));
				switch(cs) {
				case ALTAZ:
					// TODO
					break;
				case ECL:
					if (searchOptions.isDisplayEcliptic()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(eclipticAdapter.getEclipticCoordinates().getEcliptiqueLongitude()),
								Math.toRadians(eclipticAdapter.getEclipticCoordinates().getEcliptiqueLatitude()));
						optimizedData.setValue(i, 0, GeometryUtils.normalizeAngleInDegrees(Math.toDegrees(mp.getX()), -180, 180));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex()+1, "ECLIPTIQUE");
						i++;
					}
					if (searchOptions.isDisplayEquator()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(equatorAdapter.getEcliptiqueLongitude()), 
								Math.toRadians(equatorAdapter.getEcliptiqueLatitude()));
						optimizedData.setValue(i, 0, Math.toDegrees(mp.getX()));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex()+1, "EQUATEUR CELESTE");
						i++;
					}
					if (searchOptions.isDisplayGalacticPlan()) {
						EquatorialCoordinatesAdapter tmp = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(galacticAdapter.getRightAscension(), galacticAdapter.getDeclinaison()));
						Point2D mp = projectionToUse.project(
								Math.toRadians(tmp.getEcliptiqueLongitude()), 
								Math.toRadians(tmp.getEcliptiqueLatitude()));
						optimizedData.setValue(i, 0, Math.toDegrees(mp.getX()));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex()+1, "PLAN GALACTIQUE");
						i++;
					}
					break;
				case GAL:
					if (searchOptions.isDisplayEcliptic()) {
						EquatorialCoordinatesAdapter tmp = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(eclipticAdapter.getRightAscension(), eclipticAdapter.getDeclinaison()));
						Point2D mp = projectionToUse.project(
								Math.toRadians(tmp.getGalacticLongitude()), 
								Math.toRadians(tmp.getGalacticLatitude()));
						optimizedData.setValue(i, 0, GeometryUtils.normalizeAngleInDegrees(Math.toDegrees(mp.getX()), -180, 180));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex()+1, "ECLIPTIQUE");
						i++;
					}
					if (searchOptions.isDisplayEquator()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(equatorAdapter.getGalacticLongitude()), 
								Math.toRadians(equatorAdapter.getGalacticLatitude()));
						optimizedData.setValue(i, 0, GeometryUtils.normalizeAngleInDegrees(Math.toDegrees(mp.getX()), -180, 180));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex()+1, "EQUATEUR CELESTE");
						i++;
					}
					if (searchOptions.isDisplayGalacticPlan()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(galacticAdapter.getGalacticCoordinates().getGalacticLongitude()), 
								Math.toRadians(galacticAdapter.getGalacticCoordinates().getGalacticLatitude()));
						optimizedData.setValue(i, 0, GeometryUtils.normalizeAngleInDegrees(Math.toDegrees(mp.getX()), -180, 180));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex()+1, "PLAN GALACTIQUE");
					}
					break;
				case EQ:
				default:
					if (searchOptions.isDisplayEcliptic()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(eclipticAdapter.getRightAscension()), 
								Math.toRadians(eclipticAdapter.getDeclinaison()));
						optimizedData.setValue(i, 0, GeometryUtils.normalizeAngleInDegrees(Math.toDegrees(mp.getX()), 0, 360));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEclipticSerieIndex()+1, "ECLIPTIQUE");
						i++;
					}
					if (searchOptions.isDisplayEquator()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(equatorAdapter.getEquatorialCoordinates().getRightAscension()), 
								Math.toRadians(equatorAdapter.getEquatorialCoordinates().getDeclinaison()));
						optimizedData.setValue(i, 0, Math.toDegrees(mp.getX()));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getEquatorSerieIndex()+1, "EQUATEUR CELESTE");
						i++;
					}
					if (searchOptions.isDisplayGalacticPlan()) {
						Point2D mp = projectionToUse.project(
								Math.toRadians(galacticAdapter.getRightAscension()), 
								Math.toRadians(galacticAdapter.getDeclinaison()));
						optimizedData.setValue(i, 0, Math.toDegrees(mp.getX()));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex(), Math.toDegrees(mp.getY()));
						optimizedData.setValue(i, serieIndexes.getGalacticPlanSerieIndex()+1, "PLAN GALACTIQUE");
						i++;
					}
					break;
				}
			}
		}
		
		
		for (AstroObject o : objects) {
			// Should we show objects under horizon ?
			if (cs == CoordinatesSystem.ALTAZ && !appPanel.chkShowObjectsUnderHorizon.getValue()) {
				if (o.getYCoordinateForReferential(cs, observer) < 0) {
					continue;
				}
			}
			
			optimizedData = VisualizationHelper.fillRowWithNullValues(optimizedData, i);
			int serieIndexToUse = 0;
			String styleToUse = new String();
			ObjectReference objectReference = null;
			Projection projection = new NeutralProjection();
			if (o instanceof Planet) {
				// In case we are only displaying one constellation, we must check than this planet is actually inside the desired constellation's boundary.
				if (showOneConstellation && cs == CoordinatesSystem.EQ) {
					Constellation c = constellationsList.get(searchOptions.getRestrictedToConstellationCode());
					if (!c.isSpreadOnBothSidesOfRightAscensionOriginAxis()) {
						if (c.getUpperWesternMapLimit(0).getRightAscension() < o.getRightAscension() ||
								c.getUpperWesternMapLimit(0).getDeclinaison() < o.getDeclinaison() ||
								c.getLowerEasternMapLimit(0).getRightAscension() > o.getRightAscension() ||
								c.getLowerEasternMapLimit(0).getDeclinaison() > o.getDeclinaison()) {
							continue;
						}
					} else {
						if (c.getUpperWesternMapLimit(0).getRightAscension() > o.getRightAscension() ||
								c.getUpperWesternMapLimit(0).getDeclinaison() > o.getDeclinaison() ||
								c.getLowerEasternMapLimit(0).getRightAscension() < o.getRightAscension() ||
								c.getLowerEasternMapLimit(0).getDeclinaison() < o.getDeclinaison()) {
							continue;
						}
					}
				}
				serieIndexToUse = serieIndexes.getPlanetSerieIndex();
				styleToUse = STYLE_PLANETS.get(((Planet)o).getNumericIdentifier());
				objectReference = new ObjectReference(true, false, false, ((Planet)o).getNumericIdentifier());
			} else if (o instanceof Star) {
				serieIndexToUse = serieIndexes.getStarSerieIndex();
				
				// We compute the size of point to use given star magnitude...
				double mag = o.getVisualMagnitude();
				double tmp = 10 / Math.exp(0.15 * mag);
				int sizePoint = ((int)(Math.ceil(tmp))) * STYLE_STARS_POINT_SIZE_MAX / 12;
				styleToUse = STYLE_STARS.replaceAll("SIZE_STAR", ""+sizePoint);
				objectReference = new ObjectReference(false, true, false, ((Star) o).getHrNumber());
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
				objectReference = new ObjectReference(false, false, true, dso.getId());
			}
			EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(o.getRightAscension(), o.getDeclinaison()));
			Point2D p = new Point2D(0,0);
			if (cs == CoordinatesSystem.GAL) {
				projection = new MollweideProjection();
				p = projection.project(Math.toRadians(GeometryUtils.normalizeAngleInDegrees(eca.getGalacticLongitude(), -180, 180)), 
						Math.toRadians(GeometryUtils.normalizeAngleInDegrees(eca.getGalacticLatitude(), -90, 90)));
			} else if (cs == CoordinatesSystem.ALTAZ) {
				if (o.getYCoordinateForReferential(cs, observer) < 0) continue;
				projection = new StereographicProjection();
				p = projection.project(Math.toRadians(GeometryUtils.normalizeAngleInDegrees(o.getXCoordinateForReferential(cs, observer), 0, 360)), Math.toRadians(o.getYCoordinateForReferential(cs, observer)));
			} else {
				p = projection.project(Math.toRadians(o.getXCoordinateForReferential(cs, observer)), Math.toRadians(o.getYCoordinateForReferential(cs, observer)));
			}
			
			optimizedData.setValue(i, 0, Math.toDegrees(p.getX()));
			optimizedData.setValue(i, serieIndexToUse, Math.toDegrees(p.getY()));
			optimizedData.setValue(i, serieIndexToUse+1, styleToUse);
			optimizedData.setValue(i, serieIndexToUse+2, VisualizationHelper.generateTooltip(appPanel, o, observer));
			displayedObjectReferences.put(new ObjectReferenceAddressInTable(i, serieIndexToUse), objectReference);
			i++;
		}
		
		ArrayList<Constellation> constellationsToDisplay = new ArrayList<Constellation>();
		if (showOneConstellation) {
			constellationsToDisplay.add(constellationsList.get(appPanel.liConstellations.getValue(appPanel.liConstellations.getSelectedIndex())));
		} else {
			constellationsToDisplay.addAll(constellationsList.values());
		}
		
		// Constellation(s) names
		if (searchOptions.isDisplayConstellationNames()) {
			Projection projection = new NeutralProjection();
			Point2D mp = new Point2D(0, 0);
			for (Constellation constellation : constellationsToDisplay) {
				double X, Y;
				EquatorialCoordinatesAdapter eca = new EquatorialCoordinatesAdapter(
						new EquatorialCoordinates(constellation.getCenterRightAscension(), constellation.getCenterDeclinaison()));
				switch (cs) {
					case ALTAZ: {
						X = eca.getAzimuth(observatory, observer.getGreenwichSiderealTime());
						Y = eca.getElevation(observatory, observer.getGreenwichSiderealTime());
						if (Y < 0) continue;
						projection = new StereographicProjection();
						mp = projection.project(Math.toRadians(GeometryUtils.normalizeAngleInDegrees(X, 0, 360)), Math.toRadians(Y));
						break;
					}
					case ECL: {
						X = eca.getEcliptiqueLongitude();
						Y = eca.getEcliptiqueLatitude();
						mp = projection.project(Math.toRadians(X), Math.toRadians(Y));
						break;
					}
					case GAL: {
						X = eca.getGalacticLongitude();
						Y = eca.getGalacticLatitude();
						projection = new MollweideProjection();
						mp = projection.project(Math.toRadians(X>=180?X-360:X), Math.toRadians(Y));
						break;
					}
					case EQ:
					default: {
						X = constellation.getCenterRightAscension();
						Y = constellation.getCenterDeclinaison();
						mp = projection.project(Math.toRadians(X), Math.toRadians(Y));
						break;
					}
				}
				X = Math.toDegrees(mp.getX());
				Y = Math.toDegrees(mp.getY());
				optimizedData.setValue(i, 0, X);
				optimizedData.setValue(i, serieIndexes.getConstellationNameSerieIndex(), Y);
				optimizedData.setValue(i, serieIndexes.getConstellationNameSerieIndex()+1, STYLE_CONSTELLATION_NAME);
				optimizedData.setValue(i, serieIndexes.getConstellationNameSerieIndex()+2, constellation.getCode());
				optimizedData.setValue(i, serieIndexes.getConstellationNameSerieIndex()+3, constellation.getName());
				i++;
			}
		}
		// Constellation(s) boundaries...
		if (searchOptions.isDisplayConstellationBoundaries()) {
			Projection projection = new NeutralProjection();
			for (Constellation constellation : constellationsToDisplay) {
				for (ConstellationBoundaryLine l : constellation.getBoundaryLines()) {
					// Start
					EquatorialCoordinatesAdapter lineStart = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getStart().getRightAscensionAsDecimalDegrees(), l.getStart().getDeclinaisonAsDecimalDegrees()));
					// End
					EquatorialCoordinatesAdapter lineEnd = new EquatorialCoordinatesAdapter(
							new EquatorialCoordinates(l.getEnd().getRightAscensionAsDecimalDegrees(), l.getEnd().getDeclinaisonAsDecimalDegrees()));
					
					double startX = 0; 
					double startY = 0;
					double endX = 0;
					double endY = 0;
					
					switch(cs) {
						case ALTAZ: {
							projection = new StereographicProjection();
							startX = GeometryUtils.normalizeAngleInDegrees(lineStart.getAzimuth(observatory, observer.getGreenwichSiderealTime()), 0, 360);
							startY = lineStart.getElevation(observatory, observer.getGreenwichSiderealTime());
							endX = GeometryUtils.normalizeAngleInDegrees(lineEnd.getAzimuth(observatory, observer.getGreenwichSiderealTime()), 0, 360);
							endY = lineEnd.getElevation(observatory, observer.getGreenwichSiderealTime());
							if (appPanel.chkShowObjectsUnderHorizon.getValue() == false && (startY < 0 || endY < 0)) {
								continue;
							}
							break;
						}
						case GAL: {
							projection = new MollweideProjection();
							startX = GeometryUtils.normalizeAngleInDegrees(lineStart.getGalacticLongitude(), -180, 180);
							startY = lineStart.getGalacticLatitude();
							endX = GeometryUtils.normalizeAngleInDegrees(lineEnd.getGalacticLongitude(), -180, 180);
							endY = lineEnd.getGalacticLatitude();
							break;
						}
						case ECL: {
							projection = new NeutralProjection();
							startX = lineStart.getEcliptiqueLongitude();
							startY = lineStart.getEcliptiqueLatitude();
							endX = lineEnd.getEcliptiqueLongitude();
							endY = lineEnd.getEcliptiqueLatitude();
							break;
						}
						case EQ:
						default: {
							projection = new NeutralProjection();
							startX = lineStart.getEquatorialCoordinates().getRightAscension();
							startY = lineStart.getEquatorialCoordinates().getDeclinaison();
							endX = lineEnd.getEquatorialCoordinates().getRightAscension();
							endY = lineEnd.getEquatorialCoordinates().getDeclinaison();
							break;
						}
					}
					
					if (startX < endX) {
						// We swap the two points in view to draw the line from left to right...
						double t = startX; startX = endX; endX = t;
						t = startY; startY = endY; endY = t;
					}
					
					boolean isCrossingChartLimitX = GeometryUtils.isCrossingChartLimitX(startX, endX, cs);
					Point2D pStart = projection.project(Math.toRadians(startX), Math.toRadians(startY));
					Point2D pEnd = projection.project(Math.toRadians(endX), Math.toRadians(endY));
						
					optimizedData.setValue(i, 0, Math.toDegrees(pStart.getX()));
					optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex(), Math.toDegrees(pStart.getY()));
					optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex()+1, constellation.getName());
					if (isCrossingChartLimitX) {
						// We have to generate intermediate points...
						Point2D intermediatePointOnRight = GeometryUtils.giveIntermediatePointOnChartLimit(new Point2D(startX, startY), new Point2D(endX, endY), cs);
						Point2D intermediatePointOnLeft = new Point2D(intermediatePointOnRight.getX()+360, intermediatePointOnRight.getY());
						intermediatePointOnRight = projection.project(Math.toRadians(intermediatePointOnRight.getX()), Math.toRadians(intermediatePointOnRight.getY()));
						intermediatePointOnLeft = projection.project(Math.toRadians(intermediatePointOnLeft.getX()), Math.toRadians(intermediatePointOnLeft.getY()));
						
						optimizedData.setValue(i+1, 0, Math.toDegrees(intermediatePointOnLeft.getX()));
						optimizedData.setValue(i+1, serieIndexes.getConstellationBoundarySerieIndex(), Math.toDegrees(intermediatePointOnLeft.getY()));
						optimizedData.setValue(i+1, serieIndexes.getConstellationBoundarySerieIndex()+1, constellation.getName());
						optimizedData.setValue(i+2, 0, Math.toDegrees(intermediatePointOnLeft.getX()));
						optimizedData.setValueNull(i+2, serieIndexes.getConstellationBoundarySerieIndex());
						i += 3;
						
						optimizedData.addRows(3);
						optimizedData.setValue(i, 0, Math.toDegrees(intermediatePointOnRight.getX()));
						optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex(), Math.toDegrees(intermediatePointOnRight.getY()));
						optimizedData.setValue(i, serieIndexes.getConstellationBoundarySerieIndex()+1, constellation.getName());
					}
					optimizedData.setValue(i+1, 0, Math.toDegrees(pEnd.getX()));
					optimizedData.setValue(i+1, serieIndexes.getConstellationBoundarySerieIndex(), Math.toDegrees(pEnd.getY()));
					optimizedData.setValue(i+1, serieIndexes.getConstellationBoundarySerieIndex()+1, constellation.getName());
					optimizedData.setValue(i+2, 0, Math.toDegrees(pEnd.getX()));
					optimizedData.setValueNull(i+2, serieIndexes.getConstellationBoundarySerieIndex());
					i += 3;
				}
				
			}
		}
			
		// Constellation shape lines...
		if (searchOptions.isDisplayConstellationShape()) {
			Projection projection = new NeutralProjection();
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
						case ALTAZ: {
							projection = new StereographicProjection();
							startX = GeometryUtils.normalizeAngleInDegrees(lineStart.getAzimuth(observatory, observer.getGreenwichSiderealTime()), 0, 360);
							startY = lineStart.getElevation(observatory, observer.getGreenwichSiderealTime());
							endX = GeometryUtils.normalizeAngleInDegrees(lineEnd.getAzimuth(observatory, observer.getGreenwichSiderealTime()), 0, 360);
							endY = lineEnd.getElevation(observatory, observer.getGreenwichSiderealTime());
							if (appPanel.chkShowObjectsUnderHorizon.getValue() == false && (startY < 0 || endY < 0)) {
								continue;
							}
							break;
						}
						case GAL: {
							projection = new MollweideProjection();
							startX = GeometryUtils.normalizeAngleInDegrees(lineStart.getGalacticLongitude(), -180, 180);
							startY = lineStart.getGalacticLatitude();
							endX = GeometryUtils.normalizeAngleInDegrees(lineEnd.getGalacticLongitude(), -180, 180);
							endY = lineEnd.getGalacticLatitude();
							break;
						}
						case ECL: {
							projection = new NeutralProjection();
							startX = lineStart.getEcliptiqueLongitude();
							startY = lineStart.getEcliptiqueLatitude();
							endX = lineEnd.getEcliptiqueLongitude();
							endY = lineEnd.getEcliptiqueLatitude();
							break;
						}
						case EQ:
						default: {
							projection = new NeutralProjection();
							startX = l.getStartRightAscension();
							startY = l.getStartDeclinaison();
							endX = l.getEndRightAscension();
							endY = l.getEndDeclinaison();
							break;
						}
					}
					
					if (startX < endX) {
						// We swap the two points in view to draw the line from left to right...
						double t = startX; startX = endX; endX = t;
						t = startY; startY = endY; endY = t;
					}
					
					boolean isCrossingChartLimitX = GeometryUtils.isCrossingChartLimitX(startX, endX, cs);
					Point2D pStart = projection.project(Math.toRadians(startX), Math.toRadians(startY));
					Point2D pEnd = projection.project(Math.toRadians(endX), Math.toRadians(endY));
					
					optimizedData.setValue(i, 0, Math.toDegrees(pStart.getX()));
					optimizedData.setValue(i, serieIndexes.getConstellationShapeSerieIndex(), Math.toDegrees(pStart.getY()));
					optimizedData.setValue(i, serieIndexes.getConstellationShapeSerieIndex()+1, constellation.getName());
					
					if (isCrossingChartLimitX) {
						// We have to generate intermediate points...
						Point2D intermediatePointOnRight = GeometryUtils.giveIntermediatePointOnChartLimit(
								new Point2D(startX, startY), new Point2D(endX, endY), cs);
						Point2D intermediatePointOnLeft = new Point2D(intermediatePointOnRight.getX()+360, intermediatePointOnRight.getY());
						intermediatePointOnRight = projection.project(Math.toRadians(intermediatePointOnRight.getX()), Math.toRadians(intermediatePointOnRight.getY()));
						intermediatePointOnLeft = projection.project(Math.toRadians(intermediatePointOnLeft.getX()), Math.toRadians(intermediatePointOnLeft.getY()));
						optimizedData.setValue(i+1, 0, Math.toDegrees(intermediatePointOnLeft.getX()));
						optimizedData.setValue(i+1, serieIndexes.getConstellationShapeSerieIndex(), Math.toDegrees(intermediatePointOnLeft.getY()));
						optimizedData.setValue(i+1, serieIndexes.getConstellationShapeSerieIndex()+1, constellation.getName());
						optimizedData.setValue(i+2, 0, Math.toDegrees(intermediatePointOnLeft.getX()));
						optimizedData.setValueNull(i+2, serieIndexes.getConstellationShapeSerieIndex());
						i += 3;
						
						optimizedData.addRows(3);
						optimizedData.setValue(i, 0, Math.toDegrees(intermediatePointOnRight.getX()));
						optimizedData.setValue(i, serieIndexes.getConstellationShapeSerieIndex(), Math.toDegrees(intermediatePointOnRight.getY()));
						optimizedData.setValue(i, serieIndexes.getConstellationShapeSerieIndex()+1, constellation.getName());
					}
					
					optimizedData.setValue(i+1, 0, Math.toDegrees(pEnd.getX()));
					optimizedData.setValue(i+1, serieIndexes.getConstellationShapeSerieIndex(), Math.toDegrees(pEnd.getY()));
					optimizedData.setValue(i+1, serieIndexes.getConstellationShapeSerieIndex()+1, constellation.getName());
					optimizedData.setValue(i+2, 0, Math.toDegrees(pEnd.getX()));
					optimizedData.setValueNull(i+2, serieIndexes.getConstellationShapeSerieIndex());
					i += 3;
				}
			}
		}
		// We remove unused rows if any.
		// It is the case when we do not display the objects under horizon in the AltAz projection.
		optimizedData.removeRows(i, (optimizedData.getNumberOfRows() - i));
		
//		if (cs == CoordinatesSystem.ALTAZ) {
//			double yMin = 0, xMin = 0;
//			for (int j=0 ; j<optimizedData.getNumberOfRows() ; j++) {
//				if (optimizedData.getValueDouble(j, serieIndexes.getStarSerieIndex()) < yMin) {
//					yMin = optimizedData.getValueDouble(j, serieIndexes.getStarSerieIndex());
//				}
//				if (optimizedData.getValueDouble(j, 0) < xMin) {
//					xMin = optimizedData.getValueDouble(j, 0);
//				}
//			}
//			Window.alert("xMin="+xMin+" / yMin="+yMin);
//		}
		return optimizedData;
	}
	
	private void fetchObjectDetails(CatalogSearchOptions searchOptions, final ObjectReference objectReference) {
		if (objectReference.isPlanet()) {
			final PlanetEnum planet = PlanetEnum.forId(objectReference.getId());
			catalogService.computePlanetCurrentPosition(PlanetEnum.forId(objectReference.getId()), searchOptions, new AsyncCallback<Planet>() {

				@Override
				public void onFailure(Throwable caught) {
					appPanel.systemMessage.setText("Unable to fetch details about planet ["+planet.getName()+"]");
				}

				@Override
				public void onSuccess(Planet result) {
					VisualizationHelper.displayObjectDetails(result, appPanel.southPanel);
				}
			});
		} else if (objectReference.isStar()) {
			catalogService.findStarByHrNumber(objectReference.getId(), new AsyncCallback<Star>() {
				@Override
				public void onFailure(Throwable caught) {
					appPanel.systemMessage.setText("Unable to fetch details about star ["+objectReference.getId()+"]");
				}

				@Override
				public void onSuccess(Star result) {
					VisualizationHelper.displayObjectDetails(result, appPanel.southPanel);
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
					VisualizationHelper.displayObjectDetails(result, appPanel.southPanel);
				}
			});
		}
	}
}
