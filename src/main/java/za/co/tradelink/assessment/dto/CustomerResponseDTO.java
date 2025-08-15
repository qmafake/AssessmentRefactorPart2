package za.co.tradelink.assessment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerResponseDTO {

    private Long id;
    private String customerName;
    private String email;
    private BigDecimal creditLimit;
    private String phone;
    private String address;
}
