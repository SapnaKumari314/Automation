package test.java.com.proterra.testcases.ccssAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;
import main.java.com.proterra.ExtentListeners.ExtentListeners;

public class APIBaseTest {

	//Object instantiation
	public static Logger log = Logger.getLogger(APIBaseTest.class.getName());


	//Variable declaration
	public static String USER_NAME="";
	public static String PASSWORD="";

	public static String ENV_NAME="qa";
	public static String GARAGE_NAME = "Centennial";

	public static String MODULE;
	public static String ACCESS_TOKEN;
	public static String TENANT_ID;
	public static String GARAGE_ID;
	public static String GARAGE_NUMBER;
	public static Map<String, String> GARAGE_MAP = new LinkedHashMap<>();


	/*-----------------------------------------Execution Variables---------------------------------------------------*/
	//		public static String ENV_NAME=System.getProperty("env.Name");
	//		public static String GARAGE_NAME= System.getProperty("garage.Name");
	//	public static String TEST_TYPE = System.getProperty("test.Type");

	/*--------------------------------------------------------------------------------------------------------------------*/

	//Get tenantID
	public static String getTenantID() {

		switch (ENV_NAME) {
		case "dev":
			TENANT_ID = "2cbbfd0a-0ff7-4062-89da-bf2fa63807b7";
			break;

		case "qa":
			TENANT_ID = "90006981-51a0-48df-b31d-d66b2fb205e6";
			break;

		case "ets-test":
			TENANT_ID = "f83c33ad-760e-4041-a8d4-b92d79d1f35e";
			break;

		case "stage":
			TENANT_ID = "9acc4a00-9da9-4b90-8bc1-1769bedcfd78";
			break;

		default:
			break;
		}
		return TENANT_ID;
	}


	//Get Base Path
	public static String getBASE_PATH() {
		String basePath = String.format("https://exp-api.proterra.com/exp-%s-%s/", MODULE, ENV_NAME);
		return basePath;
	}


	//Get Login Path
	public static String getPATH_LOGIN() {
		return "v1/cognitoLogin";
	}


	//Get Assets Path
	public static String getPATH_ASSETS(String tenantId) {
		String path = String.format("v1/tenants/%s/assets", tenantId);
		return path;
	}


	//Get Garages Path
	public static String getPATH_GARAGES(String tenantId) {
		String path = String.format("v1/tenants/%s/trackSetups", tenantId);
		return path;
	}


	//Get Tracks Setup Path
	public static String getPATH_TRACKSETUP(String tenantId, String trackSetupId) {
		String path = String.format("v1/tenants/%s/trackSetups/%s", tenantId, trackSetupId);
		return path;
	}


	//Get Incoming Buses Path
	public static String getPATH_INCOMINGBUSES(String tenantId, String trackSetupId) {
		String path = String.format("iq/tenants/%s/garages/%s/incomingBuses", tenantId, trackSetupId);
		return path;
	}


	//Get Bus Details Path
	public static String getPATH_GETBUSDETAILS(String tenantId, String trackSetupId) {
		String path = String.format("v1/tenants/%s/garages/%s/bus/getBusDetails", tenantId, trackSetupId);
		return path;
	}


	//Get M5 Bus Status
	public static String getPATH_GETM5BUSSTATUS(String tenantId, String garageNumber) {
		String path = String.format("v1/tenants/%s/garages/%s/getm5busstatus", tenantId, garageNumber);
		return path;
	}


	//Get Notification Path
	public static String getPATH_NOTIFICATIONS(String tenantId) {
		String path = String.format("v1/tenants/%s/notifications", tenantId);
		return path;
	}


	//Get Logical Charge Queue Path
	public static String getPATH_LOGICALCHARGEQUEUE(String tenantId, String trackSetupId) {
		String path = String.format("v1/tenants/%s/trackSetups/%s/logicalChargeQueue", tenantId, trackSetupId);
		return path;
	}


	//Get Charger States Path
	public static String getPATH_CHARGERSTATES(String tenantId) {
		String path = String.format("v1/tenants/%s/chargerStates", tenantId);
		return path;
	}


	//Get Charger Faults Path
	public static String getPATH_CHARGERFAULTS() {
		String path = String.format("v1/connectedportal/counts/charger");
		return path;
	}


	//Get Charger Sessions Path
	public static String getPATH_CHARGERSESSIONS() {
		String path = String.format("v1/connectedportal/chargers/chargeSessions");
		return path;
	}


	//Get Bus Faults Path
	public static String getPATH_BUSFAULTS() {
		String path = String.format("v1/connectedportal/counts/bus");
		return path;
	}


	//Get Monitoring Path
	public static String getPATH_ECMSMONITORING(String tenantId, String trackSetupId) {
		String path = String.format("v1/tenants/%s/garages/%s/ecms/monitoring", tenantId, trackSetupId);
		return path;
	}


	//Get Power Path
	public static String getPATH_ECMSPOWER(String tenantId, String trackSetupId) {
		String path = String.format("v1/tenants/%s/garages/%s/ecms/power", tenantId, trackSetupId);
		return path;
	}



	/*--------------------------------------------------------------------------------------------------------------------*/






	/*--------------------------------------------------------------------------------------------------------------------*/

	//UUID Generator
	public String generateUUID() {

		UUID uuid = null;
		try {
			uuid = UUID.randomUUID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid.toString();

	}

	//Convert POJO to JSON Object 
	public JSONObject pojoToJonObject(Object object) {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject (object);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	//Convert API Response to JSON Object
	public JSONObject responseToJson(Response response) {
		String responseBody = response.getBody().asString();
		JSONObject obj = new JSONObject(responseBody);
		return obj;
	}


	//Convert API Response to JSON Array
	public JSONArray responseToJsonArray(Response response) {
		String responseBody = response.getBody().asString();
		JSONArray obj = new JSONArray(responseBody);
		return obj;
	}


	//Add a non assertion step to the report
	public void reportStep(Status status, String message) {

		switch (status) {
		case INFO:
			ExtentListeners.testReport.get().info(message);
			break;

		case PASS:
			ExtentListeners.testReport.get().pass(message);
			break;

		case FAIL:
			ExtentListeners.testReport.get().fail(message);
			break;

		case WARNING:
			ExtentListeners.testReport.get().warning(message);
			break;

		case SKIP:
			ExtentListeners.testReport.get().skip(message);
			break;

		case ERROR:
			ExtentListeners.testReport.get().error(message);
			break;

		default:
			ExtentListeners.testReport.get().info(message);
			break;
		}

	}


	//Read Properties File
	public Properties readPropertiesFile(String fileName) throws IOException {

		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}

		return prop;
	}
	
	
	//Write to Properties File
	public void writeToPropertiesFile(Map<String, String> propDataMap, String fileName) throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();
		
		//Get all the keys into a set
		Set<String> set = propDataMap.keySet();
		
		//Iterate over the keys to get values
		Iterator<String> itr = set.iterator();
		
		while (itr.hasNext()) {
			 String key = (String)itr.next();
			 String value = propDataMap.get(key);
			 prop.setProperty(key, value);
		}
		
		//write all properties to the file
		prop.store(new FileOutputStream(fileName), "");
		
	}


	//Configure Logger
	public void configureLogging() {
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);
	}


	/*-----------------------------------------------------------*/


	/*-----------------------Setup and Tear Down-------------------------*/

	@BeforeSuite
	public void setUpFramework() {
		configureLogging();

	}


	/*-----------------------------------------------------------*/


}
