package ie.tus.eng.tshop_services_JPA.customers;

import ie.tus.eng.tshop_services_JPA.customer.model.CustomerResponse;
import ie.tus.eng.tshop_services_JPA.customer.model.Customers;
import ie.tus.eng.tshop_services_JPA.orders.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

	private final CustomerRepository repository;
	private final WebClient webClient;

	@Autowired
	public CustomerResource(CustomerRepository repository, WebClient webClient) {
		this.repository = repository;
		this.webClient = webClient;
	}

	//combined endpoint CUST + ORDERS
	@GetMapping("/techshop")
	public Mono<ResponseEntity<Flux<CustomerResponse>>> getAllCustomersWithOrders(
	    @RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch) {

	    // Remove any double quotes from the If-None-Match header (ETag values may include quotes)
	    if (ifNoneMatch != null) {
	        ifNoneMatch = ifNoneMatch.replace("\"", "");
	    }

	    // Retrieve all customers from the repository, sorted by customer ID for consistent ordering
	    List<Customers> allCustomers = repository.findAll(Sort.by("custId"));

	    // Build a fingerprint string from all customer IDs and names
	    String dataFingerprint = allCustomers.stream()
	        .map(c -> c.getCustId() + "|" + c.getCustName())
	        .collect(Collectors.joining(","));
	    
	    // Generate an MD5 hash of the fingerprint to use as the ETag
	    String eTag = generateHash(dataFingerprint);

	    // Log the computed ETag and incoming If-None-Match header for debugging
	    System.out.println("Computed ETag: " + eTag);
	    System.out.println("If-None-Match header: " + ifNoneMatch);

	    // If the ETag matches the If-None-Match header, return 304 Not Modified (no data change)
	    if (eTag.equals(ifNoneMatch)) {
	        return Mono.just(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
	    }

	    // For each customer, retrieve their orders via the orders service and build a CustomerResponse
	    Flux<CustomerResponse> responseFlux = Flux.fromIterable(allCustomers)
	        .flatMap(customer -> webClient.get()
	            .uri("/orders/customer/{custId}", customer.getCustId())
	            .retrieve()
	            .bodyToFlux(Orders.class)
	            .collectList()
	            .map(orders -> new CustomerResponse(customer, orders))
	        );

	    // Return a 200 OK response with the computed ETag and the customer-orders data
	    return Mono.just(ResponseEntity.ok().eTag(eTag).body(responseFlux));
	}

	// GET All Customers (no orders)
	@GetMapping
	public Flux<Customers> getAllCustomers() {
		return Flux.fromIterable(repository.findAll());
	}

	// GET Customer by ID (no orders)
	@GetMapping("/{custId}")
	public Mono<ResponseEntity<Customers>> getCustomer(@PathVariable int custId) {
		return Mono.fromCallable(() -> repository.findById(custId))
				.map(optional -> optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()));
	}

	// POST - Create Customer
	@PostMapping
	public Mono<Customers> createCustomer(@RequestBody Customers newCustomer) {
		return Mono.fromCallable(() -> repository.save(newCustomer));
	}

	// PUT - Update Customer
	@PutMapping("/{custId}")
	public Mono<ResponseEntity<Customers>> updateCustomer(@PathVariable int custId,
			@RequestBody Customers updatedCustomer) {
		return Mono.fromCallable(() -> repository.findById(custId)).flatMap(optional -> {
			if (optional.isEmpty()) {
				return Mono.just(ResponseEntity.notFound().build());
			}
			Customers existing = optional.get();
			existing.setCustName(updatedCustomer.getCustName());
			existing.setCustBod(updatedCustomer.getCustBod());
			existing.setCustPhone(updatedCustomer.getCustPhone());
			return Mono.fromCallable(() -> repository.save(existing)).map(saved -> ResponseEntity.ok(saved));
		});
	}

	// DELETE Customer
	@DeleteMapping("/{custId}")
	public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable int custId) {
		return Mono.fromCallable(() -> repository.findById(custId)).flatMap(optional -> {
			if (optional.isEmpty()) {
				return Mono.just(ResponseEntity.notFound().build());
			}
			repository.delete(optional.get());
			return Mono.just(ResponseEntity.ok().<Void>build());
		});
	}

	// Utility to generate an MD5 hash (for Etag)
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
			return String.valueOf(input.hashCode());
		}
	}
}
