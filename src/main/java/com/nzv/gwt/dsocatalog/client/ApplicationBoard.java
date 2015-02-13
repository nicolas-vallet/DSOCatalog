package com.nzv.gwt.dsocatalog.client;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nzv.gwt.dsocatalog.i18n.DsoCatalogMessages;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;

public class ApplicationBoard extends SplitLayoutPanel {
	
	public static int LEFT_PANEL_WIDTH = 280;
	public static int LEFT_PANEL_HEIGHT = 660;
	public static int SOUTH_PANEL_HEIGHT = 300;
	public static int PANEL_SPLITTER_WIDTH = 2;
	
	private static DsoCatalogMessages msg = GWT.create(DsoCatalogMessages.class);
	
	private static boolean showAltAzProjectionOption = false;
	
	public ListBox getLiConstellations() {
		return liConstellations;
	}

	public ListBox getLiCoordinatesMode() {
		return liCoordinatesMode;
	}

	public CheckBox getChkShowObjectsUnderHorizon() {
		return chkShowObjectsUnderHorizon;
	}

	
	protected static final DateTimeFormat dtfDateAndTime = DateTimeFormat.getFormat(DsoCatalogGWT.DATE_FORMAT+" "+DsoCatalogGWT.TIME_FORMAT);
	protected static final DateTimeFormat dtfDate = DateTimeFormat.getFormat(DsoCatalogGWT.DATE_FORMAT);
	protected static final DateTimeFormat dtfTime = DateTimeFormat.getFormat(DsoCatalogGWT.TIME_FORMAT);

	DsoCatalogGWT application;
	VerticalPanel filterPanel = new VerticalPanel();
	CheckBox chkDisplayPlanets = new CheckBox(msg.tabFiltersDisplayPlanets());
	Label lbConstellation = new Label(msg.tabFiltersConstellation()+ ":");
	ListBox liConstellations = new ListBox();
	CheckBox chkDisplayConstellationNames = new CheckBox(msg.tabFiltersDisplayConstellationsNames());
	CheckBox chkDisplayConstellationShapes = new CheckBox(msg.tabFiltersDisplayConstellationsShapes());
	CheckBox chkDisplayConstellationBoundaries = new CheckBox(msg.tabFiltersDisplayConstellationsBoundaries());
	CheckBox chkDisplayStars = new CheckBox(msg.tabFiltersDisplayStars());
	
	Label lbStarSpectralTypeRestriction = new Label(msg.tabFiltersStarSpectralTypeRestriction()+" :");
	TextBox txtStarSpectralTypeRestriction = new TextBox();
	
	Label lbStarLimitMagnitude = new Label(msg.tabFiltersLimitMagnitudeStars()+" :");
	HorizontalPanel starLimitMagnitudePanel = new HorizontalPanel();
	TextBox txtBoxStarLimitMagnitude = new TextBox();
	PushButton btIncreaseStarLimitMagnitude = new PushButton("+");
	PushButton btDecreaseStarLimitMagnitude = new PushButton("-");

	CheckBox chkDisplayAsterisms = new CheckBox(msg.tabFiltersDisplayAsterisms());
	CheckBox chkDisplayGalaxies = new CheckBox(msg.tabFiltersDisplayGalaxies());
	CheckBox chkDisplayGlobularClusters = new CheckBox(msg.tabFiltersDisplayGlobularCluster());
	CheckBox chkDisplayOpenClusters = new CheckBox(msg.tabFiltersDisplayOpenCluster());
	CheckBox chkDisplayPlanetaryNebulas = new CheckBox(msg.tabFiltersDisplayPlanetaryNebulas());
	CheckBox chkDisplayNebulas = new CheckBox(msg.tabFiltersDisplayNebulas());
	CheckBox chkDisplaySupernovaRemnants = new CheckBox(msg.tabFiltersDisplaySnRemnants());
	CheckBox chkDisplayQuasars = new CheckBox(msg.tabFiltersDisplayQuasars());
	
	Label lbDsoSubtypeRestriction = new Label(msg.tabFiltersDsoSubtypeRestriction()+" :");
	TextBox txtDsoSubtypeRestriction = new TextBox();
	
	CheckBox chkDsoInMessierCatalog = new CheckBox(msg.tabFilterDsoInMessierCatalog());
	CheckBox chkDsoInBestNgcCatalog = new CheckBox(msg.tabFilterDsoInBestNgcCatalog());
	CheckBox chkDsoInCaldwellCatalog = new CheckBox(msg.tabFilterDsoInCaldwellCatalog());
	CheckBox chkDsoInHerschelCatalog = new CheckBox(msg.tabFilterDsoInHerschelCatalog());

	Label lbDsoLimitMagnitude = new Label(msg.tabFiltersLimitMagnitudeDso()+" :");
	HorizontalPanel dsoLimitMagnitudePanel = new HorizontalPanel();
	TextBox txtBoxDsoLimitMagnitude = new TextBox();
	PushButton btIncreaseDsoLimitMagnitude = new PushButton("+");
	PushButton btDecreaseDsoLimitMagnitude = new PushButton("-");

	Label lbCoordinatesMode = new Label(msg.tabProjectionCoordinatesSystem());
	ListBox liCoordinatesMode = new ListBox();
	CheckBox chkShowObjectsUnderHorizon = new CheckBox(msg.tabProjectionDisplayObjectUnderHorizon());
	CheckBox chkDisplayEcliptic = new CheckBox(msg.tabProjectionDisplayEcliptic());
	CheckBox chkDisplayEquator = new CheckBox(msg.tabProjectionDisplayEquator());
	CheckBox chkDisplayGalacticPlan = new CheckBox(msg.tabProjectionDisplayGalacticPlan());
	VerticalPanel projectionPanel = new VerticalPanel();

	HorizontalPanel languagePanel = new HorizontalPanel();
	Label lbObserverLanguage = new Label(" "+msg.tabObserverLanguage()+" : ");
	ListBox liObserverLanguage = new ListBox();
	Hidden hiddenObserverCurrentLanguage = new Hidden();
	Label lbObserverLatitude = new Label(msg.tabObserverLatitude());
	TextBox txtObserverLatitude = new TextBox();
	Label lbObserverLongitude = new Label(msg.tabObserverLongitude());
	TextBox txtObserverLongitude = new TextBox();
	Label lbObserverDate = new Label(msg.tabObserverDate()+" ("+msg.tabObserverDateFormat()+")");
	TextBox txtObserverDate = new TextBox();
	CheckBox chkObserverUseComputerDateAndTime = new CheckBox(msg.tabObserverUseSystemCurrentTime());
	Label lbObserverLocalTime = new Label(msg.tabObserverLocalTime()+" ("+msg.tabObserverTimeFormat()+")");
	TextBox txtObserverLocalTime = new TextBox();
	PushButton btObserverSubstractOneWeek = new PushButton(msg.tabObserverTimeButtonCaptionRemoveOneWeek());
	PushButton btObserverSubstractOneDay = new PushButton(msg.tabObserverTimeButtonCaptionRemoveOneDay());
	PushButton btObserverSubstractOneHour = new PushButton(msg.tabObserverTimeButtonCaptionRemoveOneHour());
	PushButton btObserverAddOneHour = new PushButton(msg.tabObserverTimeButtonCaptionAddOneHour());
	PushButton btObserverAddOneDay = new PushButton(msg.tabObserverTimeButtonCaptionAddOneDay());
	PushButton btObserverAddOneWeek = new PushButton(msg.tabObserverTimeButtonCaptionAddOneWeek());
	HorizontalPanel timeExplorerPanel = new HorizontalPanel();
	Label lbGreenwichHourOffset = new Label(msg.tabObserverGreenwhichOffsetInHours());
	TextBox txtGreenwichHourOffset = new TextBox();
	VerticalPanel observerPanel = new VerticalPanel();

	Panel visualizationPanel = new SimplePanel();

	PushButton btUpdateMap = new PushButton(msg.buttonCaptionUpdateMap());
	Label systemMessage = new Label("");

	StackLayoutPanel configurationPanel = new StackLayoutPanel(Unit.PX);
	VerticalPanel leftSubPanel = new VerticalPanel();
	SplitLayoutPanel leftPanel = new SplitLayoutPanel(0);
	VerticalPanel centerPanel = new VerticalPanel();
	TabLayoutPanel southPanel = new TabLayoutPanel(25, Unit.PX);
	
	private ApplicationBoard() {
		super(PANEL_SPLITTER_WIDTH);
	}
	
	public static ApplicationBoard buildApplicationBoard(DsoCatalogGWT source) {
		final ApplicationBoard appPanel = new ApplicationBoard();
		
		appPanel.application = source;
		
		// Language selection
		appPanel.languagePanel.add(appPanel.lbObserverLanguage);
		String currentLocale = "fr";
		{
			String pLocale = Window.Location.getParameter("locale");
			if (pLocale != null) {
				currentLocale = Window.Location.getParameter("locale");
			}
		}
		appPanel.hiddenObserverCurrentLanguage.setValue(currentLocale);
		appPanel.liObserverLanguage.addItem(msg.commonLanguageEnglish(), "en");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageSpanish(), "es");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageFrench(), "fr");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageRussian(), "ru");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageArab(), HasDirection.Direction.RTL, "ar");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageHindi(), "hi");
		appPanel.liObserverLanguage.addItem(msg.commonLanguageChinese(), "zh");
		if ("en".equals(currentLocale)) {
			appPanel.liObserverLanguage.setSelectedIndex(0);
		} else if ("es".equals(currentLocale)) {
			appPanel.liObserverLanguage.setSelectedIndex(1);
		} else if ("fr".equals(currentLocale)) {
			appPanel.liObserverLanguage.setSelectedIndex(2);
		} else if ("ru".equals(currentLocale)) {
			appPanel.liObserverLanguage.setSelectedIndex(3);
//		} else if ("ar".equals(currentLocale)) {
//			appPanel.liObserverLanguage.setSelectedIndex(4);
//		} else if ("hi".equals(currentLocale)) {
//			appPanel.liObserverLanguage.setSelectedIndex(5);
//		} else if ("zh".equals(currentLocale)) {
//			appPanel.liObserverLanguage.setSelectedIndex(6);
		}
		appPanel.liObserverLanguage.addChangeHandler(
			new UserLanguageUpdater(appPanel.liObserverLanguage, appPanel.hiddenObserverCurrentLanguage));
		appPanel.languagePanel.add(appPanel.liObserverLanguage);
		
		// We set the size of the stack panel.
		appPanel.configurationPanel.setPixelSize(LEFT_PANEL_WIDTH, LEFT_PANEL_HEIGHT);
		
		// We add the coordinates panel.
		appPanel.projectionPanel.setSize("270px", "30px");
		appPanel.projectionPanel.add(appPanel.lbCoordinatesMode);
		appPanel.liCoordinatesMode.addItem(msg.tabProjectionCoordinatesSystemEQ(), ""+CoordinatesSystem.EQ);
		appPanel.liCoordinatesMode.addItem(msg.tabProjectionCoordinatesSystemECL(), ""+CoordinatesSystem.ECL);
		appPanel.liCoordinatesMode.addItem(msg.tabProjectionCoordinatesSystemGAL(), ""+CoordinatesSystem.GAL);
		if (showAltAzProjectionOption) {
			appPanel.liCoordinatesMode.addItem(msg.tabProjectionCoordinatesSystemALTAZ(), ""+CoordinatesSystem.ALTAZ);
		}
		appPanel.liCoordinatesMode.addChangeHandler(new UpdateMapEventHandler(source));
		appPanel.liCoordinatesMode.addChangeHandler(new GoogleAnalyticsEventProjectionAware(appPanel.liCoordinatesMode));
		appPanel.projectionPanel.add(appPanel.liCoordinatesMode);
		
		if (showAltAzProjectionOption) {
			appPanel.chkShowObjectsUnderHorizon.addClickHandler(new UpdateMapEventHandler(source));
			appPanel.projectionPanel.add(appPanel.chkShowObjectsUnderHorizon);
		}
		appPanel.chkDisplayEcliptic.addClickHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayEcliptic.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Ecliptic"));
		appPanel.projectionPanel.add(appPanel.chkDisplayEcliptic);
		appPanel.chkDisplayEquator.addClickHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayEquator.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Equator"));
		appPanel.projectionPanel.add(appPanel.chkDisplayEquator);
		appPanel.chkDisplayGalacticPlan.addClickHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayGalacticPlan.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Galactic plan"));
		appPanel.projectionPanel.add(appPanel.chkDisplayGalacticPlan);

		// We add the coordinates panel to configuration panel.
		appPanel.configurationPanel.add(appPanel.projectionPanel, new Label(msg.tabProjectionTitle()), 28);
		
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
		appPanel.chkObserverUseComputerDateAndTime.addValueChangeHandler(new UseComputerDateAndTimeValueChangeHandler(source, appPanel));
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
		appPanel.configurationPanel.add(appPanel.observerPanel, new Label(msg.tabObserverTitle()), 28);
		
		// We put the planet display checkbox in the filter panel.
		appPanel.chkDisplayPlanets.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayPlanets.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display planets"));
		appPanel.filterPanel.add(appPanel.chkDisplayPlanets);
		
		// We put the constellation select list in the filter panel.
		appPanel.liConstellations.addChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.lbConstellation);
		appPanel.filterPanel.add(appPanel.liConstellations);
		appPanel.chkDisplayConstellationNames.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayConstellationNames.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display constellations names"));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationNames);
		appPanel.chkDisplayConstellationShapes.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayConstellationShapes.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display constellations shapes"));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationShapes);
		appPanel.chkDisplayConstellationBoundaries.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayConstellationBoundaries.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display constellations boundaries"));
		appPanel.filterPanel.add(appPanel.chkDisplayConstellationBoundaries);

		// We put the star magnitude filter field
		appPanel.chkDisplayStars.setValue(true);
		appPanel.chkDisplayStars.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayStars.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display stars"));
		appPanel.filterPanel.add(appPanel.chkDisplayStars);
		
		appPanel.filterPanel.add(appPanel.lbStarSpectralTypeRestriction);
		appPanel.txtStarSpectralTypeRestriction.addKeyUpHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.txtStarSpectralTypeRestriction);
		
		appPanel.filterPanel.add(appPanel.lbStarLimitMagnitude);
		appPanel.txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		appPanel.txtBoxStarLimitMagnitude.setTitle(msg.tabFiltersLimitMagnitudeStars());
		appPanel.txtBoxStarLimitMagnitude.setWidth("100px");
		appPanel.starLimitMagnitudePanel.add(appPanel.txtBoxStarLimitMagnitude);
		appPanel.btIncreaseStarLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxStarLimitMagnitude, +1));
		appPanel.btDecreaseStarLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxStarLimitMagnitude, -1));
		appPanel.starLimitMagnitudePanel.add(appPanel.btIncreaseStarLimitMagnitude);
		appPanel.starLimitMagnitudePanel.add(appPanel.btDecreaseStarLimitMagnitude);
		appPanel.filterPanel.add(appPanel.starLimitMagnitudePanel);
		appPanel.filterPanel.add(new HTML("<hr/>"));
		
		// We set the DSO filter fields
		appPanel.chkDisplayAsterisms.setValue(false);
		appPanel.chkDisplayAsterisms.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayAsterisms.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display asterisms"));
		appPanel.filterPanel.add(appPanel.chkDisplayAsterisms);
		
		appPanel.chkDisplayGalaxies.setValue(false);
		appPanel.chkDisplayGalaxies.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayGalaxies.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display galaxies"));
		appPanel.filterPanel.add(appPanel.chkDisplayGalaxies);
		
		appPanel.chkDisplayGlobularClusters.setValue(false);
		appPanel.chkDisplayGlobularClusters.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayGlobularClusters.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display globular clusters"));
		appPanel.filterPanel.add(appPanel.chkDisplayGlobularClusters);
		
		appPanel.chkDisplayOpenClusters.setValue(false);
		appPanel.chkDisplayOpenClusters.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayOpenClusters.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display open clusters"));
		appPanel.filterPanel.add(appPanel.chkDisplayOpenClusters);
		
		appPanel.chkDisplayPlanetaryNebulas.setValue(false);
		appPanel.chkDisplayPlanetaryNebulas.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayPlanetaryNebulas.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display planetary nebulas"));
		appPanel.filterPanel.add(appPanel.chkDisplayPlanetaryNebulas);
		
		appPanel.chkDisplayNebulas.setValue(false);
		appPanel.chkDisplayNebulas.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayNebulas.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display nebulas"));
		appPanel.filterPanel.add(appPanel.chkDisplayNebulas);
		
		appPanel.chkDisplaySupernovaRemnants.setValue(false);
		appPanel.chkDisplaySupernovaRemnants.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplaySupernovaRemnants.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display SN Remnants"));
		appPanel.filterPanel.add(appPanel.chkDisplaySupernovaRemnants);
		
		appPanel.chkDisplayQuasars.setValue(false);
		appPanel.chkDisplayQuasars.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.chkDisplayQuasars.addValueChangeHandler(new GoogleAnalyticsEventFilterAware("Display quasars"));
		appPanel.filterPanel.add(appPanel.chkDisplayQuasars);
		
		appPanel.filterPanel.add(appPanel.lbDsoSubtypeRestriction);
		appPanel.txtDsoSubtypeRestriction.addKeyUpHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.txtDsoSubtypeRestriction);
		
		appPanel.filterPanel.add(appPanel.lbDsoLimitMagnitude);
		appPanel.txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		appPanel.txtBoxDsoLimitMagnitude.setTitle(msg.tabFiltersLimitMagnitudeDso());
		appPanel.txtBoxDsoLimitMagnitude.setWidth("100px");
		appPanel.dsoLimitMagnitudePanel.add(appPanel.txtBoxDsoLimitMagnitude);
		appPanel.btIncreaseDsoLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxDsoLimitMagnitude, +1));
		appPanel.btDecreaseDsoLimitMagnitude.addClickHandler(new ChangeLimitMagnitudeClickEventHandler(source, appPanel.txtBoxDsoLimitMagnitude, -1));
		appPanel.dsoLimitMagnitudePanel.add(appPanel.btIncreaseDsoLimitMagnitude);
		appPanel.dsoLimitMagnitudePanel.add(appPanel.btDecreaseDsoLimitMagnitude);
		appPanel.filterPanel.add(appPanel.dsoLimitMagnitudePanel);

		appPanel.chkDsoInMessierCatalog.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDsoInMessierCatalog);
		appPanel.chkDsoInBestNgcCatalog.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDsoInBestNgcCatalog);
		appPanel.chkDsoInCaldwellCatalog.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDsoInCaldwellCatalog);
		appPanel.chkDsoInHerschelCatalog.addValueChangeHandler(new UpdateMapEventHandler(source));
		appPanel.filterPanel.add(appPanel.chkDsoInHerschelCatalog);

		// We add the filter panel to configuration panel.
		appPanel.configurationPanel.add(appPanel.filterPanel, new Label(msg.tabFiltersTitle()), 28);
		appPanel.configurationPanel.showWidget(appPanel.filterPanel);
		
		appPanel.leftSubPanel.add(appPanel.configurationPanel);

		// We set the button which will trigger the map's display.
		appPanel.btUpdateMap.addClickHandler(new UpdateMapEventHandler(source));
		appPanel.leftSubPanel.add(appPanel.btUpdateMap);
		
		// We put a label that will be used to give feedback to the user.
		appPanel.centerPanel.add(appPanel.systemMessage);
		
		// We add the panel that will be used to display the chart.
		appPanel.centerPanel.add(appPanel.visualizationPanel);
		
		// We place the left and the right panels in the main one.
		appPanel.leftPanel.addSouth(appPanel.languagePanel, 30);
		appPanel.leftPanel.add(appPanel.leftSubPanel);
		appPanel.addWest(appPanel.leftPanel, LEFT_PANEL_WIDTH);
		appPanel.addSouth(appPanel.southPanel, SOUTH_PANEL_HEIGHT);
		appPanel.add(appPanel.centerPanel);
		
		return appPanel;
	}
	
	public CatalogSearchOptions getCatalogSearchOptions() {
		CatalogSearchOptions options = new CatalogSearchOptions();
		options.setCoordinatesSystem(CoordinatesSystem.valueOf(liCoordinatesMode.getValue(liCoordinatesMode.getSelectedIndex())));
		options.setRestrictedToConstellationCode(liConstellations.getValue(liConstellations.getSelectedIndex()));
		options.setFindPlanets(chkDisplayPlanets.getValue());
		options.setDisplayEcliptic(chkDisplayEcliptic.getValue());
		options.setDisplayEquator(chkDisplayEquator.getValue());
		options.setDisplayGalacticPlan(chkDisplayGalacticPlan.getValue());
		options.setDisplayConstellationNames(chkDisplayConstellationNames.getValue());
		options.setDisplayConstellationShape(chkDisplayConstellationShapes.getValue());
		options.setDisplayConstellationBoundaries(chkDisplayConstellationBoundaries.getValue());
		Double starLimitMagnitude = CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE;
		try {
			starLimitMagnitude = Double.parseDouble(txtBoxStarLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			systemMessage.setText(msg.messageYouMustIndicateLimitMagnitudeValue());
			txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			systemMessage
					.setText(msg.messagePleaseCheckFormatLimitMagnitudeValue());
			txtBoxStarLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setStarLimitMagnitude(starLimitMagnitude);

		Double dsoLimitMagnitude = CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE;
		try {
			dsoLimitMagnitude = Double.parseDouble(txtBoxDsoLimitMagnitude
					.getText());
		} catch (NullPointerException ex) {
			systemMessage.setText(msg.messageYouMustIndicateLimitMagnitudeValue());
			txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_DSO_LIMIT_MAGNITUDE);
		} catch (NumberFormatException ex) {
			systemMessage
					.setText(msg.messagePleaseCheckFormatLimitMagnitudeValue());
			txtBoxDsoLimitMagnitude.setText("" + CatalogSearchOptions.DEFAULT_STAR_LIMIT_MAGNITUDE);
		}
		options.setDsoLimitMagnitude(dsoLimitMagnitude);
		
		options.setFindStars(chkDisplayStars.getValue());
		options.setSpectralTypeRestriction(txtStarSpectralTypeRestriction.getValue());
		options.setFindAsterisms(chkDisplayAsterisms.getValue());
		options.setFindGalaxies(chkDisplayGalaxies.getValue());
		options.setFindGlobularClusters(chkDisplayGlobularClusters.getValue());
		options.setFindOpenClusters(chkDisplayOpenClusters.getValue());
		options.setFindPlanetaryNebulas(chkDisplayPlanetaryNebulas.getValue());
		options.setFindNebulas(chkDisplayNebulas.getValue());
		options.setFindSupernovaRemnant(chkDisplaySupernovaRemnants.getValue());
		options.setFindQuasars(chkDisplayQuasars.getValue());
		options.setDsoSubtypeRestriction(txtDsoSubtypeRestriction.getValue());
		options.setDsoInMessierCatalog(chkDsoInMessierCatalog.getValue());
		options.setDsoInBestNgcCatalog(chkDsoInBestNgcCatalog.getValue());
		options.setDsoInCaldwellCatalog(chkDsoInCaldwellCatalog.getValue());
		options.setDsoInHerschelCatalog(chkDsoInHerschelCatalog.getValue());
		Observer observer = application.initializeObserver();
		options.setObserverCurrentDateAsString(txtObserverDate.getText());
		options.setObserverCurrentTimeAsString(txtObserverLocalTime.getText());
		options.setObserverGreenwhichHourOffset(Integer.parseInt(txtGreenwichHourOffset.getText()));
		options.setObservatoryLatitude(observer.getLatitude());
		options.setObservatoryLongitude(observer.getLongitude());
		return options;
	}
	
	protected void removeObjectDetails() {
		southPanel.clear();
	}
}