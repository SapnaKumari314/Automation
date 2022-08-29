package test.java.com.proterra.testcases.ccssAPI.realTimeData;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.DateUtils;
import test.java.com.proterra.testcases.ccssAPI.APIBaseTest;
import test.java.com.proterra.testcases.ccssAPI.Modules;
import test.java.com.proterra.testcases.ccssAPI.chargerStatesAPI.ChargerStatesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loadGaragesAPI.LoadGaragesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loginAPI.LoginAPITest;

public class ChargerRTData extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(ChargerStatesAPITest.class.getName());
	public static SoftAssertLogger sAssert;
	public static LoginAPITest loginTest;
	public static LoadGaragesAPITest garageTest;
	public static Properties props;
	public static DateUtils dateUtil;



	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test
	(
			description = "Validate the OCPP Server Data is used for Chargers"
			)
	public void CCSS_API_GetChargerStatesOCPPData() throws IOException, ParseException {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Map for all the attributes to be validated
		Map<String, String> attributeMap = new LinkedHashMap<>();
		attributeMap.put("chargerStatus", "ocppServer_chargerStatus");
		attributeMap.put("errorChargingCapabilityNotAffected", "ocppServer_errorChargingCapabilityNotAffected");
		attributeMap.put("errorPCSOrDispenser", "ocppServer_errorPCSOrDispenser");
		attributeMap.put("errorPriority", "ocppServer_errorPriority");
		attributeMap.put("errorResolution", "ocppServer_errorResolution");
		attributeMap.put("errorType", "ocppServer_errorType");
		attributeMap.put("ocppErrorCode", "ocppServer_ocppErrorCode");
		attributeMap.put("ocppErrorDescription", "ocppServer_ocppErrorDescription");
		attributeMap.put("ocppErrorDetailedDescription", "ocppServer_ocppErrorDetailedDescription");
		attributeMap.put("ocppVendorErrorCode", "ocppServer_ocppVendorErrorCode");


		//Get a all chargers
		props = readPropertiesFile(System.getProperty("user.dir") + "/src/test/resources/properties/chargerGarageMapping.properties");

		Map<String, String> chargerMap = new LinkedHashMap<String, String>();
		Set<String> chargerKeySet = props.stringPropertyNames();
		for (String ocpp_id : chargerKeySet) {
			String[] chargerPropValue = props.get(ocpp_id).toString().split(",");
			String chargerName = chargerPropValue[3];

			chargerMap.put(ocpp_id, chargerName);
		}

		//Generate Access Token
		loginTest.generateAccessToken();

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSTATES(TENANT_ID);

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", ACCESS_TOKEN);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGING_STATES-"+generateUUID());

		//Get Response
		Response response = request.request(Method.GET, path);

		//Validate the Response
		int status = response.statusCode();
		sAssert.assertEquals(status, 200, "Charger States API - Status Code for Valid user");

		//Get the Response Body
		String responseBody = response.getBody().asString();
		JSONObject jObj = new JSONObject(responseBody);

		//Get the length of the response object and compare it with the requested list of chargers
		sAssert.assertEquals(jObj.length(), chargerMap.size(), "Number of chargers returned in the chargerStates API response");

		//Iterate through the response for every charger
		Iterator<String> jObjItr = jObj.keys();
		while(jObjItr.hasNext()) {
			String assetKey = jObjItr.next();

			reportStep(Status.INFO, "---------------------------------------------------------------------------------");

			//Get the Asset Object
			JSONObject chargerObj = jObj.getJSONObject(assetKey);

			//Check if the OCPP ID and Asset have a match
			String chargerName = null;
			String ocppId;
			if (chargerObj.has("chargerName") || !(chargerObj.isNull("chargerName"))) {
				ocppId = chargerObj.getString("chargerName");
				chargerName = chargerMap.get(ocppId);
				reportStep(Status.PASS, "The "+chargerName+" exists / matches the asset "+assetKey);
			}else {
				reportStep(Status.FAIL, "The "+chargerName+" does not exist or does not match for the asset "+assetKey);
				continue;
			}


			//Check if the Timestamp fields are available
			String ocppServer_updatedTimestamp = null;
			String updatedTimestamp = null;
			Date ocppDate = null;
			Date updatedDate = null;
			boolean chargeflag=false;
			boolean ocppflag=false;

			if (!(chargerObj.has("updatedTimestamp"))) {
				reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the updatedTimestamp attribute");
			}else if(chargerObj.isNull("updatedTimestamp")) {
				reportStep(Status.SKIP, "The Charger "+chargerName+" does not have the updatedTimestamp attribute data");
			}else {
				updatedTimestamp = chargerObj.getString("updatedTimestamp");
				reportStep(Status.PASS, "The Charger "+chargerName+" has the updatedTimestamp attribute data");

				updatedDate = dateUtil.convertStringToUTCDateTime(updatedTimestamp);
				
			}

			if (!(chargerObj.has("ocppServer_updatedTimestamp"))) {
				reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the ocppServer_updatedTimestamp attribute");
			}else if(chargerObj.isNull("ocppServer_updatedTimestamp")) {
				reportStep(Status.SKIP, "The Charger "+chargerName+" does not have the ocppServer_updatedTimestamp attribute data");
			}else {
				ocppServer_updatedTimestamp = chargerObj.getString("ocppServer_updatedTimestamp");
				reportStep(Status.PASS, "The Charger "+chargerName+" has the ocppServer_updatedTimestamp attribute data");

				ocppDate = dateUtil.convertStringToUTCDateTime(ocppServer_updatedTimestamp);
			}


			//Compare the Timestamps
			if (ocppflag==true && (ocppDate.equals(updatedDate) || ocppDate.after(updatedDate))) {


				//Validate all the fields from real time and compare them with the actual fields
				for (Map.Entry<String, String> attributeEntry:attributeMap.entrySet()) {

					System.out.println("Charger "+chargerName+" : "+ocppId+" : "+attributeEntry.getKey() + " : " +attributeEntry.getValue());

					boolean nonOcppServerFieldFlag = false;
					boolean ocppServerFieldFlag = false;

					//Verify if the fields exist
					if (!(chargerObj.has(attributeEntry.getKey()))) {
						reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the field "+attributeEntry.getKey());
					}else if(chargerObj.isNull(attributeEntry.getKey())) {
						reportStep(Status.SKIP, "The Charger "+chargerName+" has a null value for the field "+attributeEntry.getKey());
					}else {
						nonOcppServerFieldFlag = true;
					}


					if (!(chargerObj.has(attributeEntry.getValue()))) {
						reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the field "+attributeEntry.getValue());
					}else if(chargerObj.isNull(attributeEntry.getValue())) {
						reportStep(Status.SKIP, "The Charger "+chargerName+" has a null value for the field "+attributeEntry.getValue());
					}else {
						ocppServerFieldFlag = true;
					}

					if ((nonOcppServerFieldFlag == true) && (ocppServerFieldFlag == true)) {
						sAssert.assertEquals(
								chargerObj.get(attributeEntry.getKey()).toString(), 
								chargerObj.get(attributeEntry.getValue()).toString(), 
								"The "+attributeEntry.getKey()+" and "+attributeEntry.getValue()+" for the Charger "+chargerName+" "+ocppId+" are equal");
					}

				}

				reportStep(Status.INFO, "---------------------------------------------------------------------------------");

			}else {
				reportStep(Status.INFO, "The Charger "+chargerName+" does not have the latest ocppServer_updatedTimestamp");
				for (Map.Entry<String, String> attributeEntry:attributeMap.entrySet()) {

					System.out.println("Charger "+chargerName+" : "+ocppId+" : "+attributeEntry.getKey() + " : " +attributeEntry.getValue());

					//Verify if the fields exist
					if (!(chargerObj.has(attributeEntry.getKey()))) {
						reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the field "+attributeEntry.getKey());
					}else if(chargerObj.isNull(attributeEntry.getKey())) {
						reportStep(Status.SKIP, "The Charger "+chargerName+" has a null value for the field "+attributeEntry.getKey());
					}else {
						reportStep(Status.INFO, "The Charger "+chargerName+" has the field "+attributeEntry.getKey()+" but data is not real time");
					}


					if (!(chargerObj.has(attributeEntry.getValue()))) {
						reportStep(Status.FAIL, "The Charger "+chargerName+" does not have the field "+attributeEntry.getValue());
					}else if(chargerObj.isNull(attributeEntry.getValue())) {
						reportStep(Status.SKIP, "The Charger "+chargerName+" has a null value for the field "+attributeEntry.getValue());
					}else {
						reportStep(Status.INFO, "The Charger "+chargerName+" has the field "+attributeEntry.getValue()+" but data is not real time");
					}
				}
				reportStep(Status.INFO, "---------------------------------------------------------------------------------");
			}
		}

		//Print the response body
		reportStep(Status.INFO, responseBody);

		sAssert.assertAll();


	}



	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {

		loginTest = new LoginAPITest();
		garageTest =  new LoadGaragesAPITest();
		getTenantID();
		dateUtil = new DateUtils();
	}

}
