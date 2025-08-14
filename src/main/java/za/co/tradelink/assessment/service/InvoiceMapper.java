package za.co.tradelink.assessment.service;

import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.InvoiceResponse;
import za.co.tradelink.assessment.model.Invoice;

@Service
public class InvoiceMapper {

    public InvoiceResponse toResponseDTO(Invoice invoice) {

        return new InvoiceResponse(
                invoice.getInvoiceId(),
                invoice.getCustomer().getCustomerName(),
                invoice.getStatus(),
                invoice.getTotalAmount()
        );
    }
}
