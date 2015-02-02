package com.nzv.gwt.dsocatalog.model;


public enum PlanetEnum {

	SUN (Planet.SUN, "SUN", null),
	MOON (Planet.MOON, "MOON", 0.11),
	MERCURY (Planet.MERCURY, "MERCURY", 0.119),
	VENUS (Planet.VENUS, "VENUS", 0.75),
	//EARTH (Planet.EARTH, "EARTH", 0.306),
	MARS (Planet.MARS, "MARS", 0.250),
	JUPITER (Planet.JUPITER, "JUPITER", 0.343),
	SATURN (Planet.SATURN, "SATURN", 0.342),
	URANUS (Planet.URANUS, "URANUS", 0.300),
	NEPTUNE (Planet.NEPTUNE, "NEPTUNE", 0.290),
	PLUTO (Planet.PLUTO, "PLUTO", 0.5);
	
	private int id;
	private String name;
	private Double meanAlbedo;
	
	private PlanetEnum(int id, String name, Double meanAlbedo) {
		this.id = id;
		this.name = name;
		this.meanAlbedo = meanAlbedo;
	}

	private PlanetEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getMeanAlbedo() {
		return meanAlbedo;
	}
	
	public static PlanetEnum forId(int planetId) {
		for (PlanetEnum p : values()) {
			if (p.getId() == planetId) {
				return p;
			}
		}
		// USE AN EXCEPTION INSTEAD!
		return null;
	}
	
}
