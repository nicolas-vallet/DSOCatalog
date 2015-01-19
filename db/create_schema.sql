DROP TABLE IF EXISTS star;
DROP TABLE IF EXISTS multiple_code;
DROP TABLE IF EXISTS parallax_code;

CREATE TABLE multiple_code (
	code char,
	description varchar(255),
	primary key (code)
) ENGINE=InnoDB;
INSERT INTO multiple_code (code, description) values ('A', 'Astrometric binary');
INSERT INTO multiple_code (code, description) values ('D', 'Duplicity discovered by occultation');
INSERT INTO multiple_code (code, description) values ('I', 'Innes, Southern Double Star Catalogue (1927)');
INSERT INTO multiple_code (code, description) values ('R', 'Rossiter, Michigan Publ. 9, 1955');
INSERT INTO multiple_code (code, description) values ('S', 'Duplicity discovered by speckle interferometry');
INSERT INTO multiple_code (code, description) values ('W', 'Worley (1978) update of the IDS');

CREATE TABLE parallax_code (
	code char,
	description varchar(255),
	primary key (code)
) ENGINE=InnoDB;
INSERT INTO parallax_code (code, description) values ('D', 'Dynamic parallax');
INSERT INTO parallax_code (code, description) values ('T', 'Trigonometric parallax');

CREATE TABLE star (
	hr_number integer,
	name varchar(10),
	durchmusterung_identification varchar(11),
	hd_number integer,
	sao_number integer,
	fk5_number integer,
	ir_source tinyint(1),
	multiple_code char,
	ads_number varchar(5),
	ads_number_component varchar(2),
	variable_id varchar(9),
	RAh1900 integer,
	RAm1900 integer,
	RAs1900 double,
	DEsignus1900 char default '+',
	DEd1900 integer,
	DEm1900 integer,
	DEs1900 double,
	RAh integer,
	RAm integer, 
	RAs double,
	DEsignus char default '+',
	DEd integer,
	DEm integer,
	DEs double,
	Glon double,
	Glat double,
	Vmag double,
	B_Vmag double,
	B_Vmag_uncertain tinyint(1),
	U_Bmag double,
	U_Bmag_uncertain tinyint(1),
	R_Imag double,
	spectral_type varchar(20),
	propermotion_RA double,
	propermotion_DE double,
	parallax_code char,
	parallax double,
	radialvelocity integer,
	Dmag double,
	separation double,
	multiple_ID varchar(4),
	multiple_count integer,
	
	PRIMARY KEY (hr_number),
	CONSTRAINT FOREIGN KEY (multiple_code) REFERENCES multiple_code(code),
	CONSTRAINT FOREIGN KEY (parallax_code) REFERENCES parallax_code(code)
) ENGINE=InnoDB;


