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
}
