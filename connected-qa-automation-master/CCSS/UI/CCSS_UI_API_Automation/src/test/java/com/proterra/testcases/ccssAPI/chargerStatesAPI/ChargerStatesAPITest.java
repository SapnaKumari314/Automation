package test.java.com.proterra.testcases.ccssAPI.chargerStatesAPI;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import test.java.com.proterra.testcases.ccssAPI.APIBaseTest;
import test.java.com.proterra.testcases.ccssAPI.Modules;
import test.java.com.proterra.testcases.ccssAPI.loadGaragesAPI.LoadGaragesAPITest;
import test.java.com.proterra.testcases.ccssAPI.loginAPI.LoginAPITest;

public class ChargerStatesAPITest extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(ChargerStatesAPITest.class.getName());
	public static SoftAssertLogger sAssert;
	public static LoginAPITest loginTest;
	public static LoadGaragesAPITest garageTest;



	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(priority = 0, description = "TOUC-3267: (CCSS.API): Charger States: Verify chargerStates API with valid data and response")
	public void CCSS_API_ChargerStatesValidData() {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Get the Garage ID
		garageTest.getGarageIDs(GARAGE_NAME);

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSTATES(TENANT_ID);

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", ACCESS_TOKEN);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGING_STATES-"+generateUUID());

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();

		String body = response.getBody().asString();
		boolean bool = false;
		if (body.isBlank() || body.isEmpty()) {
			bool = true;
		}

		//Validate the Response
		sAssert.assertEquals(status, 200, "Charger States API - Status Code for Valid user");
		sAssert.assertEquals(bool, false, "Charger States API - Response Body is not empty for Valid user");
		sAssert.assertAll();


	}


	@Test(priority = 1, description = "TOUC-3268: (CCSS.API): Charger States: Verify chargerStates API with invalid tenantID")
	public void CCSS_API_ChargerStatesInValidTenant() {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Get the Garage ID
		garageTest.getGarageIDs(GARAGE_NAME);

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSTATES(generateUUID());

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", ACCESS_TOKEN);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGING_STATES-"+generateUUID());

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();

		String body = response.getBody().asString();
		boolean bool = false;
		if (body.isBlank() || body.isEmpty()) {
			bool = true;
		}

		//Validate the Response
		sAssert.assertEquals(status, 401, "Charger States API - Status Code for Invalid tenant id");
		//TODO: To be updated with proper response after backend implementation
		sAssert.assertEquals(bool, true, "Charger States API - Response Body is empty for Invalid tenant id");
		sAssert.assertAll();

	}


	@Test(priority = 2, description = "TOUC-3269: (CCSS.API): Charger States: Verify chargerStates API with invalid authorization key")
	public void CCSS_API_ChargerStatesInValidAuthorizationKey() {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Get the Garage ID
		garageTest.getGarageIDs(GARAGE_NAME);

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSTATES(TENANT_ID);

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", "sdgjgsdjlghalghgksgk");

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGING_STATES-"+generateUUID());

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("message");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Charger States API - Status Code for invalid authorization key");
		sAssert.assertEquals(message, "Unauthorized", "Charger States API - Response Message for invalid authorization key");
		sAssert.assertAll();

	}


	@Test(priority = 3, description = "TOUC-3270: (CCSS.API): Charger States: Verify chargerStates API with expired authorization key")
	public void CCSS_API_ChargerStatesExpiredAuthorizationKey() {

		//Generate Access Token
		String expiredToken = "eyJraWQiOiJxdWxRc2RFWmw2ckZydGdCajExY0NLRktkdUUzVjRjWGRlS0N0TzBnbTJ3PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIyYmQzNTI5OS04ZDFhLTRlNWMtOWMwMC0xN2UxZTE1ZGQ3M2UiLCJ6b25laW5mbyI6IjJjYmJmZDBhLTBmZjctNDA2Mi04OWRhLWJmMmZhNjM4MDdiNyIsImN1c3RvbTpmZWRVc2VyUm9sZSI6InRlbmFudF9yZWFkb25seSIsImN1c3RvbTp1c2VyTmFtZSI6InByYWZ1bC5wcm90ZXJyYUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMV9UTnJ5UHY5ZFEiLCJjdXN0b206YXNzaWduZWRSb2xlIjoibmEiLCJjdXN0b206dGVuYW50SWQiOiIyY2JiZmQwYS0wZmY3LTQwNjItODlkYS1iZjJmYTYzODA3YjciLCJjdXN0b206dXNlclJvbGUiOiJFTkRfVVNFUiIsImN1c3RvbTpsYXN0TW9kaWZpZWRUaW1lIjoiMTU4Njg0MTY0MzcwMSIsImF1dGhfdGltZSI6MTU5NTkxNTg2MiwiZXhwIjoxNTk2MDA1NTQ5LCJpYXQiOjE1OTYwMDE5NDksImVtYWlsIjoicHJhZnVsLnByb3RlcnJhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjdXN0b206aW50ZXJuYWxVc2VyIjoiMCIsImN1c3RvbTpsYXN0TmFtZSI6IlAiLCJjdXN0b206cm9sZUluZm9ybWF0aW9uIjoidGVuYW50X3JlYWRvbmx5IiwiY29nbml0bzp1c2VybmFtZSI6IjJiZDM1Mjk5LThkMWEtNGU1Yy05YzAwLTE3ZTFlMTVkZDczZSIsImN1c3RvbTp1c2VySWQiOiJlNTYzYzBiNy1kNTMzLTQxNDAtYjA3MS1lM2JhNjRjNjU3OWYiLCJhdWQiOiJnbHZoZzBocWhxcWJkZnVhcnZmc3VnNHJ0IiwiY3VzdG9tOmNyZWF0ZWRUaW1lIjoiMTU4Njg0MTY0MzcwMSIsImV2ZW50X2lkIjoiMWNmMDliMjUtZWMwMy00YmMxLTk1NjQtNTJjYTM4Njg2ODc3IiwiY3VzdG9tOmZpcnN0TmFtZSI6IlByYWZ1bCIsInRva2VuX3VzZSI6ImlkIiwiY3VzdG9tOmFkZHJlc3NMaW5lMSI6IjE1NTIwIEVsbGVyc2xpZSBSZCBTVywgRWRtb250b24sIEFCIFQ2VyAxQTQsIENhbmFkYSIsImN1c3RvbTphZGRyZXNzSWQiOiJlYzc0OThkMy05ZWFiLTRlNzgtYmI4Mi1mMzZhM2QxYThkNzAifQ.Dthe8sXsQpJkXPbJRz-pcTqdGklNCofIEIJYgwxT-3F3mJijaYmExouJ-QS2pUigQArzqIjw4SXOOm0AE64uaQm-pd2iyvmhl4gYs3ugHgkEVDEjeM7e85GvTOV2fMScHAdJ8sx9eubUglo_OFHBoE1XhtpDSmK6OqdyldRSZ599EOx10UsdM8QXapUPf_Nim9eZZ4IAiEzOURNyaO1_FvCR9xwse37BxK9uvl7qVzP_kx2InsnGlL9kpBq7Tbir3xTHLHpbGIc9KdWnYqJhnGvt3h93C8-cTMxBnzuzygzexJ7SiMOdci06RclDQveYNhazw_HKuWvcvbCJS1Z23g";

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Get the Garage ID
		garageTest.getGarageIDs(GARAGE_NAME);

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSTATES(TENANT_ID);

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", expiredToken);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGING_STATES-"+generateUUID());

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("message");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Charger States API - Status Code for expired authorization token");
		sAssert.assertEquals(message, "The incoming token has expired", "Charger States API - Response Message for expired authorization token");
		sAssert.assertAll();

	}



	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {

		loginTest = new LoginAPITest();
		garageTest =  new LoadGaragesAPITest();
		getTenantID();
	}

}
