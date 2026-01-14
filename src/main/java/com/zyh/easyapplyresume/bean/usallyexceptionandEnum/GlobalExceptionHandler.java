package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理器
 * @author shiningCloud2025
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理业务异常
    @ExceptionHandler(BusException.class)
    public BaseResult defaultExceptionHandler(BusException e){
        BaseResult baseResult = new BaseResult(e.getCode(),e.getMsg(),null);
        return  baseResult;
    }

    // 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public BaseResult handlerRuntimeException(RuntimeException e){
        e.printStackTrace();
        BaseResult baseResult = new BaseResult(99999,e.getMessage(),null);
        return baseResult;
    }

    // 处理系统异常
    @ExceptionHandler(Exception.class)
    public BaseResult defaultExceptionHandler(Exception e){
        e.printStackTrace();
        BaseResult baseResult = new BaseResult(AdminCodeEnum.SYSTEM_ERROR.getCode(), AdminCodeEnum.SYSTEM_ERROR.getMessage(),null );
        return baseResult;
    }
}
