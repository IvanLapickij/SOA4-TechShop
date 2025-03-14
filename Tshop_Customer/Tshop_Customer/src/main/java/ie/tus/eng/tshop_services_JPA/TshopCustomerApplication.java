package ie.tus.eng.tshop_services_JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients
public class TshopCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TshopCustomerApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
	    return WebClient.create("http://localhost:8081"); // URL of the producer
	}

}
