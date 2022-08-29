package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.IncomingQueue;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3304_IQBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3304: CCSS-RRT-IQ: Click on Bus in IQ",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3304"
			)
	public void Test_TOUC_3304() throws InterruptedException {


		//Set Browser Zoom
		setBrowserZoomPercentage("80");

		//Get list of buses from UI incoming queue
		List<WebElement> listBusID = rrtPage.listIncomingQueueBusID;

		for (int i = 0; i < listBusID.size(); i++) {

			//Get the Bus ID
			String busID = listBusID.get(i).getText();
			
			//Click on the Bus Icon
			listBusID.get(i).click();
			
			//Verify that Bus details are populated
			String busDetailsBusID = rrtPage.CCSS_BusDetailsBusIDVal.getText();
			
			sAssert.assertTrue(busDetailsBusID.equals(busID), "Bus details populated after clicking Bus "+busID+" from IQ");
		}
		
		sAssert.assertAll();

		//Set Browser Zoom
		setBrowserZoomPercentage("100");

	}


}
