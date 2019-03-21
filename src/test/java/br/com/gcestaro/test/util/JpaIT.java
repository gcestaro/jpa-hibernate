package br.com.gcestaro.test.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class JpaIT {
    private static final String DB_NAME = "db_jpa_hibernate";

    protected static EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    @BeforeAll
    public static void setUpEntityManagerFactory() {
        createFactory();
    }

    @AfterAll
    public static void tearDownEntityManagerFactory() {
        closeFactory();
    }

    @BeforeEach
    public void setUp() {
        createEntityManager();
        newTransaction();
    }

    @AfterEach
    public void tearDown() {
        closeEntityManager();
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
        entityManagerFactory = Persistence.createEntityManagerFactory(DB_NAME);
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
