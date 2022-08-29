package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BusDetails;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3315_RangeAfterAssignment extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3315: CCSS-RRT-Bus Details: Range after assignment",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3315"
			)
	public void Test_TOUC_3315() throws InterruptedException {


		List<String> iqBusList;

		Map<String, String> apiBusDetailsMap;
		Map<String, String> uiBusDetailsMap;
		Map<String, String> emptyTrackPositionsMap;

		Map.Entry<String,String> trackEntry;
		String[] trackPosition;
		String track;
		String position;

		String busName;
		String busVin;

		String uiCurrentEnergy;
		String apiCurrentEnergy;

		String uiRange;
		String apiRange;
		String calculatedRange;

		String uiEnergyConsumption;
		String apiEnergyConsumption;


		//Get buses from IQ
		iqBusList = rrtPage.getAllIQBusList();

		//Get the First Bus from the list
		busName = iqBusList.get(0);

		//Search for Empty Track Positions
		emptyTrackPositionsMap = apiDat.getEmptyGarageTrackPositions();

		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Get the first empty track position
		trackEntry = emptyTrackPositionsMap.entrySet().iterator().next();
		trackPosition = trackEntry.getKey().split("-");
		track = trackPosition[0];
		position = trackPosition[1];

		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();

		//Click Override Track button
		rrtPage.clickOverrideTrack();

		//Select a Track
		rrtPage.selectOverriddenTrack(track, position);

		//Click Assign
		String toastMsg = rrtPage.clickAssignRunAndTrack();
		reportStep(Status.PASS, toastMsg);

		//Search Bus
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Get the component values from Bus Details
		uiBusDetailsMap = rrtPage.uiBusDetails();

		uiCurrentEnergy = uiBusDetailsMap.get(rrtPage.CURRENT_ENERGY).replaceAll("[^0-9]", "");
		uiRange = uiBusDetailsMap.get(rrtPage.RANGE).replaceAll("[^0-9]", "");
		uiEnergyConsumption = uiBusDetailsMap.get(rrtPage.ENERGY_CONSUMPTION).replaceAll("[a-zA-Z/ ]+", "");

		//Get the Bus Details from Bus Details API 
		busVin = datConst.getBusVinByName(busName);
		apiBusDetailsMap = apiDat.getBusDetailsUIComponentValues(busVin);

		apiCurrentEnergy = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("currentEnergy"))));
		apiRange = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("range")) * 1.609));
		apiEnergyConsumption = String.valueOf(Math.round(((Double.parseDouble(apiBusDetailsMap.get("energyConsumption")))/1.609) * 10) / 10.0);

		calculatedRange = String.valueOf(rrtPage.busDetailsRangeCalculation(uiEnergyConsumption, uiCurrentEnergy));

		//Compare both the values
		sAssert.assertEquals(uiCurrentEnergy, apiCurrentEnergy, "Current Energy for the Bus after Assignment as per Bus Details API");

		sAssert.assertEquals(uiRange, apiRange, "Range for the Bus after Assignment as per Bus Details API");

		sAssert.assertEquals(uiEnergyConsumption, apiEnergyConsumption, "Energy Consumption for the Bus after Assignment as per Bus Details API");

		sAssert.assertTrue((Integer.parseInt(calculatedRange)>= Integer.parseInt(uiRange) - 2 
				&& Integer.parseInt(calculatedRange) <= Integer.parseInt(uiRange) + 2), 
				"Range calculated after assignment as Current Energy / Energy Consumption. | Calculated Range: "+calculatedRange+" | Bus Details Range: "+uiRange);

		sAssert.assertAll();


	}


}
