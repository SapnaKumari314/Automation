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

public class CCSS_Dashboard_Page extends BasePage {
	public String SAMPLE_CELL_INPUT="10";




	@FindBy(xpath = "//a[@data-ui-sref='bus1']")
	public WebElement bus1;

	@FindBy(xpath = "//span[@data-translate='home.mbus1']")
	public WebElement bus2;

	@FindBy(xpath = "//span[@data-translate='home.bus3']")
	public WebElement bus3;


	@FindBy(xpath = "//span[@data-translate='home.bus4']")
	public WebElement bus4;


	@FindBy(xpath = "//input[@id='srch-term']")
	public WebElement bus5;


	@FindBy(xpath = "//span[@id='srch-icon']/i[@class='fa fa-search']")
	public WebElement bus6;

	@FindBy(xpath = "//div[@id='no-bus-found']")
	public WebElement bus7;


	@FindBy(xpath = "//span[@id='bus9_count']")
	public WebElement bus9;



	//==============================
	
//	public ETS_Details_page BusPageSearchByID(String Bus_name, String Bus_Id) {
//		click(CCSS_Home_Pageid, " Search TextBox : bus search ");
//		type(CCSS_Home_Pageid, BusID, "Search TextBox : BusID search for "+BusID);
//		click(CCSS_Home_Pageid, " Search Text Box : Magnifying Glass ");
//		return (ETS_Details_page) openPage(ETS_Details_page.class);
//	}

	//=========================




	@FindBy(xpath="//div[@id='atacApp']//ul[@role='menu']//a[@href='/samples']/li[@role='menuitem']")
	public WebElement menuSampleIcon;
	//*[@id="atacApp"]/div/header/div/button/span[1]/svg/g/path



	public String getAccessToken() {
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;
		String ACCESS_TOKEN = (String) jsExec.executeScript("return JSON.parse(window.localStorage.getItem('okta-token-storage')).accessToken.accessToken;");
		System.out.println("ACCESS_TOKEN : "+ ACCESS_TOKEN);
		return ACCESS_TOKEN;
	}


	public static void scrollIntoView(WebElement webelement) {

		((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", webelement);
	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(bus9);
	}


}
