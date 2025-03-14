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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    private final CustomerRepository repository;
    private final OrdersClient ordersClient;

    @Autowired
    public CustomerResource(CustomerRepository repository, OrdersClient ordersClient) {
        this.repository = repository;
        this.ordersClient = ordersClient;
    }
 // Non-blocking request
    @GetMapping
    public Flux<Customers> getAllCustomers() {
        return Flux.fromIterable(repository.findAll());
    }

    @GetMapping("/all-with-orders")
    public Flux<CustomerResponse> getAllCustomersWithOrders() {
        return Flux.fromIterable(repository.findAll())
                   .flatMap(customer ->
                       ordersClient.getOrder(customer.getOrderId()) // non-blocking call
                           .map(order -> new CustomerResponse(customer, order))
                   );
    }

    @GetMapping("/{custId}")
    public Mono<CustomerResponse> getCustomer(@PathVariable int custId) {
        return Mono.fromCallable(() -> repository.findById(custId))
            .flatMap(optional -> {
                if (optional.isEmpty()) {
                    return Mono.error(new RuntimeException("Customer not found"));
                }
                Customers customer = optional.get();
                // Call ordersClient.getOrder(...) instead of a local getOrder(...)
                return ordersClient.getOrder(customer.getOrderId())
                    .map(orders -> new CustomerResponse(customer, orders));
            });
    }
}

