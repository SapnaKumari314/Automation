package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.RunDetails;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class InProgress_TOUC3324_EstimatedChargeTimeNotReadyBusWithoutCharger extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3324: CCSS-RRT-Run Details: Estimated Charge Time when charger position is not available for Not Ready Buses",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3324"
			)
	public void Test_TOUC_3324() throws InterruptedException {


		//Variables

		
		//Get all the buses for the track
		
		
		//Get all the track positions
		
		
		//Get only the charger positions
		
		
		//Get the last position from each track which has a charger
		
		
		//Search the bus
		
		
		//Check for the bus whose current energy > 450
		
		
		//Assign the bus to the last charger position in the track
		
		
		//Remove the bus from the list
		
		
		//Repeat for all such charger positions

		sAssert.assertAll();


	}


}
