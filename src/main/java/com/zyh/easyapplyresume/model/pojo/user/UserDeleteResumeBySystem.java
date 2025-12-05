package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户删除的简历类（系统回收版）-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_userDeleteResumeBySystem")
public class UserDeleteResumeBySystem {
    /**
     * 系统删除简历id
     */
    @TableId(value = "userDeleteResumeBySystem_id", type = IdType.AUTO)
    private Integer userDeleteResumeBySystemId;
    /**
     * 系统删除简历名称
     */
    @TableField("userDeleteResumeBySystem_resumeName")
    private String userDeleteResumeBySystemResumeName;
    /**
     * 系统删除简历行业
     */
    @TableField("userDeleteResumeBySystem_industry")
    private Integer userDeleteResumeBySystemIndustry;
    /**
     * 系统删除简历React组件代码
     */
    @TableField("userDeleteResumeBySystem_resumeReactCode")
    private String userDeleteResumeBySystemResumeReactCode;
    /**
     * 系统删除简历创建时间
     */
    @TableField("userDeleteResumeBySystem_createdTime")
    private Date userDeleteResumeBySystemCreatedTime;
    /**
     * 系统删除简历最后更新时间
     */
    @TableField("userDeleteResumeBySystem_updatedTime")
    private Date userDeleteResumeBySystemUpdatedTime;
    /**
     * 系统删除简历排序序号
     */
    @TableField("userDeleteResumeBySystem_sortedNum")
    private Integer userDeleteResumeBySystemSortedNum;
    /**
     * 系统删除简历所属用户
     */
    @TableField("userDeleteResumeBySystem_userId")
    private Integer userDeleteResumeBySystemUserId;
    /**
     * 简历删除时间
     */
    @TableField("userDeleteResumeBySystem_recycleTime")
    private Date userDeleteResumeBySystemRecycleTime;
}
