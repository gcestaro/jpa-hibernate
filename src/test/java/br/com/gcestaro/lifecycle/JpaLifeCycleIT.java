package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.lifecycle.JpaUser;
import br.com.gcestaro.test.util.JpaIT;
import br.com.gcestaro.test.util.UserTestMock;
import org.junit.jupiter.api.BeforeEach;

public abstract class JpaLifeCycleIT extends JpaIT {

    protected JpaUser jpaUser;

    protected JpaUser managedJpaUser;

    protected JpaUser detachedJpaUser;

    @BeforeEach
    @Override
    public void setUp() {
        createEntityManager();
        newTransaction();
        jpaUser = UserTestMock.userGiven();
    }

    protected void doFindUserAsManaged() {
        newTransaction();
        managedJpaUser = entityManager.find(JpaUser.class, jpaUser.getId());
    }

    protected void doEmulateDetachedUser() {
        detachedJpaUser = new JpaUser(jpaUser.getId());
    }

    protected void doMergeUser() {
        super.doMerge(jpaUser);
    }

    protected void doRemoveUser() {
        entityManager.remove(jpaUser);
    }

    protected void doPersistUser() {
        entityManager.persist(jpaUser);
    }
}
