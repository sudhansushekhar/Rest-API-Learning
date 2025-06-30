package demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import files.ReUsableMethods;

public class JiraBugTest {

	public static void main(String[] args) {
		String authorization = "Basic c3VkZ3VkN0BnbWFpbC5jb206QVRBVFQzeEZmR0YwLVk3NGc2Nzk1WF9sY0JiakxaZ2RJc2JQd19IMGp6ZnhGa0FCZWtQVFpRZVAzWWJsM2Nsd2FNTVNxcGoyYnhUODVZVXRiZnpncEF0bGdWY2t1a2hYSWZxSDM3clNWX2xxbTNwTzdkOGdScElyNF9fX1FlcTJLanBVRU1pUXdsa2VoWFNHNkVIU2Y2anl3MjdsRU4zZmVPU1NxV21HbXJDdklSaFJ1SGQ3MUswPUI3MTc3NENB";
		String payload = "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"JL\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"Unable to click on button Continue in field password\",\r\n"
				+ "       \"description\": \"Creating of an issue using the REST API\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}\r\n"
				+ "";

		RestAssured.baseURI = "https://jiraconcepts.atlassian.net/";
		String resposne = given()
				.header("Content-Type", "application/json")
				.header("Authorization", authorization)
				.body(payload).log().all()
			.when()
				.post("rest/api/2/issue")
			.then()
				.log().all()
				.assertThat().statusCode(201)
			.extract()
				.response().asString();

		JsonPath js = ReUsableMethods.rawToJSON(resposne);
		String issueId = js.getString("id");
		System.out.println("bugId :" + issueId);
		
		
		//-----------Add Attachment------//
		System.out.println("-----------Add Attachment to  bug----------");
		resposne = given()
				.pathParam("issueId", issueId)
				.header("X-Atlassian-Token", "no-check")
				.header("Authorization", authorization)
				.multiPart("file", new File("CRM.png"))	// file path
				.log().all()
			.when()
				.post("rest/api/2/issue/{issueId}/attachments") //.post("rest/api/2/issue/"+bugId+"/attachments") or can directly pass variable 'issueId'
			.then()
				.log().all()
				.assertThat().statusCode(200)
			.extract()
				.response().asString();
		
		//-------GET bug details-------//
		System.out.println("-----------GET bug details----------");
		resposne = given()
				.pathParam("issueId", issueId)
				.header("Content-Type", "application/json")
				.header("Authorization", authorization)
				.log().all()
			.when()
				.get("rest/api/2/issue/{issueId}")
			.then()
				.log().all()
				.assertThat().statusCode(200)
			.extract()
				.response().asString();
	}

}
