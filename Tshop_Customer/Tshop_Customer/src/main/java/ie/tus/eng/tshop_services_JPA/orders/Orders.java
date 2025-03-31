package ie.tus.eng.tshop_services_JPA.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Orders {

	
	@Id
	private int orderId;
	private String items;
	private int price;
	@Column(name = "custid")
	private int custId;
	
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
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", items=" + items + ", price=" + price + ", custId=" + custId + "]";
	}
	public Orders(int orderId, String items, int price, int custId) {
		super();
		this.orderId = orderId;
		this.items = items;
		this.price = price;
		this.custId = custId;
	}
	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	
}
