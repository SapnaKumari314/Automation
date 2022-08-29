package test.java.com.proterra.testcases.ccss.Menu;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3299_CCSSMenu extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3299: CCSS-Menu: Verify the menu",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3299"
			)
	public void Test_TOUC_3299() throws InterruptedException {
		
		
		//Click on the Menu button
		loginPage.click(loginPage.CCSS_Menu, "Menu button");
		
		//Get all the menu options
		List<WebElement> submenuEleList = new LinkedList<>();
		submenuEleList = loginPage.CCSS_SubMenuList;
		
		List<String> submenuOptions = new LinkedList<String>();
		for (WebElement webElement : submenuEleList) {
			submenuOptions.add(webElement.getText());
		}
		
		//Verify each menu option
		sAssert.assertTrue(submenuOptions.contains(loginPage.CCSS_Submenu_RunAndTrackPage), "Run & Track Page");
		
		sAssert.assertTrue(submenuOptions.contains(loginPage.CCSS_Submenu_ChargerControlPage), "Charger Control Page");
		
		sAssert.assertTrue(submenuOptions.contains(loginPage.CCSS_Submenu_GarageOverviewPage), "Garage Overview Page");
		
		sAssert.assertTrue(submenuOptions.contains(loginPage.CCSS_Submenu_EnergyManagementPage), "Energy Management Page");
		
		sAssert.assertTrue(submenuOptions.contains(loginPage.CCSS_Submenu_ConfigurationPage), "CCSS Configuration Page");

		sAssert.assertAll();
	}
}
