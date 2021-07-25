package ibm.java.academy.cerfiticationsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private RoleName name;


    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("role")
    List<User> users;

    public enum RoleId {
        S, M, A
    }

    public enum RoleName {
        STUDENT, MANAGER, ADMINISTRATOR
    }

    public Role(String id, RoleName name) {
        this.id = id;
        this.name = name;
    }



}
