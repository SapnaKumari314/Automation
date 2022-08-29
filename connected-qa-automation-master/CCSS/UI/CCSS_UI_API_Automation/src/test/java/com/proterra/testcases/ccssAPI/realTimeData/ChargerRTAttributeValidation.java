package test.java.com.proterra.testcases.ccssAPI.realTimeData;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.DateUtils;
import test.java.com.proterra.testcases.ccssAPI.APIBaseTest;
import test.java.com.proterra.testcases.ccssAPI.Modules;
import test.java.com.proterra.testcases.ccssAPI.chargerStatesAPI.ChargerStatesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loadGaragesAPI.LoadGaragesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loginAPI.LoginAPITest;

public class ChargerRTAttributeValidation extends APIBaseTest implements Modules{

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
			description = "Validate the OCPP Server Attribues are available for Chargers"
			)
	public void CCSS_API_GetChargerStatesOCPPData() throws IOException, ParseException {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Map for all the attributes to be validated
		//List for all the attributes to be validated
		List<String> attributeList = new LinkedList<>();
		attributeList.add("ocppServer_chargerStatus");
		attributeList.add("ocppServer_errorChargingCapabilityNotAffected");
		attributeList.add("ocppServer_errorPCSOrDispenser");
		attributeList.add("ocppServer_errorPriority");
		attributeList.add("ocppServer_errorResolution");
		attributeList.add("ocppServer_errorType");
		attributeList.add("ocppServer_ocppErrorCode");
		attributeList.add("ocppServer_ocppErrorDescription");
		attributeList.add("ocppServer_ocppErrorDetailedDescription");
		attributeList.add("ocppServer_ocppVendorErrorCode");


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


			if (!(chargerObj.has("ocppServer_updatedTimestamp"))) {
				reportStep(Status.FAIL, "ocppServer_updatedTimestamp: field not available for Charger :"+chargerName);
			}else if(chargerObj.isNull("ocppServer_updatedTimestamp")) {
				reportStep(Status.SKIP, "ocppServer_updatedTimestamp: field data not available for Charger :"+chargerName);
			}else {
				reportStep(Status.PASS, "ocppServer_updatedTimestamp: field is available for Charger :"+chargerName);
			}

			//Validate all the fields from real time and compare them with the actual fields
			for (String attrib : attributeList) {


				//Verify if the fields exist
				if (!(chargerObj.has(attrib))) {
					reportStep(Status.FAIL, attrib+": field not available for Charger :"+chargerName);
				}else if(chargerObj.isNull(attrib)) {
					reportStep(Status.SKIP, attrib+": field data not available for Charger :"+chargerName);
				}else {
					reportStep(Status.PASS, attrib+": field is available for Charger :"+chargerName);
				}


			}

			reportStep(Status.INFO, "---------------------------------------------------------------------------------");
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
