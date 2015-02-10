package com.nzv.gwt.dsocatalog;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.repository.StarRepository;

public class DsoCatalogTest {

	public static void main(String[] args) {
		
//		String zae = "G2*";
//		System.out.println(zae.replace("*", "%"));
//		System.exit(0);

		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml",
				"jpaContext.xml");

		StarRepository starRep = (StarRepository) ctx.getBean(StarRepository.class);
		PublicCatalogService catalogService = (PublicCatalogService) ctx
				.getBean(PublicCatalogService.class);

		Constellation c = catalogService.findConstellationByCode("VIR");
		EquatorialCoordinates upperWesterSearchLimit = c.getUpperWesternMapLimit();
		EquatorialCoordinates lowerEasterSearchLimit = c.getLowerEasternMapLimit();

		List<Star> stars = starRep
				.findByEQCoordinatesAreaAndVisualMagnitudeLessThanAndSpectralTypeLike(
						upperWesterSearchLimit.getRightAscension(),
						upperWesterSearchLimit.getDeclinaison(),
						lowerEasterSearchLimit.getRightAscension(),
						lowerEasterSearchLimit.getDeclinaison(), 8.0, "G3%");

		for (Star s : stars) {
			System.out.println(s+" : "+s.getSpectralType());
		}
		System.out.println("Found "+stars.size()+" candidate(s).");
		
	}
}
