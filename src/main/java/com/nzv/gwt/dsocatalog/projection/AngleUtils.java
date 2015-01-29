package com.nzv.gwt.dsocatalog.projection;

/** 
 * An helper class for Angle manipulation...
 * @author nvallet
 *
 */
public class AngleUtils {
	
	public static double normalizeAngleInDegrees(double angle, double minimum, double maximum) {
		while (angle < minimum) {
			angle += 360;
		}
		while (angle >= maximum) {
			angle -= 360;
		}
		return angle % 360;
	}
	
	public static double normalizeAngleBetween0and360(double angle) {
		while (angle < 0) {
			angle += 360;
		}
		while (angle >= 360) {
			angle -= 360;
		}
		return angle % 360;
	}
	
	public static double normalizeAngleBetweenMinus180and180(double angle) {
		while (angle <= -180) {
			angle += 360;
		}
		while (angle > 180) {
			angle -= 360;
		}
		return angle % 360;
	}
}
