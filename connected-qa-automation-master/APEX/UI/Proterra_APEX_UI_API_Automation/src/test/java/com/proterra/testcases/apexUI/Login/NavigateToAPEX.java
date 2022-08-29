package test.java.com.proterra.testcases.apexUI.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import test.java.com.proterra.testcases.apexUI.BaseTestClass;

public class NavigateToAPEX extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="Navigate to the APEX Application",
			groups = "Navigation"
			)
	public void Test_Navigation() throws InterruptedException {

		//Navigate to Login Page
		loginPage.navigateToPage(APEX_APPLICATION_LOGIN_URL);
		sAssert.assertTrue(loginPage.input_email_id.isDisplayed(), "APEX Login page is displayed");
		
		sAssert.assertAll();
	}


}
