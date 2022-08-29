package main.java.com.proterra.PageObjects.apex.v2g;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.PageObjects.BasePage;
import main.java.com.proterra.utilities.DriverManager;

@SuppressWarnings("rawtypes")
public class APEX_V2G_Resources_Page extends BasePage{

	private WebDriver driver;

	public APEX_V2G_Resources_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));	
		}

	}


	//Locators

	//Customer dropdown
	@FindBy(xpath = "//select[@id='addResourceTenantId']")
	public WebElement dropdown_customer;

	//Grid Length Dropdown
	@FindBy(xpath = "//select[@name='resourceGrid_length']")
	public WebElement dropdown_grid_length;

	//Add New Resource button
	@FindBy(xpath = "//button[normalize-space()='Add New Resource']")
	public WebElement button_add_new_resource;

	//Resources Search Box
	@FindBy(xpath = "//input[@type='search']")
	public WebElement input_search;

	//Resources Table Columns
	private String column_xpath = "//th[contains(text(),'%s')]";
	public WebElement column_label_resource_table(String columnName) {
		return driver.findElement(By.xpath(String.format(column_xpath, columnName)));
	}

	//Add New Resource Modal Header
	@FindBy(xpath = "//h4[@id='add-new-resource_title']")
	public WebElement header_add_new_resource_modal;

	//Customer Input Box on Add New Resource Modal
	@FindBy(xpath = "//input[@id='resourceTenantId']")
	public WebElement input_add_new_resource_customer;

	//Resource ID Input Box on Add New Resource Modal
	@FindBy(xpath = "//input[@id='addResourceId']")
	public WebElement input_add_new_resource_resource_id;

	//Address Text Input Box on Add New Resource Modal
	@FindBy(xpath = "//input[@id='addressText']")
	public WebElement input_add_new_resource_address_text;

	//Add New Resource Close button
	@FindBy(xpath = "//button[@type='button' and text()='Close']")
	public WebElement button_add_new_resource_close;

	//Add New Resource Done button
	@FindBy(xpath = "//button[@id='submitCreateResource']")
	public WebElement button_add_new_resource_done;



	//Constants

	//Menu Details
	public final String APEX_COLUMN_LABEL_CUSTOMER = "Customer";






	//Page Object Methods





	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(dropdown_customer);
	}

}
