package com.nzv.gwt.dsocatalog.client;

public class ObjectReference {
	private boolean isStar = false;
	private boolean isDeepSkyObject = false;
	private boolean isPlanet = false;
	private Integer id = null;
	private String name = null;
	
	public ObjectReference(boolean isPlanet, boolean isStar, boolean isDeepSkyObject, Integer id, String name) {
		super();
		this.isPlanet = isPlanet;
		this.isStar = isStar;
		this.isDeepSkyObject = isDeepSkyObject;
		this.id = id;
		this.name = name;
	}
	
	
	public boolean isPlanet() {
		return isPlanet;
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
	public String getName() {
		return name;
	}


	@Override
	public String toString() {
		return "DisplayedObjectReference [isPlanet="+isPlanet+", isStar=" + isStar + ", isDeepSkyObject="
				+ isDeepSkyObject + ", id=" + id + "; name="+name+"]";
	}
}
