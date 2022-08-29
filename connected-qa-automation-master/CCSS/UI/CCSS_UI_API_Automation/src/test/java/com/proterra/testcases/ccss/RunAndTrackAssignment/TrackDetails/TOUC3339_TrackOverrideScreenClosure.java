package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.TrackDetails;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3339_TrackOverrideScreenClosure extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3339: CCSS-RRT-Track Details: Override Track screen is closed when another bus is searched",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3339"
			)
	public void Test_TOUC_3339() throws InterruptedException {


		//Variables
		Map<String, String> busMap;

		String busName = null;


		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();
		Iterator<Entry<String, String>> iterator = busMap.entrySet().iterator();

		Map.Entry<String, String> entry = iterator.next();

		busName = entry.getValue();

		//Search the bus
		rrtPage.searchBEB(busName);
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Remove Maintenance Flag
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click Recommended Run & Track
		rrtPage.clickRecommendedRunAndTrack();

		//Click the Run Override button
		rrtPage.clickOverrideTrack();

		//Verify that Run Override screen is displayed
		sAssert.assertTrue(rrtPage.CCSS_TrackOverride_Screen.isDisplayed(), "Track Override Screen display on clicking Override button from Track Details");

		//Search another bus
		rrtPage.searchBEB(iterator.next().getValue());
		reportStep(Status.PASS, "Bus "+busName+"is searched");

		//Verify that Run Override Screen is closed
		sAssert.assertFalse(rrtPage.isElementPresent(rrtPage.CCSS_TrackOverride_Screen), "Track Override Screen closure when another Bus is searched");

		//Remove Maintenance Flag
		rrtPage.needsMaintenanceSetOrRemove(false);

		//Click Recommended Run & Track
		rrtPage.clickRecommendedRunAndTrack();

		//Click the Run Override button
		rrtPage.clickOverrideTrack();

		//Verify that Run Override screen is displayed
		sAssert.assertTrue(rrtPage.CCSS_TrackOverride_Screen.isDisplayed(), "Track Override Screen display on clicking Override button from Track Details");

		//Switch the garage
		rrtPage.switchToNonActiveGarage();

		//Verify that Run Override Screen is closed
		sAssert.assertFalse(rrtPage.isElementPresent(rrtPage.CCSS_TrackOverride_Screen), "Track Override Screen closure when Garage is switched");

		//Switch back to the previous garage
		rrtPage.switchToGarage(GARAGE_NAME);

		sAssert.assertAll();


	}


}
