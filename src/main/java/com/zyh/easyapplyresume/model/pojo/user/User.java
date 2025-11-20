package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户实体类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_user")
public class User {
    /**
     * 用户id（主键，自增）
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    /**
     * 用户名称
     */
    @TableField("user_username")
    private String userUsername;
    /**
     * 用户邮箱
     */
    @TableField("user_email")
    private String userEmail;
    /**
     * 用户手机
     */
    @TableField("user_phone")
    private String userPhone;
    /**
     * 用户密码
     */
    @TableField("user_password")
    private String userPassword;
    /**
     * 用户头像
     */
    @TableField("user_image")
    private String userImage;
    /**
     * 用户介绍
     */
    @TableField("user_introduce")
    private String userIntroduce;
    /**
     * 创建时间
     */
    @TableField("user_createdTime")
    private Date userCreatedTime;
    /**
     * 用户最近登录时间
     */
    @TableField("user_loginTime")
    private Date userLoginTime;
    /**
     * 用户目标岗位
     */
    @TableField("user_dreamPosition")
    private int userDreamPosition;
    /**
     * 用户希望的最低月薪
     */
    @TableField("user_dreamMinMonthSalary")
    private BigDecimal userDreamMinMonthSalary;
    /**
     * 用户希望的最高月薪
     */
    @TableField("user_dreamMaxMonthSalary")
    private BigDecimal userDreamMaxMonthSalary;
    /**
     * 用户希望的每周工作数
     */
    @TableField("user_dreamWeekWorkDayNum")
    private int userDreamWeekWorkDayNum;
    /**
     * 用户希望的福利待遇
     */
    @TableField("user_dreamGoodWelfare")
    private String userDreamGoodWelfare;
    /**
     * 用户地址(省级)
     */
    @TableField("user_recruitLocationFirst")
    private String userRecruitLocationFirst;
    /**
     * 用户地址(市级)
     */
    @TableField("user_recruitLocationSecond")
    private String userRecruitLocationSecond;
    /**
     * 用户详细地址
     */
    @TableField("user_recruitLocationDetail")
    private String userRecruitLocationDetail;
    /**
     * 用户大学编码
     */
    @TableField("user_universityCode")
    private int userUniversityCode;


}
