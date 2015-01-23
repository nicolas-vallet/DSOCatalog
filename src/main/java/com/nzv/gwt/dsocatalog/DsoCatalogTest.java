package com.nzv.gwt.dsocatalog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");

		PublicCatalogService catalogService = ctx
				.getBean(PublicCatalogService.class);
		
		DeepSkyObject dso = catalogService.findObjectById(4901);
		System.out.println(dso);
		
//		double ra = dso.getRightAscension();
		System.out.println(dso.getRightAscension());
		Sexagesimal ra = new Sexagesimal(dso.getRightAscension() / 15);
		System.out.println(ra.toString(SexagesimalType.HOURS));
	}
}
