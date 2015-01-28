package com.nzv.gwt.dsocatalog.client;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;

public class ApplicationBoard extends SplitLayoutPanel {

	public ListBox getLiConstellations() {
		return liConstellations;
	}

	public ListBox getLiCoordinatesMode() {
		return liCoordinatesMode;
	}

	public static int LEFT_PANEL_WIDTH = 280;
	public CheckBox getChkShowObjectsUnderHorizon() {
		return chkShowObjectsUnderHorizon;
	}

	public static int RIGHT_PANEL_WIDTH = 280;
	public static int PANEL_SPLITTER_WIDTH = 2;
	
	protected static final DateTimeFormat dtfDateAndTime = DateTimeFormat.getFormat(DsoCatalogGWT.DATE_FORMAT+" "+DsoCatalogGWT.TIME_FORMAT);
	protected static final DateTimeFormat dtfDate = DateTimeFormat.getFormat(DsoCatalogGWT.DATE_FORMAT);
	protected static final DateTimeFormat dtfTime = DateTimeFormat.getFormat(DsoCatalogGWT.TIME_FORMAT);

	VerticalPanel filterPanel = new VerticalPanel();
	CheckBox chkDisplayPlanets = new CheckBox("Afficher les planètes");
	Label lbConstellation = new Label("Constellation : ");
	ListBox liConstellations = new ListBox();
	CheckBox chkDisplayConstellationNames = new CheckBox("Afficher les noms des constellations");
	CheckBox chkDisplayConstellationShapes = new CheckBox("Afficher les formes des constellations");
	CheckBox chkDisplayConstellationBoundaries = new CheckBox(
			"Afficher les limites des constellations");
	CheckBox chkDisplayStars = new CheckBox("Afficher les étoiles");
	Label lbStarLimitMagnitude = new Label("Vmag. max étoiles :");
	HorizontalPanel starLimitMagnitudePanel = new HorizontalPanel();
	TextBox txtBoxStarLimitMagnitude = new TextBox();
	PushButton btIncreaseStarLimitMagnitude = new PushButton("+");
	PushButton btDecreaseStarLimitMagnitude = new PushButton("-");

	CheckBox chkDisplayAsterisms = new CheckBox("Astérismes");
	CheckBox chkDisplayGalaxies = new CheckBox("Galaxies");
	CheckBox chkDisplayGlobularClusters = new CheckBox("Amas globulaires");
	CheckBox chkDisplayOpenClusters = new CheckBox("Amas ouverts");
	CheckBox chkDisplayPlanetaryNebulas = new CheckBox("Nébuleuses planétaires");
	CheckBox chkDisplayNebulas = new CheckBox("Nébuleuses");
	CheckBox chkDisplaySupernovaRemnants = new CheckBox("Rémanants du supernova");
	CheckBox chkDisplayQuasars = new CheckBox("Quasars");

	Label lbDsoLimitMagnitude = new Label("Vmag. max ciel profond :");
	HorizontalPanel dsoLimitMagnitudePanel = new HorizontalPanel();
	TextBox txtBoxDsoLimitMagnitude = new TextBox();
	PushButton btIncreaseDsoLimitMagnitude = new PushButton("+");
	PushButton btDecreaseDsoLimitMagnitude = new PushButton("-");

	Label lbCoordinatesMode = new Label("Système de coordonnées");
	ListBox liCoordinatesMode = new ListBox();
	CheckBox chkShowObjectsUnderHorizon = new CheckBox("Montrer les objets sous l'horizon");
	VerticalPanel coordinatesPanel = new VerticalPanel();

	Label lbObserverLatitude = new Label("Latitude de l'observateur");
	TextBox txtObserverLatitude = new TextBox();
	Label lbObserverLongitude = new Label("Longitude de l'observateur");
	TextBox txtObserverLongitude = new TextBox();
	Label lbObserverDate = new Label("Date pour l'observateur (jj/mm/yyyy)");
	TextBox txtObserverDate = new TextBox();
	CheckBox chkObserverUseComputerDateAndTime = new CheckBox("Utiliser l'heure système");
	Label lbObserverLocalTime = new Label("Heure locale de l'observateur (hh:mm:ss)");
	TextBox txtObserverLocalTime = new TextBox();
	PushButton btObserverSubstractOneWeek = new PushButton("-1w");
	PushButton btObserverSubstractOneDay = new PushButton("-1d");
	PushButton btObserverSubstractOneHour = new PushButton("-1h");
	PushButton btObserverAddOneHour = new PushButton("+1h");
	PushButton btObserverAddOneDay = new PushButton("+1d");
	PushButton btObserverAddOneWeek = new PushButton("+1w");
	HorizontalPanel timeExplorerPanel = new HorizontalPanel();
	Label lbGreenwichHourOffset = new Label("Décalage par rapport à l'heure de Greenwich");
	TextBox txtGreenwichHourOffset = new TextBox();
	VerticalPanel observerPanel = new VerticalPanel();

	Panel visualizationPanel = new SimplePanel();
	FlexTable objectDetailsTable = new FlexTable();

	PushButton btUpdateMap = new PushButton("Update map");
	Label systemMessage = new Label("");

	StackLayoutPanel configurationPanel = new StackLayoutPanel(Unit.PX);
	VerticalPanel leftPanel = new VerticalPanel();
	VerticalPanel centerPanel = new VerticalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	
	
	private ApplicationBoard() {
		super(PANEL_SPLITTER_WIDTH);
	}
	
	public static ApplicationBoard buildApplicationBoard(DsoCatalogGWT source) {
		final ApplicationBoard appPanel = new ApplicationBoard();
		
		// We set the size of the stack panel.
		appPanel.configurationPanel.setPixelSize(LEFT_PANEL_WIDTH, 500);
		
		// We add the coordinates panel.
		appPanel.coordinatesPanel.setSize("270px", "30px");
		appPanel.coordinatesPanel.add(appPanel.lbCoordinatesMode);
		appPanel.liCoordinatesMode.addItem("Equatoriaux", ""+CoordinatesSystem.EQ);
		appPanel.liCoordinatesMode.addItem("Ecliptiques", ""+CoordinatesSystem.ECL);
		appPanel.liCoordinatesMode.addItem("Galactiques", ""+CoordinatesSystem.GAL);
		//appPanel.liCoordinatesMode.addItem("Alt-Az", ""+CoordinatesSystem.ALTAZ);
		appPanel.liCoordinatesMode.addChangeHandler(new UpdateMapEventHandler(source));
		appPanel.coordinatesPanel.add(appPanel.liCoordinatesMode);
		appPanel.chkShowObjectsUnderHorizon.addClickHandler(new UpdateMapEventHandler(source));
		//appPanel.coordinatesPanel.add(appPanel.chkShowObjectsUnderHorizon);

		// We add the coordinates panel to configuration panel.
		appPanel.configurationPanel.add(appPanel.coordinatesPanel, new Label("Projection"), 28);
		
		// We add the observer panel.
		appPanel.observerPanel.setSize("270px", "250px");
		appPanel.observerPanel.add(appPanel.lbObserverLatitude);
		appPanel.txtObserverLatitude.setText("48.833");
		appPanel.observerPanel.add(appPanel.txtObserverLatitude);
		appPanel.observerPanel.add(appPanel.lbObserverLongitude);
		appPanel.txtObserverLongitude.setText("2.333");
		appPanel.observerPanel.add(appPanel.txtObserverLongitude);
		appPanel.observerPanel.add(appPanel.lbObserverDate);
		Date now = new Date();
		appPanel.txtObserverDate.setText(dtfDate.format(now));
		appPanel.txtObserverDate.addKeyUpHandler(new UpdateMapEventHandler(source));
		appPanel.observerPanel.add(appPanel.txtObserverDate);
//		appPanel.chkObserverUseComputerDateAndTime.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue() == true) {
//					setObserverTimeFromComputerTime(appPanel);
//				}
//			}
//		});
		appPanel.observerPanel.add(appPanel.chkObserverUseComputerDateAndTime);
		appPanel.observerPanel.add(appPanel.lbObserverLocalTime);
		appPanel.txtObserverLocalTime.setText(dtfTime.format(now));
		appPanel.txtObserverDate.addKeyUpHandler(new UpdateMapEventHandler(source));
		appPanel.observerPanel.add(appPanel.txtObserverLocalTime);
		appPanel.observerPanel.add(appPanel.lbGreenwichHourOffset);
		appPanel.txtGreenwichHourOffset.setText("+1");
		appPanel.observerPanel.add(appPanel.txtGreenwichHourOffset);
		
		// We set the time explorer panel.
		appPanel.btObserverSubstractOneWeek.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverSubstractOneWeek);
		appPanel.btObserverSubstractOneDay.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverSubstractOneDay);
		appPanel.btObserverSubstractOneHour.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverSubstractOneHour);
		appPanel.btObserverAddOneHour.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverAddOneHour);
		appPanel.btObserverAddOneDay.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverAddOneDay);
		appPanel.btObserverAddOneWeek.addClickHandler(new TimeExplorerClickHandler(source, appPanel));
		appPanel.timeExplorerPanel.add(appPanel.btObserverAddOneWeek);
		appPanel.observerPanel.add(appPanel.timeExplorerPanel);
		
		// We add the observer panel to configuration panel.
		appPanel.configurationPanel.add(appPanel.observerPanel, new Label("Observateur"), 28);
		
		// We put the planet display checkbox in the filter panel.
		appPanel.chkDisplayPlanets.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayPlanets);
		
		// We put the constellation select list in the filter panel.
		appPanel.liConstellations.addChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.lbConstellation);
		appPanel.filterPanel.add(appPanel.liConstellations);
		appPanel.chkDisplayConstellationNames.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationNames);
		appPanel.chkDisplayConstellationShapes.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationShapes);
		appPanel.chkDisplayConstellationBoundaries.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationBoundaries);

		// We put the star magnitude filter field
		appPanel.chkDisplayStars.setValue(true);
		appPanel.chkDisplayStars.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayStars);
		
		appPanel.filterPanel.add(appPanel.lbStarLimitMagnitude);
		appPanel.txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		appPanel.txtBoxStarLimitMagnitude.setWidth("100px");
		appPanel.starLimitMagnitudePanel.add(appPanel.txtBoxStarLimitMagnitude);
		appPanel.btIncreaseStarLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxStarLimitMagnitude, +1));
		appPanel.btDecreaseStarLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxStarLimitMagnitude, -1));
		appPanel.starLimitMagnitudePanel.add(appPanel.btIncreaseStarLimitMagnitude);
		appPanel.starLimitMagnitudePanel.add(appPanel.btDecreaseStarLimitMagnitude);
		appPanel.filterPanel.add(appPanel.starLimitMagnitudePanel);
		
		// We set the DSO filter fields
		appPanel.chkDisplayAsterisms.setValue(true);
		appPanel.chkDisplayAsterisms.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayAsterisms);
		
		appPanel.chkDisplayGalaxies.setValue(false);
		appPanel.chkDisplayGalaxies.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayGalaxies);
		
		appPanel.chkDisplayGlobularClusters.setValue(false);
		appPanel.chkDisplayGlobularClusters.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayGlobularClusters);
		
		appPanel.chkDisplayOpenClusters.setValue(false);
		appPanel.chkDisplayOpenClusters.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayOpenClusters);
		
		appPanel.chkDisplayPlanetaryNebulas.setValue(false);
		appPanel.chkDisplayPlanetaryNebulas.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayPlanetaryNebulas);
		
		appPanel.chkDisplayNebulas.setValue(false);
		appPanel.chkDisplayNebulas.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayNebulas);
		
		appPanel.chkDisplaySupernovaRemnants.setValue(false);
		appPanel.chkDisplaySupernovaRemnants.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplaySupernovaRemnants);
		
		appPanel.chkDisplayQuasars.setValue(false);
		appPanel.chkDisplayQuasars.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDisplayQuasars);
		
		appPanel.filterPanel.add(appPanel.lbDsoLimitMagnitude);
		
		appPanel.txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		appPanel.txtBoxDsoLimitMagnitude.setWidth("100px");
		appPanel.dsoLimitMagnitudePanel.add(appPanel.txtBoxDsoLimitMagnitude);
		appPanel.btIncreaseDsoLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxDsoLimitMagnitude, +1));
		appPanel.btDecreaseDsoLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxDsoLimitMagnitude, -1));
		appPanel.dsoLimitMagnitudePanel.add(appPanel.btIncreaseDsoLimitMagnitude);
		appPanel.dsoLimitMagnitudePanel.add(appPanel.btDecreaseDsoLimitMagnitude);
		appPanel.filterPanel.add(appPanel.dsoLimitMagnitudePanel);

		// We add the filter panel to configuration panel.
		appPanel.configurationPanel.add(appPanel.filterPanel, new Label("Filtres"), 28);
		appPanel.configurationPanel.showWidget(appPanel.filterPanel);
		
		appPanel.leftPanel.add(appPanel.configurationPanel);

		// We set the button which will trigger the map's display.
		appPanel.btUpdateMap.addClickHandler(new UpdateMapEventHandler(source));
		appPanel.leftPanel.add(appPanel.btUpdateMap);
		
		// We put a label that will be used to give feedback to the user.
		appPanel.centerPanel.add(appPanel.systemMessage);
		
		// We add the panel that will be used to display the chart.
		appPanel.centerPanel.add(appPanel.visualizationPanel);
		
		// We add the table that will be used to display object details
		appPanel.rightPanel.add(appPanel.objectDetailsTable);
		
		// We place the left and the right panels in the main one.
		appPanel.addWest(appPanel.leftPanel, LEFT_PANEL_WIDTH);
		appPanel.addEast(appPanel.rightPanel, RIGHT_PANEL_WIDTH);
		appPanel.add(appPanel.centerPanel);
		
		return appPanel;
	}
	
	public CatalogSearchOptions getCatalogSearchOptions() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setRestrictedToConstellationCode(liConstellations.getValue(liConstellations.getSelectedIndex()));
		options.setDisplayPlanets(chkDisplayPlanets.getValue());
		options.setDisplayConstellationNames(chkDisplayConstellationNames.getValue());
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
		options.setObservatoryLatitude(Double.parseDouble(txtObserverLatitude.getText()));
		options.setObservatoryLongitude(Double.parseDouble(txtObserverLongitude.getText()));
		return options;
	}

//	private void setObserverTimeFromComputerTime(ApplicationBoard application) {
//		Date now = new Date();
//		application.txtObserverDate.setText(ApplicationBoard.dtfDate.format(now));
//		application.txtObserverLocalTime.setText(ApplicationBoard.dtfTime.format(now));
//		updateMap();
//	}
}