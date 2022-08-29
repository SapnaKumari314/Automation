package main.java.com.proterra.PageObjects.apex.management.chargers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import main.java.com.proterra.PageObjects.BasePage;
import main.java.com.proterra.utilities.DriverManager;

@SuppressWarnings("rawtypes")
public class Charger_Page extends BasePage{

	private WebDriver driver;

	public Charger_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));	
		}

	}


	/*****************************************Locators*****************************************/

	//Results Page

	//Search Box
	@FindBy(xpath = "//input[@type='search']")
	private WebElement input_search;

	//Add New Charger
	@FindBy(xpath = "//button[@id='createNewChargerModal']")
	private WebElement button_add_new_charger;

	//Move Charger
	@FindBy(xpath = "//button[normalize-space()='Move Charger']")
	private WebElement button_move_charger;

	/*****************************************/


	//Add New / Edit Charger Modal

	//Add New / Edit Charger Title
	@FindBy(xpath = "//h4[@id='add-new-charger_title']")
	private WebElement heading_add_new_edit_charger_modal;

	//Select Customer Name
	@FindBy(xpath = "//select[@id='addChargerCustomers']")
	private WebElement select_customer_name_add_charger_modal;

	//Select Charger Vendor
	@FindBy(xpath = "//select[@id='addChargerVendor']")
	private WebElement select_charger_vendor_add_charger_modal;

	//Select Charger Model
	@FindBy(xpath = "//select[@id='addChargerModel']")
	private WebElement select_charger_model_add_charger_modal;

	//Select Charger Interface
	@FindBy(xpath = "//select[@id='addChargerType']")
	private WebElement select_charger_interface_add_charger_modal;

	//Charger Name
	@FindBy(xpath = "//input[@id='addChargerName']")
	private WebElement input_charger_name_add_charger_modal;

	//Charger Serial Number
	@FindBy(xpath = "//input[@id='chargerSerialNumber']")
	private WebElement input_charger_serial_number_add_charger_modal;

	//Cradlepoint Serial Number
	@FindBy(xpath = "//input[@id='cradlePointSerialNumber']")
	private WebElement input_cradlepoint_serial_number_add_charger_modal;

	//SIM ICCID
	@FindBy(xpath = "//input[@id='cellularICCID']")
	private WebElement input_sim_iccid_add_charger_modal;

	//Cradlepoint IP Address
	@FindBy(xpath = "//input[@id='cellularIP']")
	private WebElement input_cradlepoint_ip_address_add_charger_modal;

	//Vendor ID
	@FindBy(xpath = "//input[@id='vendorId']")
	private WebElement input_vendor_id_add_charger_modal;

	//Provision Date
	@FindBy(xpath = "//input[@id='datepicker-provisionDate']")
	private WebElement input_provision_date_add_charger_modal;	

	//Customer Accpetance Date
	@FindBy(xpath = "//input[@id='datepicker-customerAcceptanceDate']")
	private WebElement input_customer_acceptance_date_add_charger_modal;	

	//Garage
	@FindBy(xpath = "//select[@id='addGarageId']")
	private WebElement select_garage_add_charger_modal;	

	//TODO: Charger Data Sheet
	//	@FindBy(xpath = "//select[@id='addGarageId']")
	//	private WebElement select_garage_add_charger_modal;	

	//Specific Charger Location
	@FindBy(xpath = "//input[@id='specificChargerLocation']")
	private WebElement input_specific_charger_location_add_charger_modal;	

	//Charger Address
	@FindBy(xpath = "//input[@id='newChargerAddress']")
	private WebElement input_charger_address_add_charger_modal;	

	//Charger Time Zone
	@FindBy(xpath = "//select[@id='timezone']")
	private WebElement select_charger_time_zone_add_charger_modal;	

	//OCPP ID
	@FindBy(xpath = "//input[@id='ocppId']")
	private WebElement input_ocpp_id_add_charger_modal;	

	//OCPP Websocket URL
	@FindBy(xpath = "//input[@id='ocppUrl']")
	private WebElement input_ocpp_websocket_url_add_charger_modal;	

	//Close Button
	@FindBy(xpath = "//div[@id='add-new-charger']//button[@type='button'][normalize-space()='Close']")
	private WebElement button_close_add_charger_modal;	

	//Done Button
	@FindBy(xpath = "//button[@id='addCharger']")
	private WebElement button_done_add_charger_modal;

	//Delete Button
	@FindBy(xpath = "//button[normalize-space()='Delete']")
	private WebElement button_delete_edit_charger_modal;

	/*****************************************/

	//Move Charger Screen

	//Move From Customer dropdown
	@FindBy(xpath = "//select[@id='moveAssetFromCustomers']")
	private WebElement select_move_from_customer_move_charger_modal;

	//Asset Name
	@FindBy(xpath = "//select[@id='moveAssetAsset']")
	private WebElement select_asset_name_move_charger_modal;

	//Move To Customer dropdown
	@FindBy(xpath = "//select[@id='moveAssetToCustomers']")
	private WebElement select_move_to_customer_move_charger_modal;

	/*****************************************/


	/*****************************************Label Constants*****************************************/

	//Table Result Columns
	public final String CHARGER_RESULTS_COLUMN_CUSTOMER = "Customer";
	public final String CHARGER_RESULTS_COLUMN_CHARGER_NAME = "Charger Name";
	public final String CHARGER_RESULTS_COLUMN_CHARGER_MODEL = "Charger Model";
	public final String CHARGER_RESULTS_COLUMN_STATUS = "Status";
	public final String CHARGER_RESULTS_COLUMN_CUSTOMER_TYPE = "Customer Type";
	public final String CHARGER_RESULTS_COLUMN_OCPP_ID = "OCPP ID";
	public final String CHARGER_RESULTS_COLUMN_SERIAL = "Serial #";


	/*****************************************Page Object Methods*****************************************/


	/*****************************************Page Load Condition*****************************************/
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(input_search);
	}
}
