package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.query.admin.AdminPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminPageVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminService;
import com.zyh.easyapplyresume.utils.Validator.AdminFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Integer addAdmin(AdminForm adminForm) {
        if(adminForm==null) {
            return 0;
        }
        AdminFormValidator.validateForAdd(adminForm);
        Admin admin = new Admin();
        admin.setAdminLoginTime(new Date());
        BeanUtils.copyProperties(adminForm, admin);
        return adminMapper.insert(admin);

    }

    @Override
    public Integer updateAdmin(AdminForm adminForm) {
        if(adminForm==null) {
            return 0;
        }
        AdminFormValidator.validateForUpdate(adminForm);
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminForm, admin);
        return adminMapper.updateById(admin);
    }

    @Override
    public Integer deleteAdmin(Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        admin.setDeleted(1);
        adminMapper.updateById(admin);
        return adminMapper.deleteRoleByAdminId(adminId);
    }

    @Override
    public AdminInfoVO findAdminById(Integer adminId) {
        return adminMapper.findAdminInfoById(adminId);
    }

    @Override
    public Page<AdminPageVO> findAdminByPage(Integer pageNum, Integer pageSize, AdminPageQuery adminPageQuery) {
        // 核心改造：QueryWrapper → LambdaQueryWrapper，用 Lambda 引用字段
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = lambdaQuery(Admin.class);
        lambdaQueryWrapper.eq(Admin::getDeleted, 0);
        if (adminPageQuery.getAdminEmail() != null && !adminPageQuery.getAdminEmail().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminEmail, adminPageQuery.getAdminEmail());
        }
        if (adminPageQuery.getAdminPhone() != null && !adminPageQuery.getAdminPhone().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminPhone, adminPageQuery.getAdminPhone());
        }
        if (adminPageQuery.getAdminState() != null) {
            lambdaQueryWrapper.eq(Admin::getAdminState, adminPageQuery.getAdminState());
        }
        if (adminPageQuery.getAdminUsername() != null && !adminPageQuery.getAdminUsername().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminUsername, adminPageQuery.getAdminUsername());
        }
        Page<Admin> adminPage = adminMapper.selectPage(new Page(pageNum, pageSize), lambdaQueryWrapper);
        List<AdminPageVO> voList = adminPage.getRecords().stream()
                .map(admin -> {
                    AdminPageVO vo = new AdminPageVO();
                    // 复制属性（要求Admin和AdminPageVO的字段名、类型一致）
                    BeanUtils.copyProperties(admin, vo);
                    // 如果有字段名不一致的情况，需要手动补充
                    // 例如：vo.setNewName(admin.getOldName());
                    return vo;
                })
                .collect(Collectors.toList());

        Page<AdminPageVO> adminVOPage = new Page<>();
        adminVOPage.setRecords(voList);         // 设置转换后的VO列表
        adminVOPage.setSize(adminPage.getSize());   // 每页条数
        adminVOPage.setCurrent(adminPage.getCurrent()); // 当前页码
        adminVOPage.setPages(adminPage.getPages()); // 总页数
        return  adminVOPage;
        
    }

    // 查找用户拥有的角色
    @Override
    public List<RoleInfoVO> findRoleByAdmin(Integer adminId) {
        return adminMapper.findRoleByAdmin(adminId);
    }

    @Override
    public Integer assignRoleToAdmin(Integer adminId, Integer[] roleIds) {
        int count = 0;
        if (roleIds==null){
            return 0;
        } else{
            adminMapper.deleteRoleByAdminId(adminId);
            for(int role:roleIds){
               count+= adminMapper.assignRoleToAdmin(adminId,role);
            }
        }
        return count;
    }
}
