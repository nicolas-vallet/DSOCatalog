package com.nzv.gwt.dsocatalog;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.JulianChronology;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.projection.MercatorProjection;
import com.nzv.gwt.dsocatalog.projection.Point2D;
import com.nzv.gwt.dsocatalog.projection.Projection;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) {
		
		DateTime dt = new DateTime(2015, 1, 29, 12, 33, 04);
		System.out.println(dt + " / JD=" + DateTimeUtils.toJulianDay(dt.getMillis()));
		
		dt = new DateTime(2015, 1, 29, 12, 33, 04, DateTimeZone.forOffsetHours(+1));
		System.out.println(dt + " / JD=" + DateTimeUtils.toJulianDay(dt.getMillis()));
	}
}
