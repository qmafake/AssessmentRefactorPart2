package za.co.tradelink.assessment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.dto.CustomerCreateDTO;
import za.co.tradelink.assessment.dto.CustomerResponseDTO;
import za.co.tradelink.assessment.mapper.CustomerMapper;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.service.CustomerService;

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
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponseDTO> responseDtos = customers.stream()
                .map(customerMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> findCustomerById(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                customerMapper.toResponseDto(customerOptional.get())
        );
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerCreateDTO customerCreateDto) {

        Customer customer = customerService.createCustomer(customerCreateDto);
        CustomerResponseDTO responseDto = customerMapper.toResponseDto(customer);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // POST default customer
    @PostMapping("/default")
    public ResponseEntity<CustomerResponseDTO> createDefaultCustomer() {

        Customer customer = customerService.createDefaultCustomer();
        CustomerResponseDTO responseDto = customerMapper.toResponseDto(customer);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerResponseDTO>> findCustomersByName(
            @NotBlank @Size(min=2, max=100) @PathVariable String name) {

        List<Customer> customers = customerService.findByCustomerName(name);

        List<CustomerResponseDTO> dtos = customers
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<CustomerResponseDTO>> findPremiumCustomers(
            @RequestParam("creditLimit") Double creditLimit,
            @RequestParam("email") @Email String email) {

        List<Customer> customers = customerService.findPremiumCustomers(creditLimit, email);

        List<CustomerResponseDTO> dtos = customers
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
