package test.java.com.proterra.testcases.ccss.EnergyManagement;

import java.awt.AWTException;
import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

//import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import main.java.com.proterra.PageObjecs.CCSS_EnergyManagement_Page;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC2818 extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2818: Update Charge Station Monitoring Limit on the basis installed chargers",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	//@CustomAnnotations(
			//jiraNumber = "2818"
			//)
	public void Test_TOUC_2818() throws InterruptedException, AWTException {


		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);
		
		int chargersCount = apiDat.getInstalledChargersCount();
		
		// Verify active charger session count value
		sAssert.assertEquals(energyManagementPage.getPowerCapacityPercentage(), "100", "Verify power capacity percent value as 100");

		// Verify Charging limit value editable status
		sAssert.assertTrue(energyManagementPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_ActiveChargerLimit),
				"Active charging limit field is editable.");
		
		// Set Charge Limit value to 0
		sAssert.assertTrue(energyManagementPage.setChargeMonitorLimitValue("0"), "Set charge monitor limit value to 0");
		
		// Set Charge Limit value to less than 0
		sAssert.assertFalse(energyManagementPage.setChargeMonitorLimitValue("-1"), "Set charge monitor limit value to negative value");
		
		// Set Charge Limit value to non numeric value
		sAssert.assertFalse(energyManagementPage.setChargeMonitorLimitValue("A"), "Set charge monitor limit value to non numeric value");
		
		// Set Charge monitor Limit value to greaterthan session limit
		sAssert.assertFalse(energyManagementPage.setChargeMonitorLimitValue("5"), "Set charge monitor limit value to greaterthan session limit.");
		
		// Set Charge monitor limit value from 0 to installed chargers
		for(int i=0;i<=chargersCount;i++) {
			sAssert.assertTrue(energyManagementPage.setChargeMonitorLimitValue(String.valueOf(i)), "Set charge monitor limit value to "+i);
			
		}
		sAssert.assertAll();

	}


}
