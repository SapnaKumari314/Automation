package main.java.com.proterra.PageObjecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

import main.java.com.proterra.AssertManager.HardAssertLogger;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.DriverManager;
import main.java.com.proterra.utilities.StringUtils;
import test.java.com.proterra.testcases.BaseTest;

@SuppressWarnings("rawtypes")
public class CCSS_GarageOverview_Page extends BasePage {

	private WebDriver driver;
	// Initiate Logger
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert;

	public CCSS_GarageOverview_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));
		}
	}

	// Locators

	// Logical Charge Queue locators

	// Logical Charge Queue Label
	@FindBy(xpath = "//div[text()='Logical Charge Queue']")
	public static WebElement CCSS_LogicalChargeQueue_Label;

	// Logical Charge Queue fields dynamic locator
	private String lcqFieldsXpath = "//mat-table[contains(@class,'charge-queue-table')]//mat-header-cell[contains(@class,'%s')]";
	public WebElement CCSS_LCQ_Fields(String fieldName) {
		return driver.findElement(By.xpath(String.format(lcqFieldsXpath, fieldName)));
	}

	// LCQ Final Variables
	public final String lcqBusId = "busid";
	public final String lcqChargingStationId = "chargeStationId";
	public final String lcqAssignRunId = "assignedRunNumber";
	public final String lcqCurrentEnergy = "curenergy";
	public final String lcqRequiredEnergy = "reqenergy";
	public final String lcqRemChargeTime = "remaningtime";
	public final String lcqPriority = "priority";
	public final String lcqChargerStatus = "status";
	public final String lcqBookOutTime = "bookOutTime";

	// Electric Bus Faults locators

	// Electric Bus Faults Label
	@FindBy(xpath = "//div[text()='Electric Bus Faults']")
	public static WebElement CCSS_ElectricBusFaults;

	// Electric Bus Fault field Dynamic Locator
	private String electricBusFaultXpath = "//div[text()='Electric Bus Faults']/following-sibling::mat-table//mat-header-cell[contains(@class,'%s')]";
	public WebElement CCSS_ElectricBusFault(String fieldName) {
		return driver.findElement(By.xpath(String.format(electricBusFaultXpath, fieldName)));
	}

	// Electric Bus Fault final variables
	public final String electricBusFaultBusId = "busid";
	public final String electricBusFaultedSystem = "faultedsystem";
	public final String electricBusFaultCriticality = "criticallty";

	// Charge History Locators

	// Charge History label
	@FindBy(xpath = "//div[text()='Charge History']")
	public static WebElement CCSS_ChargeHistoryLabel;

	// Charging start time List
	@FindBys(@FindBy(xpath = "//mat-row//mat-cell[contains(@class,'transactionstart')]"))
	public static List<WebElement> CCSS_ChargingStartTimeList;

	// Termination Reason List
	@FindBys(@FindBy(xpath = "//mat-row//mat-cell[contains(@class,'terminationreason')]"))
	public static List<WebElement> CCSS_TerminationReasonList;

	// Charge History fields dynamic locator
	private String chargeHistoryFieldsXpath = "//mat-table[contains(@class,'charge-queue-table')]//mat-header-cell[contains(@class,'%s')]";
	public WebElement CCSS_ChargeHistory_Fields(String fieldName) {
		return driver.findElement(By.xpath(String.format(chargeHistoryFieldsXpath, fieldName)));
	}

	// Charge History Final Variables
	public final String chargeHistoryChargingStationId = "chargepoint";
	public final String chargeHistoryBusId = "vehicle";
	public final String chargeHistoryChargeStartTime = "transactionstart";
	public final String chargeHistoryChargeSessionDuration = "Charging Session Duration";
	public final String chargeHistoryEnergyDelivered = "Energy Delivered (kWh)";
	public final String chargeHistoryTerminateReason = "Termination Reason";

	// Charger Fault Locators

	// Charger faults label
	@FindBy(xpath = "//div[text()='Charger Faults']")
	public static WebElement CCSS_CF_ChargeFaultsLabel;

	// Charger Fault field dynamic locator
	private String chargerFaultXpath = "//div[text()='Charger Faults']/following-sibling::mat-table//mat-header-cell[contains(@class,'%s')]";
	public WebElement CCSS_GO_ChargerFault(String fieldName) {
		return driver.findElement(By.xpath(String.format(chargerFaultXpath, fieldName)));
	}

	// Charger Fault Final Variables
	public final String chargerFaultChargingStationId = "chargerId";
	public final String chargerFaultFaultedSystem = "faultedsystem";
	public final String chargerFaultCriticality = "criticallty";

	// BEB Bookout Status Locators

	// BEB Bookout Status Dynamic locator
	private String bookOutStatusXpath = "//div[text()='%s']/preceding-sibling::div[@class='value']";
	public WebElement CCSS_BEB_BookOutStatus(String busStatus) {
		return driver.findElement(By.xpath(String.format(bookOutStatusXpath, busStatus)));
	}

	// BEB Bookout Status final variables
	public final String bookOutStatusReadyTopped = "Ready/Topped Up";
	public final String bookOutStatusReady = "Ready";
	public final String bookOutStatusNotReady = "Not Ready";
	public final String bookOutStatusDelay = "Delay Expected (>5min)";

	// Bus List in Garage
	@FindBys(@FindBy(xpath = "//img[contains(@class,'busImageInCell')]"))
	public static List<WebElement> CCSS_BusListInGarage;

	// Bus Count Locators

	// Bus Count Dynamic locator
	private String busCountXpath = "//div[text()='%s']/preceding-sibling::div[@class='value']";
	public WebElement CCSS_BEB_BusCount(String busStatus) {
		return driver.findElement(By.xpath(String.format(busCountXpath, busStatus)));
	}

	// Bus Count final variables
	public final String busCountIncomingQueue = "Incoming Queue";
	public final String busCountCharging = "Charging";
	public final String busCountWaiting = "Waiting";
	public final String busCountParked = "Parked";

	// Charging Stations Locators

	// Charging Station Dynamic Locator
	private String chargingStationXpath = "//div[text()='%s']/following-sibling::div[@class='value']";
	public WebElement CCSS_ChargingStation(String chargeStatus) {
		return driver.findElement(By.xpath(String.format(chargingStationXpath, chargeStatus)));
	}

	// Charging Station Final variables
	public final String chargingStationCharging = "Charging";
	public final String chargingStationActive = "Active";
	public final String chargingStationFault = "Fault";
	public final String chargingStationInactive = "Inactive";

	// Bus List in Garage
	@FindBys(@FindBy(xpath = "//img[contains(@class,'charger')]"))
	public static List<WebElement> CCSS_ChargingStationList;
	// ======================================================================================

	// Page Object Methods

	// Getting bookout status values
	public HashMap<String, String> getBookOutStatusValues() {
		HashMap<String, String> bookOutStatusValuesMap = null;
		try {
			bookOutStatusValuesMap = new HashMap<String, String>();
			bookOutStatusValuesMap.put(bookOutStatusReadyTopped,
					CCSS_BEB_BookOutStatus(bookOutStatusReadyTopped).getText());
			bookOutStatusValuesMap.put(bookOutStatusReady, CCSS_BEB_BookOutStatus(bookOutStatusReady).getText());
			bookOutStatusValuesMap.put(bookOutStatusNotReady,
					CCSS_BEB_BookOutStatus(bookOutStatusNotReady).getText());
			bookOutStatusValuesMap.put(bookOutStatusDelay, CCSS_BEB_BookOutStatus(bookOutStatusDelay).getText());
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return bookOutStatusValuesMap;
	}

	// Getting Bus count values
	public HashMap<String, String> getBusCountValues() {
		HashMap<String, String> buscountValuesMap = null;
		try {
			buscountValuesMap = new HashMap<String, String>();
			buscountValuesMap.put(busCountIncomingQueue,
					CCSS_BEB_BusCount(busCountIncomingQueue).getText());
			buscountValuesMap.put(busCountCharging, CCSS_BEB_BusCount(busCountCharging).getText());
			buscountValuesMap.put(busCountWaiting, CCSS_BEB_BusCount(busCountWaiting).getText());
			buscountValuesMap.put(busCountParked, CCSS_BEB_BusCount(busCountParked).getText());
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return buscountValuesMap;
	}

	// Getting charging station Depo values
	public HashMap<String, String> getChargingStationDepotValues() {
		HashMap<String, String> chargingStationDepotValuesMap = null;
		try {
			chargingStationDepotValuesMap = new HashMap<String, String>();
			chargingStationDepotValuesMap.put("DepotCharging",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationCharging).getText(), 0, "\\|"));
			chargingStationDepotValuesMap.put("DepotActive",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationActive).getText(), 0, "\\|"));
			chargingStationDepotValuesMap.put("DepotFault",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationFault).getText(), 0, "\\|"));
			chargingStationDepotValuesMap.put("DepotInactive",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationInactive).getText(), 0, "\\|"));
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargingStationDepotValuesMap;
	}

	// Getting charging station Shop values
	public HashMap<String, String> getChargingStationShopValues() {
		HashMap<String, String> chargingStationShopValuesMap = null;
		try {
			chargingStationShopValuesMap = new HashMap<String, String>();
			chargingStationShopValuesMap.put("ShopCharging",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationCharging).getText(), 1, "\\|"));
			chargingStationShopValuesMap.put("ShopActive",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationActive).getText(), 1, "\\|"));
			chargingStationShopValuesMap.put("ShopFault", 
					StringUtils.splitString(CCSS_ChargingStation(chargingStationFault).getText(), 1, "\\|"));
			chargingStationShopValuesMap.put("ShopInactive",
					StringUtils.splitString(CCSS_ChargingStation(chargingStationInactive).getText(), 1, "\\|"));
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargingStationShopValuesMap;
	}

	//Split Charging Station Depot and Shop values.
	public String splitChargingStationValues(String chargeStationStatus, int index) {
		String[] chargingStationArray = null;
		try {
			String chargeStationValue = CCSS_ChargingStation(chargingStationCharging).getText();
			chargingStationArray = chargeStationValue.split("\\|");
		}
		catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargingStationArray[index].trim();

	}

	//Get Bookout Status values from UI
	public HashMap<String, String> computeBookOutStatusFromUi() {
		HashMap<String, String> bookOutStatusUiMap = null;
		int readyToppedCount = 0;
		int readyCount = 0;
		int notReadyCount = 0;
		int delayExpectedCount = 0;
		try {
			for(WebElement busField : CCSS_BusListInGarage){ 
				String busBookOutStatus = busField.getAttribute("src");
				if(busBookOutStatus.contains("red.svg")) {
					delayExpectedCount++;
				}
				else if(busBookOutStatus.contains("green.svg")) {
					readyToppedCount++;
				}
				else if(busBookOutStatus.contains("blue.svg")) {
					notReadyCount++;
				}
				else {
					readyCount++;
				}
			}
			bookOutStatusUiMap = new HashMap<String,String>();
			bookOutStatusUiMap.put(bookOutStatusReadyTopped, String.valueOf(readyToppedCount));
			bookOutStatusUiMap.put(bookOutStatusReady, String.valueOf(readyCount));
			bookOutStatusUiMap.put(bookOutStatusNotReady, String.valueOf(notReadyCount));
			bookOutStatusUiMap.put(bookOutStatusDelay, String.valueOf(delayExpectedCount));
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return bookOutStatusUiMap;
	}

	//Get Charging Stations values from UI
	public HashMap<String, String> computeDepotChargingStationListFromUi() {
		HashMap<String, String> chargingStationDepotUiMap = null;
		int chargingCount = 0;
		int activeCount = 0;
		int faultCount = 0;
		int inActiveCount = 0;
		try {
			for(WebElement chargerField : CCSS_ChargingStationList){ 
				String chargerStatus = chargerField.getAttribute("src");
				if(chargerStatus.contains("Active.svg")) {
					activeCount++;
				}
				else if(chargerStatus.contains("inactive.svg")) {
					inActiveCount++;
				}
				else if(chargerStatus.contains("Charging.svg")) {
					chargingCount++;
				}
				else {
					faultCount++;
				}
			}
			chargingStationDepotUiMap = new HashMap<String, String>(); 
			chargingStationDepotUiMap.put("DepotCharging", String.valueOf(chargingCount));
			chargingStationDepotUiMap.put("DepotActive", String.valueOf(activeCount));
			chargingStationDepotUiMap.put("DepotFault", String.valueOf(faultCount));
			chargingStationDepotUiMap.put("DepotInactive", String.valueOf(inActiveCount));
		}
		catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargingStationDepotUiMap;
	}

	public List<String> getChargingStartTimeList() throws InterruptedException {
		List<String> startTimeList = new ArrayList<String>();
		try {
			CCSS_ChargeHistoryLabel.click();
			for(WebElement chargeStartTime : CCSS_ChargingStartTimeList){
				startTimeList.add(chargeStartTime.getText().trim());
			}
		}
		catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return startTimeList;
	}

	public boolean isTerminationReasonContainsNull() throws InterruptedException {
		String reason;
		boolean isReasonContainNull = true;
		try {
			CCSS_ChargeHistoryLabel.click();
			for(WebElement terminationReason : CCSS_TerminationReasonList){
				reason = terminationReason.getText();
				if(reason == null || reason.isEmpty()) {
					break;
				}
				else {
					isReasonContainNull = false;
				}
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isReasonContainNull;
	}

	public List<String> reverseSortOfList(List<String> list){
		try {
			Collections.sort(list, Collections.reverseOrder());
		}
		catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return list;
	}


	// ======================================================================================
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
