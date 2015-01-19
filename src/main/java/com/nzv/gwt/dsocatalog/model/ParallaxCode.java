package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

public enum ParallaxCode implements Serializable {
	
	D("Dynamic parallax"),
	T("Trigonometric parallax");
	
	private String comment;
	
	ParallaxCode(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}
}
