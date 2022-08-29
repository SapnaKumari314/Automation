package test.java.com.proterra.testcases.apexAPI.externalAPI;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

public class FleetMetricsAPIAllBusData extends APIBaseTest{

	//Initiate Logger
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(description = "Each date reported by Fleet Metrics API should have data for all Edmonton buses")
	@Parameters({"startDate","endDate","busVins","signalNames"})
	public void ValidateFleetMetricsDataHasAllBuses(@Optional String startDate, @Optional String endDate, String busVins, String signalNames) throws ParseException {

		//Assertions
		sAssert = new SoftAssertLogger();
		hAssert = new HardAssertLogger();

		//Get Charger Ids as List
		List<String> busVinList = StringUtils.stringToArrayList(busVins, ",");

		//Set the module
		MODULE = "dataLake";

		//Set the dates
		if (startDate == null || endDate == null) {
			startDate = dateUtils.addRemoveDaysToCurrentDate(dateUtils.getCurrentUTCDateTime(), "yyyy-MM-dd", -1) + "T00:00:00-07:00";
			endDate = dateUtils.addRemoveDaysToCurrentDate(dateUtils.getCurrentUTCDateTime(), "yyyy-MM-dd", -1) + "T23:59:59-07:00";
		}

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_FLEET_EXTERNAL_METRICS();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("x-api-key", API_KEY);

		//Add Query Parameters
		request.queryParam("start", startDate);
		request.queryParam("end", endDate);
		request.queryParam("buses", busVins.replaceAll(" ", ""));
		request.queryParam("signalNames", signalNames.replaceAll(" ", ""));

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
		sAssert.assertEquals(status, 200, "Fleet Metrics API - Valid response");
		sAssert.assertEquals(bool, false, "Fleet Metrics API - Response Body is not empty");

		//Get the APi Response as a JSON Object
		JSONObject jObj = new JSONObject(body);

		//Get All Bus objects from aggByBus
		JSONObject aggByBusObj = jObj.getJSONObject("aggByBus");
		List<String> busObjectList = new ArrayList<String>(aggByBusObj.keySet());

		//Validate that Aggregate By Bus has reported all Edmonton Buses
		for(String busVin: busVinList) {

			if (busObjectList.contains(busVin)) {
				//				reportStep(Status.PASS, "Fleet Metrics Aggregate By Bus Data for Bus VIN "+busVin+" is available for the period "+startDate+" - "+endDate);
			}else {
				reportStep(Status.FAIL, "Fleet Metrics Aggregate By Bus Data for Bus VIN "+busVin+" is not available for the period "+startDate+" - "+endDate);
			}
		}


		//Get All Bus objects from aggByBus
		JSONObject statsByMetricsByBusObj = jObj.getJSONObject("statsByMetricsByBus");
		List<String> statsByMetricsByBusList = new ArrayList<String>(statsByMetricsByBusObj.keySet());

		//Loop through every Signal
		for(String stat: statsByMetricsByBusList) {
			//Get All Bus objects from the stat
			JSONObject statBusObj = statsByMetricsByBusObj.getJSONObject(stat);
			List<String> statBusList = new ArrayList<String>(statBusObj.keySet());

			for(String busVin: busVinList) {
				if (statBusList.contains(busVin)) {
					//					reportStep(Status.PASS, "Fleet Metrics "+stat+" for Bus VIN "+busVin+" is available for the period "+startDate+" - "+endDate);
				}else {
					reportStep(Status.FAIL, "Fleet Metrics for Stats By Metrics By Bus for "+stat+" for Bus VIN "+busVin+" is not available for the period "+startDate+" - "+endDate);
				}
			}
		}

		//Get All Bus objects from dailyData
		JSONObject dailyDataObj = jObj.getJSONObject("dailyData");
		List<String> dailyDataObjectList = new ArrayList<String>(dailyDataObj.keySet());

		//Loop through every Signal
		for(String stat: dailyDataObjectList) {
			//Get All Bus objects
			JSONObject statBusObj = dailyDataObj.getJSONObject(stat);
			List<String> statBusList = new ArrayList<String>(statBusObj.keySet());
			
			//Validate that dailyData has reported all Edmonton Buses
			for(String busVin: busVinList) {
				if (statBusList.contains(busVin)) {
					//						reportStep(Status.PASS, "Fleet Metrics Aggregate By Bus Data for Bus VIN "+busVin+" is available for the period "+startDate+" - "+endDate);
				}else {
					reportStep(Status.FAIL, "Fleet Metrics for Daily Data for "+stat+" for Bus VIN "+busVin+" is not available for the period "+startDate+" - "+endDate);
				}
			}
		}
		
		sAssert.assertAll();
	}


	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

}
