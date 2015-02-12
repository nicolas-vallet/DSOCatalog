package com.nzv.gwt.dsocatalog.projection;


import org.junit.Assert;
import org.junit.Test;

public class GeometryUtilsTest {
	
	@Test
	public void testFarNegativeAngleNormalization() {
		double a = -1130;
		Assert.assertEquals(-50, GeometryUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		Assert.assertEquals(310, GeometryUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
	}
	
	@Test
	public void testFarPositiveAngleNormalization() {
		double a = 1485;
		Assert.assertEquals(45, GeometryUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
		Assert.assertEquals(45, GeometryUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		
		a = 1640;
		Assert.assertEquals(200, GeometryUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		Assert.assertEquals(-160, GeometryUtils.normalizeAngleInDegrees(a, -180, 180), 0);
	}
	
	@Test
	public void testNormalizationBetweenMinus90And90() {
		double a = 100;
		Assert.assertEquals(-80, GeometryUtils.normalizeAngleInDegrees(a, -90, 90), 0);
		Assert.assertEquals(20, GeometryUtils.normalizeAngleInDegrees(200, -90, 90), 0);
		Assert.assertEquals(20, GeometryUtils.normalizeAngleInDegrees(-160, -90, 90), 0);
	}
	
	@Test
	public void testGetAngleDifference() {
		Assert.assertEquals(90, GeometryUtils.getAngleDifference(90, 180), 0);
		Assert.assertEquals(90, GeometryUtils.getAngleDifference(45, -45), 0);
		Assert.assertEquals(90, GeometryUtils.getAngleDifference(0, 270), 0);
		Assert.assertEquals(20, GeometryUtils.getAngleDifference(0, 20), 0);
		Assert.assertEquals(20, GeometryUtils.getAngleDifference(20, 0), 0);
		Assert.assertEquals(20, GeometryUtils.getAngleDifference(10, -10), 0);
		Assert.assertEquals(20, GeometryUtils.getAngleDifference(10, 350), 0);
		Assert.assertEquals(20, GeometryUtils.getAngleDifference(10, 350), 0);
	}

	@Test
	public void testIntermediatePointOnHorizon() {
		Point2D p1 = new Point2D(30, 30);
		Point2D p2 = new Point2D(30, -30);
		Point2D intermediatePoint = GeometryUtils.giveIntermediatePointOnHorizon(p1, p2);
		Assert.assertEquals(30, intermediatePoint.getX(), 0);
		Assert.assertEquals(0, intermediatePoint.getY(), 0);
		
		p1 = new Point2D(10, 10);
		p2 = new Point2D(30, -10);
		intermediatePoint = GeometryUtils.giveIntermediatePointOnHorizon(p1, p2);
		Assert.assertEquals(20, intermediatePoint.getX(), 0);
		Assert.assertEquals(0, intermediatePoint.getY(), 0);
		
		p1 = new Point2D(10, 10);
		p2 = new Point2D(20, 20);
		intermediatePoint = GeometryUtils.giveIntermediatePointOnHorizon(p1, p2);
		Assert.assertNull(intermediatePoint);
		
		p1 = new Point2D(10, -10);
		p2 = new Point2D(20, -20);
		intermediatePoint = GeometryUtils.giveIntermediatePointOnHorizon(p1, p2);
		Assert.assertNull(intermediatePoint);
		
		p1 = new Point2D(10, -10);
		p2 = new Point2D(30, 10);
		intermediatePoint = GeometryUtils.giveIntermediatePointOnHorizon(p1, p2);
		Assert.assertEquals(20, intermediatePoint.getX(), 0);
		Assert.assertEquals(0, intermediatePoint.getY(), 0);
		
	}
}
