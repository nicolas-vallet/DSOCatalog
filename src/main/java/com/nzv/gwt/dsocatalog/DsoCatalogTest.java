package com.nzv.gwt.dsocatalog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.projection.MercatorProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");
		
		PublicCatalogService catalogService = ctx
				.getBean(PublicCatalogService.class);
		
		Star etoilePolaire = catalogService.findStarBySaoNumber(308);
		System.out.println("DEC="+etoilePolaire.getDeclinaison());
		
		Projection mercator = new MercatorProjection();
		Point2D p = mercator.project(Math.toRadians(etoilePolaire.getRightAscension()), Math.toRadians(etoilePolaire.getDeclinaison()));
		System.out.println("X="+Math.toDegrees(p.getX()));
		System.out.println("Y="+Math.toDegrees(p.getY()));
	}
}
