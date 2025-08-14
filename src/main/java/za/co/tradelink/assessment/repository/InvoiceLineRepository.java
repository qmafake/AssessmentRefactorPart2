package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    List<InvoiceLine> getByInvoice(Invoice invoice);

    @Query("SELECT SUM(il.quantity * il.unitPrice) FROM InvoiceLine il WHERE il.invoice.invoiceId = ?1")
    Double calculateInvoiceTotal(Long invoiceId);

    List<InvoiceLine> findByQuantityGreaterThan(Integer min_quantity);

    default void updateLineQuantity(Long lineId, Integer newQuantity) {
        InvoiceLine line = findById(lineId).orElse(null);
        if (line != null) {
            line.setQuantity(newQuantity);
            save(line);

            Invoice invoice = line.getInvoice();
            invoice.calculateTotal(); //TODO: business logic shld not be here
        }
    }

    @Query("SELECT il FROM InvoiceLine il WHERE il.unitPrice > ?1 AND il.quantity > ?2")
    List<InvoiceLine> findExpensiveLines(Double minPrice, Integer minQuantity);

    @Modifying
    @Transactional
    @Query("UPDATE InvoiceLine il SET il.unitPrice = ?2 WHERE il.lineId = ?1")
    void updatePrice(Long id, Double price);
}
