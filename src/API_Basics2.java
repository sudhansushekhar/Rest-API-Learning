import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import files.Payload;
import files.ReUsableMethods;

public class API_Basics2 {
	public static void main(String[] args) throws IOException {
		// Validate if Add Place is working as expected
		/*
		 * It works on three ways 1. Given - preconditions, all input details 2. When -
		 * Action or Request type (resource and http methods) 3. Then - Validation
		 */
		// Passing json file into body -> content of file to string -> file content into byte-> byte into string
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		System.out.println("------------Adding Record------------");
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("E:\\eclipse-workspace\\API_learning\\addPlace.json")))).when().post("maps/api/place/add/json").then().log().all().assertThat()
				.statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract()
				.response().asString();

		// for extracting a particula value, create jsonpath for converting string into json
		JsonPath js = ReUsableMethods.rawToJSON(response);
		
		String placeId = js.getString("place_id");
		
	}

}
