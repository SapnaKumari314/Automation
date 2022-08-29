package test.java.com.proterra.testcases.ccss;

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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.Status;

import io.github.bonigarcia.wdm.WebDriverManager;
import main.java.com.proterra.AssertManager.HardAssertLogger;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.PageObjecs.CCSS_ChargerControl_Page;
import main.java.com.proterra.PageObjecs.CCSS_EnergyManagement_Page;
import main.java.com.proterra.PageObjecs.CCSS_GarageOverview_Page;
import main.java.com.proterra.PageObjecs.CCSS_Login_Page;
import main.java.com.proterra.PageObjecs.CCSS_RecommendedRunAndTrack_Page;
import main.java.com.proterra.utilities.DateUtils;
import main.java.com.proterra.utilities.DriverFactory;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponse;
import test.java.com.proterra.testcases.ccss.APICalls.GetAPIResponseData;
import test.java.com.proterra.testcases.ccss.DataConstants.SetDataConstants;
import test.java.com.proterra.testcases.ccss.dataSetup.ClearGarage;


public class BaseTestClass {

	//Object instantiation
	private WebDriver driver;
	private Properties Config = new Properties();
	private FileInputStream fis;
	public static Logger log = Logger.getLogger(BaseTestClass.class.getName());


	//Page Objects instantiation
	public static CCSS_Login_Page loginPage;
	public static CCSS_RecommendedRunAndTrack_Page rrtPage;
	public static CCSS_ChargerControl_Page chargerControlPage;
	public static CCSS_GarageOverview_Page garageOverviewPage;
	public static CCSS_EnergyManagement_Page energyManagementPage;
	public static SetDataConstants datConst;
	public static GetAPIResponse apiResp;
	public static GetAPIResponseData apiDat;
	public static ClearGarage clearGarage;
	public Actions action;


	//Utilities
	public DateUtils dateUtils = new DateUtils();


	//Loggers
	public static SoftAssertLogger sAssert;
	public static HardAssertLogger hAssert; 


	//Variable declaration
	public boolean grid=false;
	private String defaultUserName;
	private String defaultProdUserName;
	private String defaultPassword;
	private Map<String, String> testCaseStatusMap = new LinkedHashMap<String, String>();


	//Constant Variables
	public static String ENV_NAME="-qa"; //"" Prod
	public static boolean HEADLESS_EXECUTION=false;
	public static String BROWSER_TYPE="chrome";
	public static boolean BROWSER_LOGGING_ENABLED = false;

	public String USER_NAME="";
	public String PASSWORD="";
	public String CCSS_APPLICATION_LOGIN_URL="http://exp-ccss-ecms"+ENV_NAME+".connected.proterra.com.s3-website-us-east-1.amazonaws.com/";
	public String GARAGE_NAME="Centennial";


	//Get environment variables
	//	public String ENV_NAME=System.getProperty("env.Name").equalsIgnoreCase("qa")?"-"+System.getProperty("env.Name") : ""; //"" Prod
	//	public Boolean HEADLESS_EXECUTION=Boolean.valueOf(System.getProperty("browser.headless"));
	//	public String BROWSER_TYPE= System.getProperty("browser.type");
	//	public String GARAGE_NAME= System.getProperty("garage.name");


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


	public String getDefaultPassword() {
		return defaultPassword;
	}


	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}


	public void logInfo(String message) {

		ExtentListeners.testReport.get().info(message);
	}


	//Add a non assertion step to the report
	public void reportStep(Status status, String message) {

		switch (status) {
		case INFO:
			ExtentListeners.testReport.get().info(message);
			break;

		case PASS:
			ExtentListeners.testReport.get().pass(message);
			break;

		case FAIL:
			ExtentListeners.testReport.get().fail(message);
			break;

		case WARNING:
			ExtentListeners.testReport.get().warning(message);
			break;

		case SKIP:
			ExtentListeners.testReport.get().skip(message);
			break;

		case ERROR:
			ExtentListeners.testReport.get().error(message);
			break;

		default:
			ExtentListeners.testReport.get().info(message);
			break;
		}

	}

	public void configureLogging() {
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);
	}


	public List<LogEntry> getPerformanceLogs() {
		List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
		System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
		return entries;
	}



	//Read Properties File
	public Properties readPropertiesFile(String fileName) throws IOException {

		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}

		return prop;
	}


	//Set Browser Zoom
	public void setBrowserZoomPercentage(String zoomPercentage) {
		DriverManager.getJSExecutor().executeScript("document.body.style.zoom='"+zoomPercentage+"%';");
	}



	/* ---------------------------------------------- Setup and Tear Down --------------------------------------------------------*/

	@BeforeSuite
	public void setUpFramework() {

		configureLogging();
		DriverFactory.setGridPath("http://localhost:4444/wd/hub");
		DriverFactory.setConfigPropertyFilePath(
				System.getProperty("user.dir") + "//src//test//resources//properties//Config.properties");
		//		String os_name=System.getProperty("os.name");

		/*
		 * Initialize properties Initialize logs load executables
		 * 
		 */
		try {
			fis = new FileInputStream(DriverFactory.getConfigPropertyFilePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Config.load(fis);
			log.info("Config properties file loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}

		openBrowser(BROWSER_TYPE, HEADLESS_EXECUTION);
	}


	@BeforeTest
	public void setupTests() throws NoSuchElementException, InterruptedException {

		//Page Objects instantiation
		loginPage = new CCSS_Login_Page();
		rrtPage = new CCSS_RecommendedRunAndTrack_Page();
		chargerControlPage = new CCSS_ChargerControl_Page();
		garageOverviewPage = new CCSS_GarageOverview_Page();
		energyManagementPage = new CCSS_EnergyManagement_Page();
		datConst = new SetDataConstants();
		apiResp = new GetAPIResponse();
		apiDat = new GetAPIResponseData();
		clearGarage = new ClearGarage();

	}


	@BeforeMethod
	public void setupTestCase() {

		System.out.println("------------------------------------------------------------------/n");
		System.out.println("Test Started");

		sAssert = new SoftAssertLogger();
		hAssert = new HardAssertLogger();

		log.info("Test Started");
	}


	@AfterMethod
	public void tearDownTestCase(ITestResult result) {

		String testStatus;
		String jiraNumber = "";
		String testDescription = result.getMethod().getDescription();

		log.info("Test Completed");

		if (result.getStatus() == ITestResult.SUCCESS) {
			testStatus = "Pass";
		}else if(result.getStatus() == ITestResult.FAILURE) {
			testStatus = "Fail";
		}else {
			testStatus = "Skip";
		}


		try {

			if (testDescription.contains("TOUC-")) {
				jiraNumber = StringUtils.substringBetween(testDescription,"TOUC-", ":").trim();
			}


			if (jiraNumber.isBlank() || jiraNumber.isEmpty() || result.getMethod().getDescription().isEmpty() || result.getMethod().getDescription().isBlank()) {
				jiraNumber = result.getMethod().getMethodName();
			}

			testCaseStatusMap.put("TOUC-"+jiraNumber,testStatus);
			System.out.println(testCaseStatusMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Test Completed");
		System.out.println("");
	}


	@AfterSuite
	public void afterSuite() {
		//Close and Quit the driver instance
		quit();
	}

	/* ---------------------------------------------------------------------------------------------------------------------------*/


	public void openBrowser(String browser, Boolean HeadlessExecution) {

		if(System.getenv("ExecutionType")!=null && System.getenv("ExecutionType").equals("Grid")) {

			grid=true;
		}

		DriverFactory.setRemote(grid);
		DesiredCapabilities cap = null;
		if (DriverFactory.isRemote()) {

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

		} else if(browser.equals("chrome")) {
			ChromeOptions chromeOptions = new ChromeOptions();

			if(HeadlessExecution == true) {
				chromeOptions.addArguments("--headless");
			}
			System.out.println("Launching : " + browser);
			WebDriverManager.chromedriver().setup();
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--no-sandbox", "--whitelisted-ips=''");


			//Logging Preferences
			if (BROWSER_LOGGING_ENABLED) {
				LoggingPreferences logPrefs = new LoggingPreferences();
				logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
				chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
			}

			driver = new ChromeDriver(chromeOptions);
		} else if (browser.equals("firefox")) {
			System.out.println("Launching : " + browser);
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}

		DriverManager.setWebDriver(driver);
		log.info("Driver Initialized !!!");

		//		DriverManager.getDriver().get(Config.getProperty("defaultUserName"));
		try{
			DriverManager.getDriver().manage().window().maximize();
		} catch(Exception ex) {}
		DriverManager.getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		//		DriverManager.getDriver().get(Config.getProperty("testsiteurl"));;
		//		setDefaultUserName(Config.getProperty("defaultUserName"));
		//		setDefaultPassword(Config.getProperty("defaultPassword"));

	}


	public void quit() {
		if(DriverManager.getDriver() !=null)
			DriverManager.getDriver().close();
		DriverManager.getDriver().quit();
		log.info("Test Suite Execution Completed !!!");
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
