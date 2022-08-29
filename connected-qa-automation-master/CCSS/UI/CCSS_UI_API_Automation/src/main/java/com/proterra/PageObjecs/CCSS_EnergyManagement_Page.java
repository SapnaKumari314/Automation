package main.java.com.proterra.PageObjecs;

import java.awt.AWTException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.java.com.proterra.utilities.DriverManager;

public class CCSS_EnergyManagement_Page extends BasePage {

	private WebDriver driver;
	private Actions action;
	private CCSS_Login_Page login = new CCSS_Login_Page();
	public static Logger log = Logger.getLogger(CCSS_EnergyManagement_Page.class.getName());

	public CCSS_EnergyManagement_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));
		}
	}

	// Menu icon
	@FindBy(xpath = "//mat-icon[@svgicon='menu']")
	public static WebElement CCSS_MenuIcon;

	// Energy Management link
	@FindBy(xpath = "//div[text()='Energy Management']")
	public static WebElement CCSS_EnergyManagement;

	// IncomingPowerValue
	@FindBy(xpath = "//span[@class='large-font']")
	public static WebElement CCSS_IncomingPowerValue;

	// PowerCapacity Percent
	@FindBy(xpath = "//mat-card-subtitle[text()='Online Power Capacity']/following-sibling::span/div[text()=' % ']/span")
	public static WebElement CCSS_PowerCapacityPercent;

	// PowerCapacity Value
	@FindBy(xpath = "(//mat-card-subtitle[text()='Online Power Capacity']/following-sibling::span/div/span[@class='numberEditable'])[2]")
	public static WebElement CCSS_PowerCapacityValue;

	// ChargeSessionLimit Value
	@FindBy(xpath = "//mat-card-subtitle[text()='Active Charging Sessions / Limit']/following-sibling::span/span[@class='numberEditable inline']")
	public static WebElement CCSS_ActiveChargerLimit;

	// Charger session
	@FindBy(xpath = "//div/mat-card-subtitle[text()='Active Charging Sessions / Limit']/following-sibling::span")
	public static WebElement CCSS_ActiveChargeSession;
	
	// Cumulative bookout delay Value
	@FindBy(xpath = "//mat-card-subtitle[text()='Cumulative Book Out Delay']/following-sibling::span/span[@class='inline']")
	public static WebElement CCSS_CumulativeBookoutDelay;

	// Page object methods

	
	public String getActiveChargerSession() throws InterruptedException {
		String chargerSessionValue;
		String finalChargerSessionValue = null;
		try {
			chargerSessionValue = CCSS_ActiveChargeSession.getText();
			String sessionValueRemoveSpace = chargerSessionValue.replace(" ", "");
			finalChargerSessionValue = (sessionValueRemoveSpace.split("/"))[0];
			//String outerHtml = CCSS_ActiveChargeSession.getAttribute("outerHTML");
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return finalChargerSessionValue;
	}
	
	// Return incoming power value
	public String getIncomingPowerValue() throws InterruptedException {
		waitForElementPresent(CCSS_IncomingPowerValue);
		String incomingPowerValue = null;
		String finalIncomingPowerValue = null;
		try {
			incomingPowerValue = CCSS_IncomingPowerValue.getText();
			finalIncomingPowerValue= incomingPowerValue.replaceAll("[^0-9]", "");
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return finalIncomingPowerValue;
	}

	// Return power Capacity percentage value
	public String getPowerCapacityPercentage() {
		String powerCapacityPercentageValue = null;
		try {
			action = new Actions(driver);
			action.moveToElement(CCSS_PowerCapacityPercent).click(CCSS_PowerCapacityPercent).build().perform();
			
			powerCapacityPercentageValue = CCSS_PowerCapacityPercent.getText();
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return powerCapacityPercentageValue;
	}

	// Return Power capacity value
	public int getPowerCapacityValue() {
		int powerCapacityValue = 0;
		String powerAfterRemoveComa;
		try {
			action = new Actions(driver);
			action.moveToElement(CCSS_PowerCapacityPercent).click(CCSS_PowerCapacityPercent).build().perform();
			String powerValue = CCSS_PowerCapacityValue.getText();
			if (powerValue.contains(",")) {
				powerAfterRemoveComa = powerValue.replace(",", "");
			} else {
				powerAfterRemoveComa = powerValue;
			}
			powerCapacityValue = Integer.parseInt(powerAfterRemoveComa);
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return powerCapacityValue;

	}

	// Evaluate the Power capacity Percentage vs Value
	public int calculatePowerCapacityPercentage() throws NumberFormatException, InterruptedException {
		int powerCapacityPercentage = 0;
		int powerCapacityValue;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(CCSS_PowerCapacityPercent));
			powerCapacityValue = getPowerCapacityValue();
			powerCapacityPercentage = (powerCapacityValue / Integer.parseInt(getIncomingPowerValue())) * 100;

		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return powerCapacityPercentage;
	}
	

	// Evaluate the Power capacity Percentage vs Value
	public boolean calculatePowerCapacityValue() throws NumberFormatException, InterruptedException {
		double maxPowerCapacityValue;
		double minPowerCapacityValue;
		double powerCapacityPercentage;
		int powerCapacityValue;
		boolean isPowerCapacityMatched = false;
		try {
			powerCapacityPercentage = Integer.parseInt(getPowerCapacityPercentage());
			maxPowerCapacityValue = (powerCapacityPercentage / 100);
			double finalMaxValue =  maxPowerCapacityValue* Integer.parseInt(getIncomingPowerValue());
			minPowerCapacityValue = ((powerCapacityPercentage-1) / 100);
			double finalMinValue =  minPowerCapacityValue* Integer.parseInt(getIncomingPowerValue());
			powerCapacityValue = getPowerCapacityValue();
			if((powerCapacityValue >= finalMinValue) && (powerCapacityValue <= finalMaxValue)) {
				isPowerCapacityMatched = true;
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isPowerCapacityMatched;
	}

	// Return Cumulative delay value
	public String getCumulativeDelayValue() {
		String cumulativeDelayValue = null;
		try {
			cumulativeDelayValue = CCSS_CumulativeBookoutDelay.getText();
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return cumulativeDelayValue;
	}

	// Return the Charger Session limit
	public String getChargerSessionLimit() {
		String chargerSessionLimit = null;
		try {
			action = new Actions(driver);
			action.moveToElement(CCSS_ActiveChargerLimit).click(CCSS_ActiveChargerLimit).build().perform();
			chargerSessionLimit = CCSS_ActiveChargerLimit.getText();
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return chargerSessionLimit;
	}

	// Set Power Capacity percent value
	public boolean setPowerCapacityPercentValue(String value) throws AWTException, InterruptedException {
		boolean isPercentValueSet = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(CCSS_PowerCapacityPercent));
			action = new Actions(driver);
			action.moveToElement(CCSS_PowerCapacityPercent).click(CCSS_PowerCapacityPercent).sendKeys(Keys.BACK_SPACE)
					.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(CCSS_PowerCapacityPercent, value)
					.sendKeys(Keys.TAB).build().perform();
			if (getPowerCapacityPercentage().equals(value)) {
				isPercentValueSet = true;
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isPercentValueSet;
	}
	
	// Set Power Capacity percent value
	public boolean setPowerCapacityValue(String value) throws AWTException, InterruptedException {
		boolean isPercentValueSet = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(CCSS_PowerCapacityValue));
			action = new Actions(driver);
			action.moveToElement(CCSS_PowerCapacityValue).click(CCSS_PowerCapacityValue).sendKeys(Keys.BACK_SPACE)
					.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(CCSS_PowerCapacityValue, value)
					.sendKeys(Keys.TAB).build().perform();
			if (getPowerCapacityPercentage().equals(value)) {
				isPercentValueSet = true;
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isPercentValueSet;
	}

	// Set Charge Monitoring Limit value
	public boolean setChargeMonitorLimitValue(String value) throws AWTException, InterruptedException {
		boolean isChargeLimitValueSet = false;
		try {
			action = new Actions(driver);
			action.moveToElement(CCSS_ActiveChargerLimit).click(CCSS_ActiveChargerLimit).sendKeys(Keys.BACK_SPACE)
			//.sendKeys(CCSS_ChargerSessionLimit, value).sendKeys(Keys.TAB).build().perform();
			.build().perform();
			type(CCSS_ActiveChargerLimit,value,"charger limit");
			if (getChargerSessionLimit().equals(value)) {
				isChargeLimitValueSet = true;
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isChargeLimitValueSet;
	}

	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(CCSS_IncomingPowerValue);
	}

}
