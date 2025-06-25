import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;
import files.ReUsableMethods;

public class API_Basics {
	public static void main(String[] args) {
		// Validate if Add Place is working as expected
		/*
		 * It works on three ways 1. Given - preconditions, all input details 2. When -
		 * Action or Request type (resource and http methods) 3. Then - Validation
		 */

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		System.out.println("------------Adding Record------------");
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(Payload.AddPalce()).when().post("maps/api/place/add/json").then().log().all().assertThat()
				.statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract()
				.response().asString();

		// for extracting a particula value, create jsonpath for converting string into json
		JsonPath js = ReUsableMethods.rawToJSON(response);
		
		String placeId = js.getString("place_id");
		System.out.println("-----------Updating Record-------------");

		// Since we have place Id, now we'll pass that value to update, get updated record and delete it
		// Update the address
		String newAddress = "17, SP Colony, New Delhi";
		String updateBody = "{\r\n"
				+ "\"place_id\":\"" + placeId + "\",\r\n"
				+ "\"address\":\"" + newAddress + "\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(updateBody).when().put("maps/api/place/update/json").then().log().all().assertThat()
		.statusCode(200).body("msg", equalTo("Address successfully updated")).extract()
		.response().asString();
		
		System.out.println("-------------Getting Updated Record------------");
		// GET request to fetch the updated content
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.body(updateBody).when().get("maps/api/place/get/json").then().log().all().assertThat()
		.statusCode(200).body("address", equalTo(newAddress));
		
		// DELETE record
		System.out.println("-------------Deleting Record-------------");
		String deleteBody = "{\r\n"
				+ "    \"place_id\":\""+placeId+"\"\r\n"
				+ "}";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(deleteBody).when().delete("maps/api/place/delete/json").then().log().all().assertThat()
		.statusCode(200).body("status", equalTo("OK"));
		
		System.out.println("-----------Verifying Deleted Record----------");
		// GET request to fetch the updated content
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json").then().log().all().assertThat()
		.statusCode(404).body("msg", equalTo("Get operation failed, looks like place_id  doesn't exists"));
	}

}
