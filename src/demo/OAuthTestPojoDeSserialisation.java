package demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.ReUsableMethods;


public class OAuthTestPojoDeSserialisation {

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
		
		GetCourse gc =given()
			.param("access_token",accessToken)
		.when()
			.get("oauthapi/getCourseDetails")
		.then()
			.log().all()
			.assertThat().statusCode(401)
		.extract()
			.response().as(GetCourse.class);
			
		System.out.println("LinledIn :" + gc.getLinkedIn());
		System.out.println("Instructor :" + gc.getInstructor());
		System.out.println("API_Courses :" + gc.getCourses().getApi().get(1).getCourseTitle());
		
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++) {
			if(apiCourses.get(i).getCourseTitle().equals("SoapUI Webservices testing")) {
				System.out.println("API_Course_Price :" + apiCourses.get(i).getPrice());
			}
		}
		
		//Print all the courseTitleof WebAutomation
		//Expected Courses
		String[] automationCourses = {"Selenium Webdriver Java","Cypress","Protractor"};
		
		// empty arraylist to add courses
		ArrayList<String> actualWebAutomationCourses = new ArrayList<String>(); 
		
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
		for(int i=0; i<webAutomationCourses.size(); i++) {
			System.out.println("Course " + (i+1)	 +" :"+webAutomationCourses.get(i).getCourseTitle());
			actualWebAutomationCourses.add(webAutomationCourses.get(i).getCourseTitle());
		}
		
		//compare the arrays by converting simple array to arrayist
		List<String> expectedWebAutomationCourses = Arrays.asList(automationCourses);
		
		Assert.assertEquals(actualWebAutomationCourses, expectedWebAutomationCourses);
	}

}
