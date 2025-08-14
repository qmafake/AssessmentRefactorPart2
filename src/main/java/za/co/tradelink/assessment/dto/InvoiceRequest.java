package za.co.tradelink.assessment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InvoiceRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotEmpty(message = "Invoice must have at least one line item")
    private List<InvoiceLineRequest> lineItems;

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<InvoiceLineRequest> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<InvoiceLineRequest> lineItems) {
        this.lineItems = lineItems;
    }
}
