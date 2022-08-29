package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.TrackDetails;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3331_Components extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3331: CCSS-RRT-Track Details: Track Details Components",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3331"
			)
	public void Test_TOUC_3331() throws InterruptedException, IOException {
		
		//Run clear garage
		clearGarage.clearGarage();
		
		rrtPage.waitForElementNotPresent(loginPage.loadingIcon, 120000);


		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;

		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Get the first Bus
		busEntry = busMap.entrySet().iterator().next();
		busName = busEntry.getValue();

		//Search for any Bus
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Verify that all the components are displayed
		sAssert.assertEquals(rrtPage.CCSS_TrackDetailsComponentHeader.getText(), "Recommended Track", "Track Details component header");
		
		sAssert.assertTrue(rrtPage.CCSS_TrackDetailsTrackNoLabel.isDisplayed(), "Track No label display");
		
		sAssert.assertTrue(rrtPage.CCSS_TrackDetailsPositionLabel.isDisplayed(), "Position label display");
		
		sAssert.assertTrue(rrtPage.CCSS_TrackDetailsTrackTypeLabel.isDisplayed(), "Track Type label display");
		
		sAssert.assertAll();


	}


}
