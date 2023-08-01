package tests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class GKR_code_delivery {


	String dtauthloginkey;
	String dtauthloginkey2;
	String sessionId;
	String propertyId;

	@BeforeClass
	public void setUp() {
		// Base URI
		RestAssured.baseURI = "https://api-qa.dthreaddev.com";
	}

	@Test(priority=1)
	public void testAuth() {
		String requestBody = "{ \"email\": \"v28s@yopmail.com\", \"password\": \"Pass@123\" }";

		Response response =
				given()
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/api/sessions/auth")
				.then()
				.statusCode(200) // Validate the status code is 200 (OK)
				.extract().response();



		dtauthloginkey = response.jsonPath().getString("data.userData.id");
		dtauthloginkey2 = response.jsonPath().getString("data.userData.organizationId");

		Assert.assertFalse(dtauthloginkey.isEmpty(), "ID is empty or null");
		Assert.assertFalse(dtauthloginkey2.isEmpty(), "Organization ID is empty or null");

	}

	@Test(priority=2)
	public void testVerify2FA() {


		// Request Body
		String requestBody = "{ \"code\": \"QWERTY\" }";

		// Send POST request and get the response
		Response response =
				given()
				.accept(ContentType.JSON)
				.header("dt-auth-login-key", dtauthloginkey)
				.header("dt-auth-login-key-2", dtauthloginkey2)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/api/sessions/verify-2fa")
				.then()
				.statusCode(200)
				.extract().response();

		sessionId = response.jsonPath().getString("data.sessionToken");
		//System.out.println(sessionId);

		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");
	}

	@Test(priority=3)
	public void testProperties() {

		Response response =
				given()
				.accept(ContentType.JSON)
				//.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId)
				//.body(requestBody)
				.when()
				.get("/api/properties/")
				.then()
				.statusCode(200) // Validate the status code is 200 (OK)
				.extract().response();

		//System.out.println(response.getBody().asString());

		propertyId = response.jsonPath().getString("data[0].Property.id");
		Assert.assertFalse(propertyId.isEmpty(), "propertyId is empty or null");
		System.out.println(propertyId);

	}
	

//	@Test(priority=4)
//	public void testAddsmartthings() {
//
//
//		// Request Body
//		
//		String requestBody = "{ \"cloudEmailId\": \"st-s7@dthreadev.com\", \"propertyId\": \"9c880871-96fd-416a-927f-de32617116a7\", \"pat\": \"59af3838-4d59-4a86-9974-2c51119c86c0\", \"tokenname\": \"\" }";
//
//
//		// Send POST request and get the response
//		Response response =
//				given()
//				.accept(ContentType.JSON)
//				//.header("dt-auth-login-key", dtauthloginkey)
//				//.header("dt-auth-login-key-2", dtauthloginkey2)
//				//.contentType(ContentType.JSON)
//				.header("Authorization", "Bearer " + sessionId)
//				.body(requestBody)
//				.when()
//				.post("/api/smartthings")
//				.then()
//				.statusCode(200)
//				.extract().response();
//
//		//sessionId = response.jsonPath().getString("data.sessionToken");
//		System.out.println(response.getBody().asString());
//
//		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");
//	}

}
