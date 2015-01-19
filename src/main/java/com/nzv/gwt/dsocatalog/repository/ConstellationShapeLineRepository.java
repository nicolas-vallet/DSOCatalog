package com.nzv.gwt.dsocatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;

@Repository
public interface ConstellationShapeLineRepository extends JpaRepository<ConstellationShapeLine, Integer> {

}
