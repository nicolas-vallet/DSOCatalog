package com.nzv.gwt.dsocatalog.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nzv.astro.ephemeris.coordinate.impl.EquatorialCoordinates;
import com.nzv.astro.ephemeris.planetary.Latitude;
import com.nzv.astro.ephemeris.planetary.Longitude;
import com.nzv.astro.ephemeris.planetary.NoInitException;
import com.nzv.astro.ephemeris.planetary.ObsInfo;
import com.nzv.astro.ephemeris.planetary.PlanetData;
import com.nzv.gwt.dsocatalog.client.CatalogSearchOptions;
import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.DsoType;
import com.nzv.gwt.dsocatalog.model.Planet;
import com.nzv.gwt.dsocatalog.model.PlanetEnum;
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
		
		if (options.isFindPlanets()) {
			for (PlanetEnum planet : PlanetEnum.values()) {
				result.add(computePlanetCurrentPosition(planet, options));
			}
		}

		if (options.isFindStars()) {
			if (constellation != null) {
				// On limite la recherche aux étoiles présentes dans la zone de
				// cette constellation...
				EquatorialCoordinates upperWesterSearchLimit = constellation
						.getUpperWesternMapLimit();
				EquatorialCoordinates lowerEasterSearchLimit = constellation
						.getLowerEasternMapLimit();
				if (options.getSpectralTypeRestriction().isEmpty()) {
					result.addAll(starRepository
							.findByEQCoordinatesAreaAndVisualMagnitudeLessThan(
									upperWesterSearchLimit.getRightAscension(),
									upperWesterSearchLimit.getDeclinaison(),
									lowerEasterSearchLimit.getRightAscension(),
									lowerEasterSearchLimit.getDeclinaison(),
									options.getStarLimitMagnitude()));
				} else {
					if (options.getSpectralTypeRestriction().indexOf("*") != -1) {
						result.addAll(starRepository
								.findByEQCoordinatesAreaAndVisualMagnitudeLessThanAndSpectralTypeLike(
										upperWesterSearchLimit.getRightAscension(),
										upperWesterSearchLimit.getDeclinaison(),
										lowerEasterSearchLimit.getRightAscension(),
										lowerEasterSearchLimit.getDeclinaison(),
										options.getStarLimitMagnitude(), 
										options.getSpectralTypeRestriction().replace("*", "%")));
					} else {
						result.addAll(starRepository
								.findByEQCoordinatesAreaAndVisualMagnitudeLessThanAndSpectralTypeEquals(
										upperWesterSearchLimit.getRightAscension(),
										upperWesterSearchLimit.getDeclinaison(),
										lowerEasterSearchLimit.getRightAscension(),
										lowerEasterSearchLimit.getDeclinaison(),
										options.getStarLimitMagnitude(), 
										options.getSpectralTypeRestriction()));
					}
				}
			} else {
				if (options.getSpectralTypeRestriction().isEmpty()) {
					result.addAll(starRepository
							.findByVisualMagnitudeLessThanEqualOrderByVisualMagnitudeAsc(options
									.getStarLimitMagnitude()));
				} else {
					if (options.getSpectralTypeRestriction().indexOf("*") != -1) {
						result.addAll(starRepository
								.findByVisualMagnitudeLessThanEqualAndSpectralTypeContainingOrderByVisualMagnitudeAsc(
										options.getStarLimitMagnitude(),
										options.getSpectralTypeRestriction().replace("*", "")));
					} else {
						result.addAll(starRepository
								.findByVisualMagnitudeLessThanEqualAndSpectralTypeEqualsOrderByVisualMagnitudeAsc(
										options.getStarLimitMagnitude(),
										options.getSpectralTypeRestriction()));
					}
				}
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
			searchedTypes.add(DsoType.DRKNB);
			searchedTypes.add(DsoType.GX_DN);
			searchedTypes.add(DsoType.LMCDN);
			searchedTypes.add(DsoType.SMCDN);
		}
		if (options.isFindSupernovaRemnant()) {
			searchedTypes.add(DsoType.SNREM);
		}
		if (options.isFindQuasars()) {
			searchedTypes.add(DsoType.QUASR);
		}
		if (!searchedTypes.isEmpty()) {
			List<DeepSkyObject> dsos = new ArrayList<DeepSkyObject>();
			
			if (constellation != null) {
				if (options.getDsoSubtypeRestriction().isEmpty()) {
					dsos.addAll(dsoRepository
							.findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
									constellation, options.getDsoLimitMagnitude(),
									searchedTypes));
				} else {
					if (options.getDsoSubtypeRestriction().indexOf("*") != -1) {
						dsos.addAll(dsoRepository
								.findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInAndClasstypeContainingOrderByMagnitudeAsc(
										constellation, options.getDsoLimitMagnitude(),searchedTypes,
										options.getDsoSubtypeRestriction().replace("*", "")));
					} else {
						dsos.addAll(dsoRepository
								.findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInAndClasstypeEqualsOrderByMagnitudeAsc(
										constellation, options.getDsoLimitMagnitude(),searchedTypes,
										options.getDsoSubtypeRestriction()));
					}
					
				}
			} else {
				if (options.getDsoSubtypeRestriction().isEmpty()) {
					dsos.addAll(dsoRepository
							.findDistinctByMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
									options.getDsoLimitMagnitude(), searchedTypes));
				} else {
					if (options.getDsoSubtypeRestriction().indexOf("*") != -1) {
						dsos.addAll(dsoRepository
								.findDistinctByMagnitudeLessThanEqualAndTypeInAndClasstypeContainingOrderByMagnitudeAsc(
										options.getDsoLimitMagnitude(), searchedTypes, options.getDsoSubtypeRestriction().replace("*", "")));
					} else {
						dsos.addAll(dsoRepository
								.findDistinctByMagnitudeLessThanEqualAndTypeInAndClasstypeEqualsOrderByMagnitudeAsc(
										options.getDsoLimitMagnitude(), searchedTypes, options.getDsoSubtypeRestriction()));
					}
				}
			}
			// Filtering by catalog...
			if (options.isDsoInMessierCatalog()) {
				List<DeepSkyObject> tmp = new ArrayList<DeepSkyObject>();
				for (DeepSkyObject dso : dsos) {
					if (dso.getInCatalogs().contains("M")) {
						tmp.add(dso);
					}
				}
				dsos.clear();
				dsos.addAll(tmp);
				tmp = null;
			}
			if (options.isDsoInBestNgcCatalog()) {
				List<DeepSkyObject> tmp = new ArrayList<DeepSkyObject>();
				for (DeepSkyObject dso : dsos) {
					if (dso.getInCatalogs().contains("N")) {
						tmp.add(dso);
					}
				}
				dsos.clear();
				dsos.addAll(tmp);
				tmp = null;
			}
			if (options.isDsoInCaldwellCatalog()) {
				List<DeepSkyObject> tmp = new ArrayList<DeepSkyObject>();
				for (DeepSkyObject dso : dsos) {
					if (dso.getInCatalogs().contains("C")) {
						tmp.add(dso);
					}
				}
				dsos.clear();
				dsos.addAll(tmp);
				tmp = null;
			}
			if (options.isDsoInHerschelCatalog()) {
				List<DeepSkyObject> tmp = new ArrayList<DeepSkyObject>();
				for (DeepSkyObject dso : dsos) {
					if (dso.getInCatalogs().contains("H")) {
						tmp.add(dso);
					}
				}
				dsos.clear();
				dsos.addAll(tmp);
				tmp = null;
			}
			result.addAll(dsos);
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
	
	@Override
	public Planet computePlanetCurrentPosition(PlanetEnum planet, CatalogSearchOptions options) {
		PlanetData planetaryEngine = new PlanetData();
		ObsInfo observatory = new ObsInfo(new Latitude(options.getObservatoryLatitude()), new Longitude(options.getObservatoryLongitude()));
		
		String[] date = options.getObserverCurrentDateAsString().split("/");
		int j = Integer.valueOf(date[0]);
		int m = Integer.valueOf(date[1]);
		int a = Integer.valueOf(date[2]);
		String[] time = options.getObserverCurrentTimeAsString().split(":");
		int H = Integer.valueOf(time[0]);
		int M = Integer.valueOf(time[1]);
		int S = Integer.valueOf(time[2]);
		
		DateTime dt = new DateTime(a, m, j, H, M, S, DateTimeZone.forOffsetHours(options.getObserverGreenwhichHourOffset()));
		double jd = DateTimeUtils.toJulianDay(dt.getMillis());
		planetaryEngine.calc(planet.getId(), jd, observatory);
		try {
			Planet p = new Planet(planet.getId(), planet.getName(), null, 
					Math.toDegrees(planetaryEngine.getRightAscension()), Math.toDegrees(planetaryEngine.getDeclination()));
			return p;
		} catch (NoInitException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("An error occured, please consult the application log file.");
	}
}
