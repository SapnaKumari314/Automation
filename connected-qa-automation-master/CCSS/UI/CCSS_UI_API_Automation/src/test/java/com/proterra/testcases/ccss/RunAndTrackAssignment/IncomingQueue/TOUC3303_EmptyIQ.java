package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.IncomingQueue;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3303_EmptyIQ extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3303: CCSS-RRT-IQ: No Buses in IQ",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3303"
			)
	public void Test_TOUC_3303() throws InterruptedException {


		//Set Browser Zoom
		setBrowserZoomPercentage("80");

		//Get list of buses from incoming queue
		Map<String, String> apiBusMap = apiDat.getIncomingBuses();

		//Verify the count of Incoming Buses from the API
		int apiIQSize = apiBusMap.size();
		sAssert.assertTrue(apiIQSize == 0, "API Incoming Queue Size");

		//Verify that Empty IQ text is displayed
		sAssert.assertTrue(rrtPage.CCSS_EmptyIncomingQueue.isDisplayed(), "Empty Queue text");
		
		//Verify that the IQ is empty
		sAssert.assertEquals(rrtPage.CCSS_EmptyIncomingQueue.getText(), "No buses are in queue", "Incoming Queue is empty");
		
		sAssert.assertAll();

		//Set Browser Zoom
		setBrowserZoomPercentage("100");

	}


}
