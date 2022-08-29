package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3321_AssignedRunMaintenanceBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3321: CCSS-RRT-Run Details: Assigned Run Details for Maintenance Bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3321"
			)
	public void Test_TOUC_3321() throws InterruptedException {


		List<String> iqBusList;

		Map<String, String> emptyTrackPositionsMap;
		Map<String, String> uiRunDetailsMap;


		Map.Entry<String,String> trackEntry;
		String[] trackPosition;
		String track;
		String position;

		String busName;
		
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
		rrtPage.needsMaintenanceSetOrRemove(true);

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

		//Verify Maintenance Flag is false
		sAssert.assertTrue(rrtPage.getBusMaintenanceStatus(), "Needs Maintenance is set to True");

		//Store UI Run Details into a map
		uiRunDetailsMap = rrtPage.uiRunDetails();


		//Verify that the Run Details section header
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Recommended Run Details Header");

		//Verify that Run ID is displayed
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.RUN_ID).isEmpty(), "Run ID Value "+uiRunDetailsMap.get(rrtPage.RUN_ID));

		//Verify the Required Energy
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY).isEmpty(), "Required Energy Value "+uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY));
		
		//Verify the Estimated Charge Time
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME).isEmpty(), "Estimated Charge Time Value "+uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME));
		
		//Verify the Bookout Date, Time
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME).isEmpty(), "Book Out Date, Time Value "+uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME));
		
		//Verify the Distance
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.DISTANCE).isEmpty(), "Distance Value "+uiRunDetailsMap.get(rrtPage.DISTANCE));
		
		//Verify the Run Status
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.RUN_STATUS).isEmpty(), "Run Status for Recommended Run");
		

		sAssert.assertAll();


	}


}
