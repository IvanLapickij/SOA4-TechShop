package ie.tus.eng.tshop_services_JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

@EnableJpaAuditing // activates auditing features in Spring Data JPA, automatically populating audit fields like @LastModifiedDate
@SpringBootApplication
public class TshopCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TshopCustomerApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        // This bean creates and configures a WebClient with a base URL ("http://localhost:8081")
    	// so that the application can make non-blocking HTTP requests to the Tshop_Order service.
        return WebClient.create("http://localhost:8081");
    }
}
