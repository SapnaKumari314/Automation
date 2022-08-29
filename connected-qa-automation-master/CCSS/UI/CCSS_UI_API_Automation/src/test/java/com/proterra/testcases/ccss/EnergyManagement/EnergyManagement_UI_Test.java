package test.java.com.proterra.testcases.ccss.EnergyManagement;

import java.awt.AWTException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.PageObjecs.CCSS_EnergyManagement_Page;
import main.java.com.proterra.PageObjecs.CCSS_Login_Page;
import test.java.com.proterra.testcases.ccss.BaseTestClass;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponse;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;

public class EnergyManagement_UI_Test extends BaseTestClass {

	public static Logger log = Logger.getLogger(EnergyManagement_UI_Test.class.getName());
	public static SoftAssertLogger sAssert;
	public static Properties prop;

	// Instantiate Objects
	public static CCSS_Login_Page loginPage;
	public static CCSS_EnergyManagement_Page emnPage;
	public static GetAPIResponseData apiDat;
	public static GetAPIResponse apiResp;

	//@Test(priority = 1, description = "Verify the Energy management screen values are displayed and editable or not")
	public void Test_CCSSValidateEnergyManagementValue() throws IOException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		// Verify Incoming power value
		sAssert.assertNotEquals(emnPage.getIncomingPowerValue(), "N/A", "Incoming power field has numeric value");

		// Verify Incoming power value editable status
		sAssert.assertFalse(emnPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_IncomingPowerValue),
				"Incoming power field is not editable.");

		// Verify PowerCapcityPercentage value editable status
		sAssert.assertTrue(emnPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_PowerCapacityPercent),
				"PowerCapacityPercentage field is editable.");

		// Verify PowerCapcity value editable status
		sAssert.assertTrue(emnPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_PowerCapacityValue),
				"PowerCapacity field is editable.");

		// Verify Charging limit value editable status
		sAssert.assertTrue(emnPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_ActiveChargerLimit),
				"Active charging limit field is editable.");

		// Verify Cumulative delay value editable status
		sAssert.assertFalse(emnPage.isFieldEditable(CCSS_EnergyManagement_Page.CCSS_CumulativeBookoutDelay),
				"Cumulative Bookout delay field is not editable.");
	}

	//@Test(priority = 2, description = "Verify that the Online power capacity value and percentage logic")
	public void Test_CCSSValidateCapacityValueAndPercentageLogic() throws IOException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		// Verify online power capacity percentage when power capacity value changed
		sAssert.assertEquals(emnPage.calculatePowerCapacityPercentage(),
				Integer.parseInt(emnPage.getPowerCapacityPercentage()),
				"Verify Online Power Capacity Percentage when Online Power Capacity Value is changed.");

		// Verify online power capacity value when power capacity percentage changed
		sAssert.assertTrue(emnPage.calculatePowerCapacityValue(),
				"Verify Online " + "Verify Power Capacity value when Online Power Capacity percentage is changed.");
	}

	//@Test(priority = 3, description = "Verify charging station monitoring limit with different values")
	public void Test_CCSSValidateChargingMonitoringLimit() throws IOException, AWTException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		sAssert.assertTrue(emnPage.setPowerCapacityPercentValue("100"),
				"Changing power capacity percentage value not set to 100");
		System.out.println("after percentage value set");
		int chargersCount = apiDat.getInstalledChargersCount();
		HashMap<String, Integer> chargersPower = apiDat.getChargersNameAndPower();
		int totalChargerPower = 0;
		for (int i : chargersPower.values()) {
			totalChargerPower = totalChargerPower + i;
		}
		int avgChargePower = totalChargerPower / chargersCount;
		int chargerLimit = (int) ((emnPage.getPowerCapacityValue()) / avgChargePower);
		int activeChargingLimit = Math.min(chargerLimit, chargersCount);

		// Verify Max no of active chargers limit
		sAssert.assertEquals(emnPage.getChargerSessionLimit(), Integer.toString(activeChargingLimit),
				"Active charger limit value should match with max chargers limit.");

		// verify Active charger limit
		for (int i = 1; i <= activeChargingLimit; i++) {
			sAssert.assertTrue(emnPage.setChargeMonitorLimitValue(Integer.toString(i)),
					"Active charger limit value should match with: " + i);
		}
	}

	// @Test(priority = 4, description = "Verify the active chargers limit by changing online power capacity percentage")
	public void Test_CCSSValidateChargerLimitByPercentage() throws IOException, AWTException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		sAssert.assertTrue(emnPage.setPowerCapacityPercentValue("100"),
				"Power capacity percentage value should match with 100.");

		int chargersCount = apiDat.getInstalledChargersCount();
		HashMap<String, Integer> chargersPower = apiDat.getChargersNameAndPower();
		int totalChargerPower = 0;
		for (int i : chargersPower.values()) {
			totalChargerPower = totalChargerPower + i;
		}
		int avgChargePower = totalChargerPower / chargersCount;
		int chargerLimit = (int) ((emnPage.getPowerCapacityValue()) / avgChargePower);
		int activeChargingLimit = Math.min(chargerLimit, chargersCount);
		int noOfChargers = activeChargingLimit;

		for (int i = 0; i <= noOfChargers; i++) {
			int incomingPowerValue = Integer.parseInt(emnPage.getIncomingPowerValue());
			double chargeValue = avgChargePower * activeChargingLimit;
			double powercapacityPercent = (chargeValue / incomingPowerValue) * 100;
			int finalPowerPercent = (int) powercapacityPercent;
			boolean isValueSet = emnPage.setPowerCapacityPercentValue(String.valueOf(finalPowerPercent));
			if (isValueSet) {
				sAssert.assertEquals(emnPage.getChargerSessionLimit(), activeChargingLimit,
						"Verify Active charger limit value by changing Online power capacity percent value.");
				activeChargingLimit--;
			} else {
				break;
			}

		}
	}

	// @Test(priority = 5, description = "Verify Active charging sessions count")
	public void Test_CCSSValidateActiveChargingSession() throws IOException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		// Getting active charger session count
		int activeChargerSessionCount = apiDat.getActiveChargerSessionCount();

		// Verify active charger session count value
		sAssert.assertEquals(String.valueOf(activeChargerSessionCount), emnPage.getActiveChargerSession(),
				"Active charger session value should match");

	}
	
	@Test(priority = 1, description = "Verify Cumulative delay value")
	public void Test_CCSSValidateCumulativeDelayValue() throws IOException, InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Energy Management page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_EnergyManagementPage);

		// Getting active charger session count
		String activeChargerSessionCount = apiDat.getCumulativeDelayValue();

		sAssert.assertEquals(emnPage.getCumulativeDelayValue(), activeChargerSessionCount);

	}

	@BeforeTest
	public void setUp() throws InterruptedException {

		// Navigate to Login Page
		loginPage = new CCSS_Login_Page();
		emnPage = new CCSS_EnergyManagement_Page();
		apiDat = new GetAPIResponseData();
		apiResp = new GetAPIResponse();

		log.info("Test Started");
	}

	@AfterTest
	public void tearDown() {

		logInfo("Test Completed");
		log.info("Test Completed");

	}

}
