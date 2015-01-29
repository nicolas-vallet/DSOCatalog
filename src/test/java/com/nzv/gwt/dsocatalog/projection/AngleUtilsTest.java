package com.nzv.gwt.dsocatalog.projection;


import org.junit.Assert;
import org.junit.Test;

public class AngleUtilsTest {
	
	@Test
	public void testFarNegativeAngleNormalization() {
		double a = -1130;
		Assert.assertEquals(-50, AngleUtils.normalizeAngleBetweenMinus180and180(a), 0);
		Assert.assertEquals(310, AngleUtils.normalizeAngleBetween0and360(a), 0);
		Assert.assertEquals(-50, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		Assert.assertEquals(310, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
	}
	
	@Test
	public void testFarPositiveAngleNormalization() {
		double a = 1485;
		Assert.assertEquals(45, AngleUtils.normalizeAngleBetween0and360(a), 0);
		Assert.assertEquals(45, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
		Assert.assertEquals(45, AngleUtils.normalizeAngleBetweenMinus180and180(a), 0);
		Assert.assertEquals(45, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		
		a = 1640;
		Assert.assertEquals(200, AngleUtils.normalizeAngleBetween0and360(a), 0);
		Assert.assertEquals(200, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
		Assert.assertEquals(-160, AngleUtils.normalizeAngleBetweenMinus180and180(a), 0);
		Assert.assertEquals(-160, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
	}
}
