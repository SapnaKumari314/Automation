package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3294_InvalidPassword extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3294: CCSS-Login: Login with invalid Password from CCSS",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3294"
			)
	public void Test_TOUC_3294() throws InterruptedException {
		
		//Login with invalid user
		loginPage.loginAsInValidUser(USER_NAME, "123472346");

		//Verify the error
		String expectedError = loginPage.loginInvalidUserError.getText().substring(0,loginPage.loginInvalidUserError.getText().indexOf("."));
		sAssert.assertEquals(expectedError, "Incorrect username or password", "Login Page Invalid Password Error");

		Thread.sleep(2000);

		//Clear the fields
		loginPage.loginUserId.clear();
		Thread.sleep(2000);

		loginPage.loginPassword.clear();
		Thread.sleep(2000);

		sAssert.assertAll();
	}


}
