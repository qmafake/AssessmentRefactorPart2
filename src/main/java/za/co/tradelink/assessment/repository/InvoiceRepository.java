package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.Customer;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> getByCustomer(Customer customer);

    List<Invoice> findByDateBetween(Date startDate, Date endDate);

    List<Invoice> findByStatus(String invoiceStatus);

    @Query("SELECT i FROM Invoice i WHERE i.totalAmount > ?1")
    List<Invoice> findLargeInvoices(Double amount);

    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.status = ?2 WHERE i.invoiceId = ?1")
    void updateStatus(Long id, String status);
}
