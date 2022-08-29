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

public class TOUC2827_ReassignmentMaintenanceToNonManitenance extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2827: (CCSS):Re-assignment: Validate user is able to re-assign manitenance bus as Non Maintenance to run and track",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "2827"
			)
	public void Test_TOUC_2827() throws InterruptedException, IOException {


		List<String> iqBusList = new LinkedList<>();

		Map<String,List<Object>> chargerTrackPositionDetalsMap = new LinkedHashMap<>();

		Map<String, String> uiRunDetailsMapForAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForAssignment = new LinkedHashMap<>();

		Map<String, String> uiRunDetailsMapForReAssignment = new LinkedHashMap<>();
		Map<String, String> uiTrackDetailsMapForReAssignment = new LinkedHashMap<>();


		String busName;
		String trackPositionForAssignment;

		String toastMsgForAssignment;
		String expectedMessageForAssignment;

		String trackPositionForReAssignment;
		String runNoForReAssignment;

		String toastMsgForReAssignment;
		String expectedMessageForReAssignment;

		//Get the charger position details
		String chargerName = null;
		String[] chargerTrackPosition;
		String trackName;
		String positionName;

		//Get the next available charger position
		chargerTrackPositionDetalsMap = apiDat.getAllTrackPositionsData();

		for(Map.Entry<String, List<Object>> entry: chargerTrackPositionDetalsMap.entrySet()) {

			chargerName = entry.getKey();

			if ((boolean) entry.getValue().get(apiDat.listIndex_isChargerInstalled)) {
				if (((boolean) entry.getValue().get(apiDat.listIndex_chargerAvailableForCharging))==false) {
					break;
				}
			}
		}
		reportStep(Status.PASS, "The track position available is "+chargerName);

		chargerTrackPosition = chargerName.substring(chargerName.indexOf(" ")+1).split("-");
		trackName = chargerTrackPosition[0];
		positionName = chargerTrackPosition[1];

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
		rrtPage.needsMaintenanceSetOrRemove(true);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();

		//Get Recommended Run Details
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Run Details component header after clicking Recommended Run & Track button");
		uiRunDetailsMapForAssignment = rrtPage.uiRunDetails();
		sAssert.assertTrue(uiRunDetailsMapForAssignment.get(rrtPage.RUN_ID).isEmpty(), "Run details are empty");

		//Get Recommended Track Details
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Recommended Track", "Track Details component header after clicking Recommended Run & Track button");
		uiTrackDetailsMapForAssignment = rrtPage.uiTrackDetails();
		sAssert.assertTrue(uiTrackDetailsMapForAssignment.get(rrtPage.TRACK_NO).isEmpty(), "Track details are empty");

		//Click Override Track
		rrtPage.clickOverrideTrack();

		//Select Overridden track
		rrtPage.selectOverriddenTrack(trackName, positionName);
		uiTrackDetailsMapForAssignment = rrtPage.uiTrackDetails();
		trackPositionForAssignment = uiTrackDetailsMapForAssignment.get(rrtPage.TRACK_NO)+"-"+uiTrackDetailsMapForAssignment.get(rrtPage.POSITION);

		//Click Assign
		toastMsgForAssignment = rrtPage.clickAssignRunAndTrack();
		expectedMessageForAssignment = "Success: Bus "+busName+" has been successfully assigned to position "+trackPositionForAssignment;

		sAssert.assertEquals(toastMsgForAssignment, expectedMessageForAssignment, "Toast Message after Bus assignment");


		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Verify the Run and Track details component header
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Run Details component header after searching assigned bus");
		sAssert.assertTrue(rrtPage.uiRunDetails().get(rrtPage.RUN_ID).isEmpty(), "Run details are empty");

		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Assigned Track", "Track Details component header after searching assigned bus");
		sAssert.assertFalse(uiTrackDetailsMapForAssignment.get(rrtPage.TRACK_NO).isEmpty(), "Track details are not empty");


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
		sAssert.assertNotEquals("", runNoForReAssignment, "Run Number for assignment and re-assignment");

		//Compare Track Position for assignment and re-assignment
		sAssert.assertNotEquals(trackPositionForAssignment, trackPositionForReAssignment, "Track Position for assignment and re-assignment");


		//Click Assign
		toastMsgForReAssignment = rrtPage.reassignAssignRunAndTrack();
		expectedMessageForReAssignment = "Success: Bus "+busName+" has been successfully assigned to run "+runNoForReAssignment+" and position "+trackPositionForReAssignment;


		sAssert.assertEquals(toastMsgForReAssignment, expectedMessageForReAssignment, "Toast Message after Bus assignment");

		sAssert.assertAll();


	}


}
