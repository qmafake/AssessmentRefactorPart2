package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.tradelink.assessment.model.Invoice;
import za.co.tradelink.assessment.model.Customer;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Finds invoices for a specific customer
     * @param customer the customer to search by
     * @return list of customer's invoices
     */
    List<Invoice> findByCustomer(Customer customer);

    /**
     * Finds invoices between two dates (inclusive)
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of invoices in date range
     */
    List<Invoice> findByDateBetween(Date startDate, Date endDate);

    /**
     * Finds invoices by status
     * @param status the invoice status to filter by
     * @return list of invoices with matching status
     */
    List<Invoice> findByStatus(String status);

    /**
     * Finds invoices with total amount greater than specified value
     * @param amount the minimum amount threshold
     * @return list of qualifying invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.totalAmount > :amount")
    List<Invoice> findLargeInvoices(@Param("amount") Double amount);

    /**
     * Updates invoice status
     * @param id the invoice ID to update
     * @param status the new status value
     * @return number of affected rows
     */
    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.status = :status WHERE i.invoiceId = :id")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}