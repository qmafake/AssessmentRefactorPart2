package za.co.tradelink.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.tradelink.assessment.model.InvoiceStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InvoiceResponseDTO {

    private Long customerId;

    private String customerName;

    private InvoiceStatus status;

    BigDecimal totalAmount;

  }
