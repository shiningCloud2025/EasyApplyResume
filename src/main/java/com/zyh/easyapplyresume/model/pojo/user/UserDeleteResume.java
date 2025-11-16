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
 * 用户删除的简历类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_userDeleteResume")
public class UserDeleteResume {
    /**
     * 删除简历id
     */
    @TableId(value = "userDeleteResume_id", type = IdType.AUTO)
    private Integer userDeleteResumeId;
    /**
     * 删除简历名称
     */
    @TableField("userDeleteResume_resumeName")
    private String userDeleteResumeResumeName;
    /**
     * 删除简历行业
     */
    @TableField("userDeleteResume_industry")
    private Integer userDeleteResumeIndustry;
    /**
     * 删除简历React组件代码
     */
    @TableField("userDeleteResume_resumeReactCode")
    private String userDeleteResumeResumeReactCode;
    /**
     * 删除简历创建时间
     */
    @TableField("userDeleteResume_createdTime")
    private Date userDeleteResumeCreatedTime;
    /**
     * 删除简历最后更新时间
     */
    @TableField("userDeleteResume_updatedTime")
    private Date userDeleteResumeUpdatedTime;
    /**
     * 删除简历排序序号
     */
    @TableField("userDeleteResume_sortedNum")
    private Integer userDeleteResumeSortedNum;
    /**
     * 删除简历所属用户
     */
    @TableField("userDeleteResume_userId")
    private Integer userDeleteResumeUserId;
    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
}
