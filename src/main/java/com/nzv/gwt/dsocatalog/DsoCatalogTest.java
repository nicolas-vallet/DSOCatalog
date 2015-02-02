package com.nzv.gwt.dsocatalog;

import java.math.BigDecimal;
import java.math.MathContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Star;


public class DsoCatalogTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		Star s = catalogService.findStarByHrNumber(1708);
		System.out.println("RA="+s.getRightAscension());
		System.out.println("DEC="+s.getDeclinaison());
		
		MathContext round = new MathContext(5);
		Sexagesimal ra = new Sexagesimal(BigDecimal.valueOf(s.getRightAscension() / 15).round(round).doubleValue());
		Sexagesimal dec = new Sexagesimal(BigDecimal.valueOf(s.getDeclinaison()).round(round).doubleValue());
		System.out.println(ra.toString(SexagesimalType.HOURS));
		System.out.println(dec.toString(SexagesimalType.DEGREES));
	}
}
