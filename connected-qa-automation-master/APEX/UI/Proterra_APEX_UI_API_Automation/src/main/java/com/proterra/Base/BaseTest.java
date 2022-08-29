package main.java.com.proterra.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DriverFactory;
import main.java.com.proterra.utilities.DriverManager;

public class BaseTest {

	public Logger log = Logger.getLogger(BaseTest.class);
	WebDriver driver = DriverManager.getDriver();

	public void logInfo(String message) {
		try {

			main.java.com.proterra.ExtentListeners.ExtentListeners.testReport.get().info(message);
		} catch (Exception e){}
	}
	public void logFailedInfo(String message) {
		try {

			main.java.com.proterra.ExtentListeners.ExtentListeners.testReport.get().fail(message);
		} catch (Exception e){}
	}

	public void configureLogging() {
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);
	}
	//
	//	
}
