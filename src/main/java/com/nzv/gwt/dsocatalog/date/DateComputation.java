package com.nzv.gwt.dsocatalog.date;

import static java.lang.Math.pow;

import java.math.BigDecimal;

import com.nzv.astro.ephemeris.Constants;
import com.nzv.astro.ephemeris.Sexagesimal;

public class DateComputation {

	public static double getMeanSiderealTimeAsHoursFromJulianDay(double julianDayAsBigDecimal,
			Sexagesimal hourOfDay) {
		double siderealTimeForMignight = getMeanSiderealTimeAsHoursFromJulianDay(julianDayAsBigDecimal);
		double time = Sexagesimal.sexagesimalToDecimal(hourOfDay);
		time = time * 1.002737908;
		siderealTimeForMignight = siderealTimeForMignight + time;
		return siderealTimeForMignight;
	}

	public static double getMeanSiderealTimeAsHoursFromJulianDay(double julianDayAsBigDecimal) {
		if (julianDayAsBigDecimal - (int) julianDayAsBigDecimal != 0.5) {
			// throw new IllegalArgumentException(
			// "The julian day passed parameter should correspond to a date at midnight!");
			return -1;
		}
		double revolutionCounts = getMeanSiderealTimeAsRevolutionFromJulianDay(julianDayAsBigDecimal);
		double hours = (revolutionCounts - ((int) revolutionCounts)) * 24;
		return hours;
	}

	private static double getMeanSiderealTimeAsRevolutionFromJulianDay(double julianDayAsBigDecimal) {
		if (julianDayAsBigDecimal - (int) julianDayAsBigDecimal != 0.5) {
			// throw new IllegalArgumentException(
			// "The julian day passed parameter should correspond to a date at midnight!");
			return -1;
		}

		double T = (julianDayAsBigDecimal - 2415020) / 36525;
		double revolutionsCount = 0.276919398 + 100.0021359 * T + 0.000001075 * pow(T, 2);
		return revolutionsCount;
	}

	public static double getJulianDayFromDateAsDouble(double dt) {
		BigDecimal date = BigDecimal.valueOf(dt);
		int y = 0;
		int m = 0;
		BigDecimal d = BigDecimal.ZERO;
		int a = 0;
		int b = 0;

		int yyyy = date.intValue();
		BigDecimal tmp = date.subtract(BigDecimal.valueOf(yyyy)).abs();
		int mm = tmp.multiply(BigDecimal.valueOf(100)).intValue();
		d = tmp.multiply(BigDecimal.valueOf(100)).subtract(BigDecimal.valueOf(mm))
				.multiply(BigDecimal.valueOf(100));

		if (mm > 2) {
			y = yyyy;
			m = mm;
		} else if (mm == 1 || mm == 2) {
			y = yyyy - 1;
			m = mm + 12;
		} else {
			// throw new IllegalArgumentException(
			// "La date fournie en paramètre n'est pas valide!");
			return -1;
		}

		if (isInGregorianCalendar(date.doubleValue())) {
			a = (int) (y / 100);
			b = 2 - a + (int) (a / 4);
		}

		return BigDecimal.valueOf(
				(int) (365.25d * y) + (int) (30.6001 * (m + 1)) + d.doubleValue() + 1720994.5d + b)
				.doubleValue();
	}

	public static double getJulianDayFromDateAsDouble(double dt, Sexagesimal time) {
		BigDecimal date = BigDecimal.valueOf(dt);
		try {
			date.multiply(BigDecimal.valueOf(10000)).remainder(date.movePointRight(4)).intValueExact();
		} catch(ArithmeticException ex) {
			throw new IllegalArgumentException("The provided date should correspond to midnight !");
		}
		BigDecimal result = BigDecimal.valueOf(getJulianDayFromDateAsDouble(date.doubleValue()));
		BigDecimal dayFraction = 
				BigDecimal.valueOf(time.getUnit()).divide(BigDecimal.valueOf(24), Constants.BIG_DECIMAL_PRECISION)
				.add(BigDecimal.valueOf(time.getMinute()).divide(BigDecimal.valueOf(1440), Constants.BIG_DECIMAL_PRECISION))
				.add(BigDecimal.valueOf(time.getSecond()).divide(BigDecimal.valueOf(86400), Constants.BIG_DECIMAL_PRECISION));
		return result.add(dayFraction).doubleValue();
	}
	
	private static boolean isInGregorianCalendar(double date) {
		return date >= Constants.GREGORIAN_CALENDAR_START_DATE;
	}
}
