package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.Reassignment;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC2826_ReassignmentNonMaintenanceToNonManitenanceDifferentRunDifferentTrack extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2826: (CCSS):Re-assignment: Validate user is able to re-assign non manitenance bus to different run and different track",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "2825"
			)
	public void Test_TOUC_2826() throws InterruptedException, IOException {


		List<String> iqBusList = new LinkedList<>();

		Map<String, String> uiRunDetailsMapForAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForAssignment = new LinkedHashMap<>();

		Map<String, String> uiRunDetailsMapForReAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForReAssignment = new LinkedHashMap<>();


		String busName1;
		String busName2;
		
		
		String trackPositionForAssignment;
		String runNoForAssignment;

		String toastMsgForAssignment;
		String expectedMessageForAssignment;

		String trackPositionForReAssignment;
		String runNoForReAssignment;

		String toastMsgForReAssignment;
		String expectedMessageForReAssignment;

		//Get buses from IQ
		iqBusList = rrtPage.getAllIQBusList();

		//Get the First Bus from the list
		busName1 = iqBusList.get(0);


		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName1);
		reportStep(Status.PASS, "Bus "+busName1+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();

		//Get Recommended Run Details
		uiRunDetailsMapForAssignment = rrtPage.uiRunDetails();
		runNoForAssignment = uiRunDetailsMapForAssignment.get(rrtPage.RUN_ID);

		//Get Recommended Track Details
		uiTrackDetailsMapForAssignment = rrtPage.uiTrackDetails();
		trackPositionForAssignment = uiTrackDetailsMapForAssignment.get(rrtPage.TRACK_NO)+"-"+uiTrackDetailsMapForAssignment.get(rrtPage.POSITION);

		//Click Assign
		toastMsgForAssignment = rrtPage.clickAssignRunAndTrack();
		expectedMessageForAssignment = "Success: Bus "+busName1+" has been successfully assigned to run "+runNoForAssignment+" and position "+trackPositionForAssignment;

		sAssert.assertEquals(toastMsgForAssignment, expectedMessageForAssignment, "Toast Message after First Bus assignment");



		//Get the Second Bus from the list
		busName2 = iqBusList.get(1);

		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName2);
		reportStep(Status.PASS, "Bus "+busName2+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();


		//Click Assign
		toastMsgForAssignment = rrtPage.clickAssignRunAndTrack();


		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName1);
		reportStep(Status.PASS, "Bus "+busName1+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");


		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();


		//Get Recommended Run Details
		uiRunDetailsMapForReAssignment = rrtPage.uiRunDetails();
		runNoForReAssignment = uiRunDetailsMapForReAssignment.get(rrtPage.RUN_ID);

		//Get Recommended Track Details
		uiTrackDetailsMapForReAssignment = rrtPage.uiTrackDetails();
		trackPositionForReAssignment = uiTrackDetailsMapForReAssignment.get(rrtPage.TRACK_NO)+"-"+uiTrackDetailsMapForReAssignment.get(rrtPage.POSITION);


		//Compare Run No for assignment and re-assignment
		sAssert.assertNotEquals(runNoForAssignment, runNoForReAssignment, "Run Number for assignment and re-assignment");

		//Compare Track Position for assignment and re-assignment
		sAssert.assertNotEquals(trackPositionForAssignment, trackPositionForReAssignment, "Track Position for assignment and re-assignment");


		//Click Assign
		toastMsgForReAssignment = rrtPage.reassignAssignRunAndTrack();
		expectedMessageForReAssignment = "Success: Bus "+busName1+" has been successfully assigned to run "+runNoForReAssignment+" and position "+trackPositionForReAssignment;


		sAssert.assertEquals(toastMsgForReAssignment, expectedMessageForReAssignment, "Toast Message after Bus assignment");

		sAssert.assertAll();


	}


}
