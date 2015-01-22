package com.nzv.gwt.dsocatalog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.gwt.dsocatalog.client.CatalogSearchOptions;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.DsoType;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.repository.ConstellationRepository;
import com.nzv.gwt.dsocatalog.repository.DeepSkyObjectRepository;
import com.nzv.gwt.dsocatalog.repository.StarRepository;

@Service("catalogService")
public class PublicCatalogServiceImpl implements PublicCatalogService {

	private static Logger logger = Logger
			.getLogger(PublicCatalogServiceImpl.class);

	@Autowired
	private StarRepository starRepository;

	@Autowired
	private DeepSkyObjectRepository dsoRepository;

	@Autowired
	private ConstellationRepository constellationRepository;

	@Override
	@Transactional(readOnly = true)
	public Set<AstroObject> findObjectBrighterThan(CatalogSearchOptions options) {
		long tStart = System.currentTimeMillis();
		Set<AstroObject> result = new HashSet<AstroObject>();
		Constellation constellation = null;
		
		if (options.getRestrictedToConstellationCode() != null
				&& !options.getRestrictedToConstellationCode().isEmpty()) {
			constellation = findConstellationByCode(options
					.getRestrictedToConstellationCode());
		}

		if (options.isFindStars()) {
			if (constellation != null) {
				// On limite la recherche aux étoiles présentes dans la zone de
				// cette constellation...
				EquatorialCoordinates upperWesterSearchLimit = constellation
						.getUpperWesternMapLimit();
				EquatorialCoordinates lowerEasterSearchLimit = constellation
						.getLowerEasternMapLimit();
				result.addAll(starRepository
						.findByEQCoordinatesAreaAndVisualMagnitudeLessThan(
								upperWesterSearchLimit.getRightAscension(),
								upperWesterSearchLimit.getDeclinaison(),
								lowerEasterSearchLimit.getRightAscension(),
								lowerEasterSearchLimit.getDeclinaison(),
								options.getStarLimitMagnitude()));
				// ...
			} else {
				result.addAll(starRepository
						.findByVisualMagnitudeLessThanEqualOrderByVisualMagnitudeAsc(options
								.getStarLimitMagnitude()));
			}

		}

		Set<DsoType> searchedTypes = new HashSet<DsoType>();
		if (options.isFindAsterisms()) {
			searchedTypes.add(DsoType.ASTER);
		}
		if (options.isFindGalaxies()) {
			searchedTypes.add(DsoType.GALXY);
			searchedTypes.add(DsoType.GALCL);
		}
		if (options.isFindGlobularClusters()) {
			searchedTypes.add(DsoType.GLOCL);
			searchedTypes.add(DsoType.GX_GC);
			searchedTypes.add(DsoType.LMCGC);
			searchedTypes.add(DsoType.SMCGC);
		}
		if (options.isFindOpenClusters()) {
			searchedTypes.add(DsoType.CL_NB);
			searchedTypes.add(DsoType.G_C_N);
			searchedTypes.add(DsoType.LMCCN);
			searchedTypes.add(DsoType.LMCOC);
			searchedTypes.add(DsoType.OPNCL);
			searchedTypes.add(DsoType.SMCCN);
			searchedTypes.add(DsoType.SMCOC);
		}
		if (options.isFindPlanetaryNebulas()) {
			searchedTypes.add(DsoType.PLNNB);
		}
		if (options.isFindNebulas()) {
			searchedTypes.add(DsoType.BRTNB);
			searchedTypes.add(DsoType.CL_NB);
			searchedTypes.add(DsoType.DRKNB);
			searchedTypes.add(DsoType.GX_DN);
			searchedTypes.add(DsoType.G_C_N);
			searchedTypes.add(DsoType.LMCCN);
			searchedTypes.add(DsoType.LMCDN);
			searchedTypes.add(DsoType.SMCCN);
			searchedTypes.add(DsoType.SMCDN);
			searchedTypes.add(DsoType.BRTNB);
		}
		if (options.isFindSupernovaRemnant()) {
			searchedTypes.add(DsoType.SNREM);
		}
		if (options.isFindQuasars()) {
			searchedTypes.add(DsoType.QUASR);
		}
		if (!searchedTypes.isEmpty()) {
			if (constellation != null) {
				result.addAll(dsoRepository
						.findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
								constellation, options.getDsoLimitMagnitude(),
								searchedTypes));
			} else {
				result.addAll(dsoRepository
						.findDistinctByMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
								options.getDsoLimitMagnitude(), searchedTypes));
			}
		}
		long tEnd = System.currentTimeMillis();
		logger.info("Found " + result.size() + " matching AstroObject(s) in "
				+ (tEnd - tStart) + " ms. | " + options.toString());
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Constellation findConstellationByCode(String constellationCode) {
		return constellationRepository.findOne(constellationCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Constellation findConstellationByCodeFetchingBoundary(
			String constellationCode) {
		return constellationRepository
				.findByCodeFetchingBoundary(constellationCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Constellation> findAllConstellations() {
		return constellationRepository.findAllFetchingBoundaries();
	}

	@Override
	@Transactional(readOnly = true)
	public Star findStarByHrNumber(Integer hrNumber) {
		return starRepository.findByHrNumber(hrNumber);
	}

	@Override
	@Transactional(readOnly = true)
	public Star findStarByName(String name) {
		return starRepository.findByName(name);
	}

	@Override
	@Transactional(readOnly = true)
	public Star findStarByHdNumber(Integer hdNumber) {
		return starRepository.findByHdNumber(hdNumber);
	}

	@Override
	@Transactional(readOnly = true)
	public Star findStarBySaoNumber(Integer saoNumber) {
		return starRepository.findBySaoNumber(saoNumber);
	}

	@Override
	@Transactional(readOnly = true)
	public Star findStarByFk5Number(Integer fk5Number) {
		return starRepository.findByFk5Number(fk5Number);
	}

	@Override
	@Transactional(readOnly = true)
	public DeepSkyObject findObjectByName(String name) {
		return dsoRepository.findByName(name);
	}

	@Override
	@Transactional(readOnly = true)
	public DeepSkyObject findObjectById(Integer id) {
		return dsoRepository.findOne(id);
	}
}
