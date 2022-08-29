package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.AssignRunAndTrack;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3378_DefaultRunOverriddenTrackNonMaintenance extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3378: CCSS-RRT-Assign Run and Track: Assign default Run and overridden Track for Non Maintenance Bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3378"
			)
	public void Test_TOUC_3378() throws InterruptedException {


		List<String> iqBusList;

		Map<String, String> uiTrackDetailsMap;
		
		Map<String,List<Object>> chargerTrackPositionDetalsMap = new LinkedHashMap<>();


		String busName;
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
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();

		//Click Override Track
		rrtPage.clickOverrideTrack();

		//Select Overridden track
		rrtPage.selectOverriddenTrack(trackName, positionName);


		//Get Recommended Track Details
		uiTrackDetailsMap = rrtPage.uiTrackDetails();
		String trackPosition = uiTrackDetailsMap.get(rrtPage.TRACK_NO)+"-"+uiTrackDetailsMap.get(rrtPage.POSITION);

		//Click Assign
		String toastMsg = rrtPage.clickAssignRunAndTrack();
		String expectedMessage = "Success: Bus "+busName+" has been successfully assigned to position "+trackPosition;

		sAssert.assertEquals(toastMsg, expectedMessage, "Toast Message after Bus assignment");
		System.out.println(toastMsg);


		sAssert.assertAll();
	}
}
