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
 * 用户端某天访问记录-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_userDailyVisitNum")
public class AdmonitorUserDailyVisitNum {
    /**
     * 记录id（主键，自增）
     */
    @TableId(value = "userDailyVisitNum_id",type = IdType.AUTO)
    private Long userDailyVisitNumId;

    /**
     * 访问管理员id
     */
    @TableField("userDailyVisitNum_userId")
    private Integer userDailyVisitNumAdminId;

    /**
     * 访问呢日期
     */
    @TableField("userDailyVisitNum_visitTime")
    private Date userDailyVisitNumVisitTime;
}
