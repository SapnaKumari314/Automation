package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3296_BlankUsername extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3296: CCSS-Login: Login with blank username",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3296"
			)
	public void Test_TOUC_3296() throws InterruptedException {
		
		loginPage.loginUserId.clear();
		loginPage.loginPassword.clear();

		//Login with empty user
		loginPage.loginUserId.sendKeys("");
		loginPage.loginUserId.sendKeys(Keys.TAB);
		loginPage.loginPassword.sendKeys("password");


		//Verify the error
		String actualError = loginPage.emptyUserError.getText();
		sAssert.assertEquals(actualError, "Username is required", "Login Page Empty Username Error");

		loginPage.loginUserId.clear();
		loginPage.loginPassword.clear();

		sAssert.assertAll();
	}


}
