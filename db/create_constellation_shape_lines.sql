DROP TABLE IF EXISTS constellation_shape_line;

CREATE TABLE constellation_shape_line (
	id integer primary key auto_increment,
	constellation varchar(3),
	start_right_ascension double,
	start_declinaison double,
	end_right_ascension double,
	end_declinaison double,
	
	CONSTRAINT FOREIGN KEY (constellation) REFERENCES constellation (code)
) Engine=InnoDB;