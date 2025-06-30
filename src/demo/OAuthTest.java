package demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import files.ReUsableMethods;


public class OAuthTest {

	@Test
	public void getBookDetails() {
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		
		//--------Get accessToken with this request-----------//
		System.out.println("-----------Get accessToken with this request----------");
		String resposne = given()
			.formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
			.formParams("grant_type", "client_credentials")
			.formParams("scope", "trust")
		.when()
			.post("oauthapi/oauth2/resourceOwner/token")
		.then()
			.log().all()
			.assertThat().statusCode(200)
		.extract()
			.response().asString();
		
		JsonPath js = ReUsableMethods.rawToJSON(resposne);
		String accessToken = js.getString("access_token");
		System.out.println("accessToken :" + accessToken);
		
		//--------Pass accessToken to make request-----------//
		System.out.println("-----------Pass accessToken to make request----------");
		
		given()
			.param("access_token",accessToken)
		.when()
			.get("oauthapi/getCourseDetails")
		.then()
			.log().all()
			.assertThat().statusCode(401)
		.extract()
			.response().asString();
			
	}

}
