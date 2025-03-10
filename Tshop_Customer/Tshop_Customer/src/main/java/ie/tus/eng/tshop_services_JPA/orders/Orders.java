package ie.tus.eng.tshop_services_JPA.orders;


public class Orders {

	private int orderId;
	private String items;
	private int price;
	
	public Orders(int orderId, String items, int price) {
		super();
		this.orderId = orderId;
		this.items = items;
		this.price = price;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", items=" + items + ", price=" + price + "]";
	}

	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
