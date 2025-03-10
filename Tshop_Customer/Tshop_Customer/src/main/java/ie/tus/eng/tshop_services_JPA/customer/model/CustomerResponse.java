package ie.tus.eng.tshop_services_JPA.customer.model;

import ie.tus.eng.tshop_services_JPA.orders.Orders;

public class CustomerResponse {

	private int custId;
	private String custName;
	private String custBod;
	private String custPhone;
	private Orders orders;
	
	public CustomerResponse(Customers customers, Orders orders) {
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

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "CustomerResponse [custId=" + custId + ", custName=" + custName + ", custBod=" + custBod
				+ ", custPhone=" + custPhone + ", orders=" + orders + "]";
	}

	public CustomerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
