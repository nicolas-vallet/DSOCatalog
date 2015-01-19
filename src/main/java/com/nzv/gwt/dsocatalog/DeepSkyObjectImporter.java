package com.nzv.gwt.dsocatalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EnumType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.client.PublicCatalogService;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.DsoType;
import com.nzv.gwt.dsocatalog.model.SizeUnit;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

public class DeepSkyObjectImporter {

	public static final String FILE_INPUT_DATA = "C:\\Users\\nvallet\\workspace_eclipse\\DSOCatalog\\db\\SAC_DeepSky_Ver81_Fence.TXT";

	public static void main(String[] args) {

		@SuppressWarnings("resource")
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
		Set<DeepSkyObject> objects = new HashSet<DeepSkyObject>();
		try {
			Scanner scanner = new Scanner(new File(FILE_INPUT_DATA));
			int i = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				i++;
				// We skip the first line which contains header...
				if (i == 1)
					continue;

				String[] input = line.split("\\|");
				DeepSkyObject object = initializeDeepSkyObjectFromInputLine(
						input, constellations);
				System.out.println("Found object " + object);
				objects.add(object);
			}
			adminCatalogService.saveDeepSkyObjects(objects);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long tEnd = System.currentTimeMillis();
		System.out.println("Importation is over. We imported " + objects.size()
				+ " object(s) in " + (tEnd - tStart) + "ms.");
	}

	private static DeepSkyObject initializeDeepSkyObjectFromInputLine(
			String[] input, HashMap<String, Constellation> constellations) {
		DeepSkyObject object = new DeepSkyObject();
		object.setName(readName(input[1]));
		object.setOtherName(readName(input[2]));
		object.setType(readObjectType(input[3]));
		object.setConstellation(constellations.get(input[4]));
		object.setRightAscensionHour(readInteger(input[5].substring(0, 2)));
		object.setRightAscensionMinute(readDouble(input[5].substring(3, 7)));
		object.setDeclinaisonDegree(readInteger(input[6].substring(0, 3)));
		object.setDeclinaisonMinute(readDouble(input[6].substring(4, 6)));
		object.setMagnitude(readDouble(input[7]));
		object.setSurfaceBrightness(readDouble(input[8]));
		object.setU2kChartNumber(readInteger(input[9]));
		object.setTiChartNumber(readInteger(input[10]));
		object.setMaxSize(readDouble(input[11].substring(0, 7)));
		object.setMaxSizeUnit(readSizeUnit(input[11].substring(7, 8)));
		object.setMinSize(readDouble(input[12].substring(0, 7)));
		object.setMinSizeUnit(readSizeUnit(input[12].substring(7, 8)));
		object.setPositionAngle(readDouble(input[13]));
		object.setClasstype(readString(input[14]));
		object.setStarsCount(readInteger(input[15]));
		object.setBrightestStarMagnitude(readDouble(input[16]));
		object.setInBestNgcCatalog(readString(input[17]) != null
				&& readString(input[17]).contains("B"));
		object.setInCaldwellCalatalog(readString(input[17]) != null
				&& readString(input[17]).contains("C"));
		object.setInHerschelCatalog(readString(input[17]) != null
				&& readString(input[17]).contains("H"));
		object.setInMessierCatalog(readString(input[17]) != null
				&& readString(input[17]).contains("M"));
		object.setNgcDescription(readString(input[18]));
		object.setNotes(readString(input[19]));
		return object;
	}

	private static SizeUnit readSizeUnit(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		} else {
			return EnumType.valueOf(SizeUnit.class, input);
		}
	}

	private static String readName(String input) {
		input = readString(input);
		if (input == null) {
			return null;
		}
		input = input.replaceAll("\\s+", " ");
		return input;
	}

	private static DsoType readObjectType(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		}
		while (input.contains("+")) {
			input = input.replace('+', '_');
		}
		if (input.contains("STAR")) {
			input = "MSTAR";
		}
		return EnumType.valueOf(DsoType.class, input);
	}

	private static String readString(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		} else {
			return input;
		}
	}

	private static Integer readInteger(String input) {
		input = input.trim();
		if (!input.isEmpty()) {
			return Integer.parseInt(input);
		} else {
			return null;
		}
	}

	private static Double readDouble(String input) {
		input = input.trim();
		if (!input.isEmpty()) {
			return Double.parseDouble(input);
		} else {
			return null;
		}
	}
}
