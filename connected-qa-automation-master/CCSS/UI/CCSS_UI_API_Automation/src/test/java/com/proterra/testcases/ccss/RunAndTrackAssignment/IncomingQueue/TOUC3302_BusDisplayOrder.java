package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.IncomingQueue;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3302_BusDisplayOrder extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3302: CCSS-RRT-IQ: Order of Bus display",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3302"
			)
	public void Test_TOUC_3302() throws InterruptedException {


		//Set Browser Zoom
		setBrowserZoomPercentage("80");

		//Get list of buses from incoming queue
		Map<String, String> apiBusMap = apiDat.getIncomingBuses();

		//Get list of buses from UI incoming queue
		List<WebElement> listBusID = rrtPage.listIncomingQueueBusID;
		List<WebElement> listBusBookinTime = rrtPage.listIncomingQueueBusBookinTime;

		Map<String, String> uiBusMap = new LinkedHashMap<>();
		for (int i = 0; i < listBusID.size(); i++) {
			String busID = listBusID.get(i).getText();
			String busBookinTime = listBusBookinTime.get(i).getText();

			uiBusMap.put(busID, busBookinTime);
		}

		long oldVal = 0;
		long newVal;
		boolean res = true;
		//Verify that the the bookin times are sorted in ascending order
		for(Map.Entry<String, String> entry: apiBusMap.entrySet()) {
			newVal = Long.parseLong(entry.getValue());
			if (oldVal > newVal) {
				res = false;
				break;
			}
			oldVal = newVal;
		}

		sAssert.assertEquals(res, true, "Incoming Queue Buses sorting by Bookin Time");


		sAssert.assertAll();

		//Set Browser Zoom
		setBrowserZoomPercentage("100");

	}


}
