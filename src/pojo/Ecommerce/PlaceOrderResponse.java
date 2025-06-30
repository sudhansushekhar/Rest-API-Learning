package pojo.Ecommerce;

import java.util.List;

public class PlaceOrderResponse {
	private List<String> orders;
    private List<String> productOrderId;
    private String message;
    
 // Getter and Setter for orders
    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    // Getter and Setter for productOrderId
    public List<String> getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(List<String> productOrderId) {
        this.productOrderId = productOrderId;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
