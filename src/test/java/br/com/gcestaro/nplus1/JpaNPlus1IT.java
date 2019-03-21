package br.com.gcestaro.nplus1;

import br.com.gcestaro.model.nplus1.*;
import br.com.gcestaro.test.util.JpaIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;
import java.util.List;

public class JpaNPlus1IT extends JpaIT {

    private static void printEntities(Scenario scenario) {
        scenario.getEntities().forEach(System.out::println);
    }

    @AfterEach
    @Override
    public void tearDown() {
        entityManager.createNativeQuery("DELETE FROM SomeEntity").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM Scenario").executeUpdate();
        doCommit();

        closeEntityManager();
    }

    @Test
    public void eagerScenario() {
        givenEagerScenarioWithSomeEntity();
        givenEagerScenarioWithSomeEntity();
        givenEagerScenarioWithSomeEntity();

        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();

        Query query = entityManager.createNamedQuery("Scenario.n+1");
        query.setParameter("type", ScenarioType.EAGER);

        List<EagerScenario> scenarios = query.getResultList();
        scenarios.forEach(JpaNPlus1IT::printEntities);
    }

    @Test
    public void lazyScenario() {
        givenLazyScenarioWithSomeEntity();
        givenLazyScenarioWithSomeEntity();
        givenLazyScenarioWithSomeEntity();

        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();

        Query query = entityManager.createNamedQuery("Scenario.n+1");
        query.setParameter("type", ScenarioType.LAZY);

        List<LazyScenario> scenarios = query.getResultList();
        scenarios.forEach(JpaNPlus1IT::printEntities);
    }

    @Test
    public void joinFetchSolutionForAnyScenario(){
        givenEagerScenarioWithSomeEntity();
        givenLazyScenarioWithSomeEntity();

        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();

        Query query = entityManager.createNamedQuery("Scenario.joinFetchSolution");

        List<Scenario> scenarios = query.getResultList();

        scenarios.forEach(JpaNPlus1IT::printEntities);
    }

    private void givenEagerScenarioWithSomeEntity() {
        givenScenarioWithSomeEntity(new EagerScenario());
    }

    private void givenLazyScenarioWithSomeEntity() {
        givenScenarioWithSomeEntity(new LazyScenario());
    }

    private void givenScenarioWithSomeEntity(Scenario scenario) {
        doPersist(scenario);

        for (int i = 0; i < 10; i++) {
            SomeEntity someEntity = SomeEntity.builder()
                    .scenario(scenario)
                    .field1("Field " + i)
                    .field2(i)
                    .field3(Long.valueOf(i))
                    .build();

            doPersist(someEntity);

            scenario.addEntity(someEntity);
        }

        doPersist(scenario);
    }
}
