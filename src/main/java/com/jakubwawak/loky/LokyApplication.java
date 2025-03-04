/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.loky;

import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.jakubwawak.loky.database_engine.Database;
import com.jakubwawak.loky.maintanance.ConsoleColors;
import com.jakubwawak.loky.properties_engine.Properties;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;


/**
 * LokyApplication is the main class for the Loky application.
 */
@SpringBootApplication
@EnableVaadin({"com.jakubwawak"})
@Theme(value = "loky")
public class LokyApplication extends SpringBootServletInitializer implements AppShellConfigurator{

	public static String version = "0.0.1";
	public static String build = "loky04032025REV1";

	public static Properties properties;
	public static Database database;
	public static Document loky_configuration;

	/**
	 * Main method for the Loky application.
	 * @param args
	 */
	public static void main(String[] args) {
		showWelcomeScreen();
		properties = new Properties("loky.properties");
		if ( properties.fileExists ){
			properties.parsePropertiesFile();
			database = new Database();
			database.setDatabase_url(properties.getValue("databaseUrl"));
			database.connect();
			if ( database.connected ){
				database.getOrCreateLokyAdminPassword();
				loky_configuration = database.getOrCreateLokyConfiguration();
				SpringApplication.run(LokyApplication.class, args);
			}
			else{
				System.out.println("Failed to connect to database");
			}
		}
		else{
			System.out.println("Failed to load properties file");
			properties.createPropertiesFile();
			System.out.println("Properties file created - please fill in the required data and restart the application.");
		}
	}

	/**
	 * Shows the welcome screen.
	 */
	private static void showWelcomeScreen(){
		System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT);
		System.out.println("Welcome to Loky");
		System.out.println("Version: " + version + " (" + build + ")");
		System.out.println("Author: Jakub Wawak");
		System.out.println("Email: kubawawak@gmail.com");
		System.out.println("All rights reserved");
		System.out.println("--------------------------------");
		System.out.print(ConsoleColors.RESET);
	}

}
