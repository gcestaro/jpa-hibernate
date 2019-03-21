package br.com.gcestaro.model.nplus1;

import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue(value = ScenarioType.Scenarios.EAGER)
public class EagerScenario extends Scenario {

    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER)
    private List<SomeEntity> entities;

    public EagerScenario() {
        super(ScenarioType.EAGER);
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
