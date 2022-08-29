package test.java.com.proterra.testcases.apexAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
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
import main.java.com.proterra.utilities.DateUtils;

public class APIBaseTest {

	//Object instantiation
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public DateUtils dateUtils = new DateUtils();

	//Variable declaration
	public static String USER_NAME="";
	public static String PASSWORD="";


	public static String BASE_PATH;
	public static String MODULE;
	public static String ACCESS_TOKEN;
	public static String TENANT_ID;
	public static String GARAGE_ID;
	public static String GARAGE_NUMBER;
	public static Map<String, String> GARAGE_MAP = new LinkedHashMap<>();


	/*-----------------------------------------Execution Variables---------------------------------------------------*/
	public static String ENV_NAME=System.getProperty("env.Name");
	public static String GARAGE_NAME= System.getProperty("garage.Name");
	public static String TEST_TYPE = System.getProperty("test.Type");
	public static String API_KEY = System.getProperty("api.Key");

	/*--------------------------------------------------------------------------------------------------------------------*/

	//Get tenantID
	public static String getTenantID() {

		switch (ENV_NAME) {
		case "dev":
			TENANT_ID = "2cbbfd0a-0ff7-4062-89da-bf2fa63807b7";
			break;

		case "qa-launch2020":
			TENANT_ID = "90006981-51a0-48df-b31d-d66b2fb205e6";
			break;

		case "ets-test":
			TENANT_ID = "f83c33ad-760e-4041-a8d4-b92d79d1f35e";
			break;

		case "stage":
			TENANT_ID = "9acc4a00-9da9-4b90-8bc1-1769bedcfd78";
			break;

		case "prod2":
			TENANT_ID = "b9ed83eb-5a8d-489c-9639-076d408be8a4";
			break;

		default:
			break;
		}
		return TENANT_ID;
	}


	//Get Base Path
	public static String getBASE_PATH() {

		switch (ENV_NAME) {
		case "qa-launch2020":
			BASE_PATH = String.format("https://exp-api.proterra.com/exp-%s-%s/", MODULE, ENV_NAME);
			break;
		case "dev":
			BASE_PATH = String.format("https://exp-api.proterra.com/exp-%s-%s/", MODULE, ENV_NAME);
			break;
		case "prod2":
			BASE_PATH = String.format("https://api.proterra.com/%s-%s/", MODULE, ENV_NAME);
			break;
		default:
			break;
		}

		return BASE_PATH;
	}


	//Get Login Path
	public static String getPATH_LOGIN() {
		return "v1/cognitoLogin";
	}


	//Get Audit Events External Path
	public static String getPATH_EXTERNAL_EVENTS(String tenantId, String trackSetupId) {
		String path = String.format("v1/external/tenants/%s/garages/%s/audit/events", tenantId, trackSetupId);
		return path;
	}


	//Get Metrics External Path
	public static String getPATH_CHARGER_METRICS() {
		String path = String.format("v1/external/chargerMetrics");
		return path;
	}


	//Get Fleet Metrics External Path
	public static String getPATH_FLEET_EXTERNAL_METRICS() {
		String path = String.format("v1/external/fleetMetrics");
		return path;
	}


	//Get All Products 
	public static String getPATH_ALL_PRODUCTS() {
		String path = String.format("ams/v1/product");
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
	public JSONObject pojoToJsonObject(Object object) {
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
			System.out.println(message);
			break;

		case PASS:
			ExtentListeners.testReport.get().pass(message);
			System.out.println(message);
			break;

		case FAIL:
			ExtentListeners.testReport.get().fail(message);
			System.err.println(message);
			break;

		case WARNING:
			ExtentListeners.testReport.get().warning(message);
			System.out.println(message);
			break;

		case SKIP:
			ExtentListeners.testReport.get().skip(message);
			System.out.println(message);
			break;

		case ERROR:
			ExtentListeners.testReport.get().error(message);
			System.err.println(message);
			break;

		default:
			ExtentListeners.testReport.get().info(message);
			System.out.println(message);
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
