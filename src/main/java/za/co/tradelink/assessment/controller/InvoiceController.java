package za.co.tradelink.assessment.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.dto.InvoiceRequest;
import za.co.tradelink.assessment.dto.InvoiceResponse;
import za.co.tradelink.assessment.dto.InvoiceUpdateStatusRequest;
import za.co.tradelink.assessment.service.InvoiceMapper;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest request) {

        Invoice createdInvoice = invoiceService.createInvoice(request);
        InvoiceResponse responseDTO = invoiceMapper.toResponseDTO(createdInvoice);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {

        Invoice invoice = invoiceService.getInvoiceById(id);
        InvoiceResponse responseDTO = invoiceMapper.toResponseDTO(invoice);

        return ResponseEntity.ok(invoiceMapper.toResponseDTO(invoice));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<InvoiceResponse> dtoList = invoices.stream()
                .map(invoiceMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/status")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(
            @Valid @RequestBody InvoiceUpdateStatusRequest invoiceUpdateStatusRequest) {

        Invoice updatedInvoice = invoiceService.updateInvoiceStatus(invoiceUpdateStatusRequest);
        InvoiceResponse responseDTO = invoiceMapper.toResponseDTO(updatedInvoice);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }
}
