package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.IncomingQueue;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3301_Components extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3301: CCSS-RRT-IQ: Incoming Queue components",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3301"
			)
	public void Test_TOUC_3301() throws InterruptedException {


		//Set Browser Zoom
		setBrowserZoomPercentage("80");

		//Get list of buses from incoming queue
		Map<String, String> apiBusMap = apiDat.getIncomingBuses();

		//Verify the count of Incoming Buses from the API
		int apiIQSize = apiBusMap.size();
		sAssert.assertTrue(apiIQSize <= 11, "API Incoming Queue Size");

		//Get list of buses from UI incoming queue
		List<WebElement> listBusID = rrtPage.listIncomingQueueBusID;
		List<WebElement> listBusBookinTime = rrtPage.listIncomingQueueBusBookinTime;
		int uiIQSize = listBusID.size();
		sAssert.assertTrue(uiIQSize <= 11, "UI Incoming Queue Size");

		//Compare UI and API count
		sAssert.assertEquals(apiIQSize, uiIQSize, "Incoming Buses in UI and API");

		//Verify the Bus component on the UI
		for (WebElement busID : listBusID) {
			sAssert.assertNotEquals(busID.getText(), "","Incoming Bus ID "+busID.getText());
		}

		for (WebElement busBookinTime : listBusBookinTime) {
			sAssert.assertNotEquals(busBookinTime.getText(), "","Incoming Bus Bookin Time "+busBookinTime.getText());
		}


		sAssert.assertAll();

		//Set Browser Zoom
		setBrowserZoomPercentage("100");

	}


}
