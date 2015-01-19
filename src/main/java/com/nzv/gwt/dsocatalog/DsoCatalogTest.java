package com.nzv.gwt.dsocatalog;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");

		PublicCatalogService catalogService = ctx
				.getBean(PublicCatalogService.class);
		for (Constellation constellation : catalogService.findAllConstellations()) {
			System.out.println(constellation.getName()+"'s shape has "+constellation.getShapeLines().size()+" line(s)");
		}
	}
}
