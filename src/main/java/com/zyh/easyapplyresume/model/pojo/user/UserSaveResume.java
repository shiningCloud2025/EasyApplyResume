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
 * 用户自已的简历类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_userSaveResume")
public class UserSaveResume {
    /**
     * 简历主键ID
     */
    @TableId(value = "userSaveResume_id", type = IdType.AUTO)
    private Integer userSaveResumeId;
    /**
     * 简历名称
     */
    @TableField("userSaveResume_resumeName")
    private String userSaveResumeResumeName;
    /**
     * 简历行业
     */
    @TableField("userSaveResume_industry")
    private Integer userSaveResumeIndustry;
    /**
     * 简历React组件代码
     */
    @TableField("userSaveResume_resumeReactCode")
    private String userSaveResumeResumeReactCode;
    /**
     * 简历创建时间
     */
    @TableField("userSaveResume_createdTime")
    private Date userSaveResumeCreatedTime;
    /**
     * 简历最后更新时间
     */
    @TableField("userSaveResume_updatedTime")
    private Date userSaveResumeUpdatedTime;
    /**
     * 简历排序序号
     */
    @TableField("userSaveResume_sortedNum")
    private Integer userSaveResumeSortedNum;
    /**
     * 简历所属用户
     */
    @TableField("userSaveResume_userId")
    private Integer userSaveResumeUserId;
    /**
     * 逻辑删除
     */
    @TableField("deleted")
    private Integer deleted;




}
