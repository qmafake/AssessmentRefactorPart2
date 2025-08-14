package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.tradelink.assessment.model.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds customers by exact name match
     * @param name the customer name to search for
     * @return list of customers with matching name
     */
    List<Customer> findByCustomerName(String name);

    /**
     * Finds customers whose credit limit exceeds the specified amount
     * and whose email contains the given string
     * @param creditLimit minimum credit limit threshold
     * @param email email domain or fragment to search for
     * @return list of matching customers
     */
    @Query("SELECT c FROM Customer c WHERE c.creditLimit > :creditLimit AND c.email LIKE %:email%")
    List<Customer> findPremiumCustomers(
            @Param("creditLimit") Double creditLimit,
            @Param("email") String email);

    /**
     * Finds customers by exact email match
     * @param emailAddress the exact email to search for
     * @return list of customers with matching email
     */
    List<Customer> findByEmail(String emailAddress);
}