package test.java.com.proterra.testcases.ccss.dataSetup;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class ClearIQ extends BaseTestClass{

	public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


	@Test(
			description="Remove the Buses from the Incoming Queue",
			dependsOnGroups = {"Login"},
			groups = "Regression"
			)
	public void clearIncomingQueue() {

		//Get list of buses from incoming queue
		Map<String, String> apiBusMap = apiDat.getIncomingBuses();

		//Run the clear IQ
		if (!(apiBusMap.isEmpty())) {
			for(Map.Entry<String, String> entry : apiBusMap.entrySet()) {
				apiResp.deleteIQBus(entry.getKey());
			}
		}


		//Verify the count of Incoming Buses from the API
		int apiIQSize = apiBusMap.size();
		sAssert.assertTrue(apiIQSize == 0, "Incoming Queue is cleared");
	}

}
