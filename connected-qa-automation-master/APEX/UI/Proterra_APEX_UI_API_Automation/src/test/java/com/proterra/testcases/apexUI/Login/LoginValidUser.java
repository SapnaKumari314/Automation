package test.java.com.proterra.testcases.apexUI.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import test.java.com.proterra.testcases.apexUI.BaseTestClass;

public class LoginValidUser extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="Login with valid user name and password",
			dependsOnGroups = "Navigation",
			groups = {"Login","Smoke","Regression"}
			)
	public void Test_LoginValidUser() throws InterruptedException {

		//Login with invalid user
		loginPage.loginAsValidUser(USER_NAME, PASSWORD);

		//Verify that Run and Track screen is displayed
		sAssert.assertTrue(loginPage.button_user_profile.isDisplayed(), "User is logged in");
		
		sAssert.assertAll();

	}


}
