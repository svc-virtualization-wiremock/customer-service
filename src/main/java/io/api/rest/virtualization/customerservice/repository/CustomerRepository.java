package io.api.rest.virtualization.customerservice.repository;

import io.api.rest.virtualization.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
