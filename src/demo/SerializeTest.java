package demo;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import pojo.serialize.AddPlaceRequest;
import pojo.serialize.AddPlaceResponse;
import pojo.serialize.GetPlaceResponse;
import pojo.serialize.LocationRequest;

import static io.restassured.RestAssured.*;

public class SerializeTest {

	@Test
	public void serialise() {
		AddPlaceRequest  ap = new AddPlaceRequest ();
		ap.setAccuracy(30);
		ap.setName("Lakshita Singh3");
		ap.setPhone_number("(+91) 111 111 1000");
		ap.setAddress("26, Extension1, new Delhi");
		ap.setLanguage("Hindi-IN");
		ap.setWebsite("http://lakshita.com");

		//to set types, first create list and add to type
		List<String> myList = new ArrayList<String>();
		myList.add("linked park");
		myList.add("shoping");

		ap.setTypes(myList); // added myList to types because Types is a list

		// To set location we need to create object to access location methods
		LocationRequest  location = new LocationRequest();
		location.setLat(-20.383467);
		location.setLng(-40.383437);

		ap.setLocation(location); // location is added to setLocation

		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		AddPlaceResponse addPlaceResponse = given()
				.queryParam("key", "qaclick123")
				.body(ap)
			.when()
				.post("maps/api/place/add/json")
			.then()
				.log().all()
			.assertThat()
				.statusCode(200)
				.body("scope", equalTo("APP"))
				.header("Server", "Apache/2.4.52 (Ubuntu)")
			.extract()
				.response().as(AddPlaceResponse.class); // deserialized JSON to POJO

		System.out.println("Place ID: " + addPlaceResponse.getPlace_id());
		String placeId= addPlaceResponse.getPlace_id();
		Assert.assertEquals(addPlaceResponse.getStatus(), "OK");
		
		// Get details of created address
		GetPlaceResponse getResp = given()
			.queryParam("key", "qaclick123")
			.queryParam("place_id", placeId)
		.when()
			.get("maps/api/place/get/json")
		.then()
			.log().all()
			.assertThat()
			.statusCode(200)
		.extract()
			.response().as(GetPlaceResponse.class); // deserialized JSON to POJO
		
		// verify the response after adding the place and getting the place details
		String name = ap.getName();
		System.out.println("Name :"+name);
		Assert.assertEquals(getResp.getName(), name);
	}
}
