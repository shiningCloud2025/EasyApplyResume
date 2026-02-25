package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 人工客服实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_customerService")
public class AdminCustomerService {
    /**
     * 客服ID
     */
    @TableId(value = "customerService_id", type = IdType.AUTO)
    private Integer customerServiceId;
    /**
     * 客服标题
     */
    @TableField("customerService_title")
    private String customerServiceTitle;
    /**
     * 客服内容
     */
    @TableField("customerService_content")
    private String customerServiceContent;
    /**
     * 更新时间
     */
    @TableField("customerService_updatedTime")
    private Date customerServiceUpdatedTime;
}
