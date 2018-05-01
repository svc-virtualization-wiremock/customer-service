package io.api.rest.virtualization.customerservice.controller;

import io.api.rest.virtualization.customerservice.entity.Customer;
import io.api.rest.virtualization.customerservice.exception.NotFoundException;
import io.api.rest.virtualization.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable String id) {
        Optional<Customer> customerHolder = customerRepository.findById(id);
        return customerHolder.orElseThrow(NotFoundException::new);
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Optional<Customer> customerHolder = customerRepository.findById(id);
        return customerHolder.map(dbCustomer -> {
            if (!StringUtils.isEmpty(customer.getSalutation())) {
                dbCustomer.setSalutation(customer.getSalutation());
            }
            if (!StringUtils.isEmpty(customer.getFirstName())) {
                dbCustomer.setFirstName(customer.getFirstName());
            }
            if (!StringUtils.isEmpty(customer.getLastName())) {
                dbCustomer.setLastName(customer.getLastName());
            }
            if (!StringUtils.isEmpty(customer.getDateOfBirth())) {
                dbCustomer.setDateOfBirth(customer.getDateOfBirth());
            }
            return customerRepository.save(dbCustomer);
        }).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String id) {
        try {
            customerRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }
}
