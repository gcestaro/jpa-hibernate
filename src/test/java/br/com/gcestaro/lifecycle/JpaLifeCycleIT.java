package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.lifecycle.User;
import br.com.gcestaro.test.util.UserTestMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class JpaLifeCycleIT {

    protected static EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    protected User user;

    protected User managedUser;

    protected User detachedUser;

    @BeforeAll
    public static void setUpEntityManagerFactory(){
        createFactory();
    }

    @AfterAll
    public static void tearDownEntityManagerFactory(){
        closeFactory();
    }

    @BeforeEach
    public void setUp(){
        createEntityManager();
        newTransaction();
        user = UserTestMock.userGiven();
    }

    @AfterEach
    public void tearDown(){
        closeEntityManager();
    }

    protected void doFindUserAsManaged() {
        newTransaction();
        managedUser = entityManager.find(User.class, user.getId());
    }

    protected void doEmulateDetachedUser() {
        detachedUser = new User(user.getId());
    }

    protected void doMerge() {
        entityManager.merge(user);
    }

    protected void doRemove() {
        entityManager.remove(user);
    }

    protected void doPersist() {
        entityManager.persist(user);
    }

    protected void doMerge(Object obj) {
        entityManager.merge(obj);
    }

    protected void doRemove(Object obj) {
        entityManager.remove(obj);
    }

    protected void doPersist(Object obj) {
        entityManager.persist(obj);
    }

    protected static void createFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("db_jpa_hibernate");
    }

    protected void createEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    protected static void closeFactory() {
        entityManagerFactory.close();
    }

    protected void closeEntityManager() {
        entityManager.close();
    }

    protected void newTransaction() {
        entityManager.getTransaction().begin();
    }

    protected void doCommit() {
        entityManager.getTransaction().commit();
    }

    protected void closeCurrentAndStartNewSession() {
        closeEntityManager();
        closeFactory();
        createFactory();
        createEntityManager();
    }
}
