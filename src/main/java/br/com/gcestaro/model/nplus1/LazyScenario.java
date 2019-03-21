package br.com.gcestaro.model.nplus1;

import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue(value = ScenarioType.Scenarios.LAZY)
public class LazyScenario extends Scenario {

    @OneToMany(mappedBy = "scenario")
    private List<SomeEntity> entities;

    public LazyScenario() {
        super(ScenarioType.LAZY);
        entities = new ArrayList<>();
    }

    @Override
    public List<SomeEntity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public void addEntity(SomeEntity entity) {
        entities.add(entity);
    }
}
