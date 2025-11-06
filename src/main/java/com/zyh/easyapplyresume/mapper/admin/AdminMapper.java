package com.zyh.easyapplyresume.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author shiningCloud2025
 */
public interface AdminMapper extends BaseMapper<Admin> {
    // 根据admin的id删除对应的role
    void deleteRoleByAdminId(Integer adminId);

    // 根据admin的id查询admin以及对应的role和permission
    AdminInfoVO findAdminInfoById(Integer adminId);

    // 查找管理员拥有的角色
    List<RoleInfoVO> findRoleByAdmin(Integer adminId);

    // 给管理员分配角色
    void assignRoleToAdmin(@Param("adminId") Integer adminId,@Param("roleId") Integer roleId);

}
