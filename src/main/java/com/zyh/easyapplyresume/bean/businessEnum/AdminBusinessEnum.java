package com.zyh.easyapplyresume.bean.businessEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 管理端业务枚举类
 * @author shiningCloud2025
 */
@Getter
@AllArgsConstructor
public enum AdminBusinessEnum {

    //====================== 管理端反馈业务枚举类 ======================
    ADMIN_WAIT_RECEIVED(1, "待接受"),
    ADMIN_DO_RECEIVER(2, "待回复"),
    ADMIN_ALREADY_IGNORE(3, "已忽视"),
    ADMIN_ALREADY_REPLY(4, "已回复"),
    ADMIN_REJECT_REPLY(5, "已拒绝回复")








    ;
    private final Integer code;
    private final String message;
}
