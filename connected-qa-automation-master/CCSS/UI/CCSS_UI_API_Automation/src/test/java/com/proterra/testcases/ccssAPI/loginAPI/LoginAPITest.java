package test.java.com.proterra.testcases.ccssAPI.loginAPI;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.APIPojo.LoginPojo;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import test.java.com.proterra.testcases.ccssAPI.APIBaseTest;
import test.java.com.proterra.testcases.ccssAPI.Modules;

public class LoginAPITest extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(LoginAPITest.class.getName());
	public static SoftAssertLogger sAssert;


	//Initiate Objects
	public static LoginPojo loginPojo;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/


	@Test(priority = 0, description = "TOUC-3083: (CCSS.API): Login: Verify login API with valid data and response")
	public void CCSS_API_LoginValidUser() {

		//Assertions
		sAssert = new SoftAssertLogger();

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_LOGIN();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");


		//Add Payload
		loginPojo.setUserName(USER_NAME);
		loginPojo.setUserPassword(PASSWORD);

		String body= String.valueOf(pojoToJonObject(loginPojo));
		request.body(body);

		//Get Response
		Response response = request.request(Method.POST, path);
		int status = response.statusCode();

		//Validate the Response
		sAssert.assertEquals(status, 200, "Login API - Valid user details");
		sAssert.assertAll();

		ACCESS_TOKEN =  response.getBody().asString();
	}


	@Test(priority = 1, description = "TOUC-3084: (CCSS.API): Login: Verify login API with invalid user")
	public void CCSS_API_LoginInValidUserID() {

		//Assertions
		sAssert = new SoftAssertLogger();

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_LOGIN();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");


		//Add Payload
		loginPojo.setUserName("test@test");
		loginPojo.setUserPassword(PASSWORD);

		String body= String.valueOf(pojoToJonObject(loginPojo));
		request.body(body);

		//Get Response
		Response response = request.request(Method.POST, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("msg");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Login API - Status code for invalid user details");
		sAssert.assertEquals(message, "User doesnot exist or Password doesn't match", "Login API - Response message for invalid user details");
		sAssert.assertAll();

		reportStep(Status.PASS, message);
	}


	@Test(priority = 2, description = "TOUC-3085: (CCSS.API): Login: Verify login API with invalid password")
	public void CCSS_API_LoginInValidPassword() {

		//Assertions
		sAssert = new SoftAssertLogger();

		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_LOGIN();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");


		//Add Payload
		loginPojo.setUserName(USER_NAME);
		loginPojo.setUserPassword("safgkaj");

		String body= String.valueOf(pojoToJonObject(loginPojo));
		request.body(body);

		//Get Response
		Response response = request.request(Method.POST, path);
		int status = response.statusCode();
		String message = responseToJson(response).getString("msg");

		//Validate the Response
		sAssert.assertEquals(status, 401, "Login API - Status code for invalid password");
		sAssert.assertEquals(message, "User doesnot exist or Password doesn't match", "Login API - Response message for invalid password");
		sAssert.assertAll();

		reportStep(Status.PASS, message);
	}


	//Generate Access Token
	public String generateAccessToken() {
		//Set the module
		MODULE = Modules.MODULE_AMS;

		//Set base
		String base = getBASE_PATH();

		//Set the Base URI
		RestAssured.baseURI = base;

		//Set path
		String path = getPATH_LOGIN();

		//Create the Request object
		RequestSpecification request = RestAssured.given();

		//Add Headers
		request.header("Content-Type", "application/json");

		//Add Payload
		loginPojo.setUserName(USER_NAME);
		loginPojo.setUserPassword(PASSWORD);

		String body= String.valueOf(pojoToJonObject(loginPojo));
		request.body(body);

		//Get Response
		Response response = request.request(Method.POST, path);

		ACCESS_TOKEN =  response.getBody().asString();
		
		return ACCESS_TOKEN;
	}

	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {
		loginPojo = new LoginPojo();
	}

}
