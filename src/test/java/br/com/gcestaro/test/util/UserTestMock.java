package br.com.gcestaro.test.util;

import br.com.gcestaro.model.lifecycle.User;

public final class UserTestMock {

    private UserTestMock(){
        throw new IllegalStateException("Do not instantiate this");
    }

    public static User userGiven() {
        return new User("user", "hibernate");
    }
}
