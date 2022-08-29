package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BEBSearch;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3305_IQBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3305: CCSS-RRT-BEB Search: Search bus from IQ",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3305"
			)
	public void Test_TOUC_3305() throws InterruptedException {


		//Get list of buses from incoming queue
		List<WebElement> listBusID = rrtPage.listIncomingQueueBusID;

		//Loop through all the Buses and search each Bus
		for (WebElement busID : listBusID) {
			if (!(busID.getText().isBlank() || busID.getText().isEmpty())) {
				rrtPage.searchBEB(busID.getText());
				//Validate the Bus Details
				sAssert.assertTrue(rrtPage.CCSS_BusDetailsBusIDVal.getText().equals(busID.getText()), "Bus details populated after clicking Bus "+busID.getText()+" from IQ");
			}else {
				break;
			}
		}

		sAssert.assertAll();


	}


}
