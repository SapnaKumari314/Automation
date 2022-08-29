package test.java.com.proterra.testcases.ccss.APICalls;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.utilities.DriverManager;
import main.java.com.proterra.utilities.LocalStorage;
import test.java.com.proterra.testcases.ccss.BaseTestClass;

public class GetAPIResponse extends BaseTestClass{

	public static Logger log = Logger.getLogger(GetAPIResponse.class.getName());

	protected static String baseURL = "https://exp-api.proterra.com/";
	protected static String accessToken;
	protected static String tenantID;
	protected static String activeGarageID;


	//Generate UUID
	public String getUUID() {
		UUID uuid = null;
		try {
			uuid = UUID.randomUUID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid.toString();
	}

	//Get the Authentication Token Response
	public static String getAuthResponse() {
		LocalStorage storage;
		try {
			storage = new LocalStorage(DriverManager.getDriver());
			accessToken = storage.getItemFromLocalStorage("token");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	//Get the Tenant ID Response
	public static JSONObject getTenantResponse() throws JSONException {
		LocalStorage storage = new LocalStorage(DriverManager.getDriver());
		String responseBody = storage.getItemFromLocalStorage("loggedInUserDetails");
		JSONObject jObj = new JSONObject(responseBody);
		return jObj;
	}


	//Get the Active Garage Response
	public static JSONObject getActiveGarageIDResponse() throws JSONException {
		LocalStorage storage = new LocalStorage(DriverManager.getDriver());
		String responseBody = storage.getItemFromLocalStorage("selectedGarage");
		JSONObject jObj = new JSONObject(responseBody);
		return jObj;
	}


	//Get Active Garage Tracks Response
	public JSONObject getTracksResponse() {
		LocalStorage storage = new LocalStorage(DriverManager.getDriver());
		String responseBody = storage.getItemFromLocalStorage("tracksData");
		JSONObject jObj = new JSONObject(responseBody);
		return jObj;
	}


	//Get Active Garage Tracks Positions Response
	public JSONArray getTracksPositionsResponse() {
		LocalStorage storage = new LocalStorage(DriverManager.getDriver());
		String responseBody = storage.getItemFromLocalStorage("garageAllCels");
		JSONArray jArr = new JSONArray(responseBody);
		return jArr;
	}



	//Get Incoming Queue Bus Response
	public JSONObject getIncomingQueueResponse() throws JSONException{


		GetAPIResponseData.getAuthResponse();
		GetAPIResponseData.getTenantID();
		GetAPIResponseData.getActiveGarageID();

		RestAssured.baseURI = baseURL;
		String path = "exp-iq"+ENV_NAME+"/iq/garages/"+activeGarageID+"/incomingBuses";

		//Request
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", accessToken);

		//Add Query Params
		request.queryParam("size", "11");
		request.queryParam("sortBy", "bookInTime");
		request.queryParam("order", "ASC");

		//Send Request and Get Response
		Response response = request.request(Method.GET, path);

		//Convert Response Body to String
		String responseBody = response.getBody().asString();
		JSONObject jObj = new JSONObject(responseBody);
		return jObj;

	}


	//Get Bus Details API response for Bus not in the garage
	public JSONObject getBusDetails(List<String> busVin) throws Exception{

		GetAPIResponseData.getAuthResponse();
		GetAPIResponseData.getTenantID();
		GetAPIResponseData.getActiveGarageID();

		RestAssured.baseURI = baseURL;
		String path = "exp-ccss"+ENV_NAME+"/v1/tenants/"+tenantID+"/garages/"+activeGarageID+"/bus/getBusDetails";

		//Request
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", accessToken);

		//Add Payload
		JSONObject jObj = new JSONObject();
		jObj.put("id", "GET_BUS_DETAILS-"+getUUID());
		jObj.put("busVins", busVin);
		jObj.put("getBusFaults", true);

		request.body(jObj.toString());

		//Get Response
		Response response = request.request(Method.POST, path);

		if(response.getStatusCode() != 200) {
			System.err.println(response.asString());
			throw new Exception("API Call failed with status "+response.statusCode()+" and message "+response.statusLine());
		}

		//Convert Response Body to String
		String responseBody = response.getBody().asString();
		JSONObject respObj = new JSONObject(responseBody);
		return respObj;
	}


	//TODO: Get Bus Details for Bus in the garage 
	public void getBusDetailsFromGarage() {

	}


	//Get Track Setup Response
	public JSONObject getTrackSetupResponse() {
		String responseBody = null;
		try {
			GetAPIResponseData.getAuthResponse();
			GetAPIResponseData.getTenantID();
			GetAPIResponseData.getActiveGarageID();

			RestAssured.baseURI = baseURL;
			String path = "exp-ccss"+ENV_NAME+"/v1/trackSetups/"+activeGarageID;

			//Request
			RequestSpecification request = RestAssured.given();

			//Add Headers
			request.header("Content-Type", "application/json");
			request.header("authorization", accessToken);

			//Add Query Params
			request.queryParam("requestId", "LOAD_TRACK_LIST_REQUEST_ID"+getUUID());
			request.queryParam("tenantId", tenantID);

			//Get Response
			Response response = request.request(Method.GET, path);


			//Convert Response Body to String
			responseBody = response.getBody().asString();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		JSONObject respObj = new JSONObject(responseBody);
		return respObj;
	}


	//Remove Buses from Garage
	public void clearAllBusesFromGarage(String env, List<String> busVin) {

		String apiKey = null;

		try {

			GetAPIResponseData.getAuthResponse();
			GetAPIResponseData.getTenantID();
			GetAPIResponseData.getActiveGarageID();

			switch (env) {
			case "-dev":
				RestAssured.baseURI = "http://exp-dev-svc-alb.proterra.com/api/v1/";
				apiKey = "";
				break;

			case "-qa":
				RestAssured.baseURI = "https://exp-qa-svc-alb.proterra.com/api/v1/";
				apiKey = "";
				break;

			default:
				break;
			}

			for (String vin : busVin) {
				//URI Path
				String path = "tenants/"+tenantID+"/garages/"+activeGarageID+"/pullOutBusFromGarage/"+vin;

				//Request
				RequestSpecification request = RestAssured.given();

				//Add Headers
				request.header("Authorization", apiKey);

				//Get Response
				request.request(Method.DELETE, path);

			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
	}


	//Get Charger States Response
	public JSONObject getChargerStatesResponse() {
		String responseBody = null;
		try {
			GetAPIResponseData.getAuthResponse();
			GetAPIResponseData.getTenantID();
			GetAPIResponseData.getActiveGarageID();


			RestAssured.baseURI = baseURL;
			String path = "exp-ams"+ENV_NAME+"/v1/tenants/"+tenantID+"/garages/"+activeGarageID+"/chargerStates";


			//Request
			RequestSpecification request = RestAssured.given();


			//Add Headers
			request.header("Content-Type", "application/json");
			request.header("authorization", accessToken);


			//Add Query Params
			request.queryParam("requestId", "GET_CHARGING_STATES-"+getUUID());


			//Get Response
			Response response = request.request(Method.GET, path);



			//Convert Response Body to String
			responseBody = response.getBody().asString();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		JSONObject respObj = new JSONObject(responseBody);
		return respObj;
	}


	//Remove Bus from IQ
	public JSONObject deleteIQBus(String busVin) {
		String responseBody = null;
		try {

			GetAPIResponseData.getAuthResponse();
			GetAPIResponseData.getTenantID();
			GetAPIResponseData.getActiveGarageID();

			RestAssured.baseURI = baseURL;
			String path = "exp-ams"+ENV_NAME+"/v1/tenants/"+tenantID+"/garages/"+activeGarageID+"/incomingBuses/"+busVin;

			//Request
			RequestSpecification request = RestAssured.given();


			//Add Headers
			request.header("Content-Type", "application/json");
			request.header("authorization", accessToken);


			//Get Response
			Response response = request.request(Method.DELETE, path);


			//Convert Response Body to String
			responseBody = response.getBody().asString();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		JSONObject respObj = new JSONObject(responseBody);
		return respObj;
	}


	//Get the config details
	public JSONObject getConfigDetailsResponse() {
		String responseBody = null;
		try {

			GetAPIResponseData.getAuthResponse();
			GetAPIResponseData.getTenantID();
			GetAPIResponseData.getActiveGarageID();

			RestAssured.baseURI = baseURL;
			String path = "exp-ccss"+ENV_NAME+"/v1/tenants/"+tenantID+"/garages/"+activeGarageID+"/configurations";

			//Request
			RequestSpecification request = RestAssured.given();


			//Add Headers
			request.header("Content-Type", "application/json");
			request.header("authorization", accessToken);


			//Add Query Params
			request.queryParam("requestId", "GET_CONFIGURATION-"+getUUID());


			//Get Response
			Response response = request.request(Method.GET, path);


			//Convert Response Body to String
			responseBody = response.getBody().asString();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			log.info(e.getStackTrace());
		}
		JSONObject respObj = new JSONObject(responseBody);
		return respObj;
	}
}
