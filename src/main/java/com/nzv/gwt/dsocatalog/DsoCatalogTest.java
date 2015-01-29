package com.nzv.gwt.dsocatalog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.astro.ephemeris.Sexagesimal.SexagesimalType;
import com.nzv.astro.ephemeris.planetary.Latitude;
import com.nzv.astro.ephemeris.planetary.Longitude;
import com.nzv.astro.ephemeris.planetary.NoInitException;
import com.nzv.astro.ephemeris.planetary.ObsInfo;
import com.nzv.astro.ephemeris.planetary.PlanetData;
import com.nzv.astro.ephemeris.planetary.Planets;
import com.nzv.gwt.dsocatalog.projection.AngleUtils;

public class DsoCatalogTest {

	public static final double LIMIT_MAGNITUDE = 2.5d;

	public static void main(String[] args) throws NoInitException {
		DateTime dt = new DateTime();
		System.out.println("via JodaTime, JD=" + DateTimeUtils.toJulianDay(dt.getMillis()));

		PlanetData planetaryEngine = new PlanetData();
		ObsInfo observatory = new ObsInfo(new Latitude(49), new Longitude(2));
		planetaryEngine.calc(Planets.PLUTO, DateTimeUtils.toJulianDay(dt.getMillis()), observatory);
		
		Sexagesimal ra = new Sexagesimal(AngleUtils.normalizeAngleInDegrees(Math.toDegrees(planetaryEngine.getRightAscension()), 0, 360) / 15);
		Sexagesimal dec = new Sexagesimal(Math.toDegrees(planetaryEngine.getDeclination()));
		System.out.println("Pluton : RA="+ra.toString(SexagesimalType.HOURS)+" / DEC="+dec.toString(SexagesimalType.DEGREES));
	}
}
