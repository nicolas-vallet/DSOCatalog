ALTER TABLE constellation DROP COLUMN center_ra;
ALTER TABLE constellation DROP COLUMN center_dec;
ALTER TABLE constellation DROP COLUMN area;

ALTER TABLE constellation ADD COLUMN center_ra DOUBLE;
ALTER TABLE constellation ADD COLUMN center_dec DOUBLE;
ALTER TABLE constellation ADD COLUMN area DOUBLE;

