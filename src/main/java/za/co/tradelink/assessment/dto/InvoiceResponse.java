package za.co.tradelink.assessment.dto;

import za.co.tradelink.assessment.model.InvoiceStatus;

public class InvoiceResponse {

    private Long customerId;

    private String customerName;

    private InvoiceStatus status;

    Double totalAmount;

    public InvoiceResponse(Long customerId, String customerName, InvoiceStatus status, Double totalAmount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
