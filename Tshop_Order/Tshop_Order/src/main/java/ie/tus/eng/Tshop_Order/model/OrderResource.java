package ie.tus.eng.Tshop_Order.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ie.tus.eng.Tshop_Order.order.Orders;

@RestController
public class OrderResource {

private OrderJpaRepository repository;
	
	@Autowired
	public OrderResource(OrderJpaRepository repository) {
		this.repository = repository;
	}
	
	//GET ALL
	@GetMapping("/orders")
	public List<Orders> retireveAllCourses(){
		return repository.findAll();
	}
	//GET by ID
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<Orders> retrieveCourse(@PathVariable int orderId){
		Optional<Orders> orders = repository.findById(orderId);
		
		if(orders.isEmpty()) {
			System.out.println("Order not found in the database");
			return ResponseEntity.notFound().build();
		}
		else {
			return ResponseEntity.ok(orders.get()); // use dot after response entity to complete lab
		}
	}
	
	//DELETE by ID
	
	//DELETE ALL
	
	//POST 
	
	//PUT 
}
