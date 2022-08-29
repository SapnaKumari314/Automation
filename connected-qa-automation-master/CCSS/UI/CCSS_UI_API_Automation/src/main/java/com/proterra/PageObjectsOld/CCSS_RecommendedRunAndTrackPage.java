package main.java.com.proterra.PageObjectsOld;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import main.java.com.proterra.utilities.DriverManager;

public class CCSS_RecommendedRunAndTrackPage extends BasePage {
	public String SAMPLE_TIME_INPUT="10";


	//============================== All Bus Types =========================================


	// Run & Track Assignment Header Text
	@FindBy(xpath = "//div[text='Run & Track Assignment']")
	public WebElement CCSS_RunAndTrackAssignmentHeaderText;

	// Current Garage Header Text
	@FindBy(xpath = "//span[@class='active-garage']")
	public WebElement CCSS_GarageHeaderText;


	// Header bar - ccss_systemDate
	@FindBy(xpath="//span[@class='system-date']")
	public WebElement ccssSystemDateText;

	// Header bar - ccss_time
	@FindBy(xpath="//p[@class='time']")
	public WebElement ccssTimeText;

	// Header bar - ccss_login_page
	@FindBy(xpath="//div[.=' Login ']")
	public WebElement ccssLoginText;

	// Bus Details Text
	@FindBy(xpath = "//span[.='Bus Details']")
	public WebElement CCSS_BusDetailsText;

	// BusName Text
	@FindBy(xpath = "//div[text()='Bus Name']")
	public WebElement CCSS_BusDetailsBusName;
	
	// Current Energy Text
	@FindBy(xpath = "//div[text()='Current Energy']")
	public WebElement CCSS_BusDetailsCurrentEnergy;





	//==============================






//	public CCSS_RecommendedRunAndTrackPage getXlinks() {
//		//click(homePageCartLink, " Textbox : CCSS_HomePage CartLink ");
//		//type(CCSS_HomePage_FindAnBusById, Bus_ID, "Search TextBox : BusID search for "+Bus_ID);
//		return (CCSS_RecommendedRunAndTrackPage) openPage(CCSS_RecommendedRunAndTrackPage.class);
//	}



	//=========================




	

	public static void scrollIntoView(WebElement webelement) {

		((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", webelement);
	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(CCSS_RunAndTrackAssignmentHeaderText);
	}


}
