package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.Category;
import br.com.gcestaro.model.Permission;
import br.com.gcestaro.model.User;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

// FIXME
import static org.junit.jupiter.api.Assertions.*;

public class CicloVidaRelacionamentoTransientIT {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void setUpEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("db_jpa_hibernate");
    }

    @AfterAll
    public static void tearDownEntityManagerFactory() {
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
        entityManager.close();
    }

    @Test
    public void persistirUsuarioTransientComPermissaoTransient() {
        Permission admin = new Permission(Category.ADMIN);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.persist(cestaro);

        RollbackException rollbackException = assertThrows(
                RollbackException.class,
                () -> entityManager.getTransaction().commit());

        Throwable primeiraCausa = rollbackException.getCause();

        assertNotNull(primeiraCausa);
        assertEquals(IllegalStateException.class, primeiraCausa.getClass());

        Throwable segundaCausa = primeiraCausa.getCause();

        assertNotNull(segundaCausa);
        assertEquals(TransientObjectException.class, segundaCausa.getClass());
        assertEquals("object references an unsaved transient instance - save the transient instance before flushing: br.com.gcestaro.model.Permission",
                segundaCausa.getMessage());
    }

    @Test
    public void mergeUsuarioTransientComPermissaoTransient() {
        Permission admin = new Permission(Category.NONE);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.merge(cestaro);

        RollbackException rollbackException = assertThrows(
                RollbackException.class,
                () -> entityManager.getTransaction().commit());

        Throwable primeiraCausa = rollbackException.getCause();

        assertNotNull(primeiraCausa);
        assertEquals(IllegalStateException.class, primeiraCausa.getClass());

        Throwable segundaCausa = primeiraCausa.getCause();

        assertNotNull(segundaCausa);
        assertEquals(TransientObjectException.class, segundaCausa.getClass());
        assertEquals("object references an unsaved transient instance - save the transient instance before flushing: br.com.gcestaro.model.Permission",
                segundaCausa.getMessage());
    }

    @Test
    public void removeUsuarioTransientComPermissaoTransient() {
        Permission admin = new Permission(Category.USER);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.remove(cestaro);

        entityManager.getTransaction().commit();
    }

    @Test
    public void mergeUsuarioDetachedComPermissaoTransient() {
        User cestaro = new User("cestaro", "hibernate");

        entityManager.persist(cestaro);

        entityManager.getTransaction().commit();

        fecharSessaoAtualEIniciarOutraSessao();

        Permission admin = new Permission(Category.ADMIN);

        cestaro.addPermission(admin);

        entityManager.getTransaction().begin();

        entityManager.merge(cestaro);

        RollbackException rollbackException = assertThrows(RollbackException.class,
                () -> entityManager.getTransaction().commit(),
                "Error while committing the transaction");

        Throwable causa1 = rollbackException.getCause();
        assertNotNull(causa1);
        assertEquals(IllegalStateException.class, causa1.getClass());

        Throwable causa2 = causa1.getCause();
        assertNotNull(causa2);
        assertEquals(TransientObjectException.class, causa2.getClass());
        assertEquals("object references an unsaved transient instance - save the transient instance before flushing: br.com.gcestaro.model.Permission", causa2.getMessage());
    }

    private void fecharSessaoAtualEIniciarOutraSessao() {
        entityManager.close();

        entityManagerFactory.close();

        entityManagerFactory = Persistence.createEntityManagerFactory("db_jpa_hibernate");

        entityManager = entityManagerFactory.createEntityManager();
    }
}