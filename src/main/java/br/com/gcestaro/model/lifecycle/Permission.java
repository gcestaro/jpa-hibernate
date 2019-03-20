package br.com.gcestaro.model.lifecycle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Permission implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(mappedBy = "permissions")
    private List<User> users = new ArrayList<>();

    public Permission(Category category){
        this.category = category;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }
}