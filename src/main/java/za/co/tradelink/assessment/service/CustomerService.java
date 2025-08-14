package za.co.tradelink.assessment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.repository.CustomerRepository;

import java.util.List;

// In CustomerService.java:
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

    public List<Customer> findAll() {

        return customerRepository.findAll();
    }

    public List<Customer> findByCustomerName(String name) {
        return customerRepository.findByCustomerName(name);

    }

    public List<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return customer;
    }

    public List<Customer> findPremiumCustomers(Double creditLimit, String email) {

        List<Customer> customerList = customerRepository.findPremiumCustomers(creditLimit, email);

        return customerList;
    }
}