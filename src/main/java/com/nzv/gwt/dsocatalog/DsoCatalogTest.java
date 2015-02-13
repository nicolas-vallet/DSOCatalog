package com.nzv.gwt.dsocatalog;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.coordinate.GeographicCoordinates;
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.CatalogSearchOptions;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.date.DateComputation;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.OrthographicProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;

public class DsoCatalogTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		CatalogSearchOptions options = CatalogSearchOptions.createOptionsEveryTypes();
		options.setFindPlanets(false);
		options.setFindStars(false);
		options.setRestrictedToConstellationCode("UMA");
		options.setDsoLimitMagnitude(16.0);
		Set<AstroObject> dsos = catalogService.findObjectBrighterThan(options);
		
		for (AstroObject ao : dsos) {
			if (ao instanceof DeepSkyObject) {
				DeepSkyObject dso = (DeepSkyObject) ao;
				System.out.println(dso.getInCatalogs()+" : "+dso.getName()+" ("+dso.getOtherName()+")");
				
			}
		}
	}
}
