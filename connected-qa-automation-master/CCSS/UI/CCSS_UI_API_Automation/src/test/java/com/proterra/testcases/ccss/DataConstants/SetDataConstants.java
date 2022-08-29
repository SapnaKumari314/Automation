package test.java.com.proterra.testcases.ccss.DataConstants;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import test.java.com.proterra.testcases.ccss.BaseTestClass;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;

public class SetDataConstants{

	private static Logger log = Logger.getLogger(SetDataConstants.class.getName());

	private static Properties prop;
	private static BaseTestClass base = new BaseTestClass();

	public static List<String> busNameList;
	public static List<String> busVinList;
	public static Set<Object> busVinSet;
	public static Map<String, String> busVinNameMap;
	public static Map<String, String> garageTrackPositionsMap;





	/*--------------------------------------------------------------------------------------------------------*/



	//Get List of Bus Names
	public static List<String> getBusNameList(){

		try {

			//Read the bus list properties file 
			prop = base.readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/busList.properties");

			//Create new List
			busNameList = new ArrayList<String>();

			//Get All Keys - Vin # from the properties file
			busVinSet = prop.keySet();

			//Get All Bus Names by Vin Number
			for (Object object : busVinSet) {
				busNameList.add(object.toString());
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return busNameList;
	}


	//Get List of Bus Vins
	public static List<String> getBusVinList(){

		try {

			//Read the bus list properties file 
			prop = base.readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/busList.properties");

			//Create new List
			busVinList = new ArrayList<String>();

			//Get All Keys - Vin # from the properties file
			busVinSet = prop.keySet();

			//Get All Bus Vin#
			for (Object object : busVinSet) {
				busVinList.add(prop.getProperty(object.toString()));
			}
			

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return busVinList;
	}


	//Get Map of Bus Names and Vins
	public static Map<String, String> getBusNameVinMap(){

		try {
			//Read the bus list properties file 
			prop = base.readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/busList.properties");
			
			//Convert all the properties to a Map
			busVinNameMap = prop.entrySet().stream().collect(
					Collectors.toMap(
							e -> e.getKey().toString(),
							e -> e.getValue().toString()
							)
					);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return busVinNameMap;
	}


	//Get Map of Track Positions with UI displayed value and html attribute values
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getGarageTrackPositionsMap(boolean forceOverwrite){
		try {
			
			//Read the bus list properties file 
			String fileName = System.getProperty("user.dir") + "/src/test/resources/properties/trackPositions.properties";
			prop = base.readPropertiesFile(fileName);
			
			//Check if properties are empty and populate data from API
			if (forceOverwrite == true || prop.isEmpty()) {
				
				//Store the data from Garage API Data Map
				GetAPIResponseData dat = new GetAPIResponseData();
				
				Map<String, String> map = new LinkedHashMap<>();
				map = dat.getGarageAllTrackPositions();
				
				//Get all the keys into a set
				Set set = map.keySet();
				
				//Iterate over the keys to get values
				Iterator itr = set.iterator();
				
				while (itr.hasNext()) {
					 String key = (String)itr.next();
					 String value = map.get(key).toString();
					 prop.setProperty(key, value);
				}
				
				//write all properties to the file
				prop.store(new FileOutputStream(fileName), "");
			}
			
			//Read the properties file
			prop = base.readPropertiesFile(fileName);
			
			//Convert all the properties to a Map
			garageTrackPositionsMap = prop.entrySet().stream().collect(
					Collectors.toMap(
							e -> e.getKey().toString(),
							e -> e.getValue().toString()
							)
					);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return garageTrackPositionsMap;
	}
	
	public static List<String> getBusVinListByName(List<String> busNameList){
		List<String> vinList = new ArrayList<String>();
		try {

			//Read the bus list properties file 
			prop = base.readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/busList.properties");
			
			//Create new List
			for(String busName: busNameList) {
				vinList.add(prop.getProperty(busName));
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return vinList;
		
	}
	
	
	public String getBusVinByName(String busName){
		String busVin = null;
		try {

			//Read the bus list properties file 
			prop = base.readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/busList.properties");
			
			//Get All Keys - Vin # from the properties file
			busVinSet = prop.keySet();
			
			//Get All Bus Vin#
			for (Object key : busVinSet) {
				if (prop.getProperty(key.toString()).equals(busName)) {
					busVin = key.toString();
				}
			}
			
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return busVin;
		
	}

}
