package main.java.com.proterra.PageObjecs;

import java.util.List;

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

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.ccss.DataConstants.SetDataConstants;

@SuppressWarnings("rawtypes")
public class CCSS_Login_Page extends BasePage{

	private WebDriver driver;

	public CCSS_Login_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));	
		}

	}


	//Locators

	//Page header
	@FindBy(xpath = "//div[contains(@class,'header-text')]")
	public WebElement pageHeader;

	//Timestamp
	@FindBy(xpath = "//p[@class='system-date time']")
	public WebElement systemTime;


	//Date
	@FindBy(className = "system-date")
	public WebElement systemDate;


	//Login page username
	@FindBy(xpath = "//input[@id='username']")
	public WebElement loginUserId;


	//Login page password
	@FindBy(xpath = "//input[@id='password']")
	public WebElement loginPassword;


	//Login page submit
	@FindBy(xpath = "//button[text()=' Submit ']")
	public WebElement loginPageSubmit;


	//Invalid User Error
	@FindBy(xpath = "//div[contains(@class,'alert alert-dismissible alert-danger ng-star-inserted')]")
	public WebElement loginInvalidUserError;


	//Empty Username Error
	//	@FindBy(xpath =  "//input[@id='password']/parent::div/parent::div/parent::div/parent::mat-form-field/following-sibling::div[contains(@class,'ng-star-inserted')]/*[@class='error ng-star-inserted']")
	@FindBy(xpath = "//div[contains(@class,'ng-star-inserted')]/*[@class='error ng-star-inserted']")
	public WebElement emptyUserError;


	//Empty Password Error
	//	@FindBy(xpath =  "//input[@id='username']/parent::div/parent::div/parent::div/parent::mat-form-field/following-sibling::div[contains(@class,'ng-star-inserted')]/*[@class='error ng-star-inserted']")
	@FindBy(xpath = "//div[contains(@class,'ng-star-inserted')]/*[@class='error ng-star-inserted']")
	public WebElement emptyPasswordError;


	//Close the error
	@FindBy(xpath = "//div[contains(@class,'alert alert-dismissible alert-danger ng-star-inserted')]/button[@class='close']")
	public WebElement closeError;


	//Loading spinner icon
	@FindBy(xpath = "//img[@class='loading-gif']/ancestor::app-loader[@hidden]")
	public WebElement loadingIcon;


	//Loading spinner icon
	@FindBy(xpath = "//img[@class='loading-gif']/ancestor::app-loader")
	public WebElement loadingIconNotPresent;


	//Logout button
	@FindBy(xpath = "//span[text()='Logout']")
	public WebElement logoutButton;

	//Run and Track screen header
	@FindBy(xpath = "//div[contains(@class,'header-text') and text()='Run & Track Assignment']")
	public WebElement runAndTrackHeader;

	//Menu
	@FindBy(xpath = "//mat-icon[@svgicon='menu']")
	public WebElement CCSS_Menu;

	//Submenu
	private String subMenuXpath = "//div[@class='submenu-item' and text()='%s']";
	public WebElement CCSS_SubMenu(String subMenu) {

		return driver.findElement(By.xpath(String.format(subMenuXpath, subMenu)));
	}
	
	@FindBys(@FindBy(xpath = "//div[contains(@class,'mat-menu-content-item')]/a/div[contains(@class,'submenu-item')]"))
	public List<WebElement> CCSS_SubMenuList;


	//Final Variables
	public final String CCSS_Submenu_RunAndTrackPage = "Run & Track Assignment";
	public final String CCSS_Submenu_ChargerControlPage = "Charger Control";
	public final String CCSS_Submenu_GarageOverviewPage = "Garage Overview";
	public final String CCSS_Submenu_EnergyManagementPage = "Energy Management";
	public final String CCSS_Submenu_ConfigurationPage = "CCSS Configuration";

	//Page Object Methods

	//Navigate to CCSS Login Page
	public void navigateToPage(String url) throws InterruptedException, NoSuchElementException {
		DriverManager.getDriver().navigate().to(url);
		waitForElementPresent(loadingIcon);
		WaitForElementTextVisible(pageHeader, "Login");
		System.out.println("Page Opened");
	}

	//Navigate to submenu
	public void navigateToSubmenu(String subMenu) throws InterruptedException {
		try {
			action = new Actions(driver);
			action.moveToElement(CCSS_Menu)
			.click(CCSS_Menu)
			.build()
			.perform();

			waitForElementPresent(CCSS_SubMenu(subMenu));

			action.moveToElement(CCSS_SubMenu(subMenu))
			.click(CCSS_SubMenu(subMenu))
			.build()
			.perform();

			WaitForElementTextVisible(pageHeader, subMenu);
			waitForElementPresent(loadingIcon);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	//Login as Valid user
	public void loginAsValidUser(String username, String password) throws InterruptedException, NoSuchElementException {
		type(loginUserId, username, "Username textbox");
		Thread.sleep(1000);
		type(loginPassword, password, "Password textbox");
		Thread.sleep(3000);
		click(loginPageSubmit, "Sign in Button");
		waitForElementPresent(runAndTrackHeader);
		waitForElementPresent(loadingIcon);
	}


	//Login as InValid user
	public void loginAsInValidUser(String username, String password) throws InterruptedException, NoSuchElementException {
		loginUserId.clear();
		loginPassword.clear();

		type(loginUserId, username, "Username textbox");
		Thread.sleep(1000);
		type(loginPassword, password, "Password textbox");
		Thread.sleep(3000);
		click(loginPageSubmit, "Sign in Button");
		Thread.sleep(3000);
		WaitForElementVisible(loginInvalidUserError);
	}


	//Logout
	public void logoutUser() throws InterruptedException {
		click(logoutButton, "Logout Button");
		ExtentListeners.testReport.get().info("Logged out of the application");
	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(loginUserId);
	}



}
