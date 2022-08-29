package test.java.com.proterra.testcases.ccssAPI.chargerSessionsAPI;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

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

public class ChargerSessionsAPITest extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(ChargerSessionsAPITest.class.getName());
	public static SoftAssertLogger sAssert;
	public static LoginAPITest loginTest;
	public static LoadGaragesAPITest garageTest;



	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(priority = 0, description = "TOUC-3271: (CCSS.API): Charger Sessions: Verify chargerSessions API with valid data and response")
	public void CCSS_API_ChargerSessionsValidData() {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Set the module
		MODULE = Modules.MODULE_DATALAKE;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSESSIONS();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", ACCESS_TOKEN);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGER_HISTORY-"+generateUUID());
		request.queryParam("ocppIds", "NAMHVC150-US1-1619-975,HVC150-IT1-1119-045,NAMHVC150-US1-1619-974,00120C2DF6B03F01,HVC150-IT1-1019-119");
		request.queryParam("start", Instant.now().minus(1,ChronoUnit.DAYS).with(ChronoField.NANO_OF_SECOND,0));
		request.queryParam("end", Instant.now().with(ChronoField.NANO_OF_SECOND,0));

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();


		//Validate the Response
		sAssert.assertEquals(status, 200, "Charger Sessions API - Status Code for Valid user");
		sAssert.assertAll();


	}



	@Test(priority = 1, description = "TOUC-3272: (CCSS.API): Charger Sessions: Verify chargerSessions API with invalid authorization key")
	public void CCSS_API_ChargerSessionsInValidAuthorizationKey() {

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Set the module
		MODULE = Modules.MODULE_DATALAKE;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSESSIONS();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", "dsjgsdjgajlgagalgldjl");

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGER_HISTORY-"+generateUUID());
		request.queryParam("ocppIds", "NAMHVC150-US1-1619-975,HVC150-IT1-1119-045,NAMHVC150-US1-1619-974,00120C2DF6B03F01,HVC150-IT1-1019-119");
		request.queryParam("start", Instant.now().minus(1,ChronoUnit.DAYS).with(ChronoField.NANO_OF_SECOND,0));
		request.queryParam("end", Instant.now().with(ChronoField.NANO_OF_SECOND,0));

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("message");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Charger Sessions API - Status Code for invalid authorization key");
		sAssert.assertEquals(message, "Unauthorized", "Charger Sessions API - Response Message for invalid authorization key");
		sAssert.assertAll();

	}


	@Test(priority = 2, description = "TOUC-3273: (CCSS.API): Charger Sessions: Verify chargerSessions API with expired authorization key")
	public void CCSS_API_ChargerSessionsExpiredAuthorizationKey() {

		//Generate Access Token
		String expiredToken = "eyJraWQiOiJxdWxRc2RFWmw2ckZydGdCajExY0NLRktkdUUzVjRjWGRlS0N0TzBnbTJ3PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIyYmQzNTI5OS04ZDFhLTRlNWMtOWMwMC0xN2UxZTE1ZGQ3M2UiLCJ6b25laW5mbyI6IjJjYmJmZDBhLTBmZjctNDA2Mi04OWRhLWJmMmZhNjM4MDdiNyIsImN1c3RvbTpmZWRVc2VyUm9sZSI6InRlbmFudF9yZWFkb25seSIsImN1c3RvbTp1c2VyTmFtZSI6InByYWZ1bC5wcm90ZXJyYUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMV9UTnJ5UHY5ZFEiLCJjdXN0b206YXNzaWduZWRSb2xlIjoibmEiLCJjdXN0b206dGVuYW50SWQiOiIyY2JiZmQwYS0wZmY3LTQwNjItODlkYS1iZjJmYTYzODA3YjciLCJjdXN0b206dXNlclJvbGUiOiJFTkRfVVNFUiIsImN1c3RvbTpsYXN0TW9kaWZpZWRUaW1lIjoiMTU4Njg0MTY0MzcwMSIsImF1dGhfdGltZSI6MTU5NTkxNTg2MiwiZXhwIjoxNTk2MDA1NTQ5LCJpYXQiOjE1OTYwMDE5NDksImVtYWlsIjoicHJhZnVsLnByb3RlcnJhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjdXN0b206aW50ZXJuYWxVc2VyIjoiMCIsImN1c3RvbTpsYXN0TmFtZSI6IlAiLCJjdXN0b206cm9sZUluZm9ybWF0aW9uIjoidGVuYW50X3JlYWRvbmx5IiwiY29nbml0bzp1c2VybmFtZSI6IjJiZDM1Mjk5LThkMWEtNGU1Yy05YzAwLTE3ZTFlMTVkZDczZSIsImN1c3RvbTp1c2VySWQiOiJlNTYzYzBiNy1kNTMzLTQxNDAtYjA3MS1lM2JhNjRjNjU3OWYiLCJhdWQiOiJnbHZoZzBocWhxcWJkZnVhcnZmc3VnNHJ0IiwiY3VzdG9tOmNyZWF0ZWRUaW1lIjoiMTU4Njg0MTY0MzcwMSIsImV2ZW50X2lkIjoiMWNmMDliMjUtZWMwMy00YmMxLTk1NjQtNTJjYTM4Njg2ODc3IiwiY3VzdG9tOmZpcnN0TmFtZSI6IlByYWZ1bCIsInRva2VuX3VzZSI6ImlkIiwiY3VzdG9tOmFkZHJlc3NMaW5lMSI6IjE1NTIwIEVsbGVyc2xpZSBSZCBTVywgRWRtb250b24sIEFCIFQ2VyAxQTQsIENhbmFkYSIsImN1c3RvbTphZGRyZXNzSWQiOiJlYzc0OThkMy05ZWFiLTRlNzgtYmI4Mi1mMzZhM2QxYThkNzAifQ.Dthe8sXsQpJkXPbJRz-pcTqdGklNCofIEIJYgwxT-3F3mJijaYmExouJ-QS2pUigQArzqIjw4SXOOm0AE64uaQm-pd2iyvmhl4gYs3ugHgkEVDEjeM7e85GvTOV2fMScHAdJ8sx9eubUglo_OFHBoE1XhtpDSmK6OqdyldRSZ599EOx10UsdM8QXapUPf_Nim9eZZ4IAiEzOURNyaO1_FvCR9xwse37BxK9uvl7qVzP_kx2InsnGlL9kpBq7Tbir3xTHLHpbGIc9KdWnYqJhnGvt3h93C8-cTMxBnzuzygzexJ7SiMOdci06RclDQveYNhazw_HKuWvcvbCJS1Z23g";

		//Assertions
		sAssert = new SoftAssertLogger();


		//Generate Access Token
		loginTest.generateAccessToken();

		//Set the module
		MODULE = Modules.MODULE_DATALAKE;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_CHARGERSESSIONS();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");
		request.header("authorization", expiredToken);

		//Add Query Params
		request.queryParam("requestId", "GET_CHARGER_HISTORY-"+generateUUID());
		request.queryParam("ocppIds", "NAMHVC150-US1-1619-975,HVC150-IT1-1119-045,NAMHVC150-US1-1619-974,00120C2DF6B03F01,HVC150-IT1-1019-119");
		request.queryParam("start", Instant.now().minus(1,ChronoUnit.DAYS).with(ChronoField.NANO_OF_SECOND,0));
		request.queryParam("end", Instant.now().with(ChronoField.NANO_OF_SECOND,0));

		//Get Response
		Response response = request.request(Method.GET, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("message");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Charger Sessions API - Status Code for expired authorization token");
		sAssert.assertEquals(message, "The incoming token has expired", "Charger Sessions API - Response Message for expired authorization token");
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
