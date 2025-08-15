package za.co.tradelink.assessment.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.tradelink.assessment.dto.InvoiceRequestDTO;
import za.co.tradelink.assessment.dto.InvoiceResponseDTO;
import za.co.tradelink.assessment.dto.InvoiceUpdateStatusRequestDTO;
import za.co.tradelink.assessment.mapper.InvoiceMapper;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO request) {

        logger.info("Create invoice: {}", request);

        Invoice createdInvoice = invoiceService.createInvoice(request);
        InvoiceResponseDTO responseDTO = invoiceMapper.toResponseDTO(createdInvoice);

        logger.info("Created invoice: {}", responseDTO);

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
            @Valid @RequestBody InvoiceUpdateStatusRequestDTO request) {

        logger.info("Update invoice id - {} to status - {}", request.getInvoiceId(), request.getNewStatus());

        Invoice updatedInvoice = invoiceService.updateInvoiceStatus(request);
        InvoiceResponseDTO responseDTO = invoiceMapper.toResponseDTO(updatedInvoice);

        logger.info("Update customer id - {} invoice updated", responseDTO.getCustomerId());

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }
}
