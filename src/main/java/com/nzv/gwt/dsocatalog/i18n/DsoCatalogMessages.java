package com.nzv.gwt.dsocatalog.i18n;

import com.google.gwt.i18n.client.Constants;

public interface DsoCatalogMessages extends Constants {
	String commonYes();
	String commonNo();
	String commonLanguageFrench();
	String commonLanguageEnglish();
	String commonLanguageSpanish();
	String commonEcliptic();
	String commonEquator();
	String commonGalacticPlan();
	String commonHorizon();
	String commonConstellation();
	String commonStars();
	String commonPlanets();
	
	String tabProjectionTitle();
	String tabProjectionCoordinatesSystem();
	String tabProjectionCoordinatesSystemEQ();
	String tabProjectionCoordinatesSystemECL();
	String tabProjectionCoordinatesSystemGAL();
	String tabProjectionCoordinatesSystemALTAZ();
	String tabProjectionDisplayObjectUnderHorizon();
	String tabProjectionDisplayEcliptic();
	String tabProjectionDisplayEquator();
	String tabProjectionDisplayGalacticPlan();
	
	String tabObserverTitle();
	String tabObserverLanguage();
	String tabObserverLanguageConfirmChange();
	String tabObserverLatitude();
	String tabObserverLongitude();
	String tabObserverDate();
	String tabObserverDateFormat();
	String tabObserverUseSystemCurrentTime();
	String tabObserverLocalTime();
	String tabObserverTimeFormat();
	String tabObserverGreenwhichOffsetInHours();
	String tabObserverTimeButtonCaptionAddOneWeek();
	String tabObserverTimeButtonCaptionAddOneDay();
	String tabObserverTimeButtonCaptionAddOneHour();
	String tabObserverTimeButtonCaptionRemoveOneHour();
	String tabObserverTimeButtonCaptionRemoveOneDay();
	String tabObserverTimeButtonCaptionRemoveOneWeek();
	
	String tabFiltersTitle();
	String tabFiltersDisplayPlanets();
	String tabFiltersConstellation();
	String tabFiltersConstellationUnset();
	String tabFiltersDisplayConstellationsNames();
	String tabFiltersDisplayConstellationsShapes();
	String tabFiltersDisplayConstellationsBoundaries();
	String tabFiltersDisplayStars();
	String tabFiltersLimitMagnitudeStars();
	String tabFiltersDisplayAsterisms();
	String tabFiltersDisplayGalaxies();
	String tabFiltersDisplayGlobularCluster();
	String tabFiltersDisplayOpenCluster();
	String tabFiltersDisplayPlanetaryNebulas();
	String tabFiltersDisplayNebulas();
	String tabFiltersDisplaySnRemnants();
	String tabFiltersDisplayQuasars();
	String tabFiltersLimitMagnitudeDso();
	
	String messageRenderingMap();
	String messageObjectsDisplayed();
	String messageYouMustIndicateLimitMagnitudeValue();
	String messagePleaseCheckFormatLimitMagnitudeValue();
	String messageUnableToLoadConstellationList();
	String messageUnableToFetchDetailsAbout();
	
	String mapEqHaxisTitle();
	String mapEqVaxisTitle();
	String mapEclHaxisTitle();
	String mapEclVaxisTitle();
	String mapGalHaxisTitle();
	String mapGalVaxisTitle();
	String mapAltazHaxisTitle();
	String mapAltazVaxisTitle();
	String mapConstellationBoundaries();
	String mapConstellationShapes();
	String mapTooltipEclipticLongitude();
	String mapTooltipEclipticLatitude();
	String mapTooltipGalacticLongitude();
	String mapTooltipGalacticLatitude();
	String mapTooltipAzimuth();
	String mapTooltipElevation();
	String mapTooltipRightAscension();
	String mapTooltipDeclinaison();
	
	String detailsTabIdentifiersTitle();
	String detailsTabIdentifiersPlanet();
	String detailsTabIdentifiersHrNumber();
	String detailsTabIdentifiersHdNumber();
	String detailsTabIdentifiersSaoNumber();
	String detailsTabIdentifiersName();
	String detailsTabIdentifiersOtherName();
	String detailsTabIdentifiersFk5Number();
	String detailsTabIdentifiersDurchmusterungId();
	String detailsTabIdentifiersAdsNumber();
	String detailsTabIdentifiersVariableId();
	
	String detailsTabCoordinatesTitle();
	String detailsTabCoordinatesRightAscension();
	String detailsTabCoordinatesDeclinaison();
	String detailsTabCoordinatesEclipticLongitude();
	String detailsTabCoordinatesEclipticLatitude();
	String detailsTabCoordinatesGalacticLongitude();
	String detailsTabCoordinatesGalacticLatitude();
	String detailsTabCoordinatesConstellation();
	
	String detailsTabAspectTitle();
	String detailsTabAspectAlbedo();
	String detailsTabAspectSpectralType();
	String detailsTabAspectMagnitude();
	String detailsTabAspectMagnitudeUncertain();
	String detailsTabAspectMagnitudeBV();
	String detailsTabAspectMagnitudeUB();
	String detailsTabAspectMagnitudeRI();
	String detailsTabAspectIrSource();
	String detailsTabAspectSurfaceBrightness();
	String detailsTabAspectDimension();
	String detailsTabAspectOrientation();
	
	String detailsTabSpecificitiesTitle();
	String detailsTabSpecificitiesProperMotionRa();
	String detailsTabSpecificitiesProperMotionDec();
	String detailsTabSpecificitiesRadialSpeed();
	String detailsTabSpecificitiesParallaxe();
	String detailsTabSpecificitiesParallaxeCode();
	String detailsTabSpecificitiesCompanionCount();
	String detailsTabSpecificitiesMultipleStarCode();
	String detailsTabSpecificitiesIdCompanionStar();
	String detailsTabSpecificitiesSeparation();
	String detailsTabSpecificitiesMagnitudeDifferenceWithCompanion();
	String detailsTabSpecificitiesType();
	String detailsTabSpecificitiesSubtype();
	String detailsTabSpecificitiesStarCount();
	String detailsTabSpecificitiesBrightestStarMagnitude();
	String detailsTabSpecificitiesInBestofNgcCatalog();
	String detailsTabSpecificitiesInCaldwellCatalog();
	String detailsTabSpecificitiesInHerschelCatalog();
	String detailsTabSpecificitiesInMessierCatalog();
	String detailsTabSpecificitiesNgcDescription();
	String detailsTabSpecificitiesNotes();
	
	String detailsTabExternalResourcesTitle();
	
	String detailsTabImagesTitle();
	String detailsTabImagesSurveys();
	String detailsTabImagesSurveysPoss2ukstuRed();
	String detailsTabImagesSurveysPoss2ukstuBlue();
	String detailsTabImagesSurveysPoss2ukstuIr();
	String detailsTabImagesSurveysPoss1Red();
	String detailsTabImagesSurveysPoss1Blue();
	String detailsTabImagesSurveysQuickv();
	String detailsTabImagesSurveysPhase2Gsc2();
	String detailsTabImagesSurveysPhase2Gsc1();
	String detailsTabImagesSurveysSloan();
	String detailsTabImagesSurveysSloanInverted();
	
	String buttonCaptionUpdateMap();
}
