package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle))
		.when()
		.post("Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		System.out.println("Response :" + response);
		
		JsonPath js = ReUsableMethods.rawToJSON(response);
		String id = js.get("ID");
		System.out.println("ID :" + id);
	}
	
	@DataProvider(name ="BooksData")
	public Object[][] getData() {
		return new Object[][] {{"isbn5123","aisle5123"},{"isbn53","aisle53"},{"isbn45","aisle45"}};
	}
}
