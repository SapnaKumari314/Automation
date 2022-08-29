package test.java.com.proterra.testcases.apexAPI.externalAPI;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

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

public class EventsAPINonNullTest extends APIBaseTest{

	//Initiate Logger
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public static SoftAssertLogger sAssert;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(description = "Validate the Events API log to not have null or blank information")
	@Parameters({"startDate","endDate","garageID","type"})
	public void ValidateEventsDataNotNull(@Optional String startDate, @Optional String endDate, String garageID, @Optional String type) {

		//Assertions
		sAssert = new SoftAssertLogger();
		
		reportStep(Status.INFO, "Validating Events API for Non Null Attributes for garageID: "+garageID);

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

		//Event type List
		List<String> systemGeneratedEventTypeList = new ArrayList<>();
		systemGeneratedEventTypeList.add("CHARGER_STATUS_UPDATE");
		systemGeneratedEventTypeList.add("CHARGING_INITIATED");
		systemGeneratedEventTypeList.add("CHARGING_TERMINATED");
		systemGeneratedEventTypeList.add("BUS_CLASSIFICATION_UPDATE");
		systemGeneratedEventTypeList.add("LCQ_TERMINATE_CHARGING");
		systemGeneratedEventTypeList.add("LCQ_INITIATE_CHARGING");
		systemGeneratedEventTypeList.add("BUS_REMOVED_AT_BOOKOUT");


		//Loop the array to validate all Audit Logs
		for (int i = 0; i < auditDataArr.length(); i++) {
			JSONObject auditLogObj = auditDataArr.getJSONObject(i);

			String auditLog = auditLogObj.toString();
			String logTimeStamp = auditLogObj.getString("timestamp");
			String eventType = auditLogObj.getString("type");

			String failureMessage = " || Type: "+eventType+" || Timestamp: "+logTimeStamp;

			//			System.out.println("*******************************************************");

			//Verify tenantId attribute is present
			if (!(auditLogObj.has("tenantId"))) {
				reportStep(Status.FAIL, "tenantId: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("tenantId").isBlank() || auditLogObj.getString("tenantId").isEmpty() || auditLogObj.isNull("tenantId")) {
				reportStep(Status.FAIL, "tenantId: Null or Blank"+failureMessage);
			} 


			//Verify garageId attribute is present
			if (!(auditLogObj.has("garageId"))) {
				reportStep(Status.FAIL, "garageId: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("garageId").isBlank() || auditLogObj.getString("garageId").isEmpty() || auditLogObj.isNull("garageId")) {
				reportStep(Status.FAIL, "garageId: Null or Blank"+failureMessage);
			}


			//Verify Type
			if (!(auditLogObj.has("type"))) {
				reportStep(Status.FAIL, "type: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("type").isBlank() || auditLogObj.getString("type").isEmpty() || auditLogObj.isNull("type")) {
				reportStep(Status.FAIL, "type: Null or Blank"+failureMessage);
			}else {

				if (systemGeneratedEventTypeList.contains(auditLogObj.getString("type"))) {
					//Verify requestId attribute
					if (auditLogObj.has("requestId")) {
						reportStep(Status.FAIL, "requestId attribute is present for the System generated event log with type "+eventType+" at timestamp "+logTimeStamp+" . Event Log: "+auditLog);
					}

					//Verify payload attribute
					if (auditLogObj.has("payLoad")) {
						reportStep(Status.FAIL, "payLoad attribute is present for the System generated event log with type "+eventType+" at timestamp "+logTimeStamp+" . Event Log: "+auditLog);
					}

					//Verify user attribute
					if (!(auditLogObj.has("user"))) {
						reportStep(Status.FAIL, "user attribute is not present for the System generated event log with type "+eventType+" at timestamp "+logTimeStamp+" . Event Log: "+auditLog);
					}else if(auditLogObj.getString("user").isBlank() || auditLogObj.getString("user").isEmpty() || auditLogObj.isNull("type")) {
						reportStep(Status.FAIL, "user: Null or Blank"+failureMessage);
					}

				}else {
					//Verify requestId attribute
					if (!(auditLogObj.has("requestId"))) {
						reportStep(Status.FAIL, "requestId: Attribute Missing "+failureMessage);
					}else if(auditLogObj.getString("requestId").isBlank() || auditLogObj.getString("requestId").isEmpty() || auditLogObj.isNull("requestId")) {
						reportStep(Status.FAIL, "requestId: Null or Blank"+failureMessage);
					}

					//Verify payload attribute
					if (!(auditLogObj.has("payLoad"))) {
						reportStep(Status.FAIL, "payLoad: Attribute Missing "+failureMessage);
					}else if(auditLogObj.getString("payLoad").isBlank() || auditLogObj.getString("payLoad").isEmpty() || auditLogObj.isNull("payLoad")) {
						reportStep(Status.FAIL, "payLoad: Null or Blank"+failureMessage);
					}

					//Verify user attribute
					if (!(auditLogObj.has("user"))) {
						reportStep(Status.FAIL, "user attribute is not present for the System generated event log with type "+eventType+" at timestamp "+logTimeStamp+" . Event Log: "+auditLog);
					}else if(auditLogObj.getString("user").isBlank() || auditLogObj.getString("user").isEmpty() || auditLogObj.isNull("type")) {
						reportStep(Status.FAIL, "type: Null or Blank"+failureMessage);
					}
				}
			}

			//Verify timestamp attribute
			if (!(auditLogObj.has("timestamp"))) {
				reportStep(Status.FAIL, "timestamp: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("timestamp").isBlank() || auditLogObj.getString("timestamp").isEmpty() || auditLogObj.isNull("timestamp")) {
				reportStep(Status.FAIL, "timestamp: Null or Blank"+failureMessage);
			}


			//Verify module attribute
			if (!(auditLogObj.has("module"))) {
				reportStep(Status.FAIL, "module: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("module").isBlank() || auditLogObj.getString("module").isEmpty() || auditLogObj.isNull("module")) {
				reportStep(Status.FAIL, "module: Null or Blank"+failureMessage);
			}


			//Verify details attribute
			if (!(auditLogObj.has("details"))) {
				reportStep(Status.FAIL, "details: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("details").isBlank() || auditLogObj.getString("details").isEmpty() || auditLogObj.isNull("details")) {
				reportStep(Status.FAIL, "details: Null or Blank"+failureMessage);
			}


			//Verify garageName attribute
			if (!(auditLogObj.has("garageName"))) {
				reportStep(Status.FAIL, "garageName: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("garageName").isBlank() || auditLogObj.getString("garageName").isEmpty() || auditLogObj.isNull("garageName")) {
				reportStep(Status.FAIL, "garageName: Null or Blank"+failureMessage);
			}


			//Verify busVIN attribute
			if (!(auditLogObj.has("busVIN"))) {
				//				if (auditLogObj.getString("type").equals("ASSIGN_RUN_AND_TRACK_WITH_OVERRIDE")) {
				//
				//				}else {
				reportStep(Status.FAIL, "busVIN: Attribute Missing "+failureMessage);
				//				}
			}else if(auditLogObj.getString("busVIN").isBlank() || auditLogObj.getString("busVIN").isEmpty() || auditLogObj.isNull("busVIN")) {
				//Ignore failure for the following conditions
				if (
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("Active")) ||
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("In-Active")) ||
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("Maintenance")) ||
						auditLogObj.getString("type").equals("CHANGE_CONFIGURATION") ||
						auditLogObj.getString("type").equals("EMERGENCY_STOP") ||
						auditLogObj.getString("type").equals("RESET") ||
						auditLogObj.getString("type").equals("START_CHARGER") ||
						auditLogObj.getString("type").equals("STOP_CHARGER")
						) {

				}else {
					reportStep(Status.FAIL, "busVIN: Null or Blank"+failureMessage);
				}
			}



			//Verify ocppId attribute
			if (auditLogObj.getString("type").equals("ASSIGN_RUN_MAINT_FLAG_WITH_OVERRIDE") ||
					auditLogObj.getString("type").equals("ASSIGN_RUN_AND_TRACK_MAINT_FLAG") ||
					auditLogObj.getString("type").equals("ASSIGN_RUN_AND_TRACK") ||
					auditLogObj.getString("type").equals("ASSIGN_RUN_AND_TRACK_WITH_OVERRIDE") ||
					auditLogObj.getString("type").equals("CHANGE_PARKING_POSITION")
					) {
				if (auditLogObj.has("ocppId")) {
					reportStep(Status.FAIL, "ocppId: Present"+failureMessage);
				}
			}


			//Verify busName attribute
			if (!(auditLogObj.has("busName"))) {
				reportStep(Status.FAIL, "busName: Attribute Missing "+failureMessage);
			}else if(auditLogObj.getString("busName").isBlank() || auditLogObj.getString("busName").isEmpty() || auditLogObj.isNull("busName")) {
				//Ignore failure for the following conditions
				if (
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("Active")) ||
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("In-Active")) ||
						(auditLogObj.getString("type").equals("CHARGER_STATUS_UPDATE") && auditLogObj.getString("details").equals("Maintenance")) ||
						auditLogObj.getString("type").equals("CHANGE_CONFIGURATION") ||
						auditLogObj.getString("type").equals("EMERGENCY_STOP") ||
						auditLogObj.getString("type").equals("RESET") ||
						auditLogObj.getString("type").equals("START_CHARGER") ||
						auditLogObj.getString("type").equals("STOP_CHARGER")
						) {

				}else {
					reportStep(Status.FAIL, "busName: Null or Blank"+failureMessage);
				}
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
