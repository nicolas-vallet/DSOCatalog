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
	
	/**
	 * Given two points expressed as AltAzimutal coordinates, return the point on the joining segment that is on the horizon (e.g. elevation equals to zero).
	 * If both point are over or under horizon, this method returns null.
	 * 
	 * @param A point with AltAz coordinates
	 * @param A point with AltAz coordinates
	 * @return The point which is part of the segment joining the two input points and just on the horizon (e.g. with elevation equals to zero).
	 */
	public static Point2D giveIntermediatePointOnHorizon(Point2D p1, Point2D p2) {
		double iX, iY, a, b;
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		
		if ((y1 > 0 && y2 > 0) || (y1 < 0 && y2 < 0)) {
			return null;
		}
		
		if (x1 == x2) {
			return new Point2D(x1, 0);
		}
		
		a = (y2 - y1) / (x2 - x1);
		b = (x2 * y1 - x1 * y2) / (x2 - x1);
		
		iY = 0;
		
		// y = a * x + b
		// |
		// +-> a * x = y - b
		// |
		// *-> x = (y - b) / a
		iX = (iY - b) / a;
		return new Point2D(iX, iY);
	}
	
	public static Point2D giveIntermediatePointOnChartLimit(Point2D p1, Point2D p2, CoordinatesSystem cs) {
		double iX, iY, a, b;
		double x1 = p1.getX();
		double x2 = p2.getX();
		double y1 = p1.getY();
		double y2 = p2.getY();
		
		if (x1 < x2) {
			x2 -= 360;
		} else {
			x1 -= 360;
		}
		
		a = (y2 - y1) / (x2 - x1);
		b = y1 - a * x1;
		
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
		// TODO
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
	
	/**
	 * Returns the minimum difference between two angle values.
	 * For instance, the difference between 90 and 180 will be 90 and not 270.
	 * @param angle1
	 * @param angle2
	 * @return
	 */
	public static double getAngleDifference(double angle1, double angle2) {
		double a1, a2;
		a1 = normalizeAngleInDegrees(angle1, 0, 360);
		a2 = normalizeAngleInDegrees(angle2, 0, 360);
		double diff1 = Math.abs(a1 - a2);
		
		a1 = normalizeAngleInDegrees(angle1, -180, 180);
		a2 = normalizeAngleInDegrees(angle2, -180, 180);
		double diff2 = Math.abs(a1 - a2);
		
		return Math.min(diff1, diff2);
	}
	
	public static double getDistanceX(Point2D p1, Point2D p2) {
		return 0;
	}
	
	public static double getDistanceY(Point2D p1, Point2D p2) {
		return 0;
	}
}
