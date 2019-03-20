package br.com.gcestaro.lifecycle;

import br.com.gcestaro.model.lifecycle.User;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

public class JpaSimpleLifeCycleIT extends JpaLifeCycleIT {

    @Test
    public void persistTransientUser() {
        doPersist();
        doCommit();
    }

    @Test
    public void mergeTransientUser() {
        doMerge();
        doCommit();
    }

    @Test
    public void removeTransientUser() {
        doRemove();
        doCommit();
    }

    @Test
    public void persistRemovedUserFails() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
        doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser();
    }

    @Test
    public void mergeUserRemoved() {
        doPersist();
        doCommit();
        doFindUserAsManaged();
        doRemove(managedUser);
        doCommit();
        newTransaction();
        doMerge();
        doCommit();
    }

    @Test
    public void mergeUserRemovedInAnotherSession() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
        doMerge();
        doCommit();
    }

    @Test
    public void removeUserAlreadyRemovedFails() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser();
    }

    @Test
    public void removedUserMustNotExistInAnotherSession() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doFindUserAsManaged();
        doRemove(managedUser);
        doCommit();
        closeCurrentAndStartNewSession();
        newTransaction();
        managedUserMustBeRemoved();
    }

    @Test
    public void persistDetachedUserFails() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser();
    }

    @Test
    public void mergeDetachedUser() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doMerge(detachedUser);
        doCommit();
    }

    @Test
    public void removeDetachedUserFails() {
        doPersist();
        doCommit();
        closeCurrentAndStartNewSession();
        doEmulateDetachedUser();
        newTransaction();
        doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser();
    }

    @Test
    public void persistManagedUserWorks() {
        doPersist();
        doPersist();
        doCommit();
    }

    @Test
    public void persistManagedUserWorksEvenInDifferentTransactions() {
        doPersist();
        doCommit();
        newTransaction();
        doPersist();
        doCommit();
    }

    @Test
    public void mergeManagedUserWorks() {
        doPersist();
        doMerge();
        doCommit();
    }

    @Test
    public void mergeManagedUserWorksEvenInDifferentTransactions() {
        doPersist();
        doCommit();
        newTransaction();
        doMerge();
        doCommit();
    }

    @Test
    public void removeManagedUserWorks() {
        doPersist();
        doRemove();
        doCommit();
    }

    @Test
    public void removeManagedUserWorksEvenInDifferentTransactions() {
        doPersist();
        doCommit();
        newTransaction();
        doRemove();
        doCommit();
    }

    private void doThrowsPersistenceExceptionCausedByPersistentObjectExceptionWhenPersistingDetachedUser() {
        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                () -> entityManager.persist(user));

        Throwable cause = persistenceException.getCause();
        assertNotNull(cause);
        assertEquals(PersistentObjectException.class, cause.getClass());
        assertEquals("detached entity passed to persist: br.com.gcestaro.model.lifecycle.User", cause.getMessage());
    }

    private void doThrowsIllegalArgumentExceptionWhenRemovingDetachedUser() {
        assertThrows(IllegalArgumentException.class,
                () -> entityManager.remove(user.getId()),
                "Removing a detached instance br.com.gcestaro.model.lifecycle.User#" + user.getId());
    }

    private void managedUserMustBeRemoved() {
        assertNull(entityManager.find(User.class, managedUser.getId()));
    }
}