package test.java.com.proterra.testcases.ccss.dataSetup;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class ClearGarage extends BaseTestClass{

	public Properties props;
	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	@Test(
			description="Remove the Buses from the Garage",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	public void clearGarage() throws IOException, InterruptedException {
		
		List<String> busVinList = new ArrayList<String>();
		
		
		//Get the list of buses from the busGarageMapping.properties file
		props = readPropertiesFile(System.getProperty("user.dir") + File.separator + "src/test/resources/properties" + File.separator
				+ "busGarageMapping.properties");

		//Get list of buses for the selected garage
		Enumeration<?> e = props.propertyNames();
		
		while(e.hasMoreElements()) {
		    String key = (String) e.nextElement();
		    if(props.get(key).toString().equalsIgnoreCase(GARAGE_NAME)) { 
		    	busVinList.add(key);
		    }   
		}
		
		if (!(busVinList.isEmpty())) {
			
			apiResp.clearAllBusesFromGarage(ENV_NAME, busVinList);
		}
		
		reportStep(Status.INFO, "Garage is cleared");
		
	}


}
