package za.co.tradelink.assessment.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.dto.InvoiceRequestDTO;
import za.co.tradelink.assessment.dto.InvoiceLineRequestDTO;
import za.co.tradelink.assessment.dto.InvoiceUpdateStatusRequestDTO;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;
import za.co.tradelink.assessment.model.InvoiceStatus;
import za.co.tradelink.assessment.repository.CustomerRepository;
import za.co.tradelink.assessment.repository.InvoiceLineRepository;
import za.co.tradelink.assessment.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

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

    public Invoice createInvoice(InvoiceRequestDTO request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Invoice invoice = Invoice.builder()
                .customer(customer)
                .date(new Date())
                .status(InvoiceStatus.DRAFT)
                .build();

        BigDecimal total = BigDecimal.valueOf(0.0);

        List<InvoiceLine> invoiceLines = new ArrayList<>();

        for (InvoiceLineRequestDTO lineItem : request.getLineItems()) {

            InvoiceLine line = InvoiceLine.builder()
                    .itemDescription(lineItem.getDescription())
                    .quantity(lineItem.getQuantity())
                    .unitPrice(lineItem.getUnitPrice())
                    .build();

//            total += line.getQuantity() * line.getUnitPrice(); //TODO: remove

            BigDecimal quantity  = BigDecimal.valueOf(lineItem.getQuantity());
            BigDecimal unitPrice  = lineItem.getUnitPrice();

            total = total.add(quantity.multiply(unitPrice));

            invoiceLines.add(line);

            /*InvoiceLine line = new InvoiceLine();
            line.setItemDescription(lineItem.getDescription());
            line.setQuantity(lineItem.getQuantity());
            line.setUnitPrice(lineItem.getUnitPrice());
            total += line.getQuantity() * line.getUnitPrice();
            invoiceLines.add(line);*/
        }

        invoice.setTotalAmount(total);
        invoice = invoiceRepository.save(invoice); // Save once, after total is set

        for (InvoiceLine line : invoiceLines) {
            line.setInvoice(invoice);
            invoiceLineRepository.save(line);
        }

        return invoice;
    }


    public Invoice updateInvoiceStatus(InvoiceUpdateStatusRequestDTO invoiceUpdateStatusRequestDTO) {

        Invoice invoice = invoiceRepository.findById(invoiceUpdateStatusRequestDTO.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        InvoiceStatus newStatus = invoiceUpdateStatusRequestDTO.getNewStatus();

        if (!(newStatus == InvoiceStatus.PAID) && !(newStatus == InvoiceStatus.CANCELLED)) {
            throw new  IllegalArgumentException("Invalid status change: " + newStatus); //TODO: handle
        }

        if (newStatus == InvoiceStatus.PAID) {
            Customer customer = invoice.getCustomer();
            if (customer.getCreditLimit() < 5000) {
                customer.setCreditLimit(customer.getCreditLimit() + 100);
                customerRepository.save(customer);
            }
        }

        invoice.setStatus(newStatus);
        invoiceRepository.save(invoice);

        return invoice;
    }

    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            for (InvoiceLine line : invoice.getLines()) {
                invoiceLineRepository.delete(line);
            }
            invoiceRepository.delete(invoice);
    }

    public List<Invoice> getInvoicesByStatus(String Status) {
        return invoiceRepository.findByStatus(Status);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Double calculateInvoiceTotal(Long invoiceId) {
        return invoiceLineRepository.calculateInvoiceTotal(invoiceId);
    }

    public void markAsPaid(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found")); //TODO: handle exception
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);
    }
}
