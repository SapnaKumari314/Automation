package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.OverrideRuns;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3821_EstimatedChargeTimeNonRecommendedRunsOnOverrideRunsScreen extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3821: CCSS-RRT-Run Override: Non Recommended Runs Estimated Charge Time is N/A",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3821"
			)
	public void Test_TOUC_3821() throws InterruptedException {


		List<String> iqBusList;

		List<List<String>> overrideRunList = new ArrayList<>();
		
		boolean estChargeTimeFlag = true;


		String busName;

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

		//Click Override Run
		rrtPage.clickOverrideRun();

		//Get All Override Screen Run Details
		overrideRunList = rrtPage.getAllOverrideRunsDetails();

		//Verify the Required Energy of each run
		for (List<String> list : overrideRunList) {
			
			String runId = list.get(rrtPage.overrideRun_columnIndex_runID);
			String recommended = list.get(rrtPage.overrideRun_columnIndex_recommended);
			
			if (recommended == "Recommended") {
				continue;
			}
			String estimatedChargeTime = list.get(rrtPage.overrideRun_columnIndex_estimatedChargeTime);
			if (!(estimatedChargeTime.equals("N/A"))) {
				estChargeTimeFlag = false;
				reportStep(Status.FAIL, "Estimated Charge Time "+estimatedChargeTime+" for Non Recommended Run "+runId+" is calculated");
			}
		}
		sAssert.assertTrue(estChargeTimeFlag, "Estimated Charge Time is not calculated for Non Recommended Runs");
		
		rrtPage.cancelRunOverride();

		sAssert.assertAll();
	}
}
