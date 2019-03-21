package br.com.gcestaro.model.nplus1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SomeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String field1;

    private Integer field2;

    private Long field3;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Scenario scenario;

    @Override
    public String toString(){
        return this.getClass() + String.format("(id=%d, field1=%s, field2=%d, field3=%d)", id, field1, field2, field3);
    }
}
