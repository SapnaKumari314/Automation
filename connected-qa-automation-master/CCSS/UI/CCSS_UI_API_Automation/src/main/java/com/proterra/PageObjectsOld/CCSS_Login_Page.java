package main.java.com.proterra.PageObjectsOld;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.taskdefs.WaitFor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ch.qos.logback.core.joran.action.Action;
import main.java.com.proterra.utilities.DriverManager;

public class CCSS_Login_Page extends BasePage {




	//	UserID
	@FindBy(id="okta-signin-username")
	public WebElement logInUserId;

	//Password
	@FindBy(id="okta-signin-password")
	public WebElement logInPassword;

	//Submit
	@FindBy(id="okta-signin-submit")
	public WebElement loginPageSubmit;


	// LogOut Text
	@FindBy(xpath="//a[text()='Log Out']")
	public WebElement logOut_Text;

	// LogIn Text
	@FindBy(xpath="//a[text()='Log In/Register']")
	public WebElement logIn_Text;

	// LogIn Text
	@FindBy(xpath="//div[@id='countrytable']/table[@class='countrylist']//td[@class='countrylistmidleft']/a[4]")
	public WebElement country_Select_USA;

	// Feedback Advertisement
	@FindBy(xpath="//div[@id='fsrInvite']//button[@title='No thanks']")
	public WebElement feedback_PopupText;

	@SuppressWarnings("unchecked")
	public CCSS_Login_Page open(String url) {

		System.out.println("Page Opened");
		DriverManager.getDriver().navigate().to(url);
		return (CCSS_Login_Page) openPage(CCSS_Login_Page.class);
	}

	@SuppressWarnings("unchecked")
	public CCSS_Login_Page open_firstTime(String url) throws InterruptedException {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		System.out.println("Page Opened");
		DriverManager.getDriver().navigate().to(url);
		Thread.sleep(5000);

		try {
			if(DriverManager.getDriver().findElement(By.linkText("USA")).isDisplayed()) {
				Actions builder = new Actions(driver);
				builder.moveToElement(DriverManager.getDriver().findElement(By.linkText("USA"))).click(DriverManager.getDriver().findElement(By.linkText("USA"))).build().perform();
			}

		}catch (Exception ex) {

		}


		try {
			//			Actions builder = new Actions(driver);
			//			builder.moveToElement(DriverManager.getDriver().findElement(By.linkText("USA"))).click(DriverManager.getDriver().findElement(By.linkText("USA"))).build().perform();
			if(feedback_PopupText.isDisplayed()) 
				executor.executeScript("arguments[0].click();", feedback_PopupText);
			//			feedback_PopupText.click();

		}catch (Exception ex) {

		}
		Thread.sleep(3000);

		return (CCSS_Login_Page) openPage(CCSS_Login_Page.class);
	}

	public CCSS_Dashboard_Page doLoginAsValidUser(String username, String password, String url) throws InterruptedException {
//		clear(logInUserId, username);
		
		type(logInUserId, username, "Username textbox");
		//		clear(logInPassword, password);
		type(logInPassword, password, "Password textbox");
		Thread.sleep(2000);
		click(loginPageSubmit, "Sign in Button");
		Thread.sleep(5000);
		DriverManager.getDriver().navigate().to(url);
		return (CCSS_Dashboard_Page) openPage(CCSS_Dashboard_Page.class);

	}
	public CCSS_Login_Page doLoginAsInValidUser(String username, String password) throws InterruptedException {
		//		clear(userId_Email, username);
		type(logInUserId, username, "Username textbox");
		type(logInPassword, password, "Password textbox");
		Thread.sleep(2000);
		click(loginPageSubmit, "Sign in Button");
		Thread.sleep(5000);
		return (CCSS_Login_Page) openPage(CCSS_Login_Page.class);

	}
	public CCSS_Login_Page doLogOut(String logInUrl) throws InterruptedException {
		if(logOut_Text.isDisplayed()) {
			click(logOut_Text, " LogOut Button ");
			Thread.sleep(5000);
		}
		DriverManager.getDriver().navigate().to(logInUrl);
		return (CCSS_Login_Page) openPage(CCSS_Login_Page.class);

	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(logInUserId);
	}

}
