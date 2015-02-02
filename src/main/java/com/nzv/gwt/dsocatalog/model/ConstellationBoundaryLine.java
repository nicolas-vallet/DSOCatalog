package com.nzv.gwt.dsocatalog.model;

public class ConstellationBoundaryLine {

	private ConstellationBoundaryPoint start;
	private ConstellationBoundaryPoint end;

	public ConstellationBoundaryLine(ConstellationBoundaryPoint start,
			ConstellationBoundaryPoint end) {
		super();
		this.start = start;
		this.end = end;
	}

	public ConstellationBoundaryPoint getStart() {
		return start;
	}

	public ConstellationBoundaryPoint getEnd() {
		return end;
	}

}
