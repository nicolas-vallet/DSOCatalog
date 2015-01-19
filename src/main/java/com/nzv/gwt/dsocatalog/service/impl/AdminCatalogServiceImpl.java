package com.nzv.gwt.dsocatalog.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPoint;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.repository.ConstellationBoundaryPointRepository;
import com.nzv.gwt.dsocatalog.repository.ConstellationRepository;
import com.nzv.gwt.dsocatalog.repository.ConstellationShapeLineRepository;
import com.nzv.gwt.dsocatalog.repository.DeepSkyObjectRepository;
import com.nzv.gwt.dsocatalog.repository.StarRepository;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

@Service("adminCatalogService")
public class AdminCatalogServiceImpl implements AdminCatalogService {

	@Autowired
	private StarRepository starRepository;

	@Autowired
	private DeepSkyObjectRepository dsoRepository;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private ConstellationBoundaryPointRepository constellationBoundaryPointRepository;
	
	@Autowired
	private ConstellationShapeLineRepository constellationShapeLineRepository;

	@Override
	@Transactional
	public DeepSkyObject saveDeepSkyObject(DeepSkyObject object) {
		return dsoRepository.saveAndFlush(object);
	}

	@Override
	@Transactional
	public Collection<DeepSkyObject> saveDeepSkyObjects(
			Collection<DeepSkyObject> objects) {
		return dsoRepository.save(objects);
	}

	@Override
	@Transactional
	public Star saveStar(Star star) {
		return starRepository.saveAndFlush(star);
	}

	@Override
	@Transactional
	public Collection<Star> saveStars(Collection<Star> stars) {
		return starRepository.save(stars);
	}

	@Override
	@Transactional
	public Collection<ConstellationBoundaryPoint> saveConstellationBoundaryPoints(
			Collection<ConstellationBoundaryPoint> points) {
		return constellationBoundaryPointRepository.save(points);
	}

	@Override
	@Transactional
	public Collection<ConstellationShapeLine> saveConstellationShapeLines(
			Collection<ConstellationShapeLine> lines) {
		return constellationShapeLineRepository.save(lines);
	}

	@Override
	@Transactional(readOnly=true)
	public Constellation findConstellationByCode(String constellationCode) {
		return constellationRepository.findByCode(constellationCode);
	}

	@Override
	@Transactional
	public Constellation saveConstellation(Constellation constellation) {
		return constellationRepository.save(constellation);
	}
}
