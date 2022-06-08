package raf.si.racunovodstvo.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    private String username;
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_permissions",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "userId"),
        inverseJoinColumns = @JoinColumn(
            name = "permission_id", referencedColumnName = "id"))
    private List<Permission> permissions;
}
