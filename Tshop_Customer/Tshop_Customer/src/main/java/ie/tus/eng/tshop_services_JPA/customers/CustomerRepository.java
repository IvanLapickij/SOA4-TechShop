package ie.tus.eng.tshop_services_JPA.customers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.tus.eng.tshop_services_JPA.customer.model.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Integer>{
	List<Customers> findByCustName(String custName);
	
	List<Customers> findByCustPhone(String custPhone);
}
