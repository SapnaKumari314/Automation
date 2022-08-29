package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.PageObjecs.CCSS_Login_Page;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class NavigateToCCSS extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="Navigate to the CCSS Application",
			groups = "Navigation"
			)
	public void Test_Navigation() throws InterruptedException {

		//Navigate to Login Page
		loginPage = new CCSS_Login_Page();
		loginPage.navigateToPage(CCSS_APPLICATION_LOGIN_URL);
		sAssert.assertTrue(loginPage.pageHeader.isDisplayed(), "Login page is displayed");
		
		sAssert.assertAll();
	}


}
