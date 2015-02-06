package com.nzv.gwt.dsocatalog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;


public class DsoCatalogTest {

	public static void main(String[] args) {
		
		System.out.println(true && false || false);
		System.exit(0);
		
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		DeepSkyObject dso = catalogService.findObjectByName("NGC 5214");
		Sexagesimal ra = Sexagesimal.decimalToSexagesimal(dso.getRightAscension());
		Sexagesimal dec = Sexagesimal.decimalToSexagesimal(dso.getDeclinaison());
		
		System.out.println("RA="+ra.getValueAsUnits());
		System.out.println("DEC="+dec.getValueAsUnits());
	}
}
