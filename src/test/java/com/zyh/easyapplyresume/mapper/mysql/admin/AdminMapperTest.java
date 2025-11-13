package com.zyh.easyapplyresume.mapper.mysql.admin;

import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminMapperTest {
    @Resource
    private AdminMapper adminMapper;
    @Test
    public void test() {
        Admin admin = new Admin();
        admin.setAdminUsername("测试");
        adminMapper.insert(admin);
    }
}
