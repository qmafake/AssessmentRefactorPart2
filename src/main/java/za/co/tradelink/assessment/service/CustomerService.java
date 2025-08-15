package za.co.tradelink.assessment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.CustomerCreateDTO;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Value("${customer.discount.eligibility.limit:5000.0}")
    private BigDecimal discountEligibilityLimit;

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createDefaultCustomer() {

        Customer customer = Customer.builder()
                .email("default@example.com")
                .creditLimit(new BigDecimal(1000.0))
                .phone("123-456-7890")
                .address("123 Default St")
                .build();

        return customerRepository.save(customer);

    }


    public Customer createCustomer(CustomerCreateDTO dto) {

        if (dto.getCustomerName() == null || dto.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        Customer customer = Customer.builder()
                .customerName(dto.getCustomerName())
                .email(dto.getEmail())
                .creditLimit(dto.getCreditLimit())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public List<Customer> findByCustomerName(String name) {

        List<Customer> customers = customerRepository.findByCustomerName(name);

        return customers != null ? customers : Collections.emptyList();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findPremiumCustomers(Double creditLimit, String email) {

        List<Customer> customerList = customerRepository.findPremiumCustomers(creditLimit, email);

        return customerList;
    }

    public boolean isCustomerEligibleForDiscount(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customer.getCreditLimit().compareTo(discountEligibilityLimit) > 0;
    }

}