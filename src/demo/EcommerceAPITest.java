package demo;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.Ecommerce.LoginRequest;
import pojo.Ecommerce.LoginResponse;
import pojo.Ecommerce.OrderDetails;
import pojo.Ecommerce.PlaceOrderRequest;
import pojo.Ecommerce.PlaceOrderResponse;

public class EcommerceAPITest {
	String userID;
	String token;
	String productId;
	
	@Test(priority = 1)
	public void login() {	
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.setContentType(ContentType.JSON).build();
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("sud@gmail.com");
		loginRequest.setUserPassword("Sud@singh1#");
		
		RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);
		
		LoginResponse loginResponse = reqLogin.when()
			.post("api/ecom/auth/login")
		.then()
			.log().all()
			.extract().as(LoginResponse.class);
		
		userID = loginResponse.getUserId();
		token = loginResponse.getToken();
		System.out.println("userID :" +userID);
		System.out.println("token :" +token);
		Assert.assertEquals(loginResponse.getMessage(), "Login Successfully");
	}

	@Test(priority = 2)
	public void addProduct() {
		RequestSpecification addProductReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token).build();
		
		RequestSpecification reqAddingProduct = given().spec(addProductReq)
		.param("productName", "ABC Red Laungrie").param("productAddedBy", userID)
		.param("productCategory", "fashion").param("productSubCategory", "Lingrie")
		.param("productPrice", 120).param("productDescription", "Lingrie Originals")
		.param("productFor", "women")
		.multiPart("productImage", (new File("Camiset.jpg")));
		
		String addedProduct = reqAddingProduct.when()
			.post("api/ecom/product/add-product")
		.then()
			.extract().asString();
		
		JsonPath jsonPath = new JsonPath(addedProduct);
		productId = jsonPath.get("productId");
		System.out.println("productId:" + productId);
		Assert.assertEquals(jsonPath.get("message"), "Product Added Successfully");
		
	}
	
	@Test(priority = 3)
	public void placeOrder() {
		OrderDetails orderDetails1 = new OrderDetails(); // use this to add multiple products by creating multiple objects
		orderDetails1.setCountry("India");
		orderDetails1.setProductOrderedId(productId);
//		OrderDetails orderDetails2 = new OrderDetails(); // order two
//		orderDetails2.setCountry("India");
//		orderDetails2.setProductOrderedId(productId);
		
		// to add multiple product
		List<OrderDetails> orderDetailList = new ArrayList<OrderDetails>();
		orderDetailList.add(orderDetails1);
//		orderDetailList.add(orderDetails2);
		
		
		PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
		placeOrderRequest.setOrders(orderDetailList);
		RequestSpecification placeOrderReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com/")
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", token).build();
		
		// creating body
		RequestSpecification reqPlacingOrder = given().log().all().spec(placeOrderReq)
		.body(placeOrderRequest);
		
		// send request
		PlaceOrderResponse placeOrderResponse = reqPlacingOrder.when()
			.post("api/ecom/order/create-order")
		.then()
			.log().all()
			.extract().as(PlaceOrderResponse.class);
		
		// verification 
		System.out.println("orderId:" + placeOrderResponse.getProductOrderId());
		Assert.assertEquals(placeOrderResponse.getProductOrderId().get(0), productId);
		Assert.assertEquals(placeOrderResponse.getMessage(), "Order Placed Successfully");
		
	}
	@Test(priority = 4)
	public void deleteProduct() {
		RequestSpecification deleteProductReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("Authorization", token).build();
		
		RequestSpecification reqDeleteProduct = given().log().all().spec(deleteProductReq)
				.pathParam("productID", productId);
		
		String deletedProductResounse = reqDeleteProduct.when()
				.delete("api/ecom/product/delete-product/{productID}")
			.then()
				.log().all()
				.extract().asString();
		
		JsonPath jsonPath1 = new JsonPath(deletedProductResounse);
		System.out.println("message :" + jsonPath1.get("message"));
		Assert.assertEquals(jsonPath1.get("message"), "Product Deleted Successfully");
	}
}
