package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;
import com.zyh.easyapplyresume.model.pojo.admin.Role;
import com.zyh.easyapplyresume.model.query.admin.AdminPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminPageVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;

import java.util.List;
/**
 * @author shiningCloud2025
 */
public interface AdminService {
    /**
     * 新增管理员
     */
    public void addAdmin(AdminForm adminForm);

    /**
     * 修改管理员
     */
    public void updateAdmin(AdminForm adminForm);

    /**
     * 删除管理员
     */
    public void deleteAdmin(Integer adminId);

    /**
     * 查询管理员
     */
    public AdminInfoVO findAdminById(Integer adminId);

    /**
     * 分页查询
     */
    public Page<AdminPageVO> findAdminByPage(Integer pageNum, Integer pageSize, AdminPageQuery adminPageQuery);


    /**
     * 查看管理员拥有的角色
     */
    public List<RoleInfoVO> findRoleByAdmin(Integer adminId);

    /**
     * 为管理员分配角色
     */
    public void assignRoleToAdmin(Integer adminId, Integer[] roleIds);

}
