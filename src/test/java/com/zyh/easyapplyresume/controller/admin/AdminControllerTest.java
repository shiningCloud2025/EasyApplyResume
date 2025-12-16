package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminControllerTest {
    @Autowired
    private AdminController adminController;
    @Test
    public void testAddAdmin(){
        AdminForm admin = new AdminForm();
        admin.setAdminAccount("xxx");
        admin.setAdminUsername("xx");
        admin.setAdminEmail("xxxx@qq.com");
        admin.setAdminPhone("xxx");
        admin.setAdminPassword("xxxxxxxx");
        admin.setAdminIntroduce("至高无上的超级管理员");
        adminController.addAdmin(admin);
    }
}
