package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.tradelink.assessment.model.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByCustomerName(String name);

    @Query("SELECT c FROM Customer c WHERE c.creditLimit > :creditLimit AND c.email LIKE %:email%")
    List<Customer> findCustomersWithCreditLimitGreaterThanAndEmailContaining(
            @Param("creditLimit") Double creditLimit,
            @Param("email") String email);

    List<Customer> findByEmail(String emailAddress);
}
