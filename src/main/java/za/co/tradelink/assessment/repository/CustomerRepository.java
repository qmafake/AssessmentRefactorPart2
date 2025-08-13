package za.co.tradelink.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.tradelink.assessment.model.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    default Customer createDefaultCustomer() {
        Customer customer = new Customer();
        customer.setName("Default Customer");
        customer.setEmail("default@example.com");
        customer.setCreditLimit(1000.0);
        customer.setPhone("123-456-7890");
        customer.address = "123 Default St";
        return save(customer);
    }

    List<Customer> findByCustomer_name(String name);

    @Query("SELECT c FROM Customer c WHERE c.creditLimit > ?1 AND c.email LIKE %?2%")
    List<Customer> findCustomersWithCreditLimitGreaterThanAndEmailContaining(Double creditLimit, String email);

    List<Customer> findByEmail(String email_address);

    default boolean isCustomerEligibleForDiscount(Long customerId) {
        Customer customer = findById(customerId).orElse(null);
        if (customer == null) {
            return false;
        }
        return customer.getCreditLimit() > 5000.0;
    }
}
