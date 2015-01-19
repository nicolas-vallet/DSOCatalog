package com.nzv.gwt.dsocatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nzv.gwt.dsocatalog.model.Star;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {

	public Star findByName(String name);

	public Star findByHrNumber(Integer id);

	public Star findByHdNumber(Integer id);

	public Star findBySaoNumber(Integer id);

	public Star findByFk5Number(Integer id);

	public List<Star> findByVisualMagnitudeLessThanEqualOrderByVisualMagnitudeAsc(
			Double limitMagnitude);

	@Query("SELECT s FROM Star s WHERE s.rightAscension <= ?1 "
			+ "       AND s.declinaison <= ?2 AND s.rightAscension >= ?3 "
			+ "       AND s.declinaison >= ?4 AND s.visualMagnitude <= ?5 ")
	public List<Star> findByEQCoordinatesAreaAndVisualMagnitudeLessThan(double RAmin,
			double DECmax, double RAmax, double DECmin, Double limitMagnitude);
}
