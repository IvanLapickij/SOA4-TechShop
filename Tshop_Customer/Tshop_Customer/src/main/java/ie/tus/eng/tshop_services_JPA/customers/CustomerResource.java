package ie.tus.eng.tshop_services_JPA.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ie.tus.eng.tshop_services_JPA.customer.model.CustomerResponse;
import ie.tus.eng.tshop_services_JPA.customer.model.Customers;
import ie.tus.eng.tshop_services_JPA.orders.OrdersClient;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    private final CustomerRepository repository;
    private final OrdersClient ordersClient;

    // Create an ObjectMapper for JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CustomerResource(CustomerRepository repository, OrdersClient ordersClient) {
        this.repository = repository;
        this.ordersClient = ordersClient;
    }

    // ------------- ETag-enabled endpoint -------------
    @GetMapping("/all-with-orders")
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getAllCustomersWithOrders(
            @RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch) {

        return Mono.fromCallable(() -> repository.findAll()) // blocking JPA call
            .flatMapMany(customers -> Flux.fromIterable(customers))
            .flatMap(customer ->
                ordersClient.getOrder(customer.getOrderId())
                    .map(order -> new CustomerResponse(customer, order))
            )
            .collectList() // gather into a List<CustomerResponse>
            .map(customerResponses -> {
                // 1) Sort the list to ensure stable ordering
                customerResponses.sort(Comparator.comparing(CustomerResponse::getCustId));

                // 2) Convert the list to JSON for a stable representation
                String rawJson;
                try {
                    rawJson = objectMapper.writeValueAsString(customerResponses);
                } catch (Exception e) {
                    // fallback if JSON fails
                    rawJson = customerResponses.toString();
                }

                // 3) Generate a hash (MD5, for example)
                String etagValue = generateHash(rawJson);

                // 4) Compare ETag with If-None-Match
                if (etagValue.equals(ifNoneMatch)) {
                    // Data hasn't changed => 304 Not Modified
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                                         .eTag(etagValue)
                                         .build();
                } else {
                    // Return new data + ETag => 200 OK
                    return ResponseEntity.ok()
                                         .eTag(etagValue)
                                         .body(Flux.fromIterable(customerResponses));
                }
            });
    }

    // ------------- GET: Retrieve All Customers (basic) -------------
    @GetMapping
    public Flux<Customers> getAllCustomers() {
        return Flux.fromIterable(repository.findAll());
    }

    // ------------- GET: Retrieve a Single Customer by ID -------------
    @GetMapping("/{custId}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(@PathVariable int custId) {
        return Mono.fromCallable(() -> repository.findById(custId))
            .flatMap(optional -> {
                if (optional.isEmpty()) {
                    return Mono.just(ResponseEntity.notFound().build());
                }
                Customers customer = optional.get();
                return ordersClient.getOrder(customer.getOrderId())
                    .map(order -> new CustomerResponse(customer, order))
                    .map(ResponseEntity::ok);
            });
    }

    // ------------- POST: Create a New Customer -------------
    @PostMapping
    public Mono<Customers> createCustomer(@RequestBody Customers newCustomer) {
        return Mono.fromCallable(() -> repository.save(newCustomer));
    }

    // ------------- PUT: Update an Existing Customer -------------
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
                // Update fields
                existing.setCustName(updatedCustomer.getCustName());
                existing.setCustBod(updatedCustomer.getCustBod());
                existing.setCustPhone(updatedCustomer.getCustPhone());
                existing.setOrderId(updatedCustomer.getOrderId());
                return Mono.fromCallable(() -> repository.save(existing))
                           .map(saved -> ResponseEntity.ok(saved));
            });
    }

    // ------------- DELETE: Remove a Customer -------------
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

    // ------------- Hash Utility -------------
    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // fallback if MD5 not available
            return String.valueOf(input.hashCode());
        }
    }
}
