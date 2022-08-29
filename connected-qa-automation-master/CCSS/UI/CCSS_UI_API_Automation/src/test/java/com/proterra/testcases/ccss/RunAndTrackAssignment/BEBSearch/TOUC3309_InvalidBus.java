package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BEBSearch;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3309_InvalidBus extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3309: CCSS-RRT-BEB Search: Search invalid bus",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3309"
			)
	public void Test_TOUC_3309() throws InterruptedException {


		//Search Bus
		action = new Actions(DriverManager.getDriver());
		rrtPage.clear(rrtPage.bebSearchBox, "BEB Search Box");
		action.click(rrtPage.bebSearchBox).sendKeys(rrtPage.bebSearchBox, "9999").build().perform();
		
		boolean flag = rrtPage.CCSS_SearchAutoCompleteListValue.isDisplayed();
		
		sAssert.assertFalse(flag, "BEB Search Auto Complete Dropdown for Invalid Bus is not populated");

		sAssert.assertAll();


	}


}
