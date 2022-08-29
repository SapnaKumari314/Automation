package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3320_RecommendedRunMaintenanceBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3320: CCSS-RRT-Run Details: Recommended Run Details for Maintenance Bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3320"
			)
	public void Test_TOUC_3320() throws InterruptedException {


		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;

		Map<String, String> uiRunDetailsMap;

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
		rrtPage.needsMaintenanceSetOrRemove(true);

		//Click Recommended Run & Track button
		rrtPage.clickRecommendedRunAndTrack();

		//Store UI Run Details into a map
		uiRunDetailsMap = rrtPage.uiRunDetails();


		//Verify that the Run Details section header
		sAssert.assertEquals(rrtPage.CCSS_RunDetailsComponentHeader.getText(), "Recommended Run", "Recommended Run Details Header");

		//Verify that Run ID is displayed
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.RUN_ID).isEmpty(), "Run ID Value "+uiRunDetailsMap.get(rrtPage.RUN_ID));

		//Verify the Required Energy
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY).isEmpty(), "Required Energy Value "+uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY));
		
		//Verify the Estimated Charge Time
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME).isEmpty(), "Estimated Charge Time Value "+uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME));
		
		//Verify the Bookout Date, Time
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME).isEmpty(), "Book Out Date, Time Value "+uiRunDetailsMap.get(rrtPage.BOOKOUT_DATE_TIME));
		
		//Verify the Distance
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.DISTANCE).isEmpty(), "Distance Value "+uiRunDetailsMap.get(rrtPage.DISTANCE));
		
		//Verify the Run Status
		sAssert.assertTrue(uiRunDetailsMap.get(rrtPage.RUN_STATUS).isEmpty(), "Run Status for Recommended Run");
		

		sAssert.assertAll();


	}


}
