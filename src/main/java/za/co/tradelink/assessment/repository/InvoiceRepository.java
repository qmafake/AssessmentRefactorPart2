package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.Customer;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> getByCustomer(Customer customer);

    List<Invoice> findByDateBetween(Date startDate, Date endDate);

    List<Invoice> findByStatus(String invoice_status);

    @Query("SELECT i FROM Invoice i WHERE i.total_amount > ?1")
    List<Invoice> findLargeInvoices(Double amount);

    default void updateInvoiceStatus(Long invoiceId, String newStatus) {
        Invoice invoice = findById(invoiceId).orElse(null);
        if (invoice != null) {
            if (newStatus.equals("PAID") || newStatus.equals("CANCELLED")) {
                invoice.setStatus(newStatus);
                save(invoice);
            }
        }
    }

    @Query("UPDATE Invoice i SET i.status = ?2 WHERE i.invoiceId = ?1")
    void updateStatus(Long id, String status);

    default void markAsPaid(Long invoiceId) {
        Invoice invoice = findById(invoiceId).orElse(null);
        if (invoice != null) {
            invoice.setStatus("PAID");
            save(invoice);
        }
    }
}
