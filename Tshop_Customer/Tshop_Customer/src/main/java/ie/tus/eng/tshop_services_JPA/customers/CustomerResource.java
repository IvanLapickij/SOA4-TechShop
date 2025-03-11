package ie.tus.eng.tshop_services_JPA.customers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.tus.eng.tshop_services_JPA.customer.model.CustomerResponse;
import ie.tus.eng.tshop_services_JPA.customer.model.Customers;
import ie.tus.eng.tshop_services_JPA.orders.Orders;
import ie.tus.eng.tshop_services_JPA.orders.OrdersClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/customers") // Base URL applies to all endpoints
public class CustomerResource {
    private final CustomerRepository repository;
    private final OrdersClient ordersClient;

    @Autowired
    public CustomerResource(CustomerRepository repository, OrdersClient ordersClient) {
        this.repository = repository;
        this.ordersClient = ordersClient;
    }

    // GET all customers
//    @GetMapping
//    public List<Customers> retrieveAllCustomers() {
//        return repository.findAll();
//    }
    
    // Non-blocking request
    @GetMapping
    public Flux<Customers> getAllCustomers() {
        return Flux.fromIterable(repository.findAll());
    }

    // GET customer by ID
    @GetMapping("/{custId}") // Fix duplicate path issue
    public ResponseEntity<CustomerResponse> retrieveCustomers(@PathVariable int custId) {
        Optional<Customers> customers = repository.findById(custId);

        if (customers.isEmpty()) {
            System.out.println("Customer not found in the database");
            return ResponseEntity.notFound().build();
        } else {
            Orders orders = ordersClient.getOrdersById(customers.get().getOrderId());

            if (orders == null) {
                System.out.println("Order service is unreachable or order not found.");
                orders = new Orders(0, "No items", 0); // Provide default Orders object
            }

            CustomerResponse customerResponse = new CustomerResponse(customers.get(), orders);
            return ResponseEntity.ok(customerResponse);
        }
    }
}
