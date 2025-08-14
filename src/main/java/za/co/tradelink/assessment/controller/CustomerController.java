package za.co.tradelink.assessment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {

        Customer customer = customerService.findById(id);

        return ResponseEntity.ok(customer); //TODO: DTOs

//        return customer.map(ResponseEntity::ok) //TODO: explain statement
//                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    // POST default customer
    @PostMapping("/default")
    public Customer createDefaultCustomer() {

        return customerService.createDefaultCustomer();
    }

    @GetMapping("/name/{name}")
    public List<Customer> getCustomersByName(@PathVariable String name) {
        return customerService.findByCustomerName(name);
    }

    @GetMapping("/email/{email}")
    public List<Customer> getCustomersByEmail(@PathVariable String email) {
        return customerService.findByEmail(email);
    }

    @GetMapping("/filter")
    public List<Customer> getFilteredCustomers(
            @RequestParam("creditLimit") Double creditLimit,
            @RequestParam("email") String email) {
        return customerService.findPremiumCustomers(creditLimit, email);
    }

    @GetMapping("/{id}/discount-eligible")
    public ResponseEntity<Boolean> isCustomerEligibleForDiscount(@PathVariable Long id) {
        boolean eligible = customerService.isCustomerEligibleForDiscount(id);
        return ResponseEntity.ok(eligible);
    }
}
