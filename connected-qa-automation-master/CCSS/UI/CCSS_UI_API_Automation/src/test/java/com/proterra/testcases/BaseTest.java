package test.java.com.proterra.testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.testng.listener.ExtentIReporterSuiteClassListenerAdapter;

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DriverFactory;
import main.java.com.proterra.utilities.DriverManager;

import io.restassured.RestAssured;

//@Listeners({ExtentIReporterSuiteClassListenerAdapter.class})
public class BaseTest {

	private WebDriver driver;
	private Properties Config = new Properties();
	private FileInputStream fis;
	public static Logger log = Logger.getLogger(BaseTest.class.getName());
	public boolean grid=false;
	private String defaultUserName;
	private String defaultProdUserName;
	private String defaultPassword;

		public static String ENV_NAME="-qa"; //"" Prod
		public static boolean HEADLESS_EXECUTION=false;
		public static String BROWSER_TYPE="chrome";

//	public String ENV_NAME=System.getProperty("env.Name").equalsIgnoreCase("qa")?"-"+System.getProperty("env.Name") : ""; //"" Prod
//	public Boolean HEADLESS_EXECUTION=Boolean.valueOf(System.getProperty("browser.headless"));
//	public String BROWSER_TYPE= System.getProperty("browser.type");

	public static String BUS_NUMBER_TEST = "N6014";

	public String getDefaultUserName() {
		return defaultUserName;
	}

	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}

	public String getDefaultProdUserName() {
		return defaultProdUserName;
	}

	public void setDefaultProdUserName(String defaultProdUserName) {
		this.defaultProdUserName = defaultProdUserName;
	}



	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}


	@BeforeSuite
	public void setUpFramework() {
		configureLogging();
		DriverFactory.setGridPath("http://localhost:4444/wd/hub");
		DriverFactory.setConfigPropertyFilePath(
				System.getProperty("user.dir") + "//src//test//resources//properties//Config.properties");
		String os_name=System.getProperty("os.name");

		if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {

			DriverFactory.setChromeDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//mac//chromedriver");
			DriverFactory.setGeckoDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//mac//geckodriver");

		}
		else if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {

			DriverFactory.setChromeDriverExePath("//usr//bin//chromedriver");
			//			DriverFactory.setChromeDriverExePath("//usr//local//bin//chromedriver");

			DriverFactory.setGeckoDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//mac//geckodriver");
		}else {


			DriverFactory.setChromeDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//windows//chromedriver.exe");
			DriverFactory.setGeckoDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//windows//geckodriver.exe");
			DriverFactory.setIeDriverExePath(
					System.getProperty("user.dir") + "//src//test//resources//executables//windows//IEDriverServer.exe");

		}
		/*
		 * Initialize properties Initialize logs load executables
		 * 
		 */
		try {
			fis = new FileInputStream(DriverFactory.getConfigPropertyFilePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Config.load(fis);
			log.info("Config properties file loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}




	public void logInfo(String message) {

		ExtentListeners.testReport.get().info(message);
	}

	public void configureLogging() {
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);
	}

	public void destroyFramework() {

	}

	public void openBrowser(String browser, Boolean HeadlessExecution) {

		if(System.getenv("ExecutionType")!=null && System.getenv("ExecutionType").equals("Grid")) {

			grid=true;
		}


		DriverFactory.setRemote(grid);

		if (DriverFactory.isRemote()) {
			DesiredCapabilities cap = null;

			if (browser.equals("firefox")) {

				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setPlatform(Platform.ANY);

			} else if (browser.equals("chrome")) {

				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				cap.setPlatform(Platform.ANY);
			} else if (browser.equals("ie")) {

				cap = DesiredCapabilities.internetExplorer();
				cap.setBrowserName("iexplore");
				cap.setPlatform(Platform.WIN10);
			}

			try {
				driver = new RemoteWebDriver(new URL(DriverFactory.getGridPath()), cap);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else

			if (browser.equals("chrome")) {
				ChromeOptions chromeOptions = new ChromeOptions();

				if(HeadlessExecution == true) {
					chromeOptions.addArguments("--headless");
				}

				System.out.println("Launching : " + browser);
				System.setProperty("webdriver.chrome.driver",
						DriverFactory.getChromeDriverExePath());
				chromeOptions.addArguments("--no-sandbox");
				//				chromeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--no-sandbox");
				chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--no-sandbox", "--whitelisted-ips=''");
				driver = new ChromeDriver(chromeOptions);
			} else if (browser.equals("firefox")) {
				System.out.println("Launching : " + browser);
				System.setProperty("webdriver.gecko.driver",
						DriverFactory.getGeckoDriverExePath());
				driver = new FirefoxDriver();

			}

		DriverManager.setWebDriver(driver);
		log.info("Driver Initialized !!!");
		//		DriverManager.getDriver().get(Config.getProperty("defaultUserName"));
		try{
			DriverManager.getDriver().manage().window().maximize();
		} catch(Exception ex) {}
		DriverManager.getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//		DriverManager.getDriver().get(Config.getProperty("testsiteurl"));;
		//		setDefaultUserName(Config.getProperty("defaultUserName"));
		//		setDefaultPassword(Config.getProperty("defaultPassword"));
	}

	public void quit() {
		if(DriverManager.getDriver() !=null)
			DriverManager.getDriver().quit();
		log.info("Test Execution Completed !!!");
	}

	public void extentReportApiData(String requestType, String apiData) {
		try {
			if((apiData.equalsIgnoreCase("") || apiData.equalsIgnoreCase(null)))
				ExtentListeners.testReport.get().info(requestType+" : " +"is Null, as It Failed");
			else
				ExtentListeners.testReport.get().info(requestType+" : "+apiData.toString());
		} catch(Exception ex) {}

	}

	public static void extentReportUIData(String status, String testCasename) {
		try {
			ExtentListeners.testReport.get().info(status+" : "+testCasename.toString());
		} catch(Exception ex) {}

	}

	public String getCurrentDateTimeString() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("Before : " + now);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formatDateTime = now.format(formatter);

		return formatDateTime;
	}





}
