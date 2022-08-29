package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BusDetails;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3311_Components extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3311: CCSS-RRT-Bus Details: Bus Details components",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3311"
			)
	public void Test_TOUC_3311() throws InterruptedException {


		//Get all buses in the garage
		Map<String, String> busMap;
		Map.Entry<String,String> busEntry;
		String busName;

		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Get the first Bus
		busEntry = busMap.entrySet().iterator().next();
		busName = busEntry.getValue();

		//Search for any Bus
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Verify that all the components are displayed
		sAssert.assertEquals(rrtPage.CCSS_BusDetailsComponentHeader.getText(), "Bus Details", "Bus Details Component Header");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsBusIDLabel.isDisplayed(), "Bus ID label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsCurrentEnergyLabel.isDisplayed(), "Current Energy label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsSOCLabel.isDisplayed(), "SOC label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsRangeLabel.isDisplayed(), "Range label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsOdometerLabel.isDisplayed(), "Odometer label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsEnergyConsumptionLabel.isDisplayed(), "Energy Consumption label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsHDDTCSLabel.isDisplayed(), "HDDTC label display");
		
		sAssert.assertTrue(rrtPage.CCSS_BusDetailsNeedsMaintenanceLabel.isDisplayed(), "Needs Maintenance label display");
		
		sAssert.assertTrue(rrtPage.CCSS_RRT_Btn.isDisplayed(), "Recommended Run & Track button display");

		sAssert.assertAll();


	}


}
