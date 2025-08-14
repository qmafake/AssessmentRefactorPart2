package za.co.tradelink.assessment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.dto.CustomerCreateDto;
import za.co.tradelink.assessment.dto.CustomerResponseDto;
import za.co.tradelink.assessment.mapper.CustomerMapper;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponseDto> responseDtos = customers.stream()
                .map(customerMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findCustomerById(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                customerMapper.toResponseDto(customerOptional.get())
        );
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerCreateDto customerCreateDto) {

        Customer customer = customerService.createCustomer(customerCreateDto);
        CustomerResponseDto responseDto = customerMapper.toResponseDto(customer);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // POST default customer
    @PostMapping("/default")
    public ResponseEntity<CustomerResponseDto> createDefaultCustomer() {

        Customer customer = customerService.createDefaultCustomer();
        CustomerResponseDto responseDto = customerMapper.toResponseDto(customer);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerResponseDto>> findCustomersByName(
            @NotBlank @Size(min=2, max=100) @PathVariable String name) {

        List<Customer> customers = customerService.findByCustomerName(name);

        List<CustomerResponseDto> dtos = customers
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<CustomerResponseDto>> findPremiumCustomers(
            @RequestParam("creditLimit") Double creditLimit,
            @RequestParam("email") @Email String email) {

        List<Customer> customers = customerService.findPremiumCustomers(creditLimit, email);

        List<CustomerResponseDto> dtos = customers
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/discount-eligible")
    public ResponseEntity<Boolean> isCustomerEligibleForDiscount(@PathVariable Long id) {
        boolean eligible = customerService.isCustomerEligibleForDiscount(id);
        return ResponseEntity.ok(eligible);
    }
}
