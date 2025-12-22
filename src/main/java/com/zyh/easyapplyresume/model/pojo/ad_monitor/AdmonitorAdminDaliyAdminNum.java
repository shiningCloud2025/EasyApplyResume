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
 * 管理端管理员数量记录-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_adminDaliyAdminNum")
public class AdmonitorAdminDaliyAdminNum {

    @TableId(value = "adminDaliyAdminNum_id",type = IdType.AUTO)
    private Integer adminDaliyAdminNumId;

    @TableField("adminDaliyAdminNum_date")
    private Date adminDaliyAdminNumDate;

    @TableField("adminDaliyAdminNum_num")
    private Integer adminDaliyAdminNumNum;


}
