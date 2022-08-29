package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BEBSearch;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3308_BusOutsideGarage extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3308: CCSS-RRT-BEB Search: Search bus which is neither in IQ nor the garage",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3308"
			)
	public void Test_TOUC_3308() throws InterruptedException {


		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;
		
		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Get the first Bus
		busEntry = busMap.entrySet().iterator().next();
		busName = busEntry.getValue();
		
		//Search Bus
		rrtPage.searchBEB(busName);
		
		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Select Needs Maintenance
		rrtPage.needsMaintenanceSetOrRemove(true);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();
		
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
