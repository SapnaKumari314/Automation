package test.java.com.proterra.testcases.apexUI.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import test.java.com.proterra.testcases.apexUI.BaseTestClass;

public class LogoutFromAPEX extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="CCSS-Logout: Logout from CCSS",
			dependsOnGroups = "Login",
			groups = {"Logout","Smoke","Regression"}
			)
	public void Test_Logout() throws InterruptedException {

		//Verify that logout button is displayed
		loginPage.logoutUser();

		sAssert.assertTrue(loginPage.input_email_id.isDisplayed(), "User Logged Out");

	}


}
