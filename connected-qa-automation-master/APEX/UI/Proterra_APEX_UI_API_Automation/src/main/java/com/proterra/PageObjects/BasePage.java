package main.java.com.proterra.PageObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DriverManager;

public abstract class BasePage<T> {

	protected WebDriver driver;
	protected WebDriverWait wait;

	protected long LOAD_TIMEOUT = 30;
	protected int AJAX_ELEMENT_TIMEOUT = 10;

	protected Actions action;

	public static Logger log = Logger.getLogger(BasePage.class.getName());

	public BasePage() {
		this.driver = DriverManager.getDriver();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void waitForPageToLoad(ExpectedCondition pageLoadCondition) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, LOAD_TIMEOUT);
			wait.until(pageLoadCondition);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

	}


	// Wait for element
	public void WaitForElementVisible(WebElement element) {
		try {
			wait = new WebDriverWait(DriverManager.getDriver(), 50);
			wait.until(ExpectedConditions.visibilityOf(element));
			ExtentListeners.testReport.get().info("Waited for element visibility : " + element.getText());
		} catch (Exception ex) {
		}
	}

	// Wait for element
	public void WaitForElementTextVisible(WebElement element, String text) {
		try {
			wait = new WebDriverWait(DriverManager.getDriver(), 50);
			wait.until(ExpectedConditions.textToBePresentInElement(element, text));
			ExtentListeners.testReport.get().info("Waited for element text visibility : " + element.getText());
		} catch (Exception ex) {
		}
	}

	// Wait for element to be present in the DOM
	public void waitForElementPresent(WebElement element) throws InterruptedException {
		String newXpath;
		boolean isPresent;
		int counter;
		try {
			newXpath = getXpathStringFromWebElement(element);
			isPresent = false;
			counter = 0;
			while (isPresent == false) {
				isPresent = DriverManager.getDriver().findElements(By.xpath(newXpath)).size() > 0;
				Thread.sleep(1000);
				counter = counter++;
				if (counter == 30) {
					throw new ElementNotInteractableException("Element state did not change even after 30 seconds");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	// Wait for element to be present in the DOM
	public void waitForElementNotPresent(WebElement element, int timeInMills) throws InterruptedException {
		String newXpath;
		boolean isPresent;
		int counter;
		try {
			newXpath = getXpathStringFromWebElement(element);
			isPresent = true;
			counter = 0;
			while (isPresent == true) {
				isPresent = DriverManager.getDriver().findElements(By.xpath(newXpath)).size() > 0;
				Thread.sleep(1000);
				counter = counter++;
				if (counter == timeInMills) {
					throw new ElementNotInteractableException("Element state did not change even after 30 seconds");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	//Wait for element to not be displayed on the screen
	public void waitForElementNotDisplayed(WebElement element) throws InterruptedException {
		String newXpath;
		boolean isElementDisplayed;
		int counter;
		try {
			newXpath = getXpathStringFromWebElement(element);
			isElementDisplayed = true;
			counter = 0;
			while (isElementDisplayed == true) {
				isElementDisplayed = DriverManager.getDriver().findElements(By.xpath(newXpath)).size() > 0;
				Thread.sleep(2000);
				counter = counter++;
				if (counter == 5) {
					throw new ElementNotInteractableException("Element state did not change");
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}


	// Wait for element to disappear
	public void WaitForElementDisappear(WebElement element) {
		try {
			wait = new WebDriverWait(DriverManager.getDriver(), 50);
			wait.until(ExpectedConditions.invisibilityOf(element));
			ExtentListeners.testReport.get().info("Waited for element visibility : " + element.getText());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Wait for element to be interactable
	public void waitForElementClickable(WebElement element) {
		try {
			wait = new WebDriverWait(DriverManager.getDriver(), 30);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			ExtentListeners.testReport.get().info("Waited for element Invisibility : " + element.getText());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

	}


	//Check element presence
	public boolean isElementPresent(WebElement element) {

		String newXpath;
		boolean isElementPresent = true;
		int size;
		try {
			newXpath = getXpathStringFromWebElement(element);
			size = DriverManager.getDriver().findElements(By.xpath(newXpath)).size();

			if (size==0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isElementPresent;
	}


	//Is Element Present
	public boolean isElementPresent(String xpth) {

		boolean isElementPresent = true;
		int size;
		try {
			size = DriverManager.getDriver().findElements(By.xpath(xpth)).size();

			if (size==0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isElementPresent;
	}

	// Get XPATH String from Web Element
	public String getXpathStringFromWebElement(WebElement element) {
		String strWebElement = element.toString();
		String[] webElement = null;
		String xpath;
		String newXpath = null;
		try {
			if (strWebElement.contains("-> xpath")) {
				webElement = strWebElement.split("->");
				xpath = webElement[1].replaceFirst("xpath: ", "").trim();
				newXpath = StringUtils.removeEnd(xpath, "]");
			}else if(strWebElement.contains("By.xpath")) {
				webElement = strWebElement.split("By.");
				xpath = webElement[1].replaceFirst("xpath: ", "").trim();
				newXpath = StringUtils.removeEnd(xpath, "'");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return newXpath;
	}

	@SuppressWarnings("rawtypes")
	protected abstract ExpectedCondition getPageLoadCondition();

	// TODO: Update method usage comments
	@SuppressWarnings("rawtypes")
	public T openPage(Class<T> clazz) {
		T page = null;
		try {
			driver = DriverManager.getDriver();
			AjaxElementLocatorFactory ajaxElemFactory = new AjaxElementLocatorFactory(driver, AJAX_ELEMENT_TIMEOUT);
			page = PageFactory.initElements(driver, clazz);
			PageFactory.initElements(ajaxElemFactory, page);
			ExpectedCondition pageLoadCondition = ((BasePage) page).getPageLoadCondition();
			waitForPageToLoad(pageLoadCondition);
		} catch (NoSuchElementException e) {
			// Use File.separator instead of // or \\
			// String error_screenshot = System.getProperty("user.dir") +
			// "\\target\\screenshots\\" + clazz.getSimpleName() + "_error.png";
			// this.takeScreenShot(error_screenshot);
			throw new IllegalStateException(String.format("This is not the %s page", clazz.getSimpleName()));
		}
		return page;
	}

	// Click Element
	public void click(WebElement element, String elementName) {
		try {
			element.click();
			ExtentListeners.testReport.get().info("Clicking on : " + elementName);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

	}

	// Type into a field / text box
	public void type(WebElement element, String value, String elementName) {
		try {
			element.sendKeys(value);
			ExtentListeners.testReport.get().info("Typing in : " + elementName + " entered the value as : " + value);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Select Dropdown Value
	public void selectDropdownValue(WebElement element, String value, String elementName) {
		try {
			Select sel = new Select(element);
			sel.selectByValue(value);
			ExtentListeners.testReport.get().info("Selecting : " + elementName + " dropdown value as : " + value);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Clear an element
	public void clear(WebElement element, String elementName) {
		try {
			element.clear();
			ExtentListeners.testReport.get().info("Clicking on : " + elementName);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
	}

	// Mouse Click Element
	public void mouseClick(WebElement element, String elementName) {
		try {
			action = new Actions(driver);
			action.moveToElement(element).click(element).build().perform();
			ExtentListeners.testReport.get().info("Clicking on : " + elementName);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}

	}

	// TODO: Drag and Drop

	// TODO: File Upload method to be updated
	public boolean fileUpload(WebElement element, String filePath, String fileName, String elementName)
			throws InterruptedException, AWTException {
		Thread.sleep(2000);
		// JavascriptExecutor js=(JavascriptExecutor) driver;
		// js.executeScript("arguments[0].style.display = 'block';", element);
		element.sendKeys("/Users/sankar/");

		// fileUploadElement.sendKeys(path);

		// ((JavascriptExecutor) driver).executeScript("arguments[0].style.display =
		// 'block';", element);
		// driver.setFileDetector(new LocalFileDetector());
		// element.sendKeys(filePath+"/"+fileName);

		// File Need to be imported
		File file = new File("//Users//sankar//");
		StringSelection stringSelection = new StringSelection(file.getAbsolutePath());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		// Copy to clipboard
		// Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,
		// null);
		Robot robot = new Robot();
		// Cmd + Tab is needed since it launches a Java app and the browser looses focus
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// Release
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(500);
		// ENter
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);

		// //Open Goto window
		// robot.keyPress(KeyEvent.VK_META);
		// robot.keyPress(KeyEvent.VK_SHIFT);
		// robot.keyPress(KeyEvent.VK_G);
		// robot.keyRelease(KeyEvent.VK_META);
		// robot.keyRelease(KeyEvent.VK_SHIFT);
		// robot.keyRelease(KeyEvent.VK_G);
		//
		// //Paste the clipboard value
		// robot.keyPress(KeyEvent.VK_META);
		// robot.keyPress(KeyEvent.VK_V);
		// robot.keyRelease(KeyEvent.VK_META);
		// robot.keyRelease(KeyEvent.VK_V);
		//
		// //Press Enter key to close the Goto window and Upload window
		//
		// robot.keyPress(KeyEvent.VK_ENTER);
		// robot.keyRelease(KeyEvent.VK_ENTER);
		// robot.delay(500);
		// robot.keyPress(KeyEvent.VK_ENTER);
		// robot.keyRelease(KeyEvent.VK_ENTER);

		// ExtentListeners.testReport.get().info("Clearing Existing Value of :
		// "+elementName);
		return true;

	}

	// Select Sub menu
	public static void selectMenuSubmenu(WebDriver driver, WebElement menu, WebElement submenu, String ActionType)
			throws InterruptedException {
		// if submenu opens on click on menu
		if (ActionType.equalsIgnoreCase("Click")) {
			menu.click();
			submenu.click();
		}
		// if sub menu opens on hover on menu
		else {
			Actions action = new Actions(driver);
			action.moveToElement(menu).build().perform();
			Thread.sleep(3000);
			submenu.click();
		}
	}

	// Verify the field is editable or not.
	public boolean isFieldEditable(WebElement locator) {
		String className = null;
		boolean isEditable = false;
		try {
			className = locator.getAttribute("class");
			isEditable = className.indexOf("numberEditable") != -1 ? true : false;
		} catch (NoSuchElementException e) {
			System.err.println(e.getMessage());
			log.error(e.getStackTrace());
		}
		return isEditable;
	}

}
