package za.co.tradelink.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    // POST default customer
    @PostMapping("/default")
    public Customer createDefaultCustomer() {
        return customerRepository.createDefaultCustomer();  // You're using the default method here
    }

    @GetMapping("/name/{name}")
    public List<Customer> getCustomersByName(@PathVariable String name) {
        return customerRepository.findByCustomerName(name);
    }

    @GetMapping("/email/{email}")
    public List<Customer> getCustomersByEmail(@PathVariable String email) {
        return customerRepository.findByEmail(email);
    }

    @GetMapping("/filter")
    public List<Customer> getFilteredCustomers(
            @RequestParam("creditLimit") Double creditLimit,
            @RequestParam("email") String email) {
        return customerRepository.findCustomersWithCreditLimitGreaterThanAndEmailContaining(creditLimit, email);
    }

    @GetMapping("/{id}/discount-eligible")
    public ResponseEntity<Boolean> isCustomerEligibleForDiscount(@PathVariable Long id) {
        boolean eligible = customerRepository.isCustomerEligibleForDiscount(id);
        return ResponseEntity.ok(eligible);
    }
}
