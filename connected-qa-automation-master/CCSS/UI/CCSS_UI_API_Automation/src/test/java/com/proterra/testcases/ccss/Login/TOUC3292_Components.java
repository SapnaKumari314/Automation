package test.java.com.proterra.testcases.ccss.Login;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3292_Components extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3292: CCSS-Login: Login page UX",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3292"
			)
	public void Test_TOUC_3292() throws InterruptedException {


		//Verify Login header
		sAssert.assertEquals(loginPage.pageHeader.getText(), "Login", "Login Page Header Label Text");

		//Verify Date in header
		sAssert.assertEquals(loginPage.systemDate.getText(), dateUtils.getSystemDateTime("dd MMM"), "Login Page System Date Header Label Text");

		//Verify TimeStamp in header
		sAssert.assertEquals(loginPage.systemTime.getText(), dateUtils.getSystemDateTime("HH:mm"), "Login Page System Time Header Label Text");

		sAssert.assertAll();

	}


}
