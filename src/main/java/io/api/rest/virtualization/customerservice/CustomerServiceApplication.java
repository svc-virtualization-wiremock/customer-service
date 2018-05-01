package io.api.rest.virtualization.customerservice;

import io.api.rest.virtualization.customerservice.entity.Customer;
import io.api.rest.virtualization.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

    @Bean
    @Profile("mock-data")
    CommandLineRunner init(CustomerRepository customerRepository) {
        return args -> {
            List<Customer> customerList = new ArrayList<>();
            customerList.add(Customer.builder().id("1").salutation("Mr.").firstName("FirstName1").lastName("LastName1").dateOfBirth(new Date()).build());
            customerList.add(Customer.builder().id("2").salutation("Mr.").firstName("FirstName2").lastName("LastName2").dateOfBirth(new Date()).build());
            customerList.add(Customer.builder().id("3").salutation("Mr.").firstName("FirstName3").lastName("LastName3").dateOfBirth(new Date()).build());
            customerList.add(Customer.builder().id("4").salutation("Mr.").firstName("FirstName4").lastName("LastName4").dateOfBirth(new Date()).build());
            customerList.add(Customer.builder().id("5").salutation("Mr.").firstName("FirstName5").lastName("LastName5").dateOfBirth(new Date()).build());
            customerRepository.saveAll(customerList);
        };
    }
}
