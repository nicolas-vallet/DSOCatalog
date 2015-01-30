package com.nzv.gwt.dsocatalog.projection;

import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;

/** 
 * An helper class for geometrical purposes...
 */
public class GeometryUtils {
	
	private static boolean[][] QUADRANT_CROSSING = new boolean[][] {
		{false, false, true, true}, 
		{false, false, false, true}, 
		{true, false, false, false}, 
		{true, true, false, false}
	};
	
	public static int getQuadrant(double angle, CoordinatesSystem cs) {
		if (cs == CoordinatesSystem.EQ) {
			double a = normalizeAngleInDegrees(angle, 0, 360);
			if (a < 90) {
				return 0;
			} else if (a < 180) {
				return 1;
			} else if (a < 270) {
				return 2;
			} else {
				return 3;
			}
		} else {
			double a = normalizeAngleInDegrees(angle, -180, 180);
			if (a < -90) {
				return 0;
			} else if (a < 0) {
				return 1;
			} else if (a < 90) {
				return 2;
			} else {
				return 3;
			}
		}
	}
	
	public static boolean isCrossingChartLimitX(double v1, double v2, CoordinatesSystem cs) {
		int q1 = getQuadrant(v1, cs);
		int q2 = getQuadrant(v2, cs);
		
		if (q1 == q2) return false;
		if ((q1 == 0 && q2 == 2) || (q1 == 1 && q2 == 3) || (q1 == 2 && q2 == 0) || (q1 == 3 && q2 == 1)) {
			// We compute the distance between v1 and v2, in the range [0; 2pi[
			double a1, a2;
			
			a1 = normalizeAngleInDegrees(v1, 0, 360);
			a2 = normalizeAngleInDegrees(v2, 0, 360);
			double diff1 = Math.abs(a1 - a2);
			
			// We compute the distance between v1 and v2, in the range [-pi; pi[
			a1 = normalizeAngleInDegrees(v1, -180, 180);
			a2 = normalizeAngleInDegrees(v2, -180, 180);
			double diff2 = Math.abs(a1 - a2);
			
			if (cs == CoordinatesSystem.EQ) {
				return (diff1 > diff2);
			} else {
				return (diff2 > diff1); 
			}
		} else {
			return QUADRANT_CROSSING[q1][q2];
		}
	}
	
	public static Point2D giveIntermediatePointOnChartLimit(Point2D p1, Point2D p2, CoordinatesSystem cs) {
		double iX, iY, a, b;
		
		if (p1.getX() < p2.getX()) {
			p2.setX(p2.getX()-360);
		} else {
			p1.setX(p1.getX()-360);
		}
		
		a = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
		b = (p1.getY() - a * p1.getX());
		
		if (cs == CoordinatesSystem.EQ) {
			iX = 0;
		} else {
			iX = -180;
		}
		iY = b;
		return new Point2D(iX, iY);
	}
	
	public static boolean isCrossingChartLimitY(Point2D p1, Point2D p2, CoordinatesSystem cs) {
		return false;
	}
	
	public static double normalizeAngleInDegrees(double angle, double minimum, double maximum) {
		double interval = Math.abs(minimum - maximum);
		while (angle < minimum) {
			angle += interval;
		}
		while (angle >= maximum) {
			angle -= interval;
		}
		return angle % interval;
	}
}