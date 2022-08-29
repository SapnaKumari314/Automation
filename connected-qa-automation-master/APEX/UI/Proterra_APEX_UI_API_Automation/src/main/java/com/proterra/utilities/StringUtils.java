package main.java.com.proterra.utilities;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;

public class StringUtils {
	
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	
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
	
	
	//Convert String to ArrayList with delimiter
	public static List<String> stringToArrayList(String stringtoConvert, String delimiter){
		List<String> strList = new ArrayList<>();
		
		String[] temp = stringtoConvert.replaceAll(" ", "").split(delimiter);
		strList = Arrays.asList(temp);
		return strList;
	}
}
