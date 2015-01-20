package com.nzv.gwt.dsocatalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EnumType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.model.MultipleStarCode;
import com.nzv.gwt.dsocatalog.model.ParallaxCode;
import com.nzv.gwt.dsocatalog.model.Star;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

public class StarImporter {

	public static final String FILE_INPUT_DATA = "C:\\Users\\nvallet\\workspace_eclipse\\DSOCatalog\\db\\bright_star_catalogue_5th_revised_edition.txt";

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");

		AdminCatalogService adminCatalogService = ctx.getBean(AdminCatalogService.class);

		long tStart = System.currentTimeMillis();
		Set<Star> stars = new HashSet<Star>();
		try {
			Scanner scanner = new Scanner(new File(FILE_INPUT_DATA));
			int starCount = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Star star = initializeStarFromInputLine(line);
				stars.add(star);
				starCount++;
				System.out.println("Read informations about star #" + starCount
						+ " / " + star);
			}
			adminCatalogService.saveStars(stars);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long tEnd = System.currentTimeMillis();

		System.out.println("Importation is over. It took " + (tEnd - tStart)
				+ "ms.");
	}

	private static Star initializeStarFromInputLine(String input) {
		Star star = new Star();
		try {
			star.setHrNumber(readInteger(input.substring(0, 4)));
			star.setName(input.substring(4, 14).trim());
			star.setDurchmusterungIdentification(input.substring(14, 25).trim());
			star.setHdNumber(readInteger(input.substring(25, 31)));
			star.setSaoNumber(readInteger(input.substring(31, 37)));
			star.setFk5Number(readInteger(input.substring(37, 41)));
			star.setIrSource("I".equals(input.substring(41, 42)));
			System.out.println("["
					+ (input.substring(43, 44).trim().isEmpty() ? null
							: EnumType.valueOf(MultipleStarCode.class, input
									.substring(43, 44).trim())) + "]");
			star.setMultipleStarCode(input.substring(43, 44).trim().isEmpty() ? null
					: EnumType.valueOf(MultipleStarCode.class,
							input.substring(43, 44).trim()));
			star.setAdsNumber(input.substring(44, 49).trim());
			star.setAdsNumberComponent(input.substring(49, 51).trim());
			star.setVariableStarIdentification(input.substring(51, 60).trim());
			star.setRightAscension1900Hour(readInteger(input.substring(60, 62)));
			star.setRightAscention1900Minute(readInteger(input
					.substring(62, 64)));
			star.setRightAscension1900Second(readDouble(input.substring(64, 68)));
			star.setDeclinaison1900Signus(input.substring(68, 69).trim());
			star.setDeclinaison1900Degree(readInteger(input.substring(69, 71)));
			star.setDeclinaison1900Minute(readInteger(input.substring(71, 73)));
			star.setDeclinaison1900Second(readDouble(input.substring(73, 75)));
			star.setRightAscensionHour(readInteger(input.substring(75, 77)));
			star.setRightAscensionMinute(readInteger(input.substring(77, 79)));
			star.setRightAscensionSecond(readDouble(input.substring(79, 83)));
			star.setDeclinaisonSignus(input.substring(83, 84).trim());
			star.setDeclinaisonDegree(readInteger(input.substring(84, 86)));
			star.setDeclinaisonMinute(readInteger(input.substring(86, 88)));
			star.setDeclinaisonSecond(readDouble(input.substring(88, 90)));
			star.setGalacticLongitude(readDouble(input.substring(90, 96)));
			star.setGalacticLatitude(readDouble(input.substring(96, 102)));
			star.setVisualMagnitude(readDouble(input.substring(102, 107)));
			star.setBvMag(readDouble(input.substring(109, 114)));
			star.setUncertainBvMag(input.substring(114, 115).trim().equals("?"));
			star.setUbMag(readDouble(input.substring(115, 120)));
			star.setUncertainUbMag(input.substring(120, 121).trim().equals("?"));
			star.setRiMag(readDouble(input.substring(121, 126)));
			star.setSpectralType(input.substring(127, 147).trim());
			star.setProperMotionRa(readDouble(input.substring(148, 154)));
			star.setProperMotionDec(readDouble(input.substring(154, 160)));
			star.setParallaxCode("D".equals(input.substring(160, 161).trim()) ? ParallaxCode.D
					: ParallaxCode.T);
			star.setParallax(readDouble(input.substring(161, 166)));
			star.setRadialVelocity(readInteger(input.substring(166, 170)));
			star.setCompanionMagnitudeDifference(readDouble(input.substring(
					180, 184)));
			star.setCompanionSeparation(readDouble(input.substring(184, 190)));
			star.setCompanionIdentification(input.substring(190, 194).trim());
			star.setCompanionCount(readInteger(input.substring(194, 196)));
		} catch (IndexOutOfBoundsException ex) {
			System.out.println(ex.getMessage());
		}
		return star;
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
