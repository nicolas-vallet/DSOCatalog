package com.nzv.gwt.dsocatalog;

import static java.lang.Math.atan2;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.CatalogSearchOptions;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) {
		
//		@SuppressWarnings("resource")
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(
//				"applicationContext.xml", "jpaContext.xml");
//		
//		PublicCatalogService catalogService = ctx
//				.getBean(PublicCatalogService.class);
		
		EquatorialCoordinates ec = new EquatorialCoordinates(Sexagesimal.sexagesimalToDecimal(new Sexagesimal(13*15, 42*15, 12*60)), Sexagesimal.sexagesimalToDecimal(new Sexagesimal(28, 23, 0)));
		double term1 = sin(toRadians(192.25d - ec.getRightAscension()));
		double term2 = cos(toRadians(192.25d - ec.getRightAscension())) * sin(toRadians(27.4d));
		double term3 = tan(toRadians(ec.getDeclinaison())) * cos(toRadians(27.4d));
//		double x = toDegrees(atan2(term1, (term2 - term3)));
		System.out.println("term1="+term1);
		System.out.println("term2="+term2);
		System.out.println("term3="+term3);
		System.out.println("term2-term3="+(term2-term3));
		System.out.println("term1 / (term2 - term3)="+(term1 / (term2 - term3)));
		double x = toDegrees(atan(term1 / (term2 - term3)));
		double l = 303 - x;
		System.out.println("Longitude="+(l % 360));
		
		
	}
}
