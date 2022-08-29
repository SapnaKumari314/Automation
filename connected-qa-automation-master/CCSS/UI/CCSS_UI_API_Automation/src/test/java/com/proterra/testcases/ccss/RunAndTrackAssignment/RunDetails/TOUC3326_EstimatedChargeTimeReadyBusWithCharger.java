package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3326_EstimatedChargeTimeReadyBusWithCharger extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3326: CCSS-RRT-Run Details: Estimated Charge Time when charger position is available for Ready or Ready/Topped Up Buses",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3326"
			)
	public void Test_TOUC_3326() throws InterruptedException {


		//Variables
		Map<String, String> uiBusDetailsMap;
		Map<String, String> uiRunDetailsMap = new LinkedHashMap<>();
		Map<String, String> busMap;

		Map<String,List<Object>> chargerTrackPositionDetalsMap = new LinkedHashMap<>();

		List<String> selectedRunDetailsList = new LinkedList<>();

		String selectedRunsFlag = "skip";

		String busName = null;
		String chargerName = null;

		int currentEnergy = 0;
		int requiredEnergy = 0;


		//Get the next available charger position
		chargerTrackPositionDetalsMap = apiDat.getAllTrackPositionsData();

		for(Map.Entry<String, List<Object>> entry: chargerTrackPositionDetalsMap.entrySet()) {

			chargerName = entry.getKey();

			if ((boolean) entry.getValue().get(apiDat.listIndex_isChargerInstalled)) {
				if ((boolean) entry.getValue().get(apiDat.listIndex_chargerAvailableForCharging)) {
					break;
				}
			}
		}
		reportStep(Status.PASS, "The charger is available at position "+chargerName);


		//Search the Bus from Garage allocated Buses
		busMap = apiDat.getGarageSpecificBuses();

		//Search for a Bus
		for(Map.Entry<String, String> entry: busMap.entrySet()) {

			busName = entry.getValue();

			//Search for the Bus
			rrtPage.searchBEB(busName);
			reportStep(Status.PASS, "Bus "+busName+"is searched");

			//Remove Maintenance Flag
			rrtPage.needsMaintenanceSetOrRemove(false);

			//Store Bus Details
			uiBusDetailsMap = rrtPage.uiBusDetails();

			//Get the Current Energy from the Bus Details
			currentEnergy = Integer.parseInt(uiBusDetailsMap.get(rrtPage.CURRENT_ENERGY).replaceAll("[^0-9]", ""));

			//Verify that BEB Search box is clear
			sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

			//Click Recommended Run & Track button
			rrtPage.clickRecommendedRunAndTrack();

			//Store UI Run Details into a map
			uiRunDetailsMap = rrtPage.uiRunDetails();

			//Check if Current Energy < Required Energy
			requiredEnergy = Integer.parseInt(uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY).replaceAll("[^0-9]", ""));

			//If Current Energy > Required Energy for the default run, use the same run, else search for a run matching the requirement
			if (currentEnergy > requiredEnergy) {
				selectedRunsFlag = "skip";
				break;
			}else {

				//Click the Run Override button
				rrtPage.clickOverrideRun();

				//Get All Runs
				selectedRunDetailsList = rrtPage.selectRunByBusStatus(rrtPage.READY, currentEnergy);

				if (selectedRunDetailsList.isEmpty()) {
					selectedRunsFlag = "false";
					continue;
				}

				selectedRunsFlag = "true";

				//Confirm selected run
				rrtPage.confirmRunOverride();

				uiRunDetailsMap = rrtPage.uiRunDetails();

				requiredEnergy = Integer.parseInt(uiRunDetailsMap.get(rrtPage.REQUIRED_ENERGY).replaceAll("[^0-9]", ""));

				break;
			}

		}

		if (selectedRunsFlag.equalsIgnoreCase("false")) {
			throw new SkipException("Runs to match the condition of Current Energy > Required Energy are not available");
		}else if(selectedRunsFlag.equalsIgnoreCase("true")) {
			//Verify the Estimated Charge Time on the Override screen
			sAssert.assertEquals(selectedRunDetailsList.get(rrtPage.overrideRun_columnIndex_estimatedChargeTime), "00:00",
					"Estimated Charger Time for the Bus "+busName+" for the selected Run on the Run Override screen");
		}

		//Verify the Estimated Charge Time in the Run Details component
		sAssert.assertEquals(uiRunDetailsMap.get(rrtPage.ESTIMATED_CHARGE_TIME), "00:00",
				"Estimated Charger Time for the Bus "+busName+" for the selected Run on the Run Details component");

		sAssert.assertAll();
	}
}
