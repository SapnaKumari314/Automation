package test.java.com.proterra.testcases.apexAPI.externalAPI;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import test.java.com.proterra.testcases.apexAPI.APIBaseTest;

public class EventsAPIChargingTerminatedRecordsTest extends APIBaseTest{

	//Initiate Logger
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public static SoftAssertLogger sAssert;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(description = "Validate the Events API log for every CHARGING_TERMINATED record to have a corresponding CHARGING_INITIATED record")
	@Parameters({"startDate","endDate","garageID","type"})
	public void ValidateEventsDataChargingTerminated(@Optional String startDate, @Optional String endDate, String garageID, @Optional String type) {

		//Assertions
		sAssert = new SoftAssertLogger();
		
		reportStep(Status.INFO, "Validating Events API for CHARGING_INITIATED and CHARGING_TERMINATED record pairs for garageID: "+garageID);

		//Set the module
		MODULE = "dataLake";

		//Set the dates
		if (startDate == null || endDate == null) {
			startDate = dateUtils.addRemoveDaysToCurrentDate(dateUtils.getCurrentUTCDateTime(), "yyyy-MM-dd", -1) + "T00:00:00-07:00";
			endDate = dateUtils.addRemoveDaysToCurrentDate(dateUtils.getCurrentUTCDateTime(), "yyyy-MM-dd", 0) + "T00:00:00-07:00";
		}

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_EXTERNAL_EVENTS(TENANT_ID, garageID);

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("x-api-key", API_KEY);

		//Add Query Parameters
		request.queryParam("startDate", startDate);
		request.queryParam("endDate", endDate);
		if (type != null) {
			request.queryParam("type", type);
		}

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();

		String body = response.getBody().asString();
		log.info(body);
		boolean bool = false;
		if (body.isBlank() || body.isEmpty()) {
			bool = true;
		}

		//Validate the Response
		sAssert.assertEquals(status, 200, "Events API - Valid user");
		sAssert.assertEquals(bool, false, "Events API - Response Body is not empty");

		//Get the APi Response as a JSON Object
		JSONObject jObj = new JSONObject(body);
		String respMessage = jObj.getString("message");
		sAssert.assertEquals(respMessage,"Successfully fetched audit details for tenantId: "+TENANT_ID+" and garageId: "+garageID, "");
		System.out.println(jObj.get("message"));

		//Get Audit Data array
		JSONArray auditDataArr = jObj.getJSONArray("auditData");


		//Loop the array to validate all Audit Logs
		for (int i = 0; i < auditDataArr.length(); i++) {
			JSONObject auditLogObj = auditDataArr.getJSONObject(i);

			String logTimeStamp = auditLogObj.getString("timestamp");
			String eventType = auditLogObj.getString("type");
			String busVIN = auditLogObj.getString("busVIN");
			String ocppId = auditLogObj.getString("ocppId");

			//			System.out.println("*******************************************************");

			//Check if event type is CHARGING_TERMINATED
			if (eventType.equals("CHARGING_TERMINATED")) {

				//If yes, loop through the previous records
				for(int j = i-1; j >= 0; j--) {
					JSONObject preAuditLogObj = auditDataArr.getJSONObject(j);

					String preEventType = preAuditLogObj.getString("type");
					String preBusVIN = preAuditLogObj.getString("busVIN");
					String preOcppId = preAuditLogObj.getString("ocppId");
					String preLogTimeStamp = preAuditLogObj.getString("timestamp");

					//Check if previous event type is CHARGING_INITIATED
					if (preEventType.equals("CHARGING_TERMINATED") && preBusVIN.equals(busVIN) && preOcppId.equals(ocppId)) {
						reportStep(Status.FAIL, "CHARGING_TERMINATED record with timestamp "+logTimeStamp+" has another CHARGING_TERMINATED record with timestamp "+preLogTimeStamp+"before a CHARGING_INITIATED record");
						break;
					}else if (preEventType.equals("CHARGING_INITIATED") && preBusVIN.equals(busVIN) && preOcppId.equals(ocppId)) {
						break;
					}else if(j == 0) {
						//TODO: Add logic to re-run API with previous start and end dates and find a CHARGING_INITIATED Record
					}
				}
			}else {
				continue;
			}
		}

		sAssert.assertAll();
	}


	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {

		getTenantID();
	}
}
