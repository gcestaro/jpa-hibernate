package br.com.gcestaro.test.util;

import br.com.gcestaro.model.lifecycle.JpaUser;

public final class UserTestMock {

    private UserTestMock(){
        throw new IllegalStateException("Do not instantiate this");
    }

    public static JpaUser userGiven() {
        return new JpaUser("jpaUser", "hibernate");
    }
}
