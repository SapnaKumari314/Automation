package test.java.com.proterra.testcases.ccss.EnergyManagement.OnlinePowerCapacity;

import java.awt.AWTException;
import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC2817 extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2817: Online Power Capacity: Update the online power capacity value",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "2817"
			)
	public void Test_TOUC_2817() throws InterruptedException, AWTException {

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);
		
		int incomingPowerValue = Integer.parseInt(energyManagementPage.getIncomingPowerValue());
		
		int onlinePowerCapacityValue = energyManagementPage.getPowerCapacityValue();
		
		// Set Power capacity value between 0 and incoming power value
		sAssert.assertTrue(onlinePowerCapacityValue<=incomingPowerValue, "Power capacity valuue in between 0 and incoming power value");
		
		// Set Power capacity value to non numeric value
		sAssert.assertFalse(energyManagementPage.setPowerCapacityValue("ABC"), "Set online power value with non numeric value");
		
		// Set Online power capacity value to greaterthan incoming power value
		sAssert.assertFalse(energyManagementPage.setPowerCapacityValue(String.valueOf(incomingPowerValue+1)), "Set online power capacity value with more than incoming power value");
		
		// Set Online power capacity value to negative value
		sAssert.assertFalse(energyManagementPage.setPowerCapacityValue("-8"), "Set online power capacity value below 0 value");
		
		boolean status = energyManagementPage.setPowerCapacityValue(energyManagementPage.getIncomingPowerValue());
		
		sAssert.assertEquals(energyManagementPage.getPowerCapacityPercentage(), "100", "Power capacity percent value is 100 when incoming power value entered");
		
		sAssert.assertAll();

	}


}
