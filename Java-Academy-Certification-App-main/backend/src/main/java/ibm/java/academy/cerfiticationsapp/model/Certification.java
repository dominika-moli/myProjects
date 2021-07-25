package ibm.java.academy.cerfiticationsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SequenceGenerator(name = "seq", sequenceName = "h2seq", initialValue = 1000, allocationSize = 10)
public class Certification extends Auditing<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    Long id;

    @Length(max = 50, message = "The name must be max 50 characters long.")
    String name;

    String url;

    @Column(precision = 10, scale = 2)
    BigDecimal price;

    @Length(max = 3, min = 3)
    String currency;

    @Enumerated(EnumType.STRING)
    State state;

    @OneToMany(mappedBy = "certification", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Voucher> vouchers;

    @OneToMany(mappedBy = "certification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviews;

    @ManyToMany
    @JsonIgnoreProperties("certifications")
    @JoinTable(name = "certification_skill", joinColumns = {
            @JoinColumn(name = "certification_id") }, inverseJoinColumns = @JoinColumn(name = "skill_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
                    "skill_id", "certification_id" }, name = "UQ_CERT_SKILL"))
    private List<Skill> skills;

    public enum State {
        ACTIVE, PROPOSED, NEW
    }

    public Certification(Long id, String name, String url, BigDecimal price, String currency, State state) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.price = price;
        this.currency = currency;
        this.state = state;
    }

    public Certification(String name, String url, BigDecimal price, String currency, State state) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.currency = currency;
        this.state = state;
    }

}
