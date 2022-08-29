package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class LogoutFromCCSS extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="CCSS-Logout: Logout from CCSS",
			dependsOnGroups = "Login",
			groups = {"Logout","Smoke","Regression"}
			)
	@CustomAnnotations(
			jiraNumber = ""
			)
	public void Test_Logout() throws InterruptedException {

		//Verify that logout button is displayed
		loginPage.logoutUser();

		sAssert.assertEquals(loginPage.pageHeader.getText(), "Login", "User Logged Out");

	}


}
