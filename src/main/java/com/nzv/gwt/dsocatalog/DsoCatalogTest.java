package com.nzv.gwt.dsocatalog;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.coordinate.GeographicCoordinates;
import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.date.DateComputation;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.OrthographicProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;

public class DsoCatalogTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);

		Constellation constellation = catalogService.findConstellationByCode("AUR");
		Set<ConstellationShapeLine> shapeLines = constellation.getShapeLines();
		
		GeographicCoordinates observatory = new GeographicCoordinates(48.833, 2.333);
		double jd = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(2015.0212));
		Sexagesimal T = new Sexagesimal(14, 22, 15);
		double gst = DateComputation.getMeanSiderealTimeAsHoursFromJulianDay(jd, T);
		
		for (ConstellationShapeLine l : shapeLines) {
			// Start
			EquatorialCoordinatesAdapter lineStart = new EquatorialCoordinatesAdapter(
					new EquatorialCoordinates(l.getStartRightAscension(), l.getStartDeclinaison()));
			// End
			EquatorialCoordinatesAdapter lineEnd = new EquatorialCoordinatesAdapter(
					new EquatorialCoordinates(l.getEndRightAscension(), l.getEndDeclinaison()));
			
			double startX = lineStart.getAzimuth(observatory, gst);
			double startY = lineStart.getElevation(observatory, gst);
			double endX = lineEnd.getAzimuth(observatory, gst);
			double endY = lineEnd.getElevation(observatory, gst);
			
			if (startY < 0 || endY < 0) {
				Point2D pointOfSegmentOnTheHorizon = 
						GeometryUtils.giveIntermediatePointOnHorizon(new Point2D(startX, startY), new Point2D(endX, endY));
				if (pointOfSegmentOnTheHorizon == null) {
					continue;
				}
				if (startY < 0) {
					startX = pointOfSegmentOnTheHorizon.getX();
					startY = pointOfSegmentOnTheHorizon.getY();
				} else if (endY < 0) {
					endX = pointOfSegmentOnTheHorizon.getX();
					endY = pointOfSegmentOnTheHorizon.getY();
				}
			}
			
			System.out.println("Start=" + GeometryUtils.normalizeAngleInDegrees(startX, 0, 360) + " / " + startY);
			System.out.println("End  =" + GeometryUtils.normalizeAngleInDegrees(endX, 0, 360) + " / " + endY);
			System.out.println("===========================================");
		}

//			double jd1 = DateComputation.getJulianDayFromDateAsDouble(Double.valueOf(2015.0212));
//			System.out.println("jd1="+jd1);
//			Sexagesimal T = new Sexagesimal(15-1, 51, 0);
//			double gst1 = DateComputation.getMeanSiderealTimeAsHoursFromJulianDay(jd1, T);
//			System.out.println("gst1="+gst1);
//			
//			
//			DateTime dt = new DateTime(2015, 2, 12, 15, 51, 0, DateTimeZone.forOffsetHours(+1));
//			double jd2 = DateTimeUtils.toJulianDay(dt.getMillis());
//			System.out.println("jd2="+jd2);
	}
}
