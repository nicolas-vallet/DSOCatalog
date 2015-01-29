package com.nzv.gwt.dsocatalog.projection;


import org.junit.Assert;
import org.junit.Test;

public class AngleUtilsTest {
	
	@Test
	public void testFarNegativeAngleNormalization() {
		double a = -1130;
		Assert.assertEquals(-50, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		Assert.assertEquals(310, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
	}
	
	@Test
	public void testFarPositiveAngleNormalization() {
		double a = 1485;
		Assert.assertEquals(45, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		
		Assert.assertEquals(45, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
		
		a = 1640;
		Assert.assertEquals(200, AngleUtils.normalizeAngleInDegrees(a, 0, 360), 0);
		Assert.assertEquals(-160, AngleUtils.normalizeAngleInDegrees(a, -180, 180), 0);
	}
	
	@Test
	public void testNormalizationBetweenMinus90And90() {
		double a = 100;
		Assert.assertEquals(-80, AngleUtils.normalizeAngleInDegrees(a, -90, 90), 0);
		Assert.assertEquals(20, AngleUtils.normalizeAngleInDegrees(200, -90, 90), 0);
		Assert.assertEquals(20, AngleUtils.normalizeAngleInDegrees(-160, -90, 90), 0);
	}
}
