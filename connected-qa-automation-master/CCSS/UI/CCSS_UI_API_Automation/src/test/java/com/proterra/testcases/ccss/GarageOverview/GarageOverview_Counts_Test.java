package test.java.com.proterra.testcases.ccss.GarageOverview;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.PageObjecs.CCSS_GarageOverview_Page;
import main.java.com.proterra.PageObjecs.CCSS_Login_Page;
import main.java.com.proterra.PageObjecs.CCSS_RecommendedRunAndTrack_Page;
import test.java.com.proterra.testcases.ccss.BaseTestClass;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponse;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;
import test.java.com.proterra.testcases.ccss.DataConstants.SetDataConstants;

public class GarageOverview_Counts_Test extends BaseTestClass{
	public static Logger log = Logger.getLogger(GarageOverview_Counts_Test.class.getName());
	public static SoftAssertLogger sAssert;
	public static Properties prop;

	// Instantiate Objects
	public static CCSS_Login_Page loginPage;
	public static CCSS_GarageOverview_Page goPage;
	public static GetAPIResponseData apiDat;
	public static GetAPIResponse apiResp;
	
	//@Test(priority = 1, description = "Verify the BEB Bookout Status values are displayed or not")
	public void Test_CCSSVerifyBEBBookOutStatus() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);
		
		// Book Out Status Values Map
		HashMap<String,String> bookOutStatusValuesMap = goPage.getBookOutStatusValues();
		
		// Book Out Status Values Map from UI
		HashMap<String,String> bookOutStatusValuesUIMap = goPage.computeBookOutStatusFromUi();
		
		// Validating Ready/Topped Count
		sAssert.assertEquals(bookOutStatusValuesMap.get("Ready/Topped Up"), bookOutStatusValuesUIMap.get("Ready/Topped Up"),
				"Book Out Status - Ready/Topped");
		
		// Validating Ready Count
		sAssert.assertEquals(bookOutStatusValuesMap.get("Ready"), bookOutStatusValuesUIMap.get("Ready"),
				"Book Out Status - Ready");
		
		// Validating Not Ready Count
		sAssert.assertEquals(bookOutStatusValuesMap.get("Not Ready"), bookOutStatusValuesUIMap.get("Not Ready"),
				"Book Out Status - Not Ready");
		
		// Validating Delay Count
		sAssert.assertEquals(bookOutStatusValuesMap.get("Delay Expected (>5min)"), bookOutStatusValuesUIMap.get("Delay Expected (>5min)"),
				"Book Out Status - Delay Expected (>5min)");
		
		sAssert.assertAll();
		
	}
	
	// @Test(priority = 1, description = "Verify the Charging Stations values are displayed or not")
	public void Test_CCSSVerifyChargingStationValues() throws InterruptedException {

		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);
		
		// Charging Station Depot Values Map
		HashMap<String,String> chargingStationDepotValuesMap = goPage.getChargingStationDepotValues();
		
		// Charging Station Depot Values Map from UI
		HashMap<String,String> chargingStationDepotUiValuesMap = goPage.computeDepotChargingStationListFromUi();
		
		// Validating Depot Charging Count
		sAssert.assertEquals(chargingStationDepotValuesMap.get("DepotCharging"), chargingStationDepotUiValuesMap.get("DepotCharging"),
				"Charging Stations - Depot Charging");
		
		// Validating DepotActive Count
		sAssert.assertEquals(chargingStationDepotValuesMap.get("DepotActive"), chargingStationDepotUiValuesMap.get("DepotActive"),
				"Charging Stations - Depot Active");
		
		// Validating DepotFault Count
		sAssert.assertEquals(chargingStationDepotValuesMap.get("DepotFault"), chargingStationDepotUiValuesMap.get("DepotFault"),
				"Charging Stations - Depot Fault");
		
		// Validating DepotInactive Count
		sAssert.assertEquals(chargingStationDepotValuesMap.get("DepotInactive"), chargingStationDepotUiValuesMap.get("DepotInactive"),
				"Charging Stations - Depot Inactive");
		
		sAssert.assertAll();
	}
	
	//@Test(priority = 3, description = "Verify the Bus Count values are displayed or not")
	public void testCount() throws InterruptedException {
		// Assertions
		sAssert = new SoftAssertLogger();

		// Navigate to Garage Overview page
		loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);
		
		// Bus Count Map from API
		HashMap<String,String> busCountMapFromAPI = apiDat.getBusCountsMap();
		
		// Bus Count Map from API
		HashMap<String,String> busCountMapFromUi = goPage.getBusCountValues();
		
		// Validating Charging Status Count
		sAssert.assertEquals(busCountMapFromUi.get("Charging"), busCountMapFromAPI.get("Charging"),
				"Bus Count - Charging Status");
		
		// Validating Waiting Status Count
		sAssert.assertEquals(busCountMapFromUi.get("Waiting"), busCountMapFromAPI.get("Waiting"),
				"Bus Count - Waiting Status");
				
		// Validating Parked Status Count
		sAssert.assertEquals(busCountMapFromUi.get("Parked"), busCountMapFromAPI.get("Parked"),
				"Bus Count - Parked Status");
		
		
		
		sAssert.assertAll();
	}
	
	@Test(priority = 1, description = "Validating Charger history module ")
		public void Test_CCSSVerifyChargerHistoryValues() throws InterruptedException {
			// Assertions
			sAssert = new SoftAssertLogger();

			// Navigate to Garage Overview page
			loginPage.navigateToSubmenu(loginPage.CCSS_Submenu_GarageOverviewPage);
			
			
			// Validating Charging start time values are in Descending or not.
			sAssert.assertEquals(goPage.getChargingStartTimeList(),
					goPage.reverseSortOfList(goPage.getChargingStartTimeList()),
					"Charge History - Charging Start Time");
			 

			// Validating Termination Reason contains null or not. 
			sAssert.assertFalse(goPage.isTerminationReasonContainsNull(), "Charge History - Termination Reason has null");
			
			sAssert.assertAll();
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
