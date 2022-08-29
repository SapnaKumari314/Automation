package test.java.com.proterra.testcases.ccssAPI.realTimeData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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

public class BusRTDataAtrributeValidation  extends APIBaseTest implements Modules{

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
			description = "Validate the Real Time Data attributes are available for Bus Details"
			)
	public void CCSS_API_GetBusDetailsRealTimeData() throws ParseException, IOException {


		//Assertions
		sAssert = new SoftAssertLogger();

		//List for all the attributes to be validated
		List<String> attributeList = new LinkedList<>();
		attributeList.add("rt_Altitude_m");
		attributeList.add("rt_Latitude_deg");
		attributeList.add("rt_Longitude_deg");
		attributeList.add("rt_PCac_usi_AmbientTemp_c");
		attributeList.add("rt_PCac_usi_CabinTemp_c");
		attributeList.add("rt_PCbo_bo_KeyOn");
		attributeList.add("rt_PCbo_bo_PTHVReady");
		attributeList.add("rt_PCbo_usi_AvgEff_kwhpmi");
		attributeList.add("rt_PCbo_usi_DashSOC_pct");
		attributeList.add("rt_PCes_uin_CurrentIn_a");
		attributeList.add("rt_PCes_uin_V_v");
		attributeList.add("rt_PCes_usi_DashSoC_pct");
		attributeList.add("rt_PCes_usi_SystemEnergy_kwh");
		attributeList.add("rt_PCpt_int_Spd_mph");
		attributeList.add("rt_PCtc_udi_Odometer_mi");
		attributeList.add("rt_PCvi_en_VehicleState");
		attributeList.add("rt_batteryDisconnectLowVoltage");
		attributeList.add("rt_batteryVoltageLowVoltage");


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

					if (!(readingTimestampObject.has("rtReadings"))){
						reportStep(Status.FAIL, "rtReadings: field not available for Bus :"+key);
					}else if(readingTimestampObject.isNull("rtReadings")) {
						reportStep(Status.SKIP, "rtReadings: field value is not available for Bus :"+key);
					}else{
						reportStep(Status.PASS, "rtReadings: field available for Bus :"+key);
					}
					
					//Validate all the fields from real time and compare them with the actual fields
					for (String attrib : attributeList) {
						if (!(sourceObject.has(attrib))) {
							reportStep(Status.FAIL, attrib+": field is not available for the Bus :"+key);
						}else if(sourceObject.isNull(attrib)) {
							reportStep(Status.FAIL, attrib+": value is not available for the Bus :"+key);
						}else {
							reportStep(Status.PASS, attrib+": value is available for the Bus :"+key);
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
