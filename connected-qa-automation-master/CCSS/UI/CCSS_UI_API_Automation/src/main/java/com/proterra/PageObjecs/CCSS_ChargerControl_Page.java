package main.java.com.proterra.PageObjecs;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

import main.java.com.proterra.AssertManager.HardAssertLogger;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.BaseTest;

@SuppressWarnings("rawtypes")
public class CCSS_ChargerControl_Page extends BasePage{

	private WebDriver driver;
	//Initiate Logger
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert;


	public CCSS_ChargerControl_Page() {
		try {
			this.driver = DriverManager.getDriver();

			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			PageFactory.initElements(ajaxElemFactory, this);
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(String.format("This is not the %s page", this.getClass().getSimpleName()));	
		}
	}

	//Locators

	// ============================== Logical Charge Queue =========================================
	
	
	// ============================== Charge History =========================================
	
	
	// ============================== Electric Bus Faults =========================================
	@FindBy(xpath = "//div[contains(text(),'Electric Bus Faults')]/following-sibling::mat-table[@role='grid']/mat-row/mat-cell[contains(@class,'mat-column-busid')]/div")
	public List<WebElement> CCSS_BusFaultsBusId;
	
	
	// ============================== Charger Faults =========================================
	
	
	//Page Object Methods
	
	//Bus list from Bus Faults
	public List<String> getBusFaultBusNames(){
		List<String> busName = new LinkedList<>();
		try {
			for (WebElement ele : CCSS_BusFaultsBusId) {
				busName.add(ele.getText());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return busName;
	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
