package za.co.tradelink.assessment.dto;

import jakarta.validation.constraints.NotNull;
import za.co.tradelink.assessment.model.InvoiceStatus;

public class UpdateInvoiceStatusRequest {

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Status is required")
    private InvoiceStatus newStatus;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(InvoiceStatus newStatus) {
        this.newStatus = newStatus;
    }
}
