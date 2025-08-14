package za.co.tradelink.assessment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private Date date;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "invoice")
    @JsonManagedReference
    private List<InvoiceLine> lines;

    public Invoice() {
    }

    public void calculateTotal() { //TODO: better placed in a service layer as it involves business logic.
        double sum = 0.0;
        for (InvoiceLine line : lines) {
            sum += line.getQuantity() * line.getUnitPrice();
        }
        this.totalAmount = sum;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(List<InvoiceLine> lines) {
        this.lines = lines;
    }
}
