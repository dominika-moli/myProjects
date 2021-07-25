package ibm.java.academy.cerfiticationsapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seq", sequenceName = "h2seq", initialValue = 1000, allocationSize = 10)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(length = 100, nullable = false)
    private String voucherCode;

    @Nullable
    private Date validUntil;

    public enum State {
        ACTIVE, PENDING
    }
	
	@JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certification_id", nullable = false)
    private Certification certification;
    
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("vouchers")
    private User user;

    @Override
    public String toString() {
        return "Voucher [certification name=" + certification.getName() + ", id=" + id + ", state=" + state
                + ", user name and surname=" + (user != null ? user.getName() + " " + user.getSurname() : null)
                + ", validUntil=" + validUntil + ", voucherCode=" + voucherCode + "]";
    }
}