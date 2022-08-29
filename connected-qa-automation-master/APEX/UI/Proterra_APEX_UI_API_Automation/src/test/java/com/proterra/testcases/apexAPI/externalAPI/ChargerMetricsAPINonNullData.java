package test.java.com.proterra.testcases.apexAPI.externalAPI;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.AssertManager.HardAssertLogger;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.StringUtils;
import test.java.com.proterra.testcases.apexAPI.APIBaseTest;

public class ChargerMetricsAPINonNullData extends APIBaseTest{

	//Initiate Logger
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(description = "Validate the Charger Metrics API log for Charger Name to not be null, Bus VIN to not be null, Transaction ID not duplicate and Total Energy is not 0")
	@Parameters({"startDate","endDate","chargerIds"})
	public void ValidateMetricsDataNonNull(@Optional String startDate, @Optional String endDate, String chargerIds) throws ParseException {

		//Assertions
		sAssert = new SoftAssertLogger();
		hAssert = new HardAssertLogger();

		//Get Charger Ids as List
		List<String> chargerIdList = StringUtils.stringToArrayList(chargerIds, ",");

		//Define HashSet to store Transaction IDs
		Set<String> transactionIdSet = new HashSet<>();

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
		String path = getPATH_CHARGER_METRICS();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("x-api-key", API_KEY);

		//Add Query Parameters
		request.queryParam("start", startDate);
		request.queryParam("end", endDate);
		request.queryParam("chargerIds", chargerIds.replaceAll(" ", ""));

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();

		String body = response.getBody().asString();
		log.info(body);
		boolean bool = false;
		if (body.isBlank() || body.isEmpty()) {
			bool = true;
		}

		boolean isError = false;
		if (body.contains("504 Gateway Time-out")) {
			isError = true;
			hAssert.assertFalse(isError, "504 Gateway Time-out");
		}

		//Validate the Response
		sAssert.assertEquals(status, 200, "Metrics API - Valid user");
		sAssert.assertEquals(bool, false, "Metrics API - Response Body is not empty");

		//Get the APi Response as a JSON Object
		JSONObject jObj = new JSONObject(body);

		//Loop through the Charger ID list to validate data for each charger
		for (String chargerId : chargerIdList) {

			if (jObj.has(chargerId)) {
				//Get the Array for the charger id
				JSONArray chargerArray = jObj.getJSONArray(chargerId);

				//Loop through each array index
				for(int i=0; i<chargerArray.length(); i++) {

					//Get the metric object
					JSONObject metricObj = chargerArray.getJSONObject(i);

					//Get start timestamp
					String startTimestamp = metricObj.getString("start");
					Date dt_startTimestamp = dateUtils.convertStringToDate(startTimestamp, "yyyy-MM-dd'T'HH:mm:ss'Z'");
					 
					//Get end timestamp
					String endTimestamp = metricObj.getString("end");
					Date dt_endTimestamp = dateUtils.convertStringToDate(endTimestamp, "yyyy-MM-dd'T'HH:mm:ss'Z'");
					
					//Time difference of the charge session
					String timediff_message = "";
					long milliseconds_timediff =  dt_endTimestamp.getTime() - dt_startTimestamp.getTime();
					long seconds_timediff = TimeUnit.MILLISECONDS.toSeconds(milliseconds_timediff);
					long minutes_timediff = TimeUnit.MILLISECONDS.toMinutes(milliseconds_timediff);
					long hours_timediff = TimeUnit.MILLISECONDS.toHours(milliseconds_timediff);
					
					if (milliseconds_timediff < 1000) {
						timediff_message = "Charge session was for "+milliseconds_timediff+" milliseconds";
					}else if (seconds_timediff >= 1 && seconds_timediff < 60) {
						timediff_message = "Charge session was for "+seconds_timediff+" seconds";
					}else if(minutes_timediff >= 1 && minutes_timediff < 60) {
						timediff_message = "Charge session was for "+minutes_timediff+" minutes";
					}else if(hours_timediff >= 1) {
						timediff_message = "Charge session was for "+hours_timediff+" hours";
					}
					
					//Validate that Charger Name is not null or empty
					if (!(metricObj.has("chargerName"))) {
						reportStep(Status.FAIL, "chargerName attribute is missing for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else if(metricObj.getString("chargerName").isBlank() || metricObj.getString("chargerName").isEmpty() || metricObj.isNull("chargerName")) {
						reportStep(Status.FAIL, "chargerName attribute is null or blank for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}

					//Get metadata object
					JSONObject metaDataObj = metricObj.getJSONObject("metaData");

					//Validate that bus vin is not null or empty
					if (!(metaDataObj.has("busVIN"))) {
						reportStep(Status.FAIL, "busVIN attribute is missing for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else if(metaDataObj.getString("busVIN").isBlank() || metaDataObj.getString("busVIN").isEmpty() || metaDataObj.isNull("busVIN")) {
						reportStep(Status.FAIL, "busVIN attribute is null or blank for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}
					
					//Validate that bus name is not null or empty
					if (!(metaDataObj.has("busName"))) {
						reportStep(Status.FAIL, "busName attribute is missing for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else if(metaDataObj.getString("busName").isBlank() || metaDataObj.getString("busName").isEmpty() || metaDataObj.isNull("busName")) {
						reportStep(Status.FAIL, "busName attribute is null or blank for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}
					
					//Validate that transaction ID is not null or empty
					if (!(metaDataObj.has("transactionId"))) {
						reportStep(Status.FAIL, "transactionId attribute is missing for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else if(metaDataObj.getString("transactionId").isBlank() || metaDataObj.getString("transactionId").isEmpty() || metaDataObj.isNull("transactionId")) {
						reportStep(Status.FAIL, "transactionId attribute is null or blank for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else {
						//Add the transaction ID to set
						String transactionId = metaDataObj.getString("transactionId");
						boolean addedToSet = transactionIdSet.add(transactionId);

						if (addedToSet == false) {
							reportStep(Status.FAIL, "transactionId "+transactionId+" is duplicated for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
						}
					}


					//Get Integrated Fields object
					JSONObject integratedFieldsObj = metricObj.getJSONObject("integratedFields");

					//Validate that total energy is not 0
					if (!(integratedFieldsObj.has("totalEnergyKWH"))) {
						reportStep(Status.FAIL, "totalEnergyKWH attribute is missing for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
					}else {

						double totalEnergy = integratedFieldsObj.getDouble("totalEnergyKWH");

						//Check if total energy is 0
						if (totalEnergy == 0.0) {
							reportStep(Status.FAIL, timediff_message+" and the totalEnergyKWH value is 0.0 for Charger Metric with Charger "+chargerId+" at start time "+startTimestamp+" and end time "+endTimestamp);
						}

					}
				}
			}else {
				reportStep(Status.WARNING, "Charger Metrics Data for Charger ID "+chargerId+" is not available for the period "+startDate+" - "+endDate);
			}
		}
		sAssert.assertAll();
	}


	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

}
