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
 * 管理端每天访问数-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_adminDailyVisitTotalNum")
public class AdmonitorAdminDailyVisitTotalNum {

    @TableId(value = "adminDailyVisitTotalNum_id",type = IdType.AUTO)
    private Integer adminDailyVisitTotalNumId;

    @TableField("adminDailyVisitTotalNum_date")
    private Date adminDailyVisitTotalNumDate;

    @TableField("adminDailyVisitTotalNum_num")
    private Integer adminDailyVisitTotalNumNum;

}
