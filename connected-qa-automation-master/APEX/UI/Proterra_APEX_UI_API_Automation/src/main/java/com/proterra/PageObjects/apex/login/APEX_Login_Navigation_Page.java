package main.java.com.proterra.PageObjects.apex.login;

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
public class APEX_Login_Navigation_Page extends BasePage{

	private WebDriver driver;

	public APEX_Login_Navigation_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));	
		}

	}


	/*****************************************Locators*****************************************/

	//Email Id
	@FindBy(xpath = "//input[@id='congemail']")
	public WebElement input_email_id;

	//Password
	@FindBy(xpath = "//input[@id='congpassword']")
	public WebElement input_password;

	//Login button
	@FindBy(xpath = "//button[@id='loginButton']")
	public WebElement button_signin;

	//Menu
	private String menuXpath = "//ul[@id='leftTabs']//span[text()='%s']";
	public WebElement apex_menu(String menu) {
		return driver.findElement(By.xpath(String.format(menuXpath, menu)));
	}

	//Sub Menu
	private String submenuXpath = "//ul[@id='leftTabs']//span[text()='%s']";
	public WebElement apex_submenu(String submenu) {
		return driver.findElement(By.xpath(String.format(submenuXpath, submenu)));
	}

	//User Profile
	@FindBy(xpath = "//img[@id='userLogo']")
	public WebElement button_user_profile;

	//Logout Button
	@FindBy(xpath = "//a[@role='button' and text()='Logout']")
	public WebElement button_logout;

	/*****************************************Constants*****************************************/

	//Menu Details
	public final String APEX_LABEL_MENU_SUMMARY = "SUMMARY";

	public final String APEX_LABEL_MENU_REAL_TIME_DATA = "REAL-TIME DATA";
	public final String APEX_LABEL_SUBMENU_BUS_MONITORING = "BUS MONITORING";
	public final String APEX_LABEL_SUBMENU_CHARGE_MONITORING = "CHARGE MONITORING";
	public final String APEX_LABEL_SUBMENU_ALERTS = "ALERTS";

	public final String APEX_LABEL_MENU_HISTORICAL_DATA = "HISTORICAL DATA";
	public final String APEX_LABEL_SUBMENU_BUS_DAILY_SNAPSHOT = "BUS DAILY SNAPSHOT";
	public final String APEX_LABEL_SUBMENU_DEPOT_CHARGING = "DEPOT CHARGING";
	public final String APEX_LABEL_SUBMENU_STATIC_REPORTS = "STATIC REPORTS";

	public final String APEX_LABEL_MENU_LEGACY_DASHBOARDS = "LEGACY DASHBOARDS";
	public final String APEX_LABEL_SUBMENU_FLEET_USAGE_SUMMARY = "FLEET USAGE SUMMARY";
	public final String APEX_LABEL_SUBMENU_FLEET_PERFORMANCE = "FLEET PERFORMANCE";
	public final String APEX_LABEL_SUBMENU_DRIVE_AND_CHARGE_REPORTING = "DRIVE & CHARGE REPORTING";
	public final String APEX_LABEL_SUBMENU_BUS_USAGE_SUMMARY = "BUS USAGE SUMMARY";
	public final String APEX_LABEL_SUBMENU_BUS_FAULTS_REPORTING = "BUS FAULTS REPORTING";
	public final String APEX_LABEL_SUBMENU_CHARGER_SUMMARY = "CHARGER SUMMARY";
	public final String APEX_LABEL_SUBMENU_ENERGY_CONSUMPTION_BY_COMPONENT = "ENERGY CONSUMPTION BY COMPONENT";

	public final String APEX_LABEL_MENU_SMART_CHARGING = "SMART CHARGING";
	public final String APEX_LABEL_SUBMENU_CHARGE_PLANS = "CHARGE PLANS";
	public final String APEX_LABEL_SUBMENU_ENERGY_MONITORING = "ENERGY MONITORING";
	public final String APEX_LABEL_SUBMENU_ENERGY_MONITORING_NEW = "ENERGY MONITORING*";
	public final String APEX_LABEL_SUBMENU_VEHICLE_PLANS = "VEHICLE PLANS*";

	public final String APEX_LABEL_MENU_V2G = "V2G";
	public final String APEX_LABEL_SUBMENU_DR_EVENTS = "DR EVENTS";
	public final String APEX_LABEL_SUBMENU_V2G_PROGRAMS = "V2G PROGRAMS";
	public final String APEX_LABEL_SUBMENU_RESOURCES = "RESOURCES";

	public final String APEX_LABEL_MENU_MANAGEMENT = "MANAGEMENT";
	public final String APEX_LABEL_SUBMENU_BUSES = "BUSES";
	public final String APEX_LABEL_SUBMENU_CHARGERS = "CHARGERS";
	public final String APEX_LABEL_SUBMENU_USERS = "USERS";


	/*****************************************Page Object Methods*****************************************/

	//Navigate to APEX Login Page
	public void navigateToPage(String url) throws InterruptedException, NoSuchElementException {
		DriverManager.getDriver().navigate().to(url);
		WaitForElementVisible(input_email_id);
		System.out.println("Login Page Displayed");
	}


	//Navigate to submenu
	@SuppressWarnings("unchecked")
	public void navigateToMenu(String menu, String subMenu) throws InterruptedException {
		try {

			action = new Actions(driver);

			//Select the Menu
			action.moveToElement(apex_menu(menu))
			.click(apex_menu(menu))
			.build()
			.perform();

			Thread.sleep(5000);

			//Select the Sub Menu
			action.moveToElement(apex_submenu(subMenu))
			.click(apex_submenu(subMenu))
			.build()
			.perform();

			Thread.sleep(5000);

		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	//Login as Valid user
	public void loginAsValidUser(String username, String password) throws InterruptedException, NoSuchElementException {
		type(input_email_id, username, "Username textbox");
		Thread.sleep(1000);
		type(input_password, password, "Password textbox");
		Thread.sleep(2000);
		click(button_signin, "Sign in Button");
		Thread.sleep(5000);
		waitForElementPresent(apex_menu(APEX_LABEL_MENU_SUMMARY));

	}


	//Logout
	public void logoutUser() throws InterruptedException {
		click(button_user_profile, "User Profile");
		waitForElementPresent(button_logout);
		click(button_logout, "Logout Button");
		ExtentListeners.testReport.get().info("Logged out of the application");
		WaitForElementVisible(input_email_id);
		System.out.println("Login Page Displayed");
	}




	/*****************************************Page Load Condition*****************************************/
	@Override
	protected ExpectedCondition getPageLoadCondition() {
		return ExpectedConditions.visibilityOf(input_email_id);
	}

}
