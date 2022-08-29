package test.java.com.proterra.testcases.ccss.GarageOverview;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.PageObjecs.CCSS_GarageOverview_Page;
import main.java.com.proterra.PageObjecs.CCSS_Login_Page;
import test.java.com.proterra.testcases.ccss.BaseTestClass;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponse;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;

public class GarageOverview_UI_Test extends BaseTestClass {
	public static Logger log = Logger.getLogger(GarageOverview_UI_Test.class.getName());
	public static SoftAssertLogger sAssert;
	public static Properties prop;

	// Instantiate Objects
	public static CCSS_Login_Page loginPage;
	public static CCSS_GarageOverview_Page goPage;
	public static GetAPIResponseData apiDat;
	public static GetAPIResponse apiResp;

	@Test(priority = 1, description = "Verify the Logical charge Queue values are displayed or not")
	public void Test_CCSSVerifyLogicalChargeQueue() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);

		// Verify Logical Charge Queue label
		sAssert.assertEquals((CCSS_GarageOverview_Page.CCSS_LogicalChargeQueue_Label).getText(), "Logical Charge Queue",
				"Verify Logical charge queue label");

		// Validate LCQ Bus Id
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqBusId)).getText().trim(), "Bus ID",
				"Logical Charge Queue - Bus ID");

		// Validate LCQ Charge Station Id
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqChargingStationId)).getText().trim(),
				"Charging Station ID", "Logical Charge Queue -  Charging Station ID");

		// Validate LCQ Assign Run Id
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqAssignRunId)).getText().trim(), "Assigned Run ID",
				"Logical Charge queue -Assigned Run ID");

		// Validate LCQ Current Energy
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqCurrentEnergy)).getText().trim(), "Current Energy",
				"Logical Charge queue -Current Energy");

		// Validate LCQ Required Energy
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqRequiredEnergy)).getText().trim(), "Required Energy",
				"Logical Charge queue -Required Energy");

		// Validate LCQ Remaining Charge Time
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqRemChargeTime)).getText().trim(),
				"Remaining Charge Time", "Logical Charge queue - Remaining Charge Time ");

		// Validate LCQ Priority
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqPriority)).getText().trim(), "Priority",
				"Logical Charge queue - Priority");

		// Validate LCQ Charger Status
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqChargerStatus)).getText().trim(), "Charger Status",
				"Logical Charge queue - Charger Status");

		// Validate LCQ BookOut Time
		sAssert.assertEquals((goPage.CCSS_LCQ_Fields(goPage.lcqBookOutTime)).getText().trim(), "Book Out Time",
				"Logical Charge queue - Book Out Time");
	}

	@Test(priority = 2, description = "Verify the Charge History values are displayed or not")
	public void Test_CCSSVerifyChargeHistory() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);

		// Verify Charge History label
		sAssert.assertEquals((CCSS_GarageOverview_Page.CCSS_ChargeHistoryLabel).getText(), "Charge History",
				"Verify Charge History label");

		// Validate Charge History Bus Id
		sAssert.assertEquals((goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryChargingStationId)).getText().trim(),
				"Charging Station ID", "Charge History -  Charging Station ID ");

		// Validate Charge History Charge Station Id
		sAssert.assertEquals((goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryBusId)).getText().trim(), "Bus ID",
				"Charge History -   Bus ID");

		// Validate Charge History Charging Start Time
		sAssert.assertEquals((goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryChargeStartTime)).getText().trim(),
				"Charging Start time", "Charge History - Charging Start time");

		// Validate Charge History Charging session duration
		sAssert.assertEquals(
				(goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryChargeSessionDuration)).getText().trim(),
				"Charging Session Duration", "Charge History - Charging Session Duration");

		// Validate Charge History Energy Delivered
		sAssert.assertEquals((goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryEnergyDelivered)).getText().trim(),
				"Energy Delivered (kWh)", "Charge History - Energy Delivered (kWh)");

		// Validate Charge History Termination Reason
		sAssert.assertEquals((goPage.CCSS_ChargeHistory_Fields(goPage.chargeHistoryTerminateReason)).getText().trim(),
				"Termination Reason", "Charge History -  Termination Reason");

	}

	@Test(priority = 3, description = "Verify the Electric Bus Faults values are displayed or not")
	public void Test_CCSSVerifyElectricBusFaults() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);

		// Verify Electric Bus Faults label
		sAssert.assertEquals((CCSS_GarageOverview_Page.CCSS_ElectricBusFaults).getText(), "Electric Bus Faults",
				"Verify Electric Bus Faults label");

		// Validate Electric Bus Fault Bus Id
		sAssert.assertEquals((goPage.CCSS_ElectricBusFault(goPage.electricBusFaultBusId)).getText().trim(), "Bus ID",
				"Electric Bus Faults -  Bus ID");

		// Validate Electric Bus Fault Faulted System
		sAssert.assertEquals((goPage.CCSS_ElectricBusFault(goPage.electricBusFaultedSystem)).getText().trim(),
				"Faulted System", "Electric Bus Faults -  Faulted System");

		// Validate Electric Bus Fault Criticality
		sAssert.assertEquals((goPage.CCSS_ElectricBusFault(goPage.electricBusFaultCriticality)).getText().trim(),
				"Criticality", "Electric Bus Faults -  Criticality");
	}

	@Test(priority = 4, description = "Verify the Charger Faults values are displayed or not")
	public void Test_CCSSVerifyChargerFaults() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);

		// Verify Charger Faults Label
		sAssert.assertEquals((CCSS_GarageOverview_Page.CCSS_CF_ChargeFaultsLabel).getText(), "Charger Faults",
				"Verify Charger Faults label");

		// Validate Charger Faults Charging Station Id
		sAssert.assertEquals((goPage.CCSS_GO_ChargerFault(goPage.chargerFaultChargingStationId)).getText().trim(),
				"Charging Station ID", "Charger Faults - Charging Station ID");

		// Validate Charger Faults Faulted system
		sAssert.assertEquals((goPage.CCSS_GO_ChargerFault(goPage.chargerFaultFaultedSystem)).getText().trim(),
				"Faulted System", "Charger Faults - Faulted System");

		// Validate Charger Faults Criticality
		sAssert.assertEquals((goPage.CCSS_GO_ChargerFault(goPage.chargerFaultCriticality)).getText().trim(),
				"Criticality", "Charger Faults - Criticality");
	}

	@BeforeTest
	public void setUp() throws InterruptedException {

		// Navigate to Login Page
		loginPage = new CCSS_Login_Page();
		goPage = new CCSS_GarageOverview_Page();
		apiResp = new GetAPIResponse();
		apiDat = new GetAPIResponseData();

		log.info("Test Started");
	}

	@AfterTest
	public void tearDown() {

		logInfo("Test Completed");
		log.info("Test Completed");
	}
}
