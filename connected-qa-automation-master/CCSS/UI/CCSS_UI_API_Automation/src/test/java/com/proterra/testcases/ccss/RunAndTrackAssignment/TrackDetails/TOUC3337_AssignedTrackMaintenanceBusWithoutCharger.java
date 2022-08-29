package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.TrackDetails;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3337_AssignedTrackMaintenanceBusWithoutCharger extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3337: CCSS-RRT-Track Details: Assigned Track Details for Maintenance Bus in Non Charger Position",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3337"
			)
	public void Test_TOUC_3337() throws InterruptedException, IOException {

		//Run clear garage
		clearGarage.clearGarage();


		//Variables
		Map<String, String> uiTrackDetailsMapAfterAssignment = new LinkedHashMap<>();
		Map<String, String> busMap;

		Map<String,List<Object>> chargerTrackPositionDetalsMap = new LinkedHashMap<>();


		String busName = null;
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
		reportStep(Status.PASS, "The charger is available at position "+chargerName);

		chargerTrackPosition = chargerName.substring(chargerName.indexOf(" ")+1).split("-");
		trackName = chargerTrackPosition[0];
		positionName = chargerTrackPosition[1];


		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Search for a Bus
		for(Map.Entry<String, String> entry: busMap.entrySet()) {

			busName = entry.getValue();

			//Search for the Bus
			rrtPage.searchBEB(busName);
			reportStep(Status.PASS, "Bus "+busName+"is searched");

			//Verify that BEB Search box is clear
			sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

			//Remove Maintenance Flag
			rrtPage.needsMaintenanceSetOrRemove(true);

			//Click Recommended Run & Track button
			rrtPage.clickRecommendedRunAndTrack();

			//Click the Run Override button
			rrtPage.clickOverrideTrack();
			reportStep(Status.PASS, "Override button is clicked");

			//Select an overridden track
			rrtPage.selectOverriddenTrack(trackName, positionName);

			break;
		}


		// Click Assign
		rrtPage.clickAssignRunAndTrack();

		// Search Bus using BEB
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus " + busName + " is searched");

		// Get UI Track Details After Assignment
		uiTrackDetailsMapAfterAssignment = rrtPage.uiTrackDetails();

		//Verify that all the components are displayed
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Assigned Track", "Track Details component header");

		// Validate Track No
		sAssert.assertEquals(uiTrackDetailsMapAfterAssignment.get(rrtPage.TRACK_NO),trackName
				, "Track No After Assignment");

		// Validate Position
		sAssert.assertEquals(uiTrackDetailsMapAfterAssignment.get(rrtPage.POSITION),
				positionName, "Position After Assignment");

		//Validate Track Type Value
		sAssert.assertEquals(uiTrackDetailsMapAfterAssignment.get(rrtPage.TRACK_TYPE), "Not Charging", "Track Type Value for Charger Position");

		sAssert.assertAll();

	}


}
