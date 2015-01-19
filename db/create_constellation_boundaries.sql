DROP TABLE IF EXISTS constellation_boundary_point;

CREATE TABLE constellation_boundary_point (
	id integer primary key auto_increment,
	constellation varchar(3),
	right_ascension double,
	declinaison double,
	point_type enum('O', 'I'),
	
	CONSTRAINT FOREIGN KEY (constellation) REFERENCES constellation (code)
) Engine=InnoDB;