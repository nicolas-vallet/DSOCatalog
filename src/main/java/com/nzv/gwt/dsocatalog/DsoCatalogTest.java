package com.nzv.gwt.dsocatalog;

import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;
import com.nzv.gwt.dsocatalog.projection.StereographicProjection;


public class DsoCatalogTest {

	public static void main(String[] args) {
//		@SuppressWarnings("resource")
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
//		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		Point2D pointHorizonEst = new Point2D(90.0, 0.0);
		Point2D pointHorizonNord = new Point2D(0.0, 0.0);
		Point2D pointHorizonSud = new Point2D(180.0, 0.0);
		Point2D pointHorizonOuest = new Point2D(270.0, 0.0);
		Point2D pointZenith = new Point2D(0.0, 90.0);
		
		Point2D[] points = new Point2D[]{pointHorizonEst, pointHorizonSud, pointHorizonOuest, pointHorizonNord, pointZenith};
		
		Projection projection = new StereographicProjection();
		Double minX = null;
		Double maxX = null;
		Double minY = null;
		Double maxY = null;
		for (Point2D p : points) {
			Point2D projectedPoint = projection.project(Math.toRadians(p.getX()), Math.toRadians(p.getY()));
			if (minX == null || minX > Math.toDegrees(projectedPoint.getX())) {
				minX = Math.toDegrees(projectedPoint.getX());
			}
			if (minY == null || minY > Math.toDegrees(projectedPoint.getY())) {
				minY = Math.toDegrees(projectedPoint.getY());
			}
			if (maxX == null || maxX < Math.toDegrees(projectedPoint.getX())) {
				maxX = Math.toDegrees(projectedPoint.getX());
			}
			if (maxY == null || maxY < Math.toDegrees(projectedPoint.getY())) {
				maxY = Math.toDegrees(projectedPoint.getY());
			}
			
		}
		System.out.println("minX="+minX+"° / "+Math.toRadians(minX)+" rad");
		System.out.println("maxX="+maxX+"° / "+Math.toRadians(maxX)+" rad");
		System.out.println("minY="+minY+"° / "+Math.toRadians(minY)+" rad");
		System.out.println("maxY="+maxY+"° / "+Math.toRadians(maxY)+" rad");
	}
}
