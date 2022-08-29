package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3297_BlankPassword extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3297: CCSS-Login: Login with blank password",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3297"
			)
	public void Test_TOUC_3297() throws InterruptedException {
		
		loginPage.loginUserId.clear();
		loginPage.loginPassword.clear();

		//Login with empty password
		loginPage.loginUserId.sendKeys(USER_NAME);
		loginPage.loginPassword.sendKeys("");
		loginPage.loginPassword.click();
		loginPage.loginPassword.sendKeys(Keys.TAB);


		//Verify the error
		String actualError = loginPage.emptyPasswordError.getText();
		sAssert.assertEquals(actualError, "Password is required", "Login Page Empty Password Error");

		loginPage.loginUserId.clear();
		loginPage.loginPassword.clear();

		sAssert.assertAll();
	}


}
