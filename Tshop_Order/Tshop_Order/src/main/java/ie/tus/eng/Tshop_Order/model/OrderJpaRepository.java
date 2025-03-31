package ie.tus.eng.Tshop_Order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.tus.eng.Tshop_Order.order.Orders;


public interface OrderJpaRepository extends JpaRepository<Orders, Integer>{
	//It acts as the data access layer for the Orders entity,
	//providing built-in CRUD and custom query methods without requiring manual implementation.

	List<Orders> findByPrice(int price);// not implemented
	
	List<Orders> findByItems(String items);// not implemented
	
	List<Orders> findByCustId(int custId);// not implemented

}
