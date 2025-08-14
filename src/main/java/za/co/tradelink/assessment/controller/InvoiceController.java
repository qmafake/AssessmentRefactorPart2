package za.co.tradelink.assessment.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.dto.CreateInvoiceRequest;
import za.co.tradelink.assessment.dto.InvoiceResponseDTO;
import za.co.tradelink.assessment.dto.UpdateInvoiceStatusRequest;
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
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody CreateInvoiceRequest request) {

        Invoice createdInvoice = invoiceService.createInvoice(request);
        InvoiceResponseDTO responseDTO = invoiceMapper.toResponseDTO(createdInvoice);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {

        Invoice invoice = invoiceService.getInvoiceById(id);
        InvoiceResponseDTO responseDTO = invoiceMapper.toResponseDTO(invoice);

        return ResponseEntity.ok(invoiceMapper.toResponseDTO(invoice));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<InvoiceResponseDTO> dtoList = invoices.stream()
                .map(invoiceMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/status")
    public ResponseEntity<InvoiceResponseDTO> updateInvoiceStatus(
            @Valid @RequestBody UpdateInvoiceStatusRequest updateInvoiceStatusRequest) {

        Invoice updatedInvoice = invoiceService.updateInvoiceStatus(updateInvoiceStatusRequest);
        InvoiceResponseDTO responseDTO = invoiceMapper.toResponseDTO(updatedInvoice);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }
}
