DROP TABLE IF EXISTS deep_sky_object;

CREATE TABLE deep_sky_object (
	id integer PRIMARY KEY auto_increment,
	name varchar(17) not null comment 'Object name, usually the NGC number is in field #1, but for objects with no NGC value the alphabetical name used is listed in table catalog_code',
	other_name varchar(18),
	object_type varchar(5),
	constellation varchar(3), -- FOREIGN KEY
	RAh integer,
	RAm double,
	DEd integer,
	DEm double,
	magnitude double,
	surface_brigthness double,
	u2k_chart integer comment 'The charts in the first release of Uranometria 2000.0 that map the area of sky in which the object is located',
	ti_chart integer comment 'The charts in the Tirion Sky Atlas 2000.0 that map the area of sky in which the object is located',
	size_max double comment 'The large dimension of the object',
	size_max_unit enum ('d', 'm', 's'),
	size_min double comment 'The small dimension of the object',
	size_min_unit enum ('m', 's'),
	position_angle double comment 'The Position Angle of an elongated object.  The value is in degrees, starting with north as zero degrees and progressing clockwise from north',
	class varchar(11) comment 'Several professional classification schemes are contained here',
	stars_count integer comment 'Number of stars within a cluster',
	brightest_star_mag double comment 'Magnitude of brightest star in cluster or central star of planetary nebula',
	in_bestngc_catalog tinyint(1) comment 'Is this object included in Best NGC catalog',
	in_caldwell_catalog tinyint(1) comment 'Is this object included in Calwell catalog',
	in_herschel_catalog tinyint(1) comment 'Is this object included in Herschel catalog',
	in_messier_catalog tinyint(1) comment 'Is this object included in Messier catalog',
	ngc_desc varchar(55) comment 'Visual description of the object.  Most of these are from the NGC. See table ngc_desc_abbreviation',
	notes varchar(86),
	
	CONSTRAINT FOREIGN KEY (constellation) REFERENCES constellation (code) 
) Engine=InnoDB;