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

public class CCSS_LoginPage extends BasePage {




	//	UserID
	@FindBy(id="username")
	public WebElement logInUserId;

	//Password
	@FindBy(id="password")
	public WebElement logInPassword;

	//Submit
	@FindBy(xpath="//button[@type='submit']")
	public WebElement loginPageSubmit;

	// Header bar - ccss_systemDate
	@FindBy(xpath="//span[@class='system-date']")
	public WebElement ccssSystemDateText;

	// Header bar - ccss_time
	@FindBy(xpath="//p[@class='system-date time']")
	public WebElement ccssTimeText;

	// Header bar - ccss_login_page
	@FindBy(xpath="//div[text()='Login']")
	public WebElement ccssLoginText;

	//SYSTEM DATE : //span[@class='system-date']  09 Mar
	// TIME  //p[@class='time']

	// Logout

	//button[@class='navbar-left mat-icon-button mat-button-base mat-menu-trigger']
	//div[@class='logout']

	// LogOut Text
	@FindBy(xpath="//div[text()='Logout']")
	public WebElement logOut_Text;

	

	@SuppressWarnings("unchecked")
	public CCSS_LoginPage open(String url) {

		System.out.println("Page Opened");
		DriverManager.getDriver().navigate().to(url);
		return (CCSS_LoginPage) openPage(CCSS_LoginPage.class);
	}

	@SuppressWarnings("unchecked")
	public CCSS_LoginPage open_firstTime(String url) throws InterruptedException {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		System.out.println("Page Opened");
		DriverManager.getDriver().navigate().to(url);
		Thread.sleep(5000);
		return (CCSS_LoginPage) openPage(CCSS_LoginPage.class);
	}

	public CCSS_RecommendedRunAndTrackPage doLoginAsValidUser(String username, String password) throws InterruptedException {
		//		clear(logInUserId, username);
		type(logInUserId, username, "Username textbox");
		//		clear(logInPassword, password);
		type(logInPassword, password, "Password textbox");
		Thread.sleep(2000);
		click(loginPageSubmit, "Sign in Button");
		Thread.sleep(5000);
//		DriverManager.getDriver().navigate().to(url);
		return (CCSS_RecommendedRunAndTrackPage) openPage(CCSS_RecommendedRunAndTrackPage.class);

	}
	public CCSS_LoginPage doLoginAsInValidUser(String username, String password) throws InterruptedException {
		//		clear(userId_Email, username);
		type(logInUserId, username, "Username textbox");
		type(logInPassword, password, "Password textbox");
		Thread.sleep(2000);
		click(loginPageSubmit, "Sign in Button");
		Thread.sleep(5000);
		return (CCSS_LoginPage) openPage(CCSS_LoginPage.class);

	}
	public CCSS_LoginPage doLogOut(String logInUrl) throws InterruptedException {
		if(logOut_Text.isDisplayed()) {
			click(logOut_Text, " LogOut Button ");
			Thread.sleep(5000);
		}
		DriverManager.getDriver().navigate().to(logInUrl);
		return (CCSS_LoginPage) openPage(CCSS_LoginPage.class);

	}


	@Override
	protected ExpectedCondition getPageLoadCondition() {
		// TODO Auto-generated method stub
		return ExpectedConditions.visibilityOf(logInUserId);
	}

}
