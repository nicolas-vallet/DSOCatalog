package com.nzv.gwt.dsocatalog.service;

import java.util.Collection;

import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPoint;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;

public interface AdminCatalogService {

	public DeepSkyObject saveDeepSkyObject(DeepSkyObject object);

	public Collection<DeepSkyObject> saveDeepSkyObjects(Collection<DeepSkyObject> objects);

	public Star saveStar(Star star);

	public Collection<Star> saveStars(Collection<Star> stars);
	
	public Collection<ConstellationBoundaryPoint> saveConstellationBoundaryPoints(Collection<ConstellationBoundaryPoint> points);
	
	public Collection<ConstellationShapeLine> saveConstellationShapeLines(Collection<ConstellationShapeLine> lines);
	
	public Constellation findConstellationByCode(String constellationCode);
	
	public Constellation saveConstellation(Constellation constellation);

}
