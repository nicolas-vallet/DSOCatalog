package com.nzv.gwt.dsocatalog.client;

public class ObjectReference {
	private boolean isStar = false;
	private boolean isDeepSkyObject = false;
	private Integer id = null;
	
	public ObjectReference(boolean isStar, boolean isDeepSkyObject, Integer id) {
		super();
		this.isStar = isStar;
		this.isDeepSkyObject = isDeepSkyObject;
		this.id = id;
	}
	
	public boolean isStar() {
		return isStar;
	}
	public boolean isDeepSkyObject() {
		return isDeepSkyObject;
	}
	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "DisplayedObjectReference [isStar=" + isStar + ", isDeepSkyObject="
				+ isDeepSkyObject + ", id=" + id + "]";
	}
}
