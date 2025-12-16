package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserAuthControllerTest {
    @Autowired
    private UserAuthController userAuthController;

    @Test
    public void formalRegisterTest(){
        FormalRegisterForm formalRegisterForm = new FormalRegisterForm();
        formalRegisterForm.setUserAccount("xxx");
        formalRegisterForm.setUserUsername("xxx");
        formalRegisterForm.setUserEmail("xxxx@qq.com");
        formalRegisterForm.setUserPhone("xx");
        formalRegisterForm.setUserPassword("xxxxxxx");
        formalRegisterForm.setUserIntroduce("官方正版号");
        userAuthController.formalRegister(formalRegisterForm);



    }
}
