package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.AssignRunAndTrack;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3380_AssignMaintenanceBusWithoutTrack extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3380: CCSS-RRT-Assign Run and Track: Assign Maintenance Bus without track",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3380"
			)
	public void Test_TOUC_3380() throws InterruptedException, IOException {

		List<String> iqBusList;

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
		rrtPage.needsMaintenanceSetOrRemove(true);

		//Click the Recommended Run and Track
		rrtPage.clickRecommendedRunAndTrack();


		//Click Assign
		String toastMsg = rrtPage.clickAssignRunAndTrack();
		String expectedMessage = "Bus "+busName+" has been successfully set In Maintenance but not assigned to a position in the garage";

		sAssert.assertEquals(toastMsg, expectedMessage, "Toast Message after Bus assignment");
		System.out.println(toastMsg);


		sAssert.assertAll();

	}


}
