package ibm.java.academy.cerfiticationsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	Long id;

	String name;

	String surname;

	String email;

	String password;

	private String resetPasswordToken;

	public User(String name, String surname, String email) {
		this.name = name;
		this.surname = surname;
		this.email = email;
	}

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("user")
	List<Voucher> vouchers;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("user")
	List<Review> reviews;

	@JoinColumn(name = "role_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("users")
	private Role role;

	public User(String name, String surname, String email, Role role) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.role = role;
	}

}
