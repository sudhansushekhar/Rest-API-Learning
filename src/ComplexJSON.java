import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJSON {
	/*
	 * Find 1. Print No of courses returned by API 2. Print Purchase Amount 3. Print
	 * Title of the first course 4. Print All course titles and their respective
	 * Prices 5. Print no of copies sold by RPA Course 6. Verify if Sum of all
	 * Course prices matches with Purchase Amount
	 */
	public static void main(String[] args) {
		JsonPath js = new JsonPath(Payload.price());
		// 1. Print No of courses returned by API
		int courseCount = js.getInt("courses.size()");
		System.out.println("courseCount :" + courseCount);

		// 2. Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("purchaseAmount :" + purchaseAmount);

		// 3. Print Title of the first course
		String firstCourseTitle = js.getString("courses[0].title");
		System.out.println("firstCourseTitle :" + firstCourseTitle);

		// 4. Print All course titles and their respective Prices
		for (int i = 0; i < courseCount; i++) {
			String courseTitle = js.get("courses[" + i + "].title");
			String coursePrice = js.get("courses[" + i + "].price").toString();
			System.out.println("courseTitle & price:" + courseTitle + " - " + coursePrice);
		}

		// 5. Print no of copies sold by RPA Course
		for (int i = 0; i < courseCount; i++) {
			String courseTitle = js.get("courses[" + i + "].title");
			if (courseTitle.equalsIgnoreCase("RPA")) {
				int copiesSold = js.getInt("courses[" + i + "].copies");
				System.out.println("copiesSold:" + copiesSold);
				break;
			}
		}

		// 6. Verify if Sum of all Course prices matches with Purchase Amount
		int sum = 0;
		for (int i = 0; i < courseCount; i++) {
			int coursePrice = js.getInt("courses[" + i + "].price");
			int copiesSold = js.getInt("courses[" + i + "].copies");
			sum = sum + coursePrice * copiesSold;
		}

		System.out.println("sum of prices:" + sum);
		Assert.assertEquals(purchaseAmount, sum);

	}

}
