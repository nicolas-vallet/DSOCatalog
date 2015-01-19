package com.nzv.gwt.dsocatalog.model;

public enum SizeUnit {
	
	d("Degree"),
	m("Arcminute"),
	s("Arcsecond");

	private String name;
	
	SizeUnit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
