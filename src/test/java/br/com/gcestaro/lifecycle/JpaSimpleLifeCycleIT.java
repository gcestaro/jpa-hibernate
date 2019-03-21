package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.lifecycle.JpaUser;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

public class JpaSimpleLifeCycleIT extends JpaLifeCycleIT {

    @Test
    public void persistTransientUser() {
        doPersistUser();
        doCommit();
    }

    @Test
    public void mergeTransientUser() {
        doMergeUser();
        doCommit();
    }

    @Test
    public void removeTransientUser() {
        doRemoveUser();
        doCommit();
    }

    @Test
    public void persistRemovedUserFails() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedJpaUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
        doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser();
    }

    @Test
    public void mergeUserRemoved() {
        doPersistUser();
        doCommit();
        doFindUserAsManaged();
        doRemove(managedJpaUser);
        doCommit();
        newTransaction();
        doMergeUser();
        doCommit();
    }

    @Test
    public void mergeUserRemovedInAnotherSession() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedJpaUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
        doMergeUser();
        doCommit();
    }

    @Test
    public void removeUserAlreadyRemovedFails() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedJpaUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser();
    }

    @Test
    public void removedUserMustNotExistInAnotherSession() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedJpaUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
    }

    @Test
    public void persistDetachedUserFails() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser();
    }

    @Test
    public void mergeDetachedUser() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doMerge(detachedJpaUser);
        doCommit();
    }

    @Test
    public void removeDetachedUserFails() {
        doPersistUser();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser();
    }

    @Test
    public void persistManagedUserWorks() {
        doPersistUser();
        doPersistUser();
        doCommit();
    }

    @Test
    public void persistManagedUserWorksEvenInDifferentTransactions() {
        doPersistUser();
        doCommit();
        newTransaction();
        doPersistUser();
        doCommit();
    }

    @Test
    public void mergeManagedUserWorks() {
        doPersistUser();
        doMergeUser();
        doCommit();
    }

    @Test
    public void mergeManagedUserWorksEvenInDifferentTransactions() {
        doPersistUser();
        doCommit();
        newTransaction();
        doMergeUser();
        doCommit();
    }

    @Test
    public void removeManagedUserWorks() {
        doPersistUser();
        doRemoveUser();
        doCommit();
    }

    @Test
    public void removeManagedUserWorksEvenInDifferentTransactions() {
        doPersistUser();
        doCommit();
        newTransaction();
        doRemoveUser();
        doCommit();
    }

    private void doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser() {
        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                () -> entityManager.persist(jpaUser));

        Throwable cause = persistenceException.getCause();
        assertNotNull(cause);
        assertEquals(PersistentObjectException.class, cause.getClass());
        assertEquals("detached entity passed to persist: br.com.gcestaro.model.lifecycle.JpaUser", cause.getMessage());
    }

    private void doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser() {
        assertThrows(IllegalArgumentException.class,
                () -> entityManager.remove(jpaUser.getId()),
                "Removing a detached instance br.com.gcestaro.model.lifecycle.JpaUser#" + jpaUser.getId());
    }

    private void managedUserMustBeRemoved() {
        assertNull(entityManager.find(JpaUser.class, managedJpaUser.getId()));
    }
}