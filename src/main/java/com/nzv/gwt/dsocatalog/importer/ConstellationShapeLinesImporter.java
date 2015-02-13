package com.nzv.gwt.dsocatalog.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationShapeLine;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

public class ConstellationShapeLinesImporter {
	
	public static final String FILE_INPUT_DATA = "C:\\Users\\nvallet\\workspace_eclipse\\DSOCatalog\\db\\constellations_shape_lines.js";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");

		AdminCatalogService adminCatalogService = ctx
				.getBean(AdminCatalogService.class);

		PublicCatalogService catalogService = ctx
				.getBean(PublicCatalogService.class);

		// We load the list of constellations that will be necessary later...
		List<Constellation> tmp = catalogService.findAllConstellations();
		Map<String, Constellation> constellationByName = new HashMap<String, Constellation>();
		for (Constellation c : tmp) {
			constellationByName.put(c.getName(), c);
		}
		
		long tStart = System.currentTimeMillis();
		Set<ConstellationShapeLine> lines = new HashSet<ConstellationShapeLine>();
		try {
			Scanner scanner = new Scanner(new File(FILE_INPUT_DATA));
			while (scanner.hasNextLine()) {
				String inputLine = scanner.nextLine();
				inputLine = inputLine.substring(inputLine.indexOf("(")+1, inputLine.indexOf(")"));
				lines.add(initializeConstellationShape(constellationByName, inputLine));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		adminCatalogService.saveConstellationShapeLines(lines);
		long tEnd = System.currentTimeMillis();
		System.out.println("Saved "+lines.size()+" in "+(tEnd-tStart)+" ms.");
	}

	
	private static Double readDouble(String input) {
		input = input.trim();
		if (!input.isEmpty()) {
			return Double.parseDouble(input);
		} else {
			return null;
		}
	}

	private static ConstellationShapeLine initializeConstellationShape(Map<String, Constellation> constellationByName, String input) {
		String[] params = input.split(",");
		String cName = params[0].substring(1, params[0].length()-1);
		double startRightAscension = 15 * readDouble(params[2]);
		double startDeclinaison = readDouble(params[1]);
		double endRightAscension = 15 * readDouble(params[4]);
		double endDeclinaison = readDouble(params[3]);
		Constellation constellation = constellationByName.get(cName.toUpperCase());
		if (constellation == null) {
			System.out.println("Unable to determine constellation for name ["+cName+"]");
		}
		ConstellationShapeLine line = new ConstellationShapeLine();
		line.setConstellation(constellation);
		line.setStartRightAscension(startRightAscension);
		line.setStartDeclinaison(startDeclinaison);
		line.setEndRightAscension(endRightAscension);
		line.setEndDeclinaison(endDeclinaison);
		return line;
	}
}
