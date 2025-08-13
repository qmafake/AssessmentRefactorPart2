package za.co.tradelink.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private Date date;

    private Double total_amount;

    private String status;

    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @OneToMany(mappedBy = "invoice")
    private List<InvoiceLine> lines;

    public Invoice() {
    }

    public void calculateTotal() {
        double sum = 0.0;
        for (InvoiceLine line : lines) {
            sum += line.getQuantity() * line.getUnitPrice();
        }
        this.total_amount = sum;
    }

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long id) { this.invoiceId = id; }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return total_amount;
    }

    public void setAmount(Double amount) {
        this.total_amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
