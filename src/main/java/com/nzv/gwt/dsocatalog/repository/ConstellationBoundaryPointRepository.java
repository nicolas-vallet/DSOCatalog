package com.nzv.gwt.dsocatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPoint;

@Repository
public interface ConstellationBoundaryPointRepository extends JpaRepository<ConstellationBoundaryPoint, Integer> {

}
