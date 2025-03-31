package ie.tus.eng.tshop_services_JPA.customer.model;

import java.util.List;

import ie.tus.eng.tshop_services_JPA.orders.Orders;
public class CustomerResponse {

	private int custId;
	private String custName;
	private String custBod;
	private String custPhone;
	private List<Orders> orders; // list that holds orders objects
	
	public CustomerResponse(Customers customers, List<Orders> orders) {
	    this.custId = customers.getCustId();
	    this.custName = customers.getCustName();
	    this.custBod = customers.getCustBod();
	    this.custPhone = customers.getCustPhone();
	    this.orders = orders;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustBod() {
		return custBod;
	}

	public void setCustBod(String custBod) {
		this.custBod = custBod;
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "CustomerResponse [custId=" + custId + ", custName=" + custName + ", custBod=" + custBod + ", custPhone="
				+ custPhone + ", orders=" + orders + "]";
	}

	public CustomerResponse(int custId, String custName, String custBod, String custPhone, List<Orders> orders) {
		super();
		this.custId = custId;
		this.custName = custName;
		this.custBod = custBod;
		this.custPhone = custPhone;
		this.orders = orders;
	}

	public CustomerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


	
}
