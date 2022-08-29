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

public class TOUC2825_ReassignmentNonMaintenanceToNonManitenanceDifferentRunSameTrack extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2825: (CCSS):Re-assignment: Validate user is able to re-assign non manitenance bus to different run with same track",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3376"
			)
	public void Test_TOUC_2825() throws InterruptedException, IOException {


		List<String> iqBusList = new LinkedList<>();

		Map<String, String> uiRunDetailsMapForAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForAssignment = new LinkedHashMap<>();

		Map<String, String> uiRunDetailsMapForReAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForReAssignment = new LinkedHashMap<>();


		String busName;
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
		busName = iqBusList.get(0);


		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();

		//Get Recommended Run Details
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Run Details component header after clicking Recommended Run & Track button");
		uiRunDetailsMapForAssignment = rrtPage.uiRunDetails();
		runNoForAssignment = uiRunDetailsMapForAssignment.get(rrtPage.RUN_ID);

		//Get Recommended Track Details
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Recommended Track", "Track Details component header after clicking Recommended Run & Track button");
		uiTrackDetailsMapForAssignment = rrtPage.uiTrackDetails();
		trackPositionForAssignment = uiTrackDetailsMapForAssignment.get(rrtPage.TRACK_NO)+"-"+uiTrackDetailsMapForAssignment.get(rrtPage.POSITION);

		//Click Assign
		toastMsgForAssignment = rrtPage.clickAssignRunAndTrack();
		expectedMessageForAssignment = "Success: Bus "+busName+" has been successfully assigned to run "+runNoForAssignment+" and position "+trackPositionForAssignment;

		sAssert.assertEquals(toastMsgForAssignment, expectedMessageForAssignment, "Toast Message after Bus assignment");


		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Verify the Run and Track details component header
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Assigned Run", "Run Details component header after searching assigned bus");
		sAssert.assertFalse(rrtPage.uiRunDetails().isEmpty(), "Run details are not empty");
		
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Assigned Track", "Track Details component header after searching assigned bus");
		sAssert.assertFalse(rrtPage.uiTrackDetails().isEmpty(), "Track details are not empty");

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
		sAssert.assertEquals(trackPositionForAssignment, trackPositionForReAssignment, "Track Position for assignment and re-assignment");


		//Click Assign
		toastMsgForReAssignment = rrtPage.reassignAssignRunAndTrack();
		expectedMessageForReAssignment = "Success: Bus "+busName+" has been successfully assigned to run "+runNoForReAssignment+" and position "+trackPositionForReAssignment;

		
		sAssert.assertEquals(toastMsgForReAssignment, expectedMessageForReAssignment, "Toast Message after Bus assignment");

		sAssert.assertAll();


	}


}
