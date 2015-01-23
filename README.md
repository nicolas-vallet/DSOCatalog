# DSOCatalog

Just an astronomic atlas based on GWT, Google Chart API, and Spring data.

Clone the repository, 
the DB folder contains a mysql dump that you must inject in your database.
Configure the database access settings in the src/main/resources/application.properties
mvn package the project.

You now have an artifact named dsocatalog.war in the target directory and you can deploy it in your favorite JEE container.

That data are a compilation of various sources : 
 - Vizier database of Strasbourg observatory for stars up to magnitude 8.
 - the deep sky object database of the Saguaro Astronomy club (http://www.saguaroastro.org/)
 - Constellations boundaries come from Pierre Barbier websie (http://pbarbier.com/constellations/boundaries.html)
 - Constellation shape lines data come from the Javascript file provided by darethehair in this conversation : http://tinyurl.com/mpa2w56
 
Finaly the coordinates transformation formulas comes from the excellent book "Astronomical formulae for calculators" by Jean Meeus.
We use our own implementation that can be found at : https://github.com/nicolas-vallet/EphemerisEngine

I hope that you will enjoy it.
