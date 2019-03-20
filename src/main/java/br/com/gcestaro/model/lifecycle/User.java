package br.com.gcestaro.model.lifecycle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String password;

    @Getter
    private LocalDateTime creationDateTime = LocalDateTime.now();

    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();

    public User(Long id){
        this.id = id;
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public void addPermission(Permission permission){
        permissions.add(permission);
    }
}
