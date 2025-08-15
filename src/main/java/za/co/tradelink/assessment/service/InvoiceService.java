package za.co.tradelink.assessment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.tradelink.assessment.dto.InvoiceLineRequestDTO;
import za.co.tradelink.assessment.dto.InvoiceRequestDTO;
import za.co.tradelink.assessment.dto.InvoiceUpdateStatusRequestDTO;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;
import za.co.tradelink.assessment.model.InvoiceStatus;
import za.co.tradelink.assessment.repository.CustomerRepository;
import za.co.tradelink.assessment.repository.InvoiceLineRepository;
import za.co.tradelink.assessment.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    @Value("${customer.discount.eligibility.limit:5000.0}")
    private BigDecimal discountEligibilityLimit;

    private final InvoiceRepository invoiceRepository;

    private final CustomerRepository customerRepository;

    private InvoiceLineRepository invoiceLineRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository, InvoiceLineRepository invoiceLineRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.invoiceLineRepository = invoiceLineRepository;
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    @Transactional
    public Invoice createInvoice(InvoiceRequestDTO request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Invoice invoice = Invoice.builder()
                .customer(customer)
                .date(new Date())
                .status(InvoiceStatus.DRAFT)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceLineRequestDTO lineItem : request.getLineItems()) {

            if (lineItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            if (lineItem.getUnitPrice() == null ||
                    lineItem.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Invalid unit price");
            }

            InvoiceLine line = InvoiceLine.builder()
                    .itemDescription(lineItem.getDescription())
                    .quantity(lineItem.getQuantity())
                    .unitPrice(lineItem.getUnitPrice())
                    .build();

            total = total.add(line.getUnitPrice()
                    .multiply(BigDecimal.valueOf(line.getQuantity())));

            // Use relationship helper - maintains bidirectional link
            invoice.addLine(line);
        }

        invoice.setTotalAmount(total);
        return invoiceRepository.save(invoice); // Cascades to saving lines
    }

    @Transactional
    public Invoice updateInvoiceStatus(InvoiceUpdateStatusRequestDTO invoiceUpdateStatusRequestDTO) {

        Invoice invoice = invoiceRepository.findById(invoiceUpdateStatusRequestDTO.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        InvoiceStatus newStatus = invoiceUpdateStatusRequestDTO.getNewStatus();

        if (!(newStatus == InvoiceStatus.PAID) && !(newStatus == InvoiceStatus.CANCELLED)) {
            throw new  IllegalArgumentException("Invalid status change: " + newStatus);
        }

        if (newStatus == InvoiceStatus.PAID) {
            Customer customer = invoice.getCustomer();

            if (customer.getCreditLimit().compareTo(discountEligibilityLimit) < 0) {

                customer.setCreditLimit(customer.getCreditLimit().add(BigDecimal.valueOf(100)));
                customerRepository.save(customer);
            }
        }

        invoice.setStatus(newStatus);
        invoiceRepository.save(invoice);

        return invoice;
    }

    @Transactional
    public void deleteInvoice(Long id) {

        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Invoice not found");
        }
        invoiceLineRepository.deleteByInvoice_InvoiceId(id);
        invoiceRepository.deleteById(id);
    }


    public List<Invoice> getInvoicesByStatus(String statusStr) {
        try {
            InvoiceStatus status = InvoiceStatus.valueOf(statusStr.toUpperCase());
            return invoiceRepository.findByStatus(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr);
        }
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public BigDecimal calculateInvoiceTotal(Long invoiceId) {
        return invoiceLineRepository.calculateInvoiceTotal(invoiceId);
    }
}
