package com.nzv.gwt.dsocatalog.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EnumType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPoint;
import com.nzv.gwt.dsocatalog.model.ConstellationBoundaryPointType;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

public class ConstellationBoundaryPointsImporter {

	public static final String FILE_INPUT_DATA = "C:\\Users\\nvallet\\workspace_eclipse\\DSOCatalog\\db\\constellations_boundaries.txt";

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
		HashMap<String, Constellation> constellations = new HashMap<String, Constellation>();
		for (Constellation c : tmp) {
			constellations.put(c.getCode(), c);
		}

		long tStart = System.currentTimeMillis();
		List<ConstellationBoundaryPoint> points = new ArrayList<ConstellationBoundaryPoint>();
		try {
			Scanner scanner = new Scanner(new File(FILE_INPUT_DATA));
			int pointCount = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				ConstellationBoundaryPoint point = initializePointFromInputLine(line, constellations);
				// We only keep the original points of the boundary.
				if (point.getType() == ConstellationBoundaryPointType.O) {
					points.add(point);
				}
				pointCount++;
				System.out.println("Read informations about point #"
						+ pointCount);
			}
			adminCatalogService.saveConstellationBoundaryPoints(points);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		long tEnd = System.currentTimeMillis();
		System.out.println("Importation is over. It took " + (tEnd - tStart)
				+ "ms.");
	}

	private static Double readDouble(String input) {
		input = input.trim();
		if (!input.isEmpty()) {
			return Double.parseDouble(input);
		} else {
			return null;
		}
	}

	private static String readString(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		} else {
			return input;
		}
	}

	private static ConstellationBoundaryPointType readPointType(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		}
		return EnumType.valueOf(ConstellationBoundaryPointType.class, input);
	}

	private static ConstellationBoundaryPoint initializePointFromInputLine(
			String input, HashMap<String, Constellation> constellations) {
		ConstellationBoundaryPoint point = new ConstellationBoundaryPoint();
		input = input.trim();
		String[] tmp = input.split(" ");
		String[] values = new String[4];
		int i=0;
		for (int j=1 ; j<tmp.length ; j++) {
			if (tmp[j].isEmpty()) {
				continue;
			} else {
				values[i] = tmp[j];
				i++;
			}
		}
		try {
			point.setRightAscensionAsDecimalDegrees(readDouble(values[0]));
			point.setDeclinaisonAsDecimalDegrees(readDouble(values[1]));
			String constellationCode = readString(values[2]);
			if ("SER1".equals(constellationCode) || "SER2".equals(constellationCode)) {
				constellationCode = "SER";
			}
			point.setConstellation(constellations.get(constellationCode));
			point.setType(readPointType(values[3]));
		} catch (IndexOutOfBoundsException ex) {
			System.out.println(ex.getMessage());
		}
		return point;
	}
}
