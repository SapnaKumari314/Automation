package main.java.com.proterra.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

public class DriverManager {

	public static ThreadLocal<WebDriver> dr = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {

		return dr.get();

	}

	public static void setWebDriver(WebDriver driver) {

		dr.set(driver);
	}

	
	//Javascript Executer
	public static JavascriptExecutor getJSExecutor() {
		JavascriptExecutor jsDriver = (JavascriptExecutor) getDriver();
		return jsDriver;
	}
	
	//NG Webdriver
	public static NgWebDriver getNgDriver() {
		NgWebDriver ngDriver = new NgWebDriver(getJSExecutor());
		ngDriver.waitForAngularRequestsToFinish();
		return ngDriver;
	}
	
}
