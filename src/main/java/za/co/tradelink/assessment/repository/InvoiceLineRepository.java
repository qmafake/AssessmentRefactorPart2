package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.InvoiceLine;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    /**
     * Finds all invoice lines for a specific invoice
     * @param invoice the parent invoice
     * @return list of invoice lines
     */
    List<InvoiceLine> findByInvoice(Invoice invoice);

    /**
     * Calculates the total amount for an invoice
     * @param invoiceId the invoice ID
     * @return sum of (quantity * unitPrice) for all lines
     */
    @Query("SELECT SUM(il.quantity * il.unitPrice) FROM InvoiceLine il WHERE il.invoice.invoiceId = :invoiceId")
    Double calculateInvoiceTotal(@Param("invoiceId") Long invoiceId);

    /**
     * Finds invoice lines with quantity greater than specified value
     * @param minQuantity the minimum quantity threshold
     * @return list of qualifying invoice lines
     */
    List<InvoiceLine> findByQuantityGreaterThan(Integer minQuantity);

    /**
     * Finds expensive invoice lines (high unit price and quantity)
     * @param minPrice the minimum unit price
     * @param minQuantity the minimum quantity
     * @return list of qualifying invoice lines
     */
    @Query("SELECT il FROM InvoiceLine il WHERE il.unitPrice > :minPrice AND il.quantity > :minQuantity")
    List<InvoiceLine> findExpensiveLines(
            @Param("minPrice") Double minPrice,
            @Param("minQuantity") Integer minQuantity);

    /**
     * Updates an invoice line's unit price
     * @param id the line ID
     * @param price the new unit price
     * @return number of affected rows
     */
    @Modifying
    @Transactional
    @Query("UPDATE InvoiceLine il SET il.unitPrice = :price WHERE il.lineId = :id")
    int updatePrice(@Param("id") Long id, @Param("price") Double price);

    /**
     * Updates an invoice line's quantity
     * @param lineId the line ID
     * @param quantity the new quantity
     * @return number of affected rows
     */
    @Modifying
    @Transactional
    @Query("UPDATE InvoiceLine il SET il.quantity = :quantity WHERE il.lineId = :lineId")
    int updateQuantity(@Param("lineId") Long lineId, @Param("quantity") Integer quantity);
}