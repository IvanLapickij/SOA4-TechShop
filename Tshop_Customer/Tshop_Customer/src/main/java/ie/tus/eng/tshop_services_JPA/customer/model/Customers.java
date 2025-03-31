package ie.tus.eng.tshop_services_JPA.customer.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Customers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "custid")
	private int custId;
	
	@Column(name="custname")
	private String custName;
	
	@Column(name="custbod")
	private String custBod;
	
	@Column(name="custphone")
	private String custPhone;

	@LastModifiedDate // updated timeStamp
    private LocalDateTime lastModified;

    public LocalDateTime getLastModified() {
        return lastModified;
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

	@Override
	public String toString() {
		return "Customers [custId=" + custId + ", custName=" + custName + ", custBod=" + custBod + ", custPhone="
				+ custPhone + "]";
	}

	public Customers(int custId, String custName, String custBod, String custPhone) {
		super();
		this.custId = custId;
		this.custName = custName;
		this.custBod = custBod;
		this.custPhone = custPhone;
	}

	public Customers() {
		super();
	}

}
