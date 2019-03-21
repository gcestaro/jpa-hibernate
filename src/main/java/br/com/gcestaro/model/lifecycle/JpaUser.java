package br.com.gcestaro.model.lifecycle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "JPA_USER")
public class JpaUser implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

    @Getter
    private LocalDateTime creationDateTime;

    @ManyToMany
    private List<Permission> permissions;

    public JpaUser() {
        this(null);
    }

    public JpaUser(Long id) {
        this(null, null);
        this.id = id;
    }

    public JpaUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.permissions = new ArrayList<>();
        this.creationDateTime = LocalDateTime.now();
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }
}
