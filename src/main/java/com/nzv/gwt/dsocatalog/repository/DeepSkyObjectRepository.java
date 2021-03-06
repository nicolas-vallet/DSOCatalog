package com.nzv.gwt.dsocatalog.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.DsoType;

@Repository
public interface DeepSkyObjectRepository extends JpaRepository<DeepSkyObject, Integer> {

	List<DeepSkyObject> findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInAndClasstypeEqualsOrderByMagnitudeAsc(
			Constellation constellation, Double limitMagnitude, Collection<DsoType> types, String subtype);
	
	List<DeepSkyObject> findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInAndClasstypeContainingOrderByMagnitudeAsc(
			Constellation constellation, Double limitMagnitude, Collection<DsoType> types, String subtype);
	
	List<DeepSkyObject> findDistinctByConstellationAndMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
			Constellation constellation, Double limitMagnitude, Collection<DsoType> types);
	
	List<DeepSkyObject> findDistinctByMagnitudeLessThanEqualAndTypeInOrderByMagnitudeAsc(
			Double limitMagnitude, Collection<DsoType> types);
	
	List<DeepSkyObject> findDistinctByMagnitudeLessThanEqualAndTypeInAndClasstypeEqualsOrderByMagnitudeAsc(
			Double limitMagnitude, Collection<DsoType> types, String subtype);
	
	List<DeepSkyObject> findDistinctByMagnitudeLessThanEqualAndTypeInAndClasstypeContainingOrderByMagnitudeAsc(
			Double limitMagnitude, Collection<DsoType> types, String subtype);

	DeepSkyObject findByName(String name);
}
