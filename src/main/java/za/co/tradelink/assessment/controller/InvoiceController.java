package za.co.tradelink.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.model.Customer;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;
import za.co.tradelink.assessment.repository.CustomerRepository;
import za.co.tradelink.assessment.repository.InvoiceLineRepository;
import za.co.tradelink.assessment.repository.InvoiceRepository;
import za.co.tradelink.assessment.service.InvoiceService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Map<String, Object> payload) {
        try {
            Long customerId = Long.parseLong(payload.get("customerId").toString());
            List<Map<String, Object>> lineItems = (List<Map<String, Object>>) payload.get("lineItems");

            Customer customer = customerRepository.findById(customerId).orElse(null);
            if (customer == null) {
                return ResponseEntity.badRequest().build();
            }

            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setDate(new Date());
            invoice.setStatus("DRAFT");

            invoice = invoiceRepository.save(invoice);

            List<InvoiceLine> lines = new ArrayList<>();
            double total = 0.0;

            for (Map<String, Object> lineItem : lineItems) {
                InvoiceLine line = new InvoiceLine();
                line.setDescription(lineItem.get("description").toString());
                line.setQuantity(Integer.parseInt(lineItem.get("quantity").toString()));
                line.setUnitPrice(Double.parseDouble(lineItem.get("unitPrice").toString()));
                line.setInvoice(invoice);

                total += line.getQuantity() * line.getUnitPrice();

                invoiceLineRepository.save(line);
                lines.add(line);
            }

            invoice.setAmount(total);

            invoice = invoiceRepository.save(invoice);

            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateInvoiceStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");

        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        if (newStatus.equals("PAID")) {
            Customer customer = invoice.getCustomer();
            if (customer.getCreditLimit() < 5000) {
                customer.setCreditLimit(customer.getCreditLimit() + 100);
                customerRepository.save(customer);
            }
        }

        invoice.setStatus(newStatus);
        invoiceRepository.save(invoice);

        return ResponseEntity.ok("Status updated to " + newStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Invoice> getInvoicesByStatus(@PathVariable String status) {
        return invoiceService.getInvoicesByStatus(status);
    }
}
