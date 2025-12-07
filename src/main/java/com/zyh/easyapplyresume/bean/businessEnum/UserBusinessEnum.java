package com.zyh.easyapplyresume.bean.businessEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户端业务枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum UserBusinessEnum {

    //====================== 用户端反馈业务枚举类 ======================
    USER_WAIT_RECEIVED(1, "待接受"),
    USER_DO_RECEIVER(2, "待回复"),
    USER_ALREADY_IGNORE(3, "已忽视"),
    USER_ALREADY_REPLY(4, "已回复"),
    USER_REJECT_REPLY(5, "已拒绝回复")





    ;
    private final Integer code;
    private final String message;
}
