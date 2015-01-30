package com.nzv.gwt.dsocatalog;

import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.dsocatalog.projection.GeometryUtils;
import com.nzv.gwt.dsocatalog.projection.Point2D;


public class DsoCatalogTest {

	public static void main(String[] args) {
		Point2D start = new Point2D(3.3, 15.18);
		Point2D end = new Point2D(346.185, 15.2);
		Point2D intermediate = GeometryUtils.giveIntermediatePointOnChartLimit(start, end, CoordinatesSystem.EQ);
		System.out.println("RA="+intermediate.getX()+" / DEC="+intermediate.getY());
		
		intermediate = GeometryUtils.giveIntermediatePointOnChartLimit(end, start, CoordinatesSystem.EQ);
		System.out.println("RA="+intermediate.getX()+" / DEC="+intermediate.getY());
	}
}
