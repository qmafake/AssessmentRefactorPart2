package za.co.tradelink.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;
import za.co.tradelink.assessment.repository.CustomerRepository;
import za.co.tradelink.assessment.repository.InvoiceLineRepository;
import za.co.tradelink.assessment.repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceLineRepository lineRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    public Invoice findInvoiceById(Long id) {
        return invoiceRepo.findById(id).orElse(null);
    }

    public Invoice createInvoice(Long customerId, List<InvoiceLine> lines) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setDate(new Date());
        invoice.setStatus("DRAFT");

        invoice = invoiceRepo.save(invoice);

        for (InvoiceLine line : lines) {
            line.setInvoice(invoice);
            lineRepository.save(line);
        }

        invoice.setLines(lines);

        invoice.calculateTotal();

        return invoiceRepo.save(invoice);
    }

    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepo.findById(id).orElse(null);
        if (invoice != null) {
            for (InvoiceLine line : invoice.getLines()) {
                lineRepository.delete(line);
            }
            invoiceRepo.delete(invoice);
        }
    }

    public List<Invoice> getInvoicesByStatus(String Status) {
        return invoiceRepo.findByStatus(Status);
    }

    public void updateInvoiceStatus(Long invoiceId, String newStatus) {
        Invoice invoice = invoiceRepo.findById(invoiceId).orElse(null);
        if (invoice != null) {
            if (newStatus.equals("PAID")) {
                Customer customer = invoice.getCustomer();
                if (customer.getCreditLimit() < 5000) {
                    customer.setCreditLimit(customer.getCreditLimit() + 100);
                    customerRepository.save(customer);
                }
            }
            invoice.setStatus(newStatus);
            invoiceRepo.save(invoice);
        }
    }

    public Double calculateInvoiceTotal(Long invoiceId) {
        return lineRepository.calculateInvoiceTotal(invoiceId);
    }
}
