package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3317_Components extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3317: CCSS-RRT-Run Details: Run Details Components",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3317"
			)
	public void Test_TOUC_3317() throws InterruptedException {


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
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsRunIdLabel.isDisplayed(), "Run ID label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsRequiredEnergyLabel.isDisplayed(), "Required Energy label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsEstimatedChargeTimeLabel.isDisplayed(), "Estimated Charge Time label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsBookOutDateTimeLabel.isDisplayed(), "Book Out Date, Time label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsDistanceLabel.isDisplayed(), "Distance label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RunDetailsRunStatusLabel.isDisplayed(), "Run Status label display");

		sAssert.assertAll();


	}


}
