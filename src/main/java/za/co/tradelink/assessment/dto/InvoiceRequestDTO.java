package za.co.tradelink.assessment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InvoiceRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotEmpty(message = "Invoice must have at least one line item")
    private List<InvoiceLineRequestDTO> lineItems;

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceLineRequestDTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<InvoiceLineRequestDTO> lineItems) {
        this.lineItems = lineItems;
    }
}
