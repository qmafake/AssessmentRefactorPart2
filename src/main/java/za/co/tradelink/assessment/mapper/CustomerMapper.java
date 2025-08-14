package za.co.tradelink.assessment.mapper;

import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.CustomerResponseDto;
import za.co.tradelink.assessment.model.Customer;

@Service
public class CustomerMapper {

    public CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setEmail(customer.getEmail());
        dto.setCreditLimit(customer.getCreditLimit());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        return dto;
    }

}
