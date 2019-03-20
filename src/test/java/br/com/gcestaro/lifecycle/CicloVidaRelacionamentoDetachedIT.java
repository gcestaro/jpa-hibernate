package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.Category;
import br.com.gcestaro.model.Permission;
import br.com.gcestaro.model.User;
import org.junit.jupiter.api.*;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;

// FIXME
public class CicloVidaRelacionamentoDetachedIT {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void setUpEntityManagerFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("db_jpa_hibernate");
    }

    @AfterAll
    public static void tearDownEntityManagerFactory(){
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUp(){
        entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void tearDown(){
        entityManager.close();
    }

    @Test
    public void persistirUsuarioManagedComPermissaoDetachedNaoIraSalvarAlteracoesNaPermissao() {
        Permission admin = new Permission(Category.ADMIN);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.persist(admin);

        entityManager.getTransaction().commit();

        fecharSessaoAtualEIniciarOutraSessao();

        entityManager.getTransaction().begin();

        admin.setCategory(Category.USER);

        entityManager.persist(cestaro);

        entityManager.getTransaction().commit();

        Permission permissionSalva = entityManager.find(Permission.class, admin.getId());

        assertNotEquals(admin.getCategory(), permissionSalva.getCategory());
        assertEquals(Category.ADMIN, permissionSalva.getCategory());
        assertEquals(Category.USER, admin.getCategory());
    }

    @Test
    public void mergeUsuariManagedComPermissaoDetachedNaoIraSalvarAlteracoesNaPermissao() {
        Permission admin = new Permission(Category.ADMIN);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.persist(admin);

        entityManager.getTransaction().commit();

        fecharSessaoAtualEIniciarOutraSessao();

        entityManager.getTransaction().begin();

        admin.setCategory(Category.USER);

        entityManager.merge(cestaro);

        entityManager.getTransaction().commit();

        Permission permissionSalva = entityManager.find(Permission.class, admin.getId());

        assertNotEquals(admin.getCategory(), permissionSalva.getCategory());
        assertEquals(Category.ADMIN, permissionSalva.getCategory());
        assertEquals(Category.USER, admin.getCategory());
    }

    @Test
    public void removeUsuarioManagedComPermissaoDetachedNaoIraSalvarAlteracoesNaPermissao() {
        Permission admin = new Permission(Category.ADMIN);

        User cestaro = new User("cestaro", "hibernate");
        cestaro.addPermission(admin);

        entityManager.persist(admin);

        entityManager.getTransaction().commit();

        fecharSessaoAtualEIniciarOutraSessao();

        entityManager.getTransaction().begin();

        admin.setCategory(Category.USER);

        entityManager.remove(cestaro);

        entityManager.getTransaction().commit();

        Permission permissionSalva = entityManager.find(Permission.class, admin.getId());

        assertNotEquals(admin.getCategory(), permissionSalva.getCategory());
        assertEquals(Category.ADMIN, permissionSalva.getCategory());
        assertEquals(Category.USER, admin.getCategory());
    }

    private void fecharSessaoAtualEIniciarOutraSessao() {
        entityManager.close();

        entityManagerFactory.close();

        entityManagerFactory = Persistence.createEntityManagerFactory("db_jpa_hibernate");

        entityManager = entityManagerFactory.createEntityManager();
    }
}