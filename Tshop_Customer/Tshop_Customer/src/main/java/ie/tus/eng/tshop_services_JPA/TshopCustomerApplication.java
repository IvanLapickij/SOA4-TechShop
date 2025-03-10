package ie.tus.eng.tshop_services_JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TshopCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TshopCustomerApplication.class, args);
	}

}
