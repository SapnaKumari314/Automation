package main.java.com.proterra.PageObjectsOld;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.java.com.proterra.ExtentListeners.ExtentListeners;
import main.java.com.proterra.utilities.DriverManager;

public abstract class BasePage<T> {



	protected WebDriver driver;

	private long LOAD_TIMEOUT = 30;
	private int AJAX_ELEMENT_TIMEOUT = 10;

	public BasePage() {
		this.driver = DriverManager.getDriver();
	}

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
			/*    String error_screenshot = System.getProperty("user.dir") + "\\target\\screenshots\\" + clazz.getSimpleName() + "_error.png";
	            this.takeScreenShot(error_screenshot);
			 */       throw new IllegalStateException(String.format("This is not the %s page", clazz.getSimpleName()));
		}
		return page;
	}

	private void waitForPageToLoad(ExpectedCondition pageLoadCondition) {
		WebDriverWait wait = new WebDriverWait(driver,LOAD_TIMEOUT);
		wait.until(pageLoadCondition);
	}

	protected abstract ExpectedCondition getPageLoadCondition();


	public void click(WebElement element, String elementName) {
		try {
			element.click();
			ExtentListeners.testReport.get().info("Clicking on : "+elementName);
		} catch(Exception ex) {

		}

	}

	public void type(WebElement element, String value, String elementName) {
		try {
			element.sendKeys(value);
			ExtentListeners.testReport.get().info("Typing in : "+elementName+" entered the value as : "+value);
		} catch(Exception ex) {

		}

	}
	public void clear(WebElement element, String elementName) {			
		element.clear();
		ExtentListeners.testReport.get().info("Clearing Existing Value of : "+elementName);		
	}

	public boolean fileUpload(WebElement element, String filePath, String fileName, String elementName) throws InterruptedException, AWTException {
		Thread.sleep(2000);
//		JavascriptExecutor js=(JavascriptExecutor) driver;
//		js.executeScript("arguments[0].style.display = 'block';", element);
		element.sendKeys("/Users/sankar/");

//		fileUploadElement.sendKeys(path);
		
//		((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", element);
//		driver.setFileDetector(new LocalFileDetector());
//		element.sendKeys(filePath+"/"+fileName);
		
		
		
		
		//File Need to be imported
		 File file = new File("//Users//sankar//"); 
		StringSelection stringSelection= new StringSelection(file.getAbsolutePath());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		//Copy to clipboard Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null); 
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
		
//		//Open Goto window	 
//		robot.keyPress(KeyEvent.VK_META);
//		robot.keyPress(KeyEvent.VK_SHIFT);
//		robot.keyPress(KeyEvent.VK_G);
//		robot.keyRelease(KeyEvent.VK_META); 
//		robot.keyRelease(KeyEvent.VK_SHIFT);	 
//		robot.keyRelease(KeyEvent.VK_G);
//		 
//		//Paste the clipboard value	 
//		robot.keyPress(KeyEvent.VK_META); 
//		robot.keyPress(KeyEvent.VK_V);	 
//		robot.keyRelease(KeyEvent.VK_META); 
//		robot.keyRelease(KeyEvent.VK_V);
//		 
//		//Press Enter key to close the Goto window and Upload window
//		 
//		robot.keyPress(KeyEvent.VK_ENTER);
//		robot.keyRelease(KeyEvent.VK_ENTER); 
//		robot.delay(500); 
//		robot.keyPress(KeyEvent.VK_ENTER);
//		robot.keyRelease(KeyEvent.VK_ENTER);	
		
//		ExtentListeners.testReport.get().info("Clearing Existing Value of : "+elementName);	
		return true;
		
	}
	public static void selectMenuSubmenu(WebDriver driver, WebElement menu, WebElement submenu, String ActionType) throws InterruptedException
	{
		// if submenu opens on click on menu
		if(ActionType.equalsIgnoreCase("Click"))
		{
			menu.click();
			submenu.click();
		}
		// if sub menu opens on hover on menu
		else
		{
			Actions action= new Actions(driver);
			action.moveToElement(menu).build().perform();
			Thread.sleep(3000);
			submenu.click();
		}
	}
}
