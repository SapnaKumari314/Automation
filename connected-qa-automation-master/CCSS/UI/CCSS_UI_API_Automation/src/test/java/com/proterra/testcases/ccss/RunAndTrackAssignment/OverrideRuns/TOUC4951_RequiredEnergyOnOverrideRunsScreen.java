package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.OverrideRuns;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC4951_RequiredEnergyOnOverrideRunsScreen extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-4951: CCSS-RRT-Run Override: All runs displayed have Required Energy < Battery Capacity",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "4951"
			)
	public void Test_TOUC_4951() throws InterruptedException {


		List<String> iqBusList;

		List<List<String>> overrideRunList = new ArrayList<>();
		
		boolean energyFlag = true;


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
			int requiredEnergy = Integer.parseInt(list.get(rrtPage.overrideRun_columnIndex_requiredEnergy).replaceAll("[^0-9]", ""));
			if (requiredEnergy > 528) {
				energyFlag = false;
				reportStep(Status.FAIL, "Required Energy "+requiredEnergy+" for Run "+runId+" is greater than battery capacity of 528");
			}
		}
		sAssert.assertTrue(energyFlag, "Required Energy for all runs is less than battery capacity");
		
		rrtPage.cancelRunOverride();

		sAssert.assertAll();
	}
}
