package com.zyh.easyapplyresume.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
/**
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
public class BaseResult<T> implements Serializable {
    // 状态码(成功:200，失败:其他)
    private Integer code;
    // 提示信息
    private String message;
    // 返回数据
    private T data;

    // 构建成功结果
    public static <T> BaseResult<T> ok(){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(),null);
    }
    // 构建带有数据的成功结果
    public static <T> BaseResult<T>ok(T data){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), data);
    }

}
