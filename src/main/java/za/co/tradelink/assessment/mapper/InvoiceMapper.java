package za.co.tradelink.assessment.mapper;

import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.InvoiceResponseDTO;
import za.co.tradelink.assessment.model.Invoice;

@Service
public class InvoiceMapper {

    public InvoiceResponseDTO toResponseDTO(Invoice invoice) {

        return new InvoiceResponseDTO(
                invoice.getInvoiceId(),
                invoice.getCustomer().getCustomerName(),
                invoice.getStatus(),
                invoice.getTotalAmount()
        );
    }
}
