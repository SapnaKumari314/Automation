package test.java.com.proterra.testcases.apexAPI.loginAPI;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.com.proterra.APIPojo.LoginAPI.LoginRequestPojo;
import main.java.com.proterra.AssertManager.SoftAssertLogger;
import test.java.com.proterra.testcases.apexAPI.APIBaseTest;
import test.java.com.proterra.testcases.apexAPI.Modules;

public class LoginAPITest extends APIBaseTest implements Modules{

	//Initiate Logger
	public static Logger log = Logger.getLogger(LoginAPITest.class.getName());
	public static SoftAssertLogger sAssert;


	//Initiate Objects
	public static LoginRequestPojo loginPojo;

	/*-----------------------------------------------------Test Cases--------------------------------------------------------------*/




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

		String body= String.valueOf(pojoToJsonObject(loginPojo));
		request.body(body);

		//Get Response
		Response response = request.request(Method.POST, path);

		ACCESS_TOKEN =  response.getBody().asString();
		
		return ACCESS_TOKEN;
	}

	/*-----------------------------------------------------Setup and tear down--------------------------------------------------------------*/

	@BeforeMethod
	public void beforeTestCase() {
		loginPojo = new LoginRequestPojo();
	}

}
