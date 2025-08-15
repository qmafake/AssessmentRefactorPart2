package za.co.tradelink.assessment.mapper;

import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.CustomerResponseDTO;
import za.co.tradelink.assessment.model.Customer;

@Service
public class CustomerMapper {

    public CustomerResponseDTO toResponseDto(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setEmail(customer.getEmail());
        dto.setCreditLimit(customer.getCreditLimit());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        return dto;
    }

}
