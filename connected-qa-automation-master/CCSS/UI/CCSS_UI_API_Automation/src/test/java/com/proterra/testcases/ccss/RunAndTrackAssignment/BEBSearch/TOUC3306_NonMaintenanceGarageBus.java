package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BEBSearch;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3306_NonMaintenanceGarageBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3306: CCSS-RRT-BEB Search: Search Non Maintenance bus from Garage",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3306"
			)
	public void Test_TOUC_3306() throws InterruptedException {


		//Get all buses in the garage
		Map<String, String> busMap;
		Map<String, String> emptyTrackPositionsMap;
		Map.Entry<String,String> busEntry;
		Map.Entry<String,String> trackEntry;
		String busName;
		String[] trackPosition;
		String track;
		String position;

		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Search for Empty Track Positions
		emptyTrackPositionsMap = apiDat.getEmptyGarageTrackPositions();

		//Get the first Bus
		busEntry = busMap.entrySet().iterator().next();
		busName = busEntry.getValue();

		//Get the first empty track position
		trackEntry = emptyTrackPositionsMap.entrySet().iterator().next();
		trackPosition = trackEntry.getKey().split("-");
		track = trackPosition[0];
		position = trackPosition[1];

		//Search Bus
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

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

		//Validate the Bus Details
		sAssert.assertEquals(rrtPage.CCSS_BusDetailsBusIDVal.getText(), busName,"Bus ID in Bus Details");

		sAssert.assertAll();


	}


}
