package ie.tus.eng.tshop_services_JPA.customer.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customers {
	
	@Id
	@Column(name = "CUSTID")
	private int custId;
	
	@Column(name="CUSTNAME")
	private String custName;
	
	@Column(name="CUSTBOD")
	private String custBod;
	
	@Column(name="CUSTPHONE")
	private String custPhone;
	
	@Column(name="ORDERID")
	private int orderId;

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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "Customers [custId=" + custId + ", custName=" + custName + ", custBod=" + custBod + ", custPhone="
				+ custPhone + ", orderId=" + orderId + "]";
	}

	public Customers() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	
}

