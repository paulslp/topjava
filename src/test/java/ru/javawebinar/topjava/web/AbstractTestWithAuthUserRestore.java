package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTestWithAuthUserRestore extends AbstractControllerTest{

    private int userId_before;


    @BeforeEach
    void saveUserId_before(){
        userId_before = SecurityUtil.authUserId();
    }

    @AfterEach
    void restoreAuthUserId(){
        SecurityUtil.setAuthUserId(userId_before);
    }
}
