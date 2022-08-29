package test.java.com.proterra.testcases.ccss.EnergyManagement.OnlinePowerCapacity;

import java.awt.AWTException;
import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

// import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC2816 extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-2816: Update the online power capacity percentage",
			dependsOnGroups = "Navigation",
			groups = "Regression"
			)
			/*
			 * @CustomAnnotations( jiraNumber = "2816" )
			 */
	public void Test_TOUC_2816() throws InterruptedException, AWTException {

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);
		
		// Set Online Power Capacity Percent Value to between 0 to 100
		sAssert.assertTrue(energyManagementPage.setPowerCapacityPercentValue("60"),"set online power capacity value 0 to 100");
		
		// Set Online Power Capacity Percent Value to non numeric value
		sAssert.assertFalse(energyManagementPage.setPowerCapacityPercentValue("ABC"), "Set online power capacity with non numeric value");
		
		// Set Online Power Capacity Percent Value to greaterthan 100
		sAssert.assertFalse(energyManagementPage.setPowerCapacityPercentValue("105"), "Set online power capacity beyond value 100");
		
		// Set Online Power Capacity Percent Value to negative value
		sAssert.assertFalse(energyManagementPage.setPowerCapacityPercentValue("-8"), "Set online power capacity below 0 value");
		
		// Set Online Power Capacity Percent Value to 100
		boolean powerCapacityPercentValue = energyManagementPage.setPowerCapacityPercentValue("100");
		
		// Verify Online power value is equal to incoming power value when power capacity is 100
		sAssert.assertEquals(energyManagementPage.getPowerCapacityValue(), energyManagementPage.getIncomingPowerValue(), "Power capacity value is equal to incoming power value when it is 100");

		sAssert.assertAll();

	}


}
