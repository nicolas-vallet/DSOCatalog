package com.nzv.gwt.dsocatalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.service.AdminCatalogService;

public class ConstellationCentersUpdater {

	public static final String FILE_INPUT_DATA = "C:\\Users\\nvallet\\workspace_eclipse\\DSOCatalog\\db\\constellations_centers_and_area.txt";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", "jpaContext.xml");

		AdminCatalogService adminCatalogService = ctx.getBean(AdminCatalogService.class);
		
		long tStart = System.currentTimeMillis();
		int i=0;
		try {
			Scanner scanner = new Scanner(new File(FILE_INPUT_DATA));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String constellationCode = readString(line.substring(30, 33));
				Constellation constellation = adminCatalogService.findConstellationByCode(constellationCode);
				constellation.setCenterRightAscension(readDouble(line.substring(0, 8)));
				constellation.setCenterDeclinaison(readDouble(line.substring(9, 18)));
				constellation.setAreaInSquareDegrees(readDouble(line.substring(19, 26)));
				System.out.println("Updating constellation ["+constellation.getName()+"]");
				adminCatalogService.saveConstellation(constellation);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		long tEnd = System.currentTimeMillis();
		System.out.println("Update process is over. "+i+" constellation(s) in "+(tEnd-tStart)+" ms.");
	}
	
	private static String readString(String input) {
		input = input.trim();
		if (input.isEmpty()) {
			return null;
		} else {
			return input;
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
