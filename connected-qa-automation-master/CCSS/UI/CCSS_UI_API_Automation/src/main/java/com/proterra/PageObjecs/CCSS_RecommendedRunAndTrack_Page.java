package main.java.com.proterra.PageObjecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import org.testng.TestException;

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DateUtils;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.ccss.DataConstants.SetDataConstants;

@SuppressWarnings("rawtypes")
public class CCSS_RecommendedRunAndTrack_Page extends BasePage {

	private WebDriver driver;
	private Actions action;
	private CCSS_Login_Page login = new CCSS_Login_Page();
	public static Logger log = Logger.getLogger(CCSS_RecommendedRunAndTrack_Page.class.getName());
	public DateUtils dateUtil = new DateUtils();

	public CCSS_RecommendedRunAndTrack_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));
		}
	}

	// Locators

	// Overlay element //div[@class='overlay']
	@FindBy(xpath = "//div[@class='overlay']")
	public static WebElement OverlayElement;

	// ============================== Header Bar =========================================

	// Active Garage
	@FindBy(xpath = "//div[contains(@class,'garage-dropdown')]/button")
	public WebElement activeGarage;

	// Switch Garage
	@FindBy(xpath = "//div[contains(@class,'garage-dropdown')]/button/following-sibling::div/button")
	public WebElement switchGarage;

	// Pop up toast message icon
	@FindBy(xpath = "//img[@class = 'toast-icon']")
	public WebElement CCSS_ToastMessageIcon;

	// Pop up toast message
	@FindBy(xpath = "//span[@class = 'toast-message']")
	public WebElement CCSS_ToastMessage;

	// ============================== Incoming Queue =========================================

	// Incoming Queue Bus ID
	@FindBys(@FindBy(xpath = "//div[contains(@id,'mat-tab-label')]/div/span[contains(@class,'bus-id')]"))
	public List<WebElement> listIncomingQueueBusID;

	// Incoming Queue Bus Bookin Time
	@FindBys(@FindBy(xpath = "//div[contains(@id,'mat-tab-label')]/div/span[contains(@class,'bus-time')]"))
	public List<WebElement> listIncomingQueueBusBookinTime;

	// Bus tab on the incoming queue
	private String busTabXpath = "//div[contains(@id,'mat-tab-label')]/div/span[contains(@class,'bus-id') and text()='%s']";

	public WebElement busTab(String busName) {
		return driver.findElement(By.xpath(String.format(busTabXpath, busName)));
	}

	// No buses in the Incoming Queue
	@FindBy(xpath = "//div[@class='empty-incoming-queue ng-star-inserted']")
	public WebElement CCSS_EmptyIncomingQueue;

	// ============================== BEB Search
	// =========================================
	@FindBy(xpath = "//input[@aria-label='Search BEB']")
	public WebElement bebSearchBox;

	// Autocomplete list
	@FindBy(xpath = "//div[@role='listbox' and contains(@id,'mat-autocomplete')]")
	public WebElement CCSS_SearchAutoCompleteListValue;

	//BEB Search Box list values
	@FindBy(xpath = "//div[@role='listbox' and contains(@id,'mat-autocomplete')]/mat-option[contains(@id,'mat-option')]")
	public List<WebElement> CCSS_BEBSearchListValues;

	// ============================== Bus Details
	// =========================================
	// Bus Details Text Header
	@FindBy(xpath = "//app-bus-details/div/span")
	public WebElement CCSS_BusDetailsComponentHeader;

	// Bus ID Label
	@FindBy(xpath = "//div[text()='Bus ID']")
	public WebElement CCSS_BusDetailsBusIDLabel;

	// Bus ID Value
	@FindBy(xpath = "(//div[@class='col-3'])[5]")
	public WebElement CCSS_BusDetailsBusIDVal;

	// Current Energy Label
	@FindBy(xpath = "//div[text()='Current Energy']")
	public WebElement CCSS_BusDetailsCurrentEnergyLabel;

	// Current Energy Val
	@FindBy(xpath = "(//div[@class='col-3'])[6]")
	public WebElement CCSS_BusDetailsCurrentEnergyVal;

	// SOC Label
	@FindBy(xpath = "//div[text()='SOC']")
	public WebElement CCSS_BusDetailsSOCLabel;

	// SOC Val
	@FindBy(xpath = "(//div[@class='col-3'])[7]")
	public WebElement CCSS_BusDetailsSOCVal;

	// Range Label
	@FindBy(xpath = "//div[text()='Range']")
	public WebElement CCSS_BusDetailsRangeLabel;

	// Range Val
	@FindBy(xpath = "(//div[@class='col-3'])[8]")
	public WebElement CCSS_BusDetailsRangeVal;

	// Odometer Label
	@FindBy(xpath = "//div[text()='Odometer']")
	public WebElement CCSS_BusDetailsOdometerLabel;

	// Odometer Val
	@FindBy(xpath = "(//div[@class='col-3'])[13]")
	public WebElement CCSS_BusDetailsOdometerVal;

	// Energy Consumption Label
	@FindBy(xpath = "//div[text()='Energy Consumption']")
	public WebElement CCSS_BusDetailsEnergyConsumptionLabel;

	// Energy Consumption Val
	@FindBy(xpath = "(//div[@class='col-3'])[14]")
	public WebElement CCSS_BusDetailsEnergyConsumptionVal;

	// HDDTCS Label
	@FindBy(xpath = "//div[text()='HDDTC']")
	public WebElement CCSS_BusDetailsHDDTCSLabel;

	// HDDTCS Val
	@FindBy(xpath = "(//div[@class='col-3'])[15]")
	public WebElement CCSS_BusDetailsHDDTCSVal;

	// Needs Maintenance Label
	@FindBy(xpath = "//div[text()='Needs Maintenance']")
	public WebElement CCSS_BusDetailsNeedsMaintenanceLabel;

	// Needs Maintenance Val
	@FindBy(xpath = "//input[@id='mat-checkbox-1-input']/parent::div")
	public WebElement CCSS_BusDetailsNeedsMaintenanceCheckBox;

	// RRT Button
	@FindBy(xpath = "//button[@class='button-yellow recommended-run-button mat-raised-button mat-button-base ng-star-inserted']")
	public WebElement CCSS_RRT_Btn;

	// ============================== Run Details
	// =========================================

	//Run Details Component Header
	@FindBy(xpath = "//app-recommended-runs/div/span")
	public WebElement CCSS_RunDetailsComponentHeader;

	// Run Id Label
	@FindBy(xpath = "//div[text()='Run ID']")
	public WebElement CCSS_RunDetailsRunIdLabel;

	// Run Id Val
	@FindBy(xpath = "//div[text()='Run ID']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsRunIdVal;

	// Required Energy Label
	@FindBy(xpath = "//div[text()='Required Energy']")
	public WebElement CCSS_RunDetailsRequiredEnergyLabel;

	// Required Energy Val
	@FindBy(xpath = "//div[text()='Required Energy']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsRequiredEnergyVal;

	// Estimated Charge Time Label
	@FindBy(xpath = "//div[text()='Estimated Charge Time']")
	public WebElement CCSS_RunDetailsEstimatedChargeTimeLabel;

	// Estimated Charge Time Val
	@FindBy(xpath = "//div[text()='Estimated Charge Time']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsEstimatedChargeTimeVal;

	// Book Out Date, Time Label
	@FindBy(xpath = "//div[text()='Book Out Date, Time']")
	public WebElement CCSS_RunDetailsBookOutDateTimeLabel;

	// Book Out Date, Time Val
	@FindBy(xpath = "//div[text()='Book Out Date, Time']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsBookOutDateTimeVal;

	// Distance Label
	@FindBy(xpath = "//div[text()='Distance']")
	public WebElement CCSS_RunDetailsDistanceLabel;

	// Distance Val
	@FindBy(xpath = "//div[text()='Distance']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsDistanceVal;

	// Run Status Label
	@FindBy(xpath = "//div[text()='Run Status']")
	public WebElement CCSS_RunDetailsRunStatusLabel;

	// Run Status Val
	@FindBy(xpath = "//div[text()='Run Status']/following-sibling::div[@class='data']")
	public WebElement CCSS_RunDetailsRunStatusVal;

	// Override Button
	@FindBy(xpath = "//div[contains(@class,'heading')]/span[text()='Recommended Run']/parent::div/following-sibling::div//span[text()='Override']")
	public WebElement CCSS_RunDetails_OverrideButton;

	// Override Confirm Button
	@FindBy(xpath = "//span[text()='Confirm']")
	public WebElement CCSS_RunDetails_OverrideConfirmButton;

	// Override Comments
	@FindBy(xpath = "//textarea[@id='comments']")
	public WebElement CCSS_RunDetails_OverrideComments;

	// Override Done Button
	@FindBy(xpath = "//span[text()='Done']")
	public WebElement CCSS_RunDetails_OverrideCommentDoneButton;

	// ============================== Run Override screen=========================================

	//Index constants
	public final int overrideRun_columnIndex_runID = 0;
	public final int overrideRun_columnIndex_recommended = 1;
	public final int overrideRun_columnIndex_distance = 2;
	public final int overrideRun_columnIndex_requiredEnergy = 3;
	public final int overrideRun_columnIndex_estimatedChargeTime = 4;
	public final int overrideRun_columnIndex_bookoutDateTime = 5;
	public final int overrideRun_columnIndex_bookinDateTime = 6;

	//Run Override screen
	@FindBy(xpath = "//app-bus-run-list")
	public WebElement CCSS_RunOverride_Screen;

	//Run Id column label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-runNumber')]")
	public WebElement CCSS_RunOverride_RunID_Label;

	//Run ID value list
	@FindBy(xpath = "//table[contains(@class,'runs-table')]/tbody/tr/td[contains(@class,'mat-column-runNumber')]")
	public List<WebElement> CCSS_RunOverride_RunID_Value;

	//Recommended Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-recommended')]")
	public WebElement CCSS_RunOverride_Recommended_Label;

	//Recommended Value
	String recommendedXpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-recommended')]";
	public WebElement CCSS_RunOverride_Recommended_Value(String runID) {
		return driver.findElement(By.xpath(String.format(recommendedXpath, runID)));
	}

	//Distance Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-distance')]")
	public WebElement CCSS_RunOverride_Distance_Label;

	//Distance Value
	String distanceXpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-distance')]";
	public WebElement CCSS_RunOverride_Distance_Value(String runID) {
		return driver.findElement(By.xpath(String.format(distanceXpath, runID)));
	}

	//Required Energy Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-reqEnergy')]")
	public WebElement CCSS_RunOverride_RequiredEnergy_Label;

	//Required Energy Value
	String requiredEnergyxpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-reqEnergy')]";
	public WebElement CCSS_RunOverride_RequiredEnergy_Value(String runID) {
		return driver.findElement(By.xpath(String.format(requiredEnergyxpath, runID)));
	}

	//Estimated Charge Time Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-estChargeTime')]")
	public WebElement CCSS_RunOverride_EstimatedChargeTime_Label;

	//Estimated Charge Time Value
	String estimatedChargeTimexpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-estChargeTime')]";
	public WebElement CCSS_RunOverride_EstimatedChargeTime_Value(String runID) {
		return driver.findElement(By.xpath(String.format(estimatedChargeTimexpath, runID)));
	}

	//Bookout Date Time Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-bookOutTime')]")
	public WebElement CCSS_RunOverride_BookoutDateTime_Label;

	//Bookout Date Time Value
	String bookOutDateTimexpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-bookOutTime')]";
	public WebElement CCSS_RunOverride_BookoutDateTime_Value(String runID) {
		return driver.findElement(By.xpath(String.format(bookOutDateTimexpath, runID)));
	}

	//Bookin Date Time Label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-bookInTime')]")
	public WebElement CCSS_RunOverride_BookinDateTime_Label;

	//Bookin Date Time Value
	String bookInDateTimexpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-bookInTime')]";
	public WebElement CCSS_RunOverride_BookinDateTime_Value(String runID) {
		return driver.findElement(By.xpath(String.format(bookInDateTimexpath, runID)));
	}

	//Selection radio button label
	@FindBy(xpath = "//table[contains(@class,'runs-table')]//th[@role='columnheader' and contains(@class,'mat-column-selection')]")
	public WebElement CCSS_RunOverride_SelectionRadio_Label;

	//Selection radio button
	String selectionxpath = "//tbody/tr/td[contains(@class,'mat-column-runNumber') and text()=' %s']/parent::tr/td[contains(@class,'mat-column-selection')]/mat-radio-button/label/div/div[contains(@class,'mat-radio-outer-circle')]";
	public WebElement CCSS_RunOverride_SelectionRadio_Button(String runID) {
		return driver.findElement(By.xpath(String.format(selectionxpath, runID)));
	}

	//Pagination Last Page
	@FindBy(xpath = "//nav/ul/li[contains(@class,'page-item')][last()]")
	public WebElement CCSS_RunOverride_PaginationLastPage;

	//Pagination First Page
	@FindBy(xpath = "//nav/ul/li[contains(@class,'page-item')][1]")
	public WebElement CCSS_RunOverride_PaginationFirstPage;

	//Previous button
	@FindBy(xpath = "//p[@class='pagination-line']/preceding-sibling::li/a[text()='Previous']")
	public WebElement CCSS_RunOverride_PreviousButton;

	//Next button
	@FindBy(xpath = "//p[@class='pagination-line']/following-sibling::li/a[text()='Next']/parent::li")
	public WebElement CCSS_RunOverride_NextButton;

	// Override Confirm Button
	@FindBy(xpath = "//span[text()='Confirm']")
	public WebElement CCSS_RunOverride_ConfirmButton;

	// Override Cancel Button
	@FindBy(xpath = "//span[text()='Cancel']")
	public WebElement CCSS_RunOverride_CancelButton;

	// Override Comments
	@FindBy(xpath = "//textarea[@id='comments']")
	public WebElement CCSS_RunOverride_Comments;

	// Override Done Button
	@FindBy(xpath = "//span[text()='Done']")
	public WebElement CCSS_RunOverride_CommentDoneButton;


	// ============================== Track Details=========================================

	//Run Override screen
	@FindBy(xpath = "//app-recommended-tracks")
	public WebElement CCSS_TrackOverride_Screen;

	//Track Details Component Header
	@FindBy(xpath = "//app-recommended-tracks/div/span")
	public WebElement CCSS_TrackDetailsComponentHeader;

	// Track No Label
	@FindBy(xpath = "//div[text()='Track No']")
	public WebElement CCSS_TrackDetailsTrackNoLabel;

	// Track No Val
	@FindBy(xpath = "//div[text()='Track No']/following-sibling::div[@class='data']")
	public WebElement CCSS_TrackDetailsTrackNoVal;

	// Position Label
	@FindBy(xpath = "//div[text()='Position']")
	public WebElement CCSS_TrackDetailsPositionLabel;

	// Position Val
	@FindBy(xpath = "//div[text()='Position']/following-sibling::div[@class='data']")
	public WebElement CCSS_TrackDetailsPositionVal;

	// Track Type Label
	@FindBy(xpath = "//div[text()='Track Type']")
	public WebElement CCSS_TrackDetailsTrackTypeLabel;

	// Track Type Val
	@FindBy(xpath = "//div[text()='Track Type']/following-sibling::div[@class='data']")
	public WebElement CCSS_TrackDetailsTrackTypeVal;

	// Override Button
	@FindBy(xpath = "//div[contains(@class,'heading')]/span[text()='Recommended Track']/parent::div/following-sibling::div//span[text()='Override']")
	public WebElement CCSS_TrackDetails_OverrideButton;

	// Override Confirm Button
	@FindBy(xpath = "//span[text()='Confirm']")
	public WebElement CCSS_TrackDetails_OverrideConfirmButton;

	// Override Comments
	@FindBy(xpath = "//textarea[@id='comments']")
	public WebElement CCSS_TrackDetails_OverrideComments;

	// Override Done Button
	@FindBy(xpath = "//span[text()='Done']")
	public WebElement CCSS_TrackDetails_OverrideCommentDoneButton;

	// Assign Run and Track Button
	@FindBy(xpath = "//span[text()='Assign Run & Track']")
	public WebElement CCSS_TrackDetails_AssignButton;


	// ============================== Re-assignment Details=========================================
	// Re-assignment pop up message
	@FindBy(xpath = "//div[contains(@class,'dialogBox')]/h6")
	public WebElement CCSS_Reassignment_Message;


	//Re-assignment confirmation button
	@FindBy(xpath = "//div[contains(@class,'dialogBox')]/button[contains(@class,'button-yellow')]/span")
	public WebElement CCSS_Reassignment_Yes_Button;


	//Re-assignment cancel button
	@FindBy(xpath = "//div[contains(@class,'dialogBox')]/button[contains(@class,'button-cancel')]/span")
	public WebElement CCSS_Reassignment_Cancel_Button;




	// ============================== Garage Layout =========================================

	// Garage Layout Track Position
	private String trackPositionXpath = "//div[@data-row='%s' and @data-col='%s']";

	public WebElement CCSS_Garage_TrackPosition(String track, String position) {

		String trackPosition[] = SetDataConstants.getGarageTrackPositionsMap(true).get(track + "-" + position)
				.split("-");
		String data_row = trackPosition[0];
		String data_col = trackPosition[1];

		return driver.findElement(By.xpath(String.format(trackPositionXpath, data_row, data_col)));
	}

	// ======================================================================================

	//Constants

	//Bus Details Labels
	public final String BUS_ID = "Bus ID";
	public final String CURRENT_ENERGY = "Current Energy";
	public final String SOC = "SOC";
	public final String RANGE = "Range";
	public final String ODOMETER = "Odometer";
	public final String ENERGY_CONSUMPTION = "Energy Consumption";
	public final String HDDTC = "HDDTC";
	public final String NEEDS_MAINTENANCE = "Needs Maintenance";


	//Run Details Labels
	public final String RUN_ID = "Run ID";
	public final String REQUIRED_ENERGY = "Required Energy";
	public final String ESTIMATED_CHARGE_TIME = "Estimated Charge Time";
	public final String BOOKOUT_DATE_TIME = "Book Out Date, Time";
	public final String DISTANCE = "Distance";
	public final String RUN_STATUS = "Run Status";


	//Track Details Labels
	public final String TRACK_NO = "Track No";
	public final String POSITION = "Position";
	public final String TRACK_TYPE = "Track Type";

	//Bus Status
	public final String NOT_READY = "Not Ready";
	public final String READY = "Ready";
	public final String READY_TOPPED_UP = "Ready/Topped Up";


	//Run Date
	public final String TODAYS_RUN = "Today";
	public final String NEXT_DAY_RUN = "Next Day";


	//Re-assignment message
	public final String REASSIGNMENT_MESSAGE = "Are you sure you want to re-assign the bus to this run and track No.?";
	// ======================================================================================

	// Page Object Methods
	//Switch to Garage
	public void switchToGarage(String garageName) {
		try {
			//Check the currently selected garage
			if (!(activeGarage.getText().equalsIgnoreCase(garageName))) {
				click(activeGarage, "Garage Menu");
				if (switchGarage.getText().equalsIgnoreCase(garageName)) {
					click(switchGarage, "Selecting the "+garageName+" Garage");
					waitForElementPresent(login.loadingIcon);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	//Switch to Garage
	public void switchToNonActiveGarage() {
		try {
			click(activeGarage, "Garage Menu");
			click(switchGarage, "Selecting the other Garage");
			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}




	// Search Bus using BEB Search
	public void searchBEB(String busName) {
		try {
			action = new Actions(DriverManager.getDriver());
			clear(bebSearchBox, "BEB Search Box");
			action.click(bebSearchBox).sendKeys(bebSearchBox, busName).build().perform();
			action.moveToElement(CCSS_SearchAutoCompleteListValue).click(CCSS_SearchAutoCompleteListValue).build()
			.perform();
			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		if (CCSS_BusDetailsCurrentEnergyVal.getText().equals(null)
				|| CCSS_BusDetailsCurrentEnergyVal.getText().equals("")) {
			System.err.println("Bus details are not loading");
			ExtentListeners.testReport.get().skip("Bus details are not loading");
			throw new SkipException("Bus details are not loading");
		}

	}

	// Check if Queue is empty
	public void isIQEmpty() {
		boolean present;
		try {
			CCSS_EmptyIncomingQueue.isDisplayed();
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		if (present) {
			System.err.println("There are no buses in the Icoming Queue");
			ExtentListeners.testReport.get().skip("There are no buses in the Icoming Queue");
			throw new SkipException("There are no buses in the Icoming Queue");
		}
	}

	// Click the Bus from incoming Queue
	public void selectIQBus(String busName) throws InterruptedException {
		try {
			busTab(busName).click();
			waitForElementPresent(login.loadingIcon);
			if (CCSS_BusDetailsCurrentEnergyVal.getText().equals(null)
					|| CCSS_BusDetailsCurrentEnergyVal.getText().equals("")) {
				System.err.println("Bus details are not loading");
				ExtentListeners.testReport.get().skip("Bus details are not loading");
				throw new SkipException("Bus details are not loading");
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

	}

	// Get All IQ Bus list
	public List<String> getAllIQBusList() {
		List<String> busNameList = new ArrayList<>();
		try {
			for (WebElement ele : listIncomingQueueBusID) {
				busNameList.add(ele.getText());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return busNameList;
	}

	// Get IQ Bus list
	public List<String> getValidIQBusList(Properties prop) {
		List<String> busVin = new ArrayList<>();
		try {
			for (WebElement ele : listIncomingQueueBusID) {
				if (prop.getProperty(ele.getText()) == null || prop.getProperty(ele.getText()) == "") {
					continue;
				}
				busVin.add(ele.getText());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return busVin;
	}

	// Validate IQ Bus count
	public boolean iqBusCount() {
		boolean result = false;
		int size;
		try {
			size = listIncomingQueueBusID.size();
			if (size <= 11) {
				result = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return result;
	}

	// Get Bus details
	public Map<String, String> uiBusDetails() {

		Map<String, String> busDetails = new HashMap<String, String>();
		try {
			busDetails.put(BUS_ID, CCSS_BusDetailsBusIDVal.getText());
			busDetails.put(CURRENT_ENERGY, CCSS_BusDetailsCurrentEnergyVal.getText());
			busDetails.put(SOC, CCSS_BusDetailsSOCVal.getText());
			busDetails.put(RANGE, CCSS_BusDetailsRangeVal.getText());
			busDetails.put(ODOMETER, CCSS_BusDetailsOdometerVal.getText());
			busDetails.put(ENERGY_CONSUMPTION, CCSS_BusDetailsEnergyConsumptionVal.getText());
			busDetails.put(HDDTC, CCSS_BusDetailsHDDTCSVal.getText());
			busDetails.put(NEEDS_MAINTENANCE, String.valueOf(getBusMaintenanceStatus()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busDetails;
	}

	// Verify the Range calculation
	public int busDetailsRangeCalculation(String energyConsumption, String currentEnergy) {
		int curEnergy;
		float energyConsump;
		int range = 0;
		try {
			curEnergy = Integer.parseInt(currentEnergy.replaceAll("[^0-9]", ""));
			energyConsump = Float.parseFloat(energyConsumption.replaceAll("[^0-9.]", ""));
			range = (int) (curEnergy / energyConsump);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return range;
	}

	// TODO: Get Tooltip Hover details
	public Map<String, String> getTooltipDetails() {
		Map<String, String> tooltipDetails = new HashMap<String, String>();
		try {

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return tooltipDetails;
	}

	// Get Run Details
	public Map<String, String> uiRunDetails() {
		Map<String, String> runDetails = new HashMap<String, String>();
		try {
			runDetails.put(RUN_ID, CCSS_RunDetailsRunIdVal.getText());
			runDetails.put(REQUIRED_ENERGY, CCSS_RunDetailsRequiredEnergyVal.getText());
			runDetails.put(ESTIMATED_CHARGE_TIME, CCSS_RunDetailsEstimatedChargeTimeVal.getText());
			runDetails.put(BOOKOUT_DATE_TIME, CCSS_RunDetailsBookOutDateTimeVal.getText());
			runDetails.put(DISTANCE, CCSS_RunDetailsDistanceVal.getText());
			runDetails.put(RUN_STATUS, CCSS_RunDetailsRunStatusVal.getText());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return runDetails;
	}

	//Validate Required Energy value
	public long calculateRequiredEnergy(int distanceInKm, double energyConsumptionRate, float safetyMargin, float reservedRatio, int batteryCapacity) {
		long busRequiredEnergy = 0;
		try {
			busRequiredEnergy = Math.round((distanceInKm * energyConsumptionRate *(1 + safetyMargin))+ (reservedRatio * batteryCapacity));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return busRequiredEnergy;
	}

	//Validate Estimated Charge Time value
	public long calculateEstimatedChargeTime(int chargerPower, int currentEnergy, int requiredEnergy) {
		long calculatedEstimatedChargeTime = 0;
		try {
			calculatedEstimatedChargeTime = Math.round((double) (1.0 * (requiredEnergy - currentEnergy)/chargerPower)*60);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return calculatedEstimatedChargeTime;
	}

	// Get Track Details
	public Map<String, String> uiTrackDetails() {
		Map<String, String> trackDetails = new HashMap<String, String>();
		try {
			trackDetails.put(TRACK_NO, CCSS_TrackDetailsTrackNoVal.getText());
			trackDetails.put(POSITION, CCSS_TrackDetailsPositionVal.getText());
			trackDetails.put(TRACK_TYPE, CCSS_TrackDetailsTrackTypeVal.getText());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return trackDetails;
	}

	// Get the Maintenance Status of the Bus
	public boolean getBusMaintenanceStatus() {
		String html = CCSS_BusDetailsNeedsMaintenanceCheckBox.getAttribute("innerHTML");
		boolean flag = null != null;
		try {
			if (html.contains("aria-checked=\"false\"")) {
				flag = false;
			} else if (html.contains("aria-checked=\"true\"")) {
				flag = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return flag;
	}

	// Select / Unselect Needs Maintenance
	public void needsMaintenanceSetOrRemove(boolean needsMaintenanceFlag) {
		String html = CCSS_BusDetailsNeedsMaintenanceCheckBox.getAttribute("innerHTML");
		try {
			if (needsMaintenanceFlag == true) {
				if (html.contains("aria-checked=\"false\"")) {
					mouseClick(CCSS_BusDetailsNeedsMaintenanceCheckBox, "Needs Maintenance");
				}
			} else if (needsMaintenanceFlag == false) {
				if (html.contains("aria-checked=\"true\"")) {
					mouseClick(CCSS_BusDetailsNeedsMaintenanceCheckBox, "Needs Maintenance");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Click Recommended Run and Track
	public void clickRecommendedRunAndTrack() {
		try {
			click(CCSS_RRT_Btn, "Recommended Run and Track button");
			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		// Check to see if any toast message appears
		try {
			if (isElementPresent(CCSS_ToastMessage)) {
				String toastMsg = checkToastMessage();
				if (toastMsg.contains("Failure")) {
					System.err.println("Recommend Run and Track Failed. Error \"" + toastMsg + "\"");
					ExtentListeners.testReport.get().error("Recommend Run and Track Failed. Error \"" + toastMsg + "\"");
					throw new TestException("Recommend Run and Track Failed. Error \"" + toastMsg + "\"");
				}
			}
		} catch (NoSuchElementException e) {

		}
	}

	// Get Recommended Track Type from Track Details
	public String getTrackTypeForSelectedTrack(String trackNo) {
		String trackType = null;
		try {
			if (trackNo.equals("36")) {
				trackType = "Charging";
			} else {
				trackType = "Not Charging";
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return trackType;
	}

	// Click Override Run
	public void clickOverrideRun() {
		try {
			click(CCSS_RunDetails_OverrideButton, "Override Run Button");
			waitForElementPresent(login.loadingIcon);

			action.moveToElement(CCSS_RunOverride_PreviousButton);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	//Get All Runs from Override screen
	public List<List<String>> getAllOverrideRunsDetails() {

		List<List<String>> allRunsList = new LinkedList<>();
		List<String> runDetailsList;
		List<WebElement> allRuns = CCSS_RunOverride_RunID_Value;

		String outerHtml;
		String innerHTML;

		try {
			//Get List of all the Runs from override screen
			do {
				//Get the HTML text
				outerHtml = CCSS_RunOverride_NextButton.getAttribute("outerHTML");

				for (WebElement ele : allRuns) {
					runDetailsList = new LinkedList<>();

					String runId = ele.getText();

					innerHTML = CCSS_RunOverride_Recommended_Value(runId).getAttribute("innerHTML");
					String recommended;
					if (innerHTML.contains("Recomended_ic.svg")) {
						recommended = "Recommended";
					}else {
						recommended = "Not Recommended";
					}

					String distance = CCSS_RunOverride_Distance_Value(runId).getText();
					String requiredEnergy = CCSS_RunOverride_RequiredEnergy_Value(runId).getText();
					String estChargeTime = CCSS_RunOverride_EstimatedChargeTime_Value(runId).getText();
					String bookoutTime = CCSS_RunOverride_BookoutDateTime_Value(runId).getText();
					String bookinTime = CCSS_RunOverride_BookinDateTime_Value(runId).getText();

					//Add Run details to the list
					runDetailsList.add(overrideRun_columnIndex_runID,runId);
					runDetailsList.add(overrideRun_columnIndex_recommended,recommended);
					runDetailsList.add(overrideRun_columnIndex_distance,distance);
					runDetailsList.add(overrideRun_columnIndex_requiredEnergy,requiredEnergy);
					runDetailsList.add(overrideRun_columnIndex_estimatedChargeTime,estChargeTime);
					runDetailsList.add(overrideRun_columnIndex_bookoutDateTime,bookoutTime);
					runDetailsList.add(overrideRun_columnIndex_bookinDateTime,bookinTime);

					allRunsList.add(runDetailsList);
					//					System.out.println(runDetailsList);
				}

				//Check if Next Button is enabled and click
				if (!(outerHtml.contains("disabled"))) {
					click(CCSS_RunOverride_NextButton, "Run Override Pagination Next Button");
					Thread.sleep(1000);
				}
			} while (!(outerHtml.contains("disabled")));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return allRunsList;
	}


	//Select a run to ve overriden
	public List<String> selectRunByBusStatus(String RequiredBusStatus, int currentEnergy) {

		List<WebElement> allRuns = CCSS_RunOverride_RunID_Value;
		List<String> runDetailsList = new LinkedList<>();

		String outerHtml;
		String innerHTML;

		boolean selected = false;

		try {
			//Get List of all the Runs from override screen
			do {
				//Get the HTML text
				outerHtml = CCSS_RunOverride_NextButton.getAttribute("outerHTML");

				for (WebElement ele : allRuns) {

					String runId = ele.getText();

					innerHTML = CCSS_RunOverride_Recommended_Value(runId).getAttribute("innerHTML");
					String recommended;
					if (innerHTML.contains("Recomended_ic.svg")) {
						recommended = "Recommended";
					}else {
						recommended = "Not Recommended";
					}
					String distance = CCSS_RunOverride_Distance_Value(runId).getText();
					String requiredEnergy = CCSS_RunOverride_RequiredEnergy_Value(runId).getText();
					String estChargeTime = CCSS_RunOverride_EstimatedChargeTime_Value(runId).getText();
					String bookoutTime = CCSS_RunOverride_BookoutDateTime_Value(runId).getText();
					String bookinTime = CCSS_RunOverride_BookinDateTime_Value(runId).getText();

					//Get the Required Energy
					int reqEnergy = Integer.parseInt(requiredEnergy.replaceAll("[^0-9]", ""));

					if (RequiredBusStatus.equals(READY) || RequiredBusStatus.equals(READY_TOPPED_UP)) {
						if (currentEnergy > reqEnergy) {
							mouseClick(CCSS_RunOverride_SelectionRadio_Button(runId), "Run ID select button");
							selected = true;
						}else {
							continue;
						}
					}else {

						if (currentEnergy < reqEnergy) {
							mouseClick(CCSS_RunOverride_SelectionRadio_Button(runId), "Run ID select button");
							selected = true;
						}else {
							continue;
						}
					}

					//Add Run details to the list
					runDetailsList.add(overrideRun_columnIndex_runID,runId);
					runDetailsList.add(overrideRun_columnIndex_recommended,recommended);
					runDetailsList.add(overrideRun_columnIndex_distance,distance);
					runDetailsList.add(overrideRun_columnIndex_requiredEnergy,requiredEnergy);
					runDetailsList.add(overrideRun_columnIndex_estimatedChargeTime,estChargeTime);
					runDetailsList.add(overrideRun_columnIndex_bookoutDateTime,bookoutTime);
					runDetailsList.add(overrideRun_columnIndex_bookinDateTime,bookinTime);
					break;
				}

				if (selected) {
					break;
				}

				//Check if Next Button is enabled and click
				if (!(outerHtml.contains("disabled"))) {
					click(CCSS_RunOverride_NextButton, "Run Override Pagination Next Button");
					Thread.sleep(1000);
				}
			} while (!(outerHtml.contains("disabled")));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return runDetailsList;
	}


	//Select Recommended Overridden run
	public List<String> selectOverriddenRecommendedRun(){

		List<WebElement> allRuns = CCSS_RunOverride_RunID_Value;
		List<String> runDetailsList = new LinkedList<>();

		String innerHTML;


		try {
			//Get List of all the Runs from override screen

			for (WebElement ele : allRuns) {

				String runId = ele.getText();

				innerHTML = CCSS_RunOverride_Recommended_Value(runId).getAttribute("innerHTML");
				String recommended;
				if (innerHTML.contains("Recomended_ic.svg")) {
					recommended = "Recommended";
				}else {
					continue;
				}

				//Fetch Run Details
				String distance = CCSS_RunOverride_Distance_Value(runId).getText();
				String requiredEnergy = CCSS_RunOverride_RequiredEnergy_Value(runId).getText();
				String estChargeTime = CCSS_RunOverride_EstimatedChargeTime_Value(runId).getText();
				String bookoutTime = CCSS_RunOverride_BookoutDateTime_Value(runId).getText();
				String bookinTime = CCSS_RunOverride_BookinDateTime_Value(runId).getText();

				//Select Run
				mouseClick(CCSS_RunOverride_SelectionRadio_Button(runId), "Run ID select button");

				//Add Run details to the list
				runDetailsList.add(overrideRun_columnIndex_runID,runId);
				runDetailsList.add(overrideRun_columnIndex_recommended,recommended);
				runDetailsList.add(overrideRun_columnIndex_distance,distance);
				runDetailsList.add(overrideRun_columnIndex_requiredEnergy,requiredEnergy);
				runDetailsList.add(overrideRun_columnIndex_estimatedChargeTime,estChargeTime);
				runDetailsList.add(overrideRun_columnIndex_bookoutDateTime,bookoutTime);
				runDetailsList.add(overrideRun_columnIndex_bookinDateTime,bookinTime);
				break;
			}


		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return runDetailsList;


	}


	//Select a Non recommended run to be overriden
	public List<String> selectOverriddenNonRecommendedRun() {

		List<WebElement> allRuns = CCSS_RunOverride_RunID_Value;
		List<String> runDetailsList = new LinkedList<>();

		String outerHtml;
		String innerHTML;

		boolean selected = false;

		try {
			//Get List of all the Runs from override screen
			do {
				//Get the HTML text
				outerHtml = CCSS_RunOverride_NextButton.getAttribute("outerHTML");

				for (WebElement ele : allRuns) {

					String runId = ele.getText();

					innerHTML = CCSS_RunOverride_Recommended_Value(runId).getAttribute("innerHTML");
					String recommended;
					if (innerHTML.contains("Recomended_ic.svg")) {
						continue;
					}else {
						recommended = "Not Recommended";
					}
					String distance = CCSS_RunOverride_Distance_Value(runId).getText();
					String requiredEnergy = CCSS_RunOverride_RequiredEnergy_Value(runId).getText();
					String estChargeTime = CCSS_RunOverride_EstimatedChargeTime_Value(runId).getText();
					String bookoutTime = CCSS_RunOverride_BookoutDateTime_Value(runId).getText();
					String bookinTime = CCSS_RunOverride_BookinDateTime_Value(runId).getText();

					mouseClick(CCSS_RunOverride_SelectionRadio_Button(runId), "Run ID select button");
					selected = true;

					//Add Run details to the list
					runDetailsList.add(overrideRun_columnIndex_runID,runId);
					runDetailsList.add(overrideRun_columnIndex_recommended,recommended);
					runDetailsList.add(overrideRun_columnIndex_distance,distance);
					runDetailsList.add(overrideRun_columnIndex_requiredEnergy,requiredEnergy);
					runDetailsList.add(overrideRun_columnIndex_estimatedChargeTime,estChargeTime);
					runDetailsList.add(overrideRun_columnIndex_bookoutDateTime,bookoutTime);
					runDetailsList.add(overrideRun_columnIndex_bookinDateTime,bookinTime);
					break;
				}

				if (selected) {
					break;
				}

				//Check if Next Button is enabled and click
				if (!(outerHtml.contains("disabled"))) {
					click(CCSS_RunOverride_NextButton, "Run Override Pagination Next Button");
					Thread.sleep(1000);
				}
			} while (!(outerHtml.contains("disabled")));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		return runDetailsList;
	}


	//Click Confirm Run Override
	public void confirmRunOverride() {
		try {
			click(CCSS_RunOverride_ConfirmButton, "Run Override confirm button");

			// Enter override comments
			type(CCSS_TrackDetails_OverrideComments, "test", "Run Override Comments");

			// Click Done
			click(CCSS_RunDetails_OverrideCommentDoneButton, "Run Override Done Button");
			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	//Click Cancel Run Override
	public void cancelRunOverride() {
		try {
			click(CCSS_RunOverride_CancelButton, "Run Override cancel button");

			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	// Click Override Track
	public void clickOverrideTrack() {
		try {
			action = new Actions(DriverManager.getDriver());
			action.moveToElement(CCSS_TrackDetails_OverrideButton).click(CCSS_TrackDetails_OverrideButton).build()
			.perform();
			// click(CCSS_TrackDetails_OverrideButton, "Override Track Button");
			waitForElementPresent(login.loadingIcon);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Select a track to be overriden
	public void selectOverriddenTrack(String newTrack, String newPosition) {
		try {
			action = new Actions(DriverManager.getDriver());
			action.moveToElement(CCSS_Garage_TrackPosition(newTrack, newPosition))
			.click(CCSS_Garage_TrackPosition(newTrack, newPosition)).build().perform();
			// Select the track position
			// click(CCSS_Garage_TrackPosition(newTrack, newPosition), "Garage Track
			// Position");

			click(CCSS_TrackDetails_OverrideConfirmButton, "Track Override confirm button");

			// Enter override comments
			type(CCSS_TrackDetails_OverrideComments, "test", "Track Override Comments");

			// Click Done
			click(CCSS_RunDetails_OverrideCommentDoneButton, "Track Override Done Button");
			waitForElementPresent(login.loadingIcon);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Assign Run and Track
	public String clickAssignRunAndTrack() {
		try {
			click(CCSS_TrackDetails_AssignButton, "Assign Run and Track Button");
			waitForElementPresent(login.loadingIcon);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		// Check to see if any toast message appears
		String toastMsg = checkToastMessage();
		if (toastMsg.contains("Failure")) {
			System.err.println("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
			ExtentListeners.testReport.get().error("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
			throw new TestException("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
		}

		return toastMsg;
	}


	// Re-assign confirmation
	public String reassignAssignRunAndTrack() {
		try {

			click(CCSS_TrackDetails_AssignButton, "Assign Run and Track Button");

			CCSS_Reassignment_Message.isDisplayed();

			click(CCSS_Reassignment_Yes_Button, "Re-assignment Confirm");

			waitForElementPresent(login.loadingIcon);


		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

		// Check to see if any toast message appears
		String toastMsg = checkToastMessage();
		if (toastMsg.contains("Failure")) {
			System.err.println("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
			ExtentListeners.testReport.get().error("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
			throw new TestException("Run and Track Assignment Failed. Error \"" + toastMsg + "\"");
		}

		return toastMsg;
	}


	// Check if toast message appears
	public String checkToastMessage() {
		String toastMessage = "N/A";
		try {
			//			isElementPresent(CCSS_ToastMessage);
			if (CCSS_ToastMessage.isDisplayed()) {
				if (CCSS_ToastMessageIcon.getAttribute("src").contains("red_square.png")) {
					toastMessage = "Failure: " + CCSS_ToastMessage.getText();
				} else {
					toastMessage = "Success: " + CCSS_ToastMessage.getText();
				}
				ExtentListeners.testReport.get().info("Toast Message: " + toastMessage);
			}

			waitForElementNotDisplayed(CCSS_ToastMessage);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return toastMessage;
	}

	// TODO: Drag and Drop Bus
	public void dragAndDropBus(String oldTrackPosition, String newTrackPosition) {
		try {

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	/*---------------------------------------------------------------------------------------------------------------*/

	@SuppressWarnings("rawtypes")
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(activeGarage);
	}

}
