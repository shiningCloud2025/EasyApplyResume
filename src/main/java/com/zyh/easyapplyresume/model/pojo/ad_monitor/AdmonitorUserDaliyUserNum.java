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
 * 用户端每天用户数-监测端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_userDaliyUserNum")
public class AdmonitorUserDaliyUserNum {
    @TableId(value = "userDaliyUserNum_id",type = IdType.AUTO)
    private Integer userDaliyUserNumId;

    @TableField("userDaliyUserNum_date")
    private Date userDaliyUserNumDate;

    @TableField("userDaliyUserNum_num")
    private Integer userDaliyUserNumNum;
}
