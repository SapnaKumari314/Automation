package test.java.com.proterra.testcases.ccss.APICalls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAPIResponseData extends GetAPIResponse {

	public static Logger log = Logger.getLogger(GetAPIResponseData.class.getName());

	// Get Tenant ID
	public static String getTenantID() {
		tenantID = getTenantResponse().get("custom:tenantId").toString();
		return tenantID;
	}

	// Get Active Garage ID
	public static String getActiveGarageID() {
		activeGarageID = getActiveGarageIDResponse().get("trackSetupId").toString();
		return activeGarageID;
	}

	// Get Incoming Queue Bus details
	public Map<String, String> getIncomingBuses() {
		JSONObject jObj = getIncomingQueueResponse();
		JSONArray jArr = jObj.getJSONArray("content");
		// Add the Bus Vin and Bookin Time to a map
		Map<String, String> apiBusMap = new LinkedHashMap<>();

		JSONObject arrObj;

		for (int i = 0; i < jArr.length(); i++) {
			arrObj = jArr.getJSONObject(i);
			apiBusMap.put(arrObj.get("vin").toString(), arrObj.get("bookInTime").toString());
		}
		return apiBusMap;
	}

	// Incoming bus queue size
	public int getIncomingBusQueueSize() {
		JSONObject jObj = getIncomingQueueResponse();
		int queueSize = jObj.getInt("numberOfElements");
		return queueSize;
	}

	// Incoming bus queue size total elements
	public int getIncomingBusTotalSize() {
		JSONObject jObj = getIncomingQueueResponse();
		int queueSize = jObj.getInt("totalElements");
		return queueSize;
	}

	// Get Selected Garage Bus Detals
	public Map<String, String> getAllBusesForSelectedGarage() {

		JSONArray busesArray = getTracksResponse().getJSONArray("buses");
		Map<String, String> busMap = new LinkedHashMap<>();
		for (int i = 0; i < busesArray.length(); i++) {
			JSONObject busObject = busesArray.getJSONObject(i);
			String busName = busObject.getString("busName");
			String busVIN = busObject.getString("busVIN");
			busMap.put(busVIN, busName);
		}
		return busMap;
	}

	// Get all the buses that are parked in the garage
	public Map<String, String> getBusesInGarage() throws JSONException {

		JSONArray tracksArray = getTracksResponse().getJSONArray("tracks");
		Map<String, String> busMap = new LinkedHashMap<>();
		for (int i = 0; i < tracksArray.length(); i++) {
			JSONObject trackObject = tracksArray.getJSONObject(i);
			JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");
			for (int j = 0; j < chargerPositionsArray.length(); j++) {
				JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);
				if (chargerPositionObject.isNull("bus")) {
					continue;
				}
				JSONObject busObject = chargerPositionObject.getJSONObject("bus");
				String busName = busObject.getString("busName");
				String busVIN = busObject.getString("busVIN");
				busMap.put(busVIN, busName);
			}
		}
		return busMap;
	}


	// Get all the buses that are allocated to a garage
	public Map<String, String> getGarageSpecificBuses() throws JSONException {

		JSONArray busesArray = getTracksResponse().getJSONArray("buses");
		Map<String, String> busMap = new LinkedHashMap<>();
		for (int i = 0; i < busesArray.length(); i++) {
			JSONObject busesObject = busesArray.getJSONObject(i);
			String busName = busesObject.getString("busName");
			String busVIN = busesObject.getString("busVIN");
			busMap.put(busVIN, busName);
		}
		return busMap;
	}

	// Get all the buses that are not parked in the garage nor are in the incoming
	// queue
	public Map<String, String> getBusesNotInQueueAndGarage() throws JSONException {

		Map<String, String> busInGarageMap = getBusesInGarage();
		Map<String, String> allBusesForGarage = getAllBusesForSelectedGarage();
		List<String> busInQueueList = new ArrayList<>();
		Map<String, String> busNotInQueueAndGarageMap = new LinkedHashMap<>();

		JSONObject incomingQueueObj = getIncomingQueueResponse();
		JSONArray contentArray = incomingQueueObj.getJSONArray("content");

		if (contentArray.length() > 0) {
			for (int i = 0; i < contentArray.length(); i++) {
				JSONObject contentObject = contentArray.getJSONObject(i);
				String busVin = contentObject.getString("vin");
				busInQueueList.add(busVin);
			}
		}
		for (Map.Entry<String, String> entry : allBusesForGarage.entrySet()) {
			if (busInGarageMap.containsKey(entry.getKey()) || busInQueueList.contains(entry.getKey())) {
			} else {
				busNotInQueueAndGarageMap.put(entry.getKey(), entry.getValue());
			}
		}
		return busNotInQueueAndGarageMap;
	}

	// Get the ui row and ui col for the track and position in a garage
	public Map<String, String> getGarageAllTrackPositions() {

		String trackName;
		String positionNumber;

		String uiRowVal;
		String uiColVal;
		Map<String, String> trackPositionMap = new LinkedHashMap<>();

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the Track Name
				trackName = trackObject.getString("trackName");

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the position number
					positionNumber = String.valueOf(j + 1);

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					// Get the UI Row value based on provided track
					uiRowVal = chargerPositionObject.get("uiRowStart").toString();

					// Get the UI Col Value based on provided position
					uiColVal = chargerPositionObject.get("uiColumnStart").toString();

					// Add the Track-Position = uiRowVal-uiColVal to the map
					trackPositionMap.put(trackName + "-" + positionNumber, uiRowVal + "-" + uiColVal);
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}

		return trackPositionMap;

	}


	// Get the empty garage track positions
	public Map<String, String> getEmptyGarageTrackPositions() {

		String trackName;
		String positionNumber;

		String uiRowVal;
		String uiColVal;
		Map<String, String> trackPositionMap = new LinkedHashMap<>();

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the Track Name
				trackName = trackObject.getString("trackName");

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the position number
					positionNumber = String.valueOf(j + 1);

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					//Check if position is occupied
					if (!(chargerPositionObject.isNull("busVIN"))) {
						continue;
					}

					// Get the UI Row value based on provided track
					uiRowVal = chargerPositionObject.get("uiRowStart").toString();

					// Get the UI Col Value based on provided position
					uiColVal = chargerPositionObject.get("uiColumnStart").toString();

					// Add the Track-Position = uiRowVal-uiColVal to the map
					trackPositionMap.put(trackName + "-" + positionNumber, uiRowVal + "-" + uiColVal);
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}

		return trackPositionMap;

	}

	// Get Number of Installed Chargers
	public int getInstalledChargersCount() {
		int chargerCount = 0;
		boolean isChargerInstalled;

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					// Checking Is Charger Installed Property is null or not.
					if (chargerPositionObject.isNull("isChargerInstalled")) {
						continue;
					} else {
						isChargerInstalled = (boolean) chargerPositionObject.get("isChargerInstalled");
						if (isChargerInstalled) {
							chargerCount = chargerCount + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}

		return chargerCount;
	}

	// Get pair of charger name and power
	public HashMap<String, Integer> getChargersNameAndPower() {
		HashMap<String, Integer> chargerNamePowerMap = new HashMap<String, Integer>();
		boolean isChargerInstalled;

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					// Checking Is Charger Installed Property is null or not.
					if (chargerPositionObject.isNull("isChargerInstalled")) {
						continue;
					} else {
						isChargerInstalled = (boolean) chargerPositionObject.get("isChargerInstalled");
						if (isChargerInstalled) {
							String chargerName = chargerPositionObject.getString("chargerName");
							if (chargerPositionObject.isNull("chargerName")
									|| chargerPositionObject.isNull("chargerPower")) {
								break;
							} else {
								int chargerPower = chargerPositionObject.getInt("chargerPower");
								chargerNamePowerMap.put(chargerName, chargerPower);
							}

						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}

		return chargerNamePowerMap;
	}

	// Get Active charger session count
	public int getActiveChargerSessionCount() {
		int activeChargerCount = 0;
		String chargerStatus;

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					// Checking Charger Status Property is null or not.
					if (chargerPositionObject.isNull("chargerStatus")) {
						continue;
					} else {
						chargerStatus = chargerPositionObject.getString("chargerStatus");
						if (chargerStatus.equalsIgnoreCase("Charging")) {
							activeChargerCount = activeChargerCount + 1;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}

		return activeChargerCount;
	}

	// Get Cumulative delay value
	public String getCumulativeDelayValue() {
		int bookOutDelay = 0;
		String bookOutDelayString = null;

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {

					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					if (chargerPositionObject.isNull("bus")) {
						continue;
					} else {
						System.out.println("into bus object");
						JSONObject busObject = chargerPositionObject.getJSONObject("bus");
						if (busObject.getBoolean("isBookOutDelayed")) {
							if ((busObject.get("bookOutDelayInS")).equals("InDefinite")) {
								bookOutDelayString = "InDefinite";
								break;
							} else {
								System.out.println("value of delay:"+busObject.getInt("bookOutDelayInS"));
								bookOutDelay =  busObject.getInt("bookOutDelayInS");
								bookOutDelay += bookOutDelay;

							}
						}
					}

				}
			}
			if(bookOutDelayString == null || bookOutDelayString.isEmpty() || !bookOutDelayString.equals("InDefinite")) {
				bookOutDelayString = String.valueOf(bookOutDelay);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		return bookOutDelayString;
	}

	// Get Bus Counts Map
	public HashMap<String, String> getBusCountsMap() {
		int chargingCount = 0;
		int waitingCount = 0;
		int parkedCount = 0;
		int currentEnergy;
		int requiredEnergy;
		HashMap<String, String> buscountValuesMap = null;

		try {

			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {
					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);

					if (chargerPositionObject.isNull("busVIN") || chargerPositionObject.getString("busVIN").isEmpty()) {
						continue;
					}
					else {
						if(chargerPositionObject.getString("chargerStatus").equalsIgnoreCase("Charging")) {
							chargingCount++;
						}
						else {
							if (chargerPositionObject.isNull("bus")) {
								continue;
							} else {
								JSONObject busObject = chargerPositionObject.getJSONObject("bus");
								currentEnergy = busObject.getInt("currentEnergy");
								requiredEnergy = busObject.getInt("energyRequired");
								if(currentEnergy < requiredEnergy) {
									waitingCount++;
								}
								else if(busObject.getBoolean("isMaintenanceRequired") || (currentEnergy > requiredEnergy)){
									parkedCount++;
								}
							}
						}
					}
				}
			}
			buscountValuesMap = new HashMap<String, String>();
			/*
			 * buscountValuesMap.put("Incoming Queue",
			 * String.valueOf(getIncomingBusTotalSize()));
			 */
			buscountValuesMap.put("Charging", String.valueOf(chargingCount));
			buscountValuesMap.put("Waiting", String.valueOf(waitingCount));
			buscountValuesMap.put("Parked", String.valueOf(parkedCount));

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		return buscountValuesMap;
	}

	public int shopChargerCount() {
		return 0;

	}


	//Get Config Values Map
	public Map<String, String> getConfigValuesForActiveGarage(String garageId){

		Map<String, String> configMap = new LinkedHashMap<>();

		try {

			// Get the Track Setup API response
			JSONObject responseObj = getConfigDetailsResponse();
			JSONArray configArray = responseObj.getJSONArray("configs");

			for (int i =0; i<configArray.length();i++) {

				JSONObject configObject = configArray.getJSONObject(i);

				if (configObject.get("garageId").toString().equals(garageId)) {

					configMap.put(configObject.get("nameOnUi").toString(), configObject.get("value").toString());

				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		return configMap;
	}


	//Get Bus Details component values from API Response
	public Map<String, String> getBusDetailsUIComponentValues(String busVin){

		Map<String, String> busDetailsMap = new LinkedHashMap<>();
		List<String> busList = new ArrayList<>();
		busList.add(busVin);

		try {

			//Get Bus Details Response
			JSONObject respObj = getBusDetails(busList);
			JSONObject busesObject = respObj.getJSONObject("buses");
			JSONObject busVinObject = busesObject.getJSONObject(busVin);

			busDetailsMap.put("range", busVinObject.get("range").toString());
			busDetailsMap.put("hddtc", busVinObject.get("faultsCount").toString());
			busDetailsMap.put("energyConsumption", busVinObject.get("energyConsumption").toString());

			JSONObject sourceObject = busVinObject.getJSONObject("_source");

			busDetailsMap.put("currentEnergy", sourceObject.get("PCes_usi_SystemEnergy_kwh").toString());
			busDetailsMap.put("soc", sourceObject.get("PCes_usi_DashSoC_pct").toString());
			busDetailsMap.put("odometer", sourceObject.get("PCtc_udi_Odometer_mi").toString());

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		return busDetailsMap;
	}


	//Get Bus Details component values from Track Setup API Response
	public Map<String, String> getTrackSetupBusDetails(String busVin){

		Map<String, String> busDetailsMap = new LinkedHashMap<>();
		List<String> busList = new ArrayList<>();
		busList.add(busVin);

		try {

			//Get Bus Details Response
			JSONArray respArray = getTracksPositionsResponse();
			for (int i = 0; i < respArray.length(); i++) {
				
				JSONObject trackObject = respArray.getJSONObject(i);
				
				if (!(trackObject.get("busVIN").equals(busVin))) {
					continue;
				}
				JSONObject busObject = trackObject.getJSONObject("bus");
				
				busDetailsMap.put("needsMaintenance", busObject.get("isMaintenanceRequired").toString());
				busDetailsMap.put("currentEnergy", busObject.get("currentEnergy").toString());
				busDetailsMap.put("availableEnergy", busObject.get("availableEnergy").toString());
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		return busDetailsMap;
	}

	
	//TODO: Get all the Track Position Data
	//Add index variables
	public final int listIndex_ocppId = 0;
	public final int listIndex_chargerStatus = 1;
	public final int listIndex_chargerPower = 2;
	public final int listIndex_isChargerInstalled = 3;
	public final int listIndex_busVIN = 4;
	public final int listIndex_chargerAvailableForCharging = 5;
	
	public Map<String,List<Object>> getAllTrackPositionsData() {
		
		Map<String,List<Object>> chargerDataMap = new LinkedHashMap<String, List<Object>>();
		List<Object> chargerPositionList;
		
		String chargerName = null;
		String ocppId = null;
		String chargerStatus = null;
		int chargerPower = 0;
		boolean isChargerInstalled = false;
		String busVIN = null;
		boolean chargerAvailableForCharging = false;
		
		try {
			
			// Get the Track Setup API response
			JSONObject trackSetupResponseJSON = getTracksResponse();

			// Get the Tracks Array
			JSONArray tracksArray = trackSetupResponseJSON.getJSONArray("tracks");
			

			// Loop through the Tracks Array
			for (int i = 0; i < tracksArray.length(); i++) {

				// Get the Track Object
				JSONObject trackObject = tracksArray.getJSONObject(i);

				String trackType = trackObject.get("uiTrackType").toString();

				// Skip the loop if it is a header track
				if (trackType.equalsIgnoreCase("HEADER_TRACK")) {
					continue;
				}

				// Get the charger positions array
				JSONArray chargerPositionsArray = trackObject.getJSONArray("chargerPositions");

				// Loop through the chargerPositionArray
				for (int j = 0; j < chargerPositionsArray.length(); j++) {
					
					chargerPositionList = new LinkedList<>();
					
					chargerName = null;
					ocppId = null;
					chargerStatus = null;
					chargerPower = 0;
					isChargerInstalled = false;
					busVIN = null;
					chargerAvailableForCharging = false;
					
					// Get the Charger Position Object
					JSONObject chargerPositionObject = chargerPositionsArray.getJSONObject(j);
					
					if (chargerPositionObject.isNull("chargerName")) {
						continue;
					}else {
						chargerName = chargerPositionObject.get("chargerName").toString();
					}
					
					if (!(chargerPositionObject.isNull("ocppId"))) {
						ocppId = chargerPositionObject.get("ocppId").toString();
					}
					chargerPositionList.add(listIndex_ocppId, ocppId);
					
					if (!(chargerPositionObject.isNull("chargerStatus"))) {
						chargerStatus = chargerPositionObject.get("chargerStatus").toString();
					}
					chargerPositionList.add(listIndex_chargerStatus, chargerStatus);
					
					if (!(chargerPositionObject.isNull("chargerPower"))) {
						chargerPower = chargerPositionObject.getInt("chargerPower");
					}
					chargerPositionList.add(listIndex_chargerPower, chargerPower);
					
					if (!(chargerPositionObject.isNull("isChargerInstalled"))) {
						isChargerInstalled = chargerPositionObject.getBoolean("isChargerInstalled");
					}
					chargerPositionList.add(listIndex_isChargerInstalled, isChargerInstalled);
					
					if (!(chargerPositionObject.isNull("busVIN"))) {
						busVIN = chargerPositionObject.get("busVIN").toString();
					}
					chargerPositionList.add(listIndex_busVIN, busVIN);
					
					if (!(chargerPositionObject.isNull("chargerAvailableForCharging"))) {
						chargerAvailableForCharging = chargerPositionObject.getBoolean("chargerAvailableForCharging");
					}
					chargerPositionList.add(listIndex_chargerAvailableForCharging, chargerAvailableForCharging);
					
					
					chargerDataMap.put(chargerName, chargerPositionList);
				}
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		
		return chargerDataMap;
	}
}
