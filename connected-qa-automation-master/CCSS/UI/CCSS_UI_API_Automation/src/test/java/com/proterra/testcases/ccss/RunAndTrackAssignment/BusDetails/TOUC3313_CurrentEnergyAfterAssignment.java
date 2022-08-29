package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BusDetails;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3313_CurrentEnergyAfterAssignment extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3313: CCSS-RRT-Bus Details: Current Energy after Bus Assignment",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3313"
			)
	public void Test_TOUC_3313() throws InterruptedException {


		List<String> iqBusList;

		Map<String, String> apiBusDetailsMap;
		Map<String, String> emptyTrackPositionsMap;

		Map.Entry<String,String> trackEntry;
		String[] trackPosition;
		String track;
		String position;

		String busName;
		String busVin;

		String uiCurrentEnergy;
		String apiCurrentEnergy;


		//Get buses from IQ
		iqBusList = rrtPage.getAllIQBusList();

		//Get the First Bus from the list
		busName = iqBusList.get(0);

		//Search for Empty Track Positions
		emptyTrackPositionsMap = apiDat.getEmptyGarageTrackPositions();

		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

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
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");


		//Get the component values from Bus Details
		uiCurrentEnergy = rrtPage.CCSS_BusDetailsCurrentEnergyVal.getText().replaceAll("[^0-9]", "");

		//Get the Bus Details from Bus Details API 
		busVin = datConst.getBusVinByName(busName);
		apiBusDetailsMap = apiDat.getTrackSetupBusDetails(busVin);

		apiCurrentEnergy = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("currentEnergy"))));



		//Compare both the values
		sAssert.assertEquals(uiCurrentEnergy, apiCurrentEnergy, "Current Energy for the Bus after Assignment as per Bus Details API");

		sAssert.assertAll();


	}


}
