package za.co.tradelink.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_line")
public class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;

    private String item_description;

    private Double unitPrice;

    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "invoice_id") 
    private Invoice invoice;


    public Long getLineId() {
        return lineId; 
    }

    public void setLineId(Long lineId) { 
        this.lineId = lineId; 
    }

    public String getDescription() {
        return item_description;
    }

    public void setDescription(String description) {
        this.item_description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Double calculateLineTotal() {
        return quantity * unitPrice;
    }
}
