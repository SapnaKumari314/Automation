package main.java.com.proterra.utilities;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;

import test.java.com.proterra.testcases.BaseTest;

public class StringUtils {
	
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	
	// Split a string using symbol
	public static String splitString(String stringToSplit, int returnIndex, String splitSymbol)
	{
		String[] chargingStationArray = null;
		try {
			chargingStationArray = stringToSplit.split(splitSymbol);
		}
		catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargingStationArray[returnIndex].trim();
	}
}
