package ie.tus.eng.Tshop_Order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.tus.eng.Tshop_Order.order.Orders;

public interface OrderJpaRepository extends JpaRepository<Orders, Integer>{

	List<Orders> findByPrice(int price);
	
	List<Orders> findByItems(String items);
}
