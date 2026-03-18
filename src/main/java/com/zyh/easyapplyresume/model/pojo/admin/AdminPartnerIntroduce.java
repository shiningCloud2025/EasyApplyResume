package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 合作伙伴实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_partnerIntroduce")
public class AdminPartnerIntroduce {
    /**
     * 合作伙伴id（主键，自增）
     */
    @TableId(value = "partnerIntroduce_id", type = IdType.AUTO)
    private Integer partnerIntroduceId;
    /**
     * 合作伙伴标题
     */
    @TableField("partnerIntroduce_title")
    private String partnerIntroduceTitle;
    /**
     * 合作伙伴内容
     */
    @TableField("partnerIntroduce_content")
    private String partnerIntroduceContent;
    /**
     * 修改时间
     */
    @TableField("partnerIntroduce_updatedTime")
    private LocalDateTime partnerIntroduceUpdatedTime;
}
