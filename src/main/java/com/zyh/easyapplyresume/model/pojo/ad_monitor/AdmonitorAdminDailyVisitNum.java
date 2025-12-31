package com.zyh.easyapplyresume.model.pojo.ad_monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理端某天访问记录-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_adminDailyVisitNum")
public class AdmonitorAdminDailyVisitNum {
    /**
     * 记录id（主键，自增）
     */
    @TableId(value = "adminDailyVisitNum_id",type = IdType.AUTO)
    private Long adminDailyVisitNumId;

    /**
     * 访问管理员id
     */
    @TableField("adminDailyVisitNum_adminId")
    private Integer adminDailyVisitNumAdminId;

    /**
     * 访问呢日期
     */
    @TableField("adminDailyVisitNum_visitTime")
    private Date adminDailyVisitNumVisitTime;

}
