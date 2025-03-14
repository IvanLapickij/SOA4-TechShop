package ie.tus.eng.tshop_services_JPA.customers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ie.tus.eng.tshop_services_JPA.customer.model.CustomerResponse;
import ie.tus.eng.tshop_services_JPA.customer.model.Customers;
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

    // ---------------------------
    // READ (All / By ID)
    // ---------------------------
    @GetMapping
    public Flux<Customers> getAllCustomers() {
        return Flux.fromIterable(repository.findAll());
    }

    @GetMapping("/all-with-orders")
    public Flux<CustomerResponse> getAllCustomersWithOrders() {
        return Flux.fromIterable(repository.findAll())
                   .flatMap(customer ->
                       ordersClient.getOrder(customer.getOrderId())
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
                return ordersClient.getOrder(customer.getOrderId())
                    .map(orders -> new CustomerResponse(customer, orders));
            });
    }

    // ---------------------------
    // CREATE
    // ---------------------------
    @PostMapping
    public Mono<Customers> createCustomer(@RequestBody Customers newCustomer) {
        return Mono.fromCallable(() -> repository.save(newCustomer));
    }


    // ---------------------------
    // UPDATE
    // ---------------------------
    @PutMapping("/{custId}")
    public Mono<ResponseEntity<Customers>> updateCustomer(
            @PathVariable int custId,
            @RequestBody Customers updatedCustomer) {

        return Mono.fromCallable(() -> repository.findById(custId))
            .flatMap(optional -> {
                if (optional.isEmpty()) {
                    return Mono.just(ResponseEntity.notFound().build());
                }
                Customers existing = optional.get();
                // Update fields you want to change
                existing.setCustName(updatedCustomer.getCustName());
                existing.setCustBod(updatedCustomer.getCustBod());
                existing.setCustPhone(updatedCustomer.getCustPhone());
                existing.setOrderId(updatedCustomer.getOrderId());

                return Mono.fromCallable(() -> repository.save(existing))
                           .map(saved -> ResponseEntity.ok(saved));
            });
    }


    // ---------------------------
    // DELETE
    // ---------------------------
    @DeleteMapping("/{custId}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable int custId) {
        return Mono.fromCallable(() -> repository.findById(custId))
            .flatMap(optional -> {
                if (optional.isEmpty()) {
                    return Mono.just(ResponseEntity.notFound().build());
                }
                repository.delete(optional.get());
                return Mono.just(ResponseEntity.ok().<Void>build());
            });
    }
}
