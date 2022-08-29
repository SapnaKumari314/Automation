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

public class CCSS_Details_page extends BasePage {

	public final static String BUS_PAGE_HEADER="//h1[text()='Bus Details']";
	

	// Header Text Mutation Detection
	@FindBy(xpath = BUS_PAGE_HEADER)
	public WebElement busDetailsPageMainHeader;

	



	@FindBy(xpath = "//")
	public WebElement garagePageLink;


	public static void scrollIntoView(WebElement webelement) {

		((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", webelement);
	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(busDetailsPageMainHeader);
	}


}
