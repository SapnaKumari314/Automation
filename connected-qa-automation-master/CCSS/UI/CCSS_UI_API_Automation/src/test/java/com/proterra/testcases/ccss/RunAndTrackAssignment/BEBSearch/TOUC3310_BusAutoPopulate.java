package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BEBSearch;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.google.common.collect.Ordering;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import main.java.com.proterra.utilities.DriverManager;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3310_BusAutoPopulate extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	public Properties props;


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3310: CCSS-RRT-BEB Search: List of buses in the auto populate",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3310"
			)
	public void Test_TOUC_3310() throws InterruptedException, IOException {

		//Dropdown Bus List
		List<String> busList = new LinkedList<>();

		//Search Bus
		action = new Actions(DriverManager.getDriver());
		rrtPage.clear(rrtPage.bebSearchBox, "BEB Search Box");
		action.click(rrtPage.bebSearchBox).sendKeys(rrtPage.bebSearchBox, "8").build().perform();

		boolean flag = rrtPage.CCSS_SearchAutoCompleteListValue.isDisplayed();

		sAssert.assertTrue(flag, "BEB Search Auto Complete Dropdown for Vlid Bus is populated");

		//Verify that the list is displayed in a sorted order
		for (WebElement	element: rrtPage.CCSS_BEBSearchListValues) {
			busList.add(element.getAttribute("ng-reflect-value"));
		}

		boolean isSorted = Ordering.natural().isOrdered(busList);
		sAssert.assertTrue(isSorted, "Buses in the BEB search dropdown are sorted in ascending order");

		//Verify that the list displays all the buses from the garage
		props = readPropertiesFile(System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "busGarageMapping.properties");

		//Get list of buses for the selected garage
		Enumeration<?> e = props.propertyNames();
		List<String> busVinList = new ArrayList<String>();
		while(e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if(key.equalsIgnoreCase(GARAGE_NAME)) { 
				busVinList.add(props.getProperty(key));
			}   
		}


		//Get a list of busNames by Vin
		props = readPropertiesFile(System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "busList.properties");
		for (String busVin : busVinList) {
			String busName = (String) props.get(busVin);
			sAssert.assertTrue(busList.contains(busName), "The BEB Search dropdown displays the Bus "+busName);
		}

		sAssert.assertAll();


	}


}
