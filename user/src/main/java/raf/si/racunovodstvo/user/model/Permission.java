package raf.si.racunovodstvo.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity(name = "permission")
@Getter
@Setter
public class Permission implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private List<User> users;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return null;
    }
}
