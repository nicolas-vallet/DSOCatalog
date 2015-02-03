package com.nzv.gwt.dsocatalog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;


public class DsoCatalogTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		Constellation c = catalogService.findConstellationByCode("UMA");
		EquatorialCoordinates uw = c.getUpperWesternMapLimit();
		EquatorialCoordinates le = c.getLowerEasternMapLimit();
		
		
		System.out.println("UW : RA="+uw.getRightAscension()+" / DEC="+uw.getDeclinaison());
		System.out.println("LE : RA="+le.getRightAscension()+" / DEC="+le.getDeclinaison());
		
		int windowWidth = 500;
		int windowHeight = 200;
		double windowWidthHeightRatio = windowWidth / windowHeight;
		
		double constellationWidth = 
				GeometryUtils.getAngleDifference(uw.getRightAscension(), le.getRightAscension());
		double constellationHeight =
				GeometryUtils.getAngleDifference(uw.getDeclinaison(), le.getDeclinaison());
		double chartWidthHeightRatio = constellationWidth / constellationHeight;
		
		System.out.println("ratio W:H = "+chartWidthHeightRatio);
		
		double w, h;
		if (chartWidthHeightRatio > windowWidthHeightRatio) {
			w = windowWidth;
			h = w / chartWidthHeightRatio;
		} else {
			h = windowHeight;
			w = h * chartWidthHeightRatio;
		}
		System.out.println("W="+w);
		System.out.println("H="+h);
	}
}
