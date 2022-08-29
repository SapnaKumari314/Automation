package test.java.com.proterra.testcases.ccss.RunAndTrackAssignment.BusDetails;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import main.java.com.proterra.ExtentListeners.CustomAnnotations;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class TOUC3312_CurrentEnergyBeforeAssignment extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/



	@Test(
			description="TOUC-3312: CCSS-RRT-Bus Details: Current Energy before Bus Assignment",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	@CustomAnnotations(
			jiraNumber = "3312"
			)
	public void Test_TOUC_3312() throws InterruptedException {


		List<String> iqBusList;
		
		Map<String, String> apiBusDetailsMap;
		Map<String, String> uiBusDetailsMap;

		String busName;
		String busVin;
		
		String uiCurrentEnergy;
		String apiCurrentEnergy;
		
		String uiSoc;
		String apiSoc;
		
		String uiRange;
		String apiRange;
		
		long uiOdometer;
		long apiOdometer;
		
		String uiEnergyConsumption;
		String apiEnergyConsumption;
		
		String uiHddtc;
		String apiHddtc;


		//Get buses from IQ
		iqBusList = rrtPage.getAllIQBusList();

		//Get the First Bus from the list
		busName = iqBusList.get(0);

		//Search the Bus using BEB Search
		rrtPage.searchBEB(busName);

		//Verify that BEB Search box is clear
		sAssert.assertTrue(rrtPage.bebSearchBox.getText().isBlank(), "BEB search box after searching is empty");

		//Get the component values from Bus Details
		uiBusDetailsMap = rrtPage.uiBusDetails();
		
		uiCurrentEnergy = uiBusDetailsMap.get(rrtPage.CURRENT_ENERGY).replaceAll("[^0-9]", "");
		uiSoc = uiBusDetailsMap.get(rrtPage.SOC).replaceAll("%", "").trim();
		uiRange = uiBusDetailsMap.get(rrtPage.RANGE).replaceAll("[^0-9]", "");
		uiOdometer = Long.parseLong(uiBusDetailsMap.get(rrtPage.ODOMETER).replaceAll("[^0-9]", ""));
		uiEnergyConsumption = uiBusDetailsMap.get(rrtPage.ENERGY_CONSUMPTION).replaceAll("[a-zA-Z/ ]+", "");
		uiHddtc = uiBusDetailsMap.get(rrtPage.HDDTC);
		
		//Get the Bus Details from Bus Details API 
		busVin = datConst.getBusVinByName(busName);
		apiBusDetailsMap = apiDat.getBusDetailsUIComponentValues(busVin);
		
		apiCurrentEnergy = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("currentEnergy"))));
		apiSoc = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("soc"))));
		apiRange = String.valueOf(Math.round(Double.parseDouble(apiBusDetailsMap.get("range")) * 1.609));
		apiOdometer = Math.round(Double.parseDouble(apiBusDetailsMap.get("odometer")) * 1.609);
		apiEnergyConsumption = String.valueOf(Math.round(((Double.parseDouble(apiBusDetailsMap.get("energyConsumption")))/1.609) * 10) / 10.0);
		apiHddtc = apiBusDetailsMap.get("hddtc");
		
		//Compare both the values
		sAssert.assertEquals(uiCurrentEnergy, apiCurrentEnergy, "Current Energy for the Bus before Assignment as per Bus Details API");
		
		sAssert.assertEquals(uiSoc, apiSoc, "SOC for the Bus before Assignment as per Bus Details API");
		
		sAssert.assertEquals(uiRange, apiRange, "Range for the Bus before Assignment as per Bus Details API");
		
		sAssert.assertTrue((apiOdometer >= uiOdometer - 3 && apiOdometer <= uiOdometer + 3),
				"Odometer for the Bus before Assignment as per Bus Details API");
		
		sAssert.assertEquals(uiEnergyConsumption, apiEnergyConsumption, "Energy Consumption for the Bus before Assignment as per Bus Details API");
		
		sAssert.assertEquals(uiHddtc, apiHddtc, "HDDTC for the Bus before Assignment as per Bus Details API");

		sAssert.assertAll();


	}


}
