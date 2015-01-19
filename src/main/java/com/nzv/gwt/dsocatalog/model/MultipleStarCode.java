package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

public enum MultipleStarCode implements Serializable {
	A("Astrometric binary"),
	D("Duplicity discovered by occultation"),
	I("Innes, Southern Double Star Catalogue (1927)"),
	R("Rossiter, Michigan Publ. 9, 1955"),
	S("Duplicity discovered by speckle interferometry"),
	W("Worley (1978) update of the IDS");
	
	private String comment;
	
	MultipleStarCode(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return this.comment;
	}
}
