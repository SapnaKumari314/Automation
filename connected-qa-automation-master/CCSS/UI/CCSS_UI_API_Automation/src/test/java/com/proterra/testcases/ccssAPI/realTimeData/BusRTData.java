package test.java.com.proterra.testcases.ccssAPI.realTimeData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.APIPojo.GetBusDetailsPojo;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.DateUtils;
import test.java.com.proterra.testcases.ccssAPI.APIBaseTest;
import test.java.com.proterra.testcases.ccssAPI.Modules;
import test.java.com.proterra.testcases.ccssAPI.getBusDetailsAPI.GetBusDetailsAPITest;
import test.java.com.proterra.testcases.ccssAPI.loadGaragesAPI.LoadGaragesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loginAPI.LoginAPITest;

public class BusRTData  extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(GetBusDetailsAPITest.class.getName());
	public static SoftAssertLogger sAssert;
	public static LoginAPITest loginTest;
	public static LoadGaragesAPITest garageTest;
	public static GetBusDetailsPojo getBusDetailsPojo;
	public static DateUtils dateUtil;
	public static Properties props;


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test
	(
			description = "Validate the Real Time Data is used for Bus Details"
			)
	public void CCSS_API_GetBusDetailsRealTimeData() throws ParseException, IOException {


		//Assertions
		sAssert = new SoftAssertLogger();

		//Map for all the attributes to be validated
		Map<String, String> attributeMap = new LinkedHashMap<>();
		attributeMap.put("PCes_usi_SystemEnergy_kwh", "rt_PCes_usi_SystemEnergy_kwh");
		attributeMap.put("PCes_usi_DashSoC_pct", "rt_PCes_usi_DashSoC_pct");
		attributeMap.put("PCac_usi_CabinTemp_c", "rt_PCac_usi_CabinTemp_c");
		attributeMap.put("PCtc_udi_Odometer_mi", "rt_PCtc_udi_Odometer_mi");
		attributeMap.put("PCac_usi_AmbientTemp_c", "rt_PCac_usi_AmbientTemp_c");
		attributeMap.put("PCbo_usi_AvgEff_kwhpmi", "rt_PCbo_usi_AvgEff_kwhpmi");
		attributeMap.put("PCbo_usi_AvgEff_kwhpmi", "rt_PCbo_usi_AvgEff_kwhpmi");
		attributeMap.put("PCbo_usi_AvgEff_kwhpmi", "rt_PCbo_usi_AvgEff_kwhpmi");
		attributeMap.put("Longitude_deg", "rt_Longitude_deg");
		attributeMap.put("Latitude_deg", "rt_Latitude_deg");
		attributeMap.put("PCbo_bo_PTHVReady", "rt_PCbo_bo_PTHVReady");

		//Generate Access Token
		loginTest.generateAccessToken();

		//Iterate through each garage to run the API for all buses
		for (Map.Entry<String, String> entry:GARAGE_MAP.entrySet()) {

			reportStep(Status.INFO, "Running test for the Garage "+entry.getKey()+" with Track Setup ID "+entry.getValue());

			//Set the module
			MODULE = Modules.MODULE_CCSS;

			//Set base
			String base = getBASE_PATH();

			//Set the Base URI
			RestAssured.baseURI = base;

			//Set path
			String path = getPATH_GETBUSDETAILS(TENANT_ID, entry.getValue());

			//Create the Request object
			RequestSpecification request = RestAssured.given();

			//Add Headers
			request.header("Content-Type", "application/json");
			request.header("authorization", ACCESS_TOKEN);

			//Send the Request Payload with all buses
			List<String> busVin = new ArrayList<String>();
			props = readPropertiesFile(System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
					+ "busGarageMapping.properties");
			Set<String> busKeySet = props.stringPropertyNames();
			for (String key : busKeySet) {
				String value = props.getProperty(key);

				if (value.equals(entry.getKey())) {
					busVin.add(key);
				}
			}


			getBusDetailsPojo.setId("GET_BUS_DETAILS-"+generateUUID());
			getBusDetailsPojo.setBusVins(busVin);
			String payload= String.valueOf(pojoToJonObject(getBusDetailsPojo));
			request.body(payload);


			//Get Response
			Response response = request.request(Method.POST, path);

			//Validate the Response
			int status = response.statusCode();
			sAssert.assertEquals(status, 200, "Get Bus Details API - Status Code for Valid user");

			//Get the Response Body
			String responseBody = response.getBody().asString();
			JSONObject jObj = new JSONObject(responseBody);

			//Get the Buses JOSN Object from the response
			JSONObject busesObject = jObj.getJSONObject("buses");

			//Get the length of the buses object and compare it with the requested list of buses
			sAssert.assertEquals(busesObject.length(), busVin.size(), "Number of buses returned in the response");

			//Iterate through the busesObject and validate if all the buses are returned in the response
			Iterator<String> keys = busesObject.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if (busesObject.get(key) instanceof JSONObject) {
					if (!(busVin.contains(key))) {
						continue;
					}

					//Get the Bus JSON Object
					JSONObject busVinObject = busesObject.getJSONObject(key);

					//Get the source Json Object
					JSONObject sourceObject = busVinObject.getJSONObject("_source");

					//Get Reading Timestamp Object
					JSONObject readingTimestampObject = sourceObject.getJSONObject("readingTimestamp");

					//Get rt timestamp
					String rtTimestamp;
					Date rtDate;
					Date curDate;

					if (!(readingTimestampObject.has("rtReadings"))){
						reportStep(Status.FAIL, "Real Time Data rtReadings field not available for Bus "+key);
						continue;
					}else if(readingTimestampObject.isNull("rtReadings")) {
						reportStep(Status.SKIP, "Real Time Data rtReadings field value is not available for Bus "+key);
						continue;
					}
					
					rtTimestamp = readingTimestampObject.getString("rtReadings");

					//Convert rt timestamp string to date
					rtDate = dateUtil.convertStringToUTCDateTime(rtTimestamp);

					//Get Current UTC Timestamp
					curDate = dateUtil.getCurrentUTCDateTime();

					reportStep(Status.INFO, "---------------------------------------------------------------------------------");
					//Check if rt timestamp is <= 5 mins from current timestamp
					if (curDate.getTime() - rtDate.getTime() <= 5*60*1000) {
						reportStep(Status.SKIP, "Real Time Data latest Timestamp is not available for Bus "+key);
					}

					//Validate all the fields from real time and compare them with the actual fields
					for (Map.Entry<String, String> attributeEntry:attributeMap.entrySet()) {

						if (!(sourceObject.has(attributeEntry.getValue()))) {
							reportStep(Status.FAIL, attributeEntry.getValue()+" field is not available for the Bus "+key);
						}else if(sourceObject.isNull(attributeEntry.getValue())) {
							reportStep(Status.FAIL, attributeEntry.getValue()+" value is not available for the Bus "+key);
						}else if(curDate.getTime() - rtDate.getTime() > 5*60*1000){
							reportStep(Status.INFO, "The Field "+attributeEntry.getValue()+" is available but data is not realtime for the Bus "+key);
						}else {
							sAssert.assertEquals(sourceObject.get(attributeEntry.getKey()).toString(), sourceObject.get(attributeEntry.getValue()).toString(), attributeEntry.getKey()+" is using real time data for the Bus "+key);
						}
					}
					reportStep(Status.INFO, "---------------------------------------------------------------------------------");

				}
			}

			//Print the response body
			reportStep(Status.INFO, responseBody);
		}



		sAssert.assertAll();

	}


	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {

		loginTest = new LoginAPITest();
		garageTest =  new LoadGaragesAPITest();
		getBusDetailsPojo = new GetBusDetailsPojo();
		dateUtil = new DateUtils();
		getTenantID();
	}

}
