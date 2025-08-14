package za.co.tradelink.assessment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.CustomerCreateDto;
import za.co.tradelink.assessment.dto.CustomerResponseDto;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Value("${customer.discount.eligibility.limit:5000.0}")
    private Double discountEligibilityLimit;

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createDefaultCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("Default Customer");
        customer.setEmail("default@example.com");
        customer.setCreditLimit(1000.0);
        customer.setPhone("123-456-7890");
        customer.setAddress("123 Default St");
        return customerRepository.save(customer);
    }

    public boolean isCustomerEligibleForDiscount(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customer.getCreditLimit() > discountEligibilityLimit;
    }

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public List<Customer> findByCustomerName(String name) {

        List<Customer> customers = customerRepository.findByCustomerName(name);

        return customers != null ? customers : Collections.emptyList();
    }

    public Customer createCustomer(CustomerCreateDto dto) {

        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setCreditLimit(dto.getCreditLimit());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());

        return customerRepository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findPremiumCustomers(Double creditLimit, String email) {

        List<Customer> customerList = customerRepository.findPremiumCustomers(creditLimit, email);

        return customerList;
    }
}