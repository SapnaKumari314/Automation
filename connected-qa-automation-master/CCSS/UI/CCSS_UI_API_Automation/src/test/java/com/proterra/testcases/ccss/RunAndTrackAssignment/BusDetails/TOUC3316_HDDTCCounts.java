package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BusDetails;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3316_HDDTCCounts extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3316: CCSS-RRT-Bus Details: HDDTC count",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3316"
			)
	public void Test_TOUC_3316() throws InterruptedException {

		Map<String, Integer> ccBusFaultsMap = new LinkedHashMap<>();
		
		int uiFaultCount;
		int busFaultsCount;
		

		//Navigate to Charger Control Screen
		loginPage.navigateToSubmenu("Charger Control");

		//Get the Bus Map with counts for the Bus Faults
		for (String bus : chargerControlPage.getBusFaultBusNames()) {
			
			if (ccBusFaultsMap.containsKey(bus)) {
				int currentCount = ccBusFaultsMap.get(bus);
				ccBusFaultsMap.replace(bus,  currentCount + 1);
			}else {
				ccBusFaultsMap.put(bus, 1);
			}
		}

		//Navigate to Run and Track Screen
		loginPage.navigateToSubmenu("Run & Track Assignment");


		//Iterate through the map for all buses
		for(Map.Entry<String, Integer> entry: ccBusFaultsMap.entrySet()) {
		
			//Search the bus
			rrtPage.searchBEB(entry.getKey());
			
			//Verify that BEB Search box is clear
			sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");
		
			//Compare the UI HDDTC Count Value with the value from the map
			uiFaultCount = Integer.parseInt(rrtPage.CCSS_BusDetailsHDDTCSVal.getText());
			busFaultsCount = ccBusFaultsMap.get(entry.getKey().toString());
			
			sAssert.assertEquals(uiFaultCount, busFaultsCount, "The Fault count from Bus Details and Charger Control Bus Faults match for the Bus "+entry.getKey().toString());
		}

		sAssert.assertAll();


	}


}
