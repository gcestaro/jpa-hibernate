package br.com.gcestaro.model.nplus1;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

/**
 * Discriminator is used here just to show that it is possible. It must to be used very carefully because of coupling.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SCENARIO_TYPE", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
        @NamedQuery(name = "Scenario.joinFetchSolution", query = "select distinct s from Scenario s join fetch s.entities"),
        @NamedQuery(name = "Scenario.n+1", query = "select s from Scenario s where s.scenarioType = :type")
})
public abstract class Scenario {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "SCENARIO_TYPE", nullable = false, insertable = false, updatable = false)
    private ScenarioType scenarioType;

    public Scenario(ScenarioType scenarioType) {
        this.scenarioType = scenarioType;
    }

    public abstract void addEntity(SomeEntity entity);

    public abstract List<SomeEntity> getEntities();
}
