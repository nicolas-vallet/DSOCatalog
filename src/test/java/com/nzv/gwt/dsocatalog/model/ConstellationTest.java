package com.nzv.gwt.dsocatalog.model;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class ConstellationTest {

	@Test
	public void boundingBoxOfconstellationSpreadBeetweenRa0AndRa360() {
		ArrayList<ConstellationBoundaryPoint> boundaryPoints = new ArrayList<ConstellationBoundaryPoint>();
		boundaryPoints.add(new ConstellationBoundaryPoint(30, 30));
		boundaryPoints.add(new ConstellationBoundaryPoint(30, 0));
		boundaryPoints.add(new ConstellationBoundaryPoint(20, 0));
		boundaryPoints.add(new ConstellationBoundaryPoint(20, -20));
		boundaryPoints.add(new ConstellationBoundaryPoint(40, -20));
		boundaryPoints.add(new ConstellationBoundaryPoint(40, -40));
		boundaryPoints.add(new ConstellationBoundaryPoint(80, -40));
		boundaryPoints.add(new ConstellationBoundaryPoint(80, -10));
		boundaryPoints.add(new ConstellationBoundaryPoint(100, -10));
		boundaryPoints.add(new ConstellationBoundaryPoint(100, 20));
		boundaryPoints.add(new ConstellationBoundaryPoint(70, 20));
		boundaryPoints.add(new ConstellationBoundaryPoint(70, 30));
		Constellation constellation = new Constellation();
		constellation.setBoundaryPoints(boundaryPoints);
		constellation.setCenterRightAscension(50.0);
		constellation.setCenterDeclinaison(0.0);
		Assert.assertEquals(100, constellation.getUpperWesternMapLimit().getRightAscension(), 0);
		Assert.assertEquals(30, constellation.getUpperWesternMapLimit().getDeclinaison(), 0);
		Assert.assertEquals(20, constellation.getLowerEasternMapLimit().getRightAscension(), 0);
		Assert.assertEquals(-40, constellation.getLowerEasternMapLimit().getDeclinaison(), 0);
	}

	@Test
	public void boundingBoxOfConstellationSpreadOverRa0() {
		ArrayList<ConstellationBoundaryPoint> boundaryPoints = new ArrayList<ConstellationBoundaryPoint>();
		boundaryPoints.add(new ConstellationBoundaryPoint(350, 30));
		boundaryPoints.add(new ConstellationBoundaryPoint(350, 0));
		boundaryPoints.add(new ConstellationBoundaryPoint(345, 0));
		boundaryPoints.add(new ConstellationBoundaryPoint(345, -20));
		boundaryPoints.add(new ConstellationBoundaryPoint(355, -20));
		boundaryPoints.add(new ConstellationBoundaryPoint(355, -40));
		boundaryPoints.add(new ConstellationBoundaryPoint(15, -40));
		boundaryPoints.add(new ConstellationBoundaryPoint(15, -10));
		boundaryPoints.add(new ConstellationBoundaryPoint(25, -10));
		boundaryPoints.add(new ConstellationBoundaryPoint(25, 20));
		boundaryPoints.add(new ConstellationBoundaryPoint(10, 20));
		boundaryPoints.add(new ConstellationBoundaryPoint(10, 30));
		Constellation constellation = new Constellation();
		constellation.setBoundaryPoints(boundaryPoints);
		constellation.setCenterRightAscension(0.0);
		constellation.setCenterDeclinaison(0.0);
		Assert.assertEquals(25, constellation.getUpperWesternMapLimit().getRightAscension(), 0);
		Assert.assertEquals(30, constellation.getUpperWesternMapLimit().getDeclinaison(), 0);
		Assert.assertEquals(345, constellation.getLowerEasternMapLimit().getRightAscension(), 0);
		Assert.assertEquals(-40, constellation.getLowerEasternMapLimit().getDeclinaison(), 0);
	}
	
}
