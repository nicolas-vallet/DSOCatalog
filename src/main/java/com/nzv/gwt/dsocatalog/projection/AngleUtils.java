package com.nzv.gwt.dsocatalog.projection;

/** 
 * An helper class for Angle manipulation...
 * @author nvallet
 *
 */
public class AngleUtils {
	
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
