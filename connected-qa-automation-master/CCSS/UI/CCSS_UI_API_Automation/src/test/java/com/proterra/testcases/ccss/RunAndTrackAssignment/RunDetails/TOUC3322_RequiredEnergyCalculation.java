package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;

public class TOUC3322_RequiredEnergyCalculation extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3322: CCSS-RRT-Run Details: Required Energy calculation",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3322"
			)
	public void Test_TOUC_3322() throws InterruptedException {


		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;

		Map<String, String> uiRunDetailsMap;
		Map<String,String> configDetailsMap;

		String garageId = GetAPIResponseData.getActiveGarageID();

		long requiredEnergy;
		long uiRequiredEnergy;


		//Get values from config API
		configDetailsMap = apiDat.getConfigValuesForActiveGarage(garageId);

		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Get the first Bus
		busEntry = busMap.entrySet().iterator().next();
		busName = busEntry.getValue();

		//Search for the Bus
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Remove Maintenance Flag
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click Recommended Run & Track button
		rrtPage.clickRecommendedRunAndTrack();

		//Store UI Run Details into a map
		uiRunDetailsMap = rrtPage.uiRunDetails();


		//Verify that the Run Details section header
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Recommended Run Details Header");

		//Verify that Run ID is displayed
		sAssert.assertFalse(uiRunDetailsMap.get(rrtPage.RUN_ID).isEmpty(), "Run ID Value "+uiRunDetailsMap.get(rrtPage.RUN_ID));

		//Verify the Required Energy
		int distanceInKm = Integer.parseInt(uiRunDetailsMap.get(rrtPage.DISTANCE).replaceAll("[^0-9]", ""));
		double energyConsumptionRate = Math.round((Double.parseDouble(configDetailsMap.get("Energy Consumption Rate")) * 0.625) * 10) / 10.0;
		float safetyMargin = Float.parseFloat(configDetailsMap.get("Required Energy Safety Margin"));
		float reservedRatio = Float.parseFloat(configDetailsMap.get("Required Energy Reserved Ratio"));
		int batteryCapacity = 528;

		requiredEnergy = rrtPage.calculateRequiredEnergy(distanceInKm, energyConsumptionRate, safetyMargin, reservedRatio, batteryCapacity);

		uiRequiredEnergy = Long.parseLong(uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY).replaceAll("[^0-9]", ""));

		sAssert.assertTrue((requiredEnergy >= uiRequiredEnergy -2 && requiredEnergy <= uiRequiredEnergy + 2), 
				"Required Energy | Calculated Required Energy: "+requiredEnergy+" | Run Details Required Energy: "+uiRequiredEnergy);

		
		//Verify the Estimated Charge Time
		sAssert.assertFalse(uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME).isEmpty(), "Estimated Charge Time Value "+uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME));
		
		//Verify the Bookout Date, Time
		sAssert.assertFalse(uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME).isEmpty(), "Book Out Date, Time Value "+uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME));
		
		//Verify the Distance
		sAssert.assertFalse(uiRunDetailsMap.get(rrtPage.DISTANCE).isEmpty(), "Distance Value "+uiRunDetailsMap.get(rrtPage.DISTANCE));
		
		//Verify the Run Status
		sAssert.assertEquals(uiRunDetailsMap.get(rrtPage.RUN_STATUS), "Not Assigned", "Run Status for Recommended Run");
		

		sAssert.assertAll();


	}


}
