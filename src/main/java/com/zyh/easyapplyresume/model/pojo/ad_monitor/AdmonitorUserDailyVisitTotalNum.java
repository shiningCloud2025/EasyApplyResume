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
 * 用户端每天访问数-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_userDailyVisitTotalNum")
public class AdmonitorUserDailyVisitTotalNum {

    @TableId(value = "userDailyVisitTotalNum_id",type = IdType.AUTO)
    private Integer userDailyVisitTotalNumId;

    @TableField("userDailyVisitTotalNum_date")
    private Date userDailyVisitTotalNumDate;

    @TableField("userDailyVisitTotalNum_num")
    private Integer userDailyVisitTotalNumNum;
}
