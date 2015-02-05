package com.nzv.gwt.dsocatalog;

import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;
import com.nzv.gwt.dsocatalog.projection.StereographicProjection;


public class DsoCatalogTest {

	public static void main(String[] args) {
//		@SuppressWarnings("resource")
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
//		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);

		double widthInArcmin = (int) Math.ceil(1.2 * ((1.0 / 60) * 122));
		System.out.println(widthInArcmin);
	}
}
