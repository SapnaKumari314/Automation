package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.AssignRunAndTrack;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3377_OverriddenRunRecommendedTrackNonMaintenance extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3377: CCSS-RRT-Assign Run and Track: Assign Overriden Run and default Track for Non Maintenance Bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3377"
			)
	public void Test_TOUC_3377() throws InterruptedException {


		List<String> iqBusList;

		Map<String, String> uiTrackDetailsMap;


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

		//Select Overridden run
		rrtPage.selectOverriddenRecommendedRun();

		//Confirm selected run
		rrtPage.confirmRunOverride();

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
