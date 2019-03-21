package br.com.gcestaro.model.lifecycle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
public class Permission implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(mappedBy = "permissions")
    private List<JpaUser> jpaUsers = new ArrayList<>();

    public Permission(Category category) {
        this.category = category;
    }

    public List<JpaUser> getJpaUsers() {
        return Collections.unmodifiableList(jpaUsers);
    }
}