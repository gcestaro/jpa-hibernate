package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.lifecycle.Category;
import br.com.gcestaro.model.lifecycle.Permission;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;

import static org.junit.jupiter.api.Assertions.*;

public class JpaRelationshipLifeCycleIT extends JpaLifeCycleIT {

    private Permission permission;

    private Permission managedPermission;

    @Test
    public void removeUserWithTransientPermissionWorks() {
        givenUserWithUserPermission();
        doRemoveUser();
        doCommit();
    }

    @Test
    public void mergeUserWithTransientPermissionThrowsRollbackExceptionCausedByTransientObjectException() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        givenUserWithAdminPermission();
        newTransaction();
        doMergeUser();
        doThrowsRollbackExceptionCausedByTransientObjectExceptionWhenCommitingUserRefenencesToTransientPermission();
    }

    @Test
    public void persistUserWithDetachedPermissionUpdatedWontSaveChanges() {
        givenUserWithAdminPermission();
        doPersistAndCommitPermissionOnAnotherSession();
        updatePermissionToUserCategoryOnNewTransaction();
        doPersistUser();
        doCommit();
        closeSessionAndFindPermission();
        permissionMustNotChange();
    }

    @Test
    public void mergeUserWithDetachedPermissionUpdatedWontSaveChanges() {
        givenUserWithAdminPermission();
        doPersistAndCommitPermissionOnAnotherSession();
        updatePermissionToUserCategoryOnNewTransaction();
        doMergeUser();
        doCommit();
        closeSessionAndFindPermission();
        permissionMustNotChange();
    }

    @Test
    public void removeUserWithDetachedPermissionUpdatedWontSaveChanges() {
        givenUserWithAdminPermission();
        doPersistAndCommitPermissionOnAnotherSession();
        updatePermissionToUserCategoryOnNewTransaction();
        doRemoveUser();
        doCommit();
        closeSessionAndFindPermission();
        permissionMustNotChange();
    }

    private void closeSessionAndFindPermission() {
        closeCurrentAndStartNewSession();
        doFindPermission();
    }

    private void doPersistAndCommitPermissionOnAnotherSession() {
        doPersist(permission);
        doCommit();
        closeCurrentAndStartNewSession();
    }

    private void updatePermissionToUserCategoryOnNewTransaction() {
        newTransaction();
        updatePermissionToUserCategory();
    }

    private void permissionMustNotChange() {
        assertNotEquals(permission.getCategory(), managedPermission.getCategory());
        assertEquals(Category.ADMIN, managedPermission.getCategory());
        assertEquals(Category.USER, permission.getCategory());
    }

    private void doThrowsRollbackExceptionCausedByTransientObjectExceptionWhenCommitingUserRefenencesToTransientPermission() {
        RollbackException rollbackException = assertThrows(RollbackException.class,
                () -> entityManager.getTransaction().commit(),
                "Error while committing the transaction");

        Throwable cause1 = rollbackException.getCause();
        assertNotNull(cause1);
        assertEquals(IllegalStateException.class, cause1.getClass());

        Throwable cause2 = cause1.getCause();
        assertNotNull(cause2);
        assertEquals(TransientObjectException.class, cause2.getClass());
        assertEquals("object references an unsaved transient instance - save the transient instance before flushing: br.com.gcestaro.model.lifecycle.Permission",
                cause2.getMessage());
    }

    private void doFindPermission() {
        managedPermission = entityManager.find(Permission.class, permission.getId());
    }

    private void givenUserWithUserPermission() {
        addUserPermission(Category.USER);
    }

    private void givenUserWithAdminPermission() {
        addUserPermission(Category.ADMIN);
    }

    private void addUserPermission(Category category) {
        permission = new Permission(category);
        jpaUser.addPermission(permission);
    }

    private void updatePermissionToUserCategory() {
        permission.setCategory(Category.USER);
    }
}