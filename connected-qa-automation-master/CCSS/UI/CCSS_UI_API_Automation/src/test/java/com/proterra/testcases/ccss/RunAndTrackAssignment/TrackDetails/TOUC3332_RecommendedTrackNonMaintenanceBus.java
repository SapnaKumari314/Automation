package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.TrackDetails;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3332_RecommendedTrackNonMaintenanceBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3332: CCSS-RRT-Track Details: Recommended Track Details for Non Maintenance Bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3332"
			)
	public void Test_TOUC_3332() throws InterruptedException, IOException {

		//Run clear garage
		clearGarage.clearGarage();
		rrtPage.waitForElementNotPresent(loginPage.loadingIcon, 120000);

		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;

		Map<String, String> uiTrackDetailsMap;

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
		uiTrackDetailsMap = rrtPage.uiTrackDetails();


		//Verify that the Run Details section header
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Recommended Track", "Recommended Track Details Header");

		//Verify that Track No is displayed
		sAssert.assertFalse(uiTrackDetailsMap.get(rrtPage.TRACK_NO).isEmpty(), "Track No Value "+uiTrackDetailsMap.get(rrtPage.TRACK_NO));

		//Verify that Position is displayed
		sAssert.assertFalse(uiTrackDetailsMap.get(rrtPage.POSITION).isEmpty(), "Position Value "+uiTrackDetailsMap.get(rrtPage.POSITION));

		//Verify that Track Type is displayed
		sAssert.assertFalse(uiTrackDetailsMap.get(rrtPage.TRACK_TYPE).isEmpty(), "Track Type Value "+uiTrackDetailsMap.get(rrtPage.TRACK_TYPE));

		sAssert.assertAll();

	}


}
