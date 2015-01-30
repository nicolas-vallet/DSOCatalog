package com.nzv.gwt.dsocatalog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.astro.ephemeris.coordinate.adapter.EquatorialCoordinatesAdapter;
import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.Point2D;


public class DsoCatalogTest {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "jpaContext.xml");
		PublicCatalogService catalogService = (PublicCatalogService) ctx.getBean(PublicCatalogService.class);
		
		List<CoordinatesSystem> css = Arrays.asList(new CoordinatesSystem[]{CoordinatesSystem.EQ, CoordinatesSystem.ECL, CoordinatesSystem.GAL});
		
		List<Constellation> constellations = catalogService.findAllConstellations();
		for (CoordinatesSystem cs : css) {
			for (Constellation constellation : constellations) {
				for (ConstellationShapeLine line : constellation.getShapeLines()) {
					EquatorialCoordinatesAdapter ecaStart = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(line.getStartRightAscension(), line.getStartDeclinaison()));
					EquatorialCoordinatesAdapter ecaEnd = new EquatorialCoordinatesAdapter(new EquatorialCoordinates(line.getEndRightAscension(), line.getEndDeclinaison()));
					
					double startX, endX;
					double startY, endY;
					switch(cs) {
					case ECL:
						startX = ecaStart.getEcliptiqueLongitude();
						startY = ecaStart.getEcliptiqueLatitude();
						endX = ecaEnd.getEcliptiqueLongitude();
						endY = ecaEnd.getEcliptiqueLatitude();
						break;
					case GAL:
						startX = GeometryUtils.normalizeAngleInDegrees(ecaStart.getGalacticLongitude(), cs.getChartMinValueX(), cs.getChartMaxValueX());
						startY = ecaStart.getGalacticLatitude();
						endX = GeometryUtils.normalizeAngleInDegrees(ecaEnd.getGalacticLongitude(), cs.getChartMinValueX(), cs.getChartMaxValueX());
						endY = ecaEnd.getGalacticLatitude();
						break;
					case EQ:
					default:
						startX = ecaStart.getEquatorialCoordinates().getRightAscension();
						startY = ecaStart.getEquatorialCoordinates().getDeclinaison();
						endX = ecaEnd.getEquatorialCoordinates().getRightAscension();
						endY = ecaEnd.getEquatorialCoordinates().getDeclinaison();
						break;
					}
					
					boolean cross = GeometryUtils.isCrossingChartLimitX(startX, endX, cs);
					if (!cross) continue;
					Point2D pStart = new Point2D(startX, startY);
					Point2D pEnd = new Point2D(endX, endY);
					Point2D intermediatePoint = GeometryUtils.giveIntermediatePointOnChartLimit(pStart, pEnd, cs);
					System.out.println("["+cs+"]. In ["+constellation.getCode()+"], line id["+line.getId()+"] "+(cross ? " crosses " : " does not cross ")+" the boundary => start="+pStart+" / end="+pEnd);
					System.out.println("iPoint="+intermediatePoint);
				}
			}
		}
	}
}
