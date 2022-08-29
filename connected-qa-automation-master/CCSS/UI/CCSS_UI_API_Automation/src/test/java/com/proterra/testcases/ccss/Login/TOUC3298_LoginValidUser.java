package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3298_LoginValidUser extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3298: CCSS-Login: Login with valid user name and password",
			dependsOnGroups = "Navigation",
			groups = {"Login","Smoke","Regression"}
			)
	@CustomAnnotations(
			jiraNumber = "3298"
			)
	public void Test_TOUC_3298() throws InterruptedException {

		//Login with invalid user
		loginPage.loginAsValidUser(USER_NAME, PASSWORD);

		//Verify that Run and Track screen is displayed
		sAssert.assertTrue(loginPage.runAndTrackHeader.isDisplayed(), "User is logged in");
		
		//Switch the garage
		rrtPage.switchToGarage(GARAGE_NAME);

		sAssert.assertAll();

	}


}
