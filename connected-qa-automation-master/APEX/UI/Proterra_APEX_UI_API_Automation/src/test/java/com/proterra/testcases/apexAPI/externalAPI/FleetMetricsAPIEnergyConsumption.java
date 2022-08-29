package test.java.com.proterra.testcases.apexAPI.externalAPI;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class FleetMetricsAPIEnergyConsumption extends APIBaseTest{

	//Initiate Logger
	public static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(description = "Ensure If distanceInMilesOdo > 0 then energy consumption should be > 0")
	@Parameters({"startDate","endDate","busVins","signalNames"})
	public void ValidateFleetMetricsEnergyConsumption(@Optional String startDate, @Optional String endDate, String busVins, String signalNames) throws ParseException {

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

		//Get the overallStatsByMetrics Object
		JSONObject overallStatsByMetricsObj =  jObj.getJSONObject("overallStatsByMetrics");
		
		//Get the distanceInMilesOdo Object
		JSONObject osbm_distanceInMilesOdoObj = overallStatsByMetricsObj.getJSONObject("distanceInMilesOdo");
		
		//Get the value for distance in miles odo
		Double osbm_distInMilesOdoVal = osbm_distanceInMilesOdoObj.getDouble("sum");
		
		//Get the energyConsumptionResult.energyRegeneratedKWH Object
		JSONObject osbm_energyConsumptionResult_energyRegeneratedKWH = overallStatsByMetricsObj.getJSONObject("energyConsumptionResult.energyRegeneratedKWH");
		
		//Get the value for energyConsumptionResult.energyRegeneratedKWH
		Double osbm_energyConsumptionResult_energyRegeneratedKWHVal = osbm_energyConsumptionResult_energyRegeneratedKWH.getDouble("sum");
		
		//Validate the Energy Consumption for overallStatsByMetrics
		if (osbm_distInMilesOdoVal > 0) {
			sAssert.assertTrue(osbm_energyConsumptionResult_energyRegeneratedKWHVal > 0, "Energy Consumption for overallStatsByMetrics");
		}
		
		//Validate Energy Consumption by Bus for statsByMetricsByBus
		JSONObject statsByMetricsByBusObject = jObj.getJSONObject("statsByMetricsByBus");
		
		JSONObject sbmbb_distanceInMilesOdo = statsByMetricsByBusObject.getJSONObject("distanceInMilesOdo");
		
		JSONObject sbmbb_energyConsumptionResult_energyRegeneratedKWH = statsByMetricsByBusObject.getJSONObject("energyConsumptionResult.energyRegeneratedKWH");
		
		//Loop through the Bus List
		for(String busVin: busVinList) {
			if (!(sbmbb_distanceInMilesOdo.has(busVin))) {
				reportStep(Status.FAIL, "distanceInMilesOdo Fleet Metrics for statsByMetricsByBus for Bus VIN "+busVin+" is not available for the period "+startDate+" - "+endDate);
				continue;
			}
			JSONObject busVinObj = sbmbb_distanceInMilesOdo.getJSONObject(busVin);
			Double dist = busVinObj.getDouble("sum");
			
			if (!(sbmbb_energyConsumptionResult_energyRegeneratedKWH.has(busVin))) {
				reportStep(Status.FAIL, "energyConsumptionResult.energyRegeneratedKWH Fleet Metrics for statsByMetricsByBus for Bus VIN "+busVin+" is not available for the period "+startDate+" - "+endDate);
				continue;
			}
			JSONObject busVinObj2 = sbmbb_energyConsumptionResult_energyRegeneratedKWH.getJSONObject(busVin);
			Double energy = busVinObj2.getDouble("sum");
			
			if (dist > 0) {
				sAssert.assertTrue(energy > 0, "Energy Consumption for statsByMetricsByBus for Bus Vin: "+busVin);
			}
		}
		
		
		//Validate Energy Consumption by Bus for dailyData
		sAssert.assertAll();
	}


	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

}
