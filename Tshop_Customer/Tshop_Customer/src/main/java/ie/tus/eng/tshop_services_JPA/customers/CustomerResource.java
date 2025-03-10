package ie.tus.eng.tshop_services_JPA.customers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ie.tus.eng.tshop_services_JPA.customer.model.CustomerResponse;
import ie.tus.eng.tshop_services_JPA.customer.model.Customers;
import ie.tus.eng.tshop_services_JPA.orders.Orders;
import ie.tus.eng.tshop_services_JPA.orders.OrdersClient;

@RestController
public class CustomerResource {
	CustomerRepository repository;
	private OrdersClient ordersClient;
	
	@Autowired
	public CustomerResource(CustomerRepository repository, OrdersClient ordersClient) {
		this.repository = repository;
		this.ordersClient = ordersClient;
	}
	
	// GET ALL
		@GetMapping("/customers")
		public List<Customers> retireveAllCustomers() {
			return repository.findAll();
		}

//		//GET by ID
		@GetMapping("/customers/{custId}")
		public ResponseEntity<CustomerResponse> retrieveCustomers(@PathVariable int custId) {
			Optional<Customers> customers = repository.findById(custId);

			if (customers.isEmpty()) {
				System.out.println("Customer not found in the database");
				return ResponseEntity.notFound().build();
			} else {
				Orders orders = ordersClient.getOrdersById(customers.get().getOrderId());
				CustomerResponse customerResponse = new CustomerResponse(customers.get(), orders);
				return ResponseEntity.ok(customerResponse); // use dot after response entity to complete lab
			}
		}

	
}
