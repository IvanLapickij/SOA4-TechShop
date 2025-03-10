package ie.tus.eng.tshop_services_JPA.orders;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tshop-order", url = "http://localhost:8081")
public interface OrdersClient {

	@GetMapping("/orders/{orderId}")
	Orders getOrdersById(@PathVariable int orderId);
}
