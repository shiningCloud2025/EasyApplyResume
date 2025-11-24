package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义业务异常/**
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusException extends RuntimeException{
    // 状态码
    private Integer code;
    //错误消息
    private String msg;

    public BusException(AdminCodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
    }
    public BusException(IndustryMapEnum industryMapEnum){
        this.code = industryMapEnum.getCode();
        this.msg = industryMapEnum.getMessage();
    }
}