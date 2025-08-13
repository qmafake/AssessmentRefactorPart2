package za.co.tradelink.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer_name;

    private String email;

    private Double creditLimit;

    private String phn;

    public String address;

    @OneToMany(mappedBy = "customer")
    private List<Invoice> invoices;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return customer_name;
    }

    public void setName(String name) {
        this.customer_name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(Double creditLimit) { this.creditLimit = creditLimit; }

    public String getPhone() {
        return phn;
    }

    public void setPhone(String phone) {
        this.phn = phone;
    }
}
