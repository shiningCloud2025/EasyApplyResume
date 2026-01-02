package com.zyh.easyapplyresume.utils.admonitorvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdMonitorCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineForm;

/**
 * 服务器信息表单检查工具类，用于验证服务器表单数据
 * 支持新增场景和修改场景的字段校验
 * @author shiningCloud2025
 */
public class AdmonitorServiceMachineFormValidator {

    // 服务器名称最大长度（25位）
    private static final int SERVICE_MACHINE_NAME_MAX_LENGTH = 25;
    // 外网IP/域名最大长度（255位）
    private static final int SERVICE_MACHINE_HOST_MAX_LENGTH = 255;
    // SSH端口最大位数（10位）
    private static final int SERVICE_MACHINE_PORT_MAX_LENGTH = 10;
    // 登录账号最大长度（60位）
    private static final int SERVICE_MACHINE_USERNAME_MAX_LENGTH = 60;
    // 登录密码最大长度（60位）
    private static final int SERVICE_MACHINE_PASSWORD_MAX_LENGTH = 60;
    // 备注最大长度（200位）
    private static final int SERVICE_MACHINE_REMARK_MAX_LENGTH = 200;

    /**
     * 新增场景校验：id可以为空，其他字段必填
     * @param form 待验证的服务器表单对象
     * @throws BusException 字段不满足规则时抛出
     */
    public static void validateForAdd(AdmonitorServiceMachineForm form) {
        validateCommonFields(form);
    }

    /**
     * 修改场景校验：id不能为空，其他字段必填
     * @param form 待验证的服务器表单对象
     * @throws BusException 字段不满足规则时抛出
     */
    public static void validateForUpdate(AdmonitorServiceMachineForm form) {
        validateCommonFields(form);
        validateServiceMachineId(form);
    }

    /**
     * 私有辅助方法：校验新增/修改通用字段
     */
    private static void validateCommonFields(AdmonitorServiceMachineForm form) {
        validateServiceMachineName(form);
        validateServiceMachineHost(form);
        validateServiceMachinePort(form);
        validateServiceMachineUsername(form);
        validateServiceMachinePassword(form);
        validateServiceMachineRemark(form);
    }

    /**
     * 校验服务器名称：非空 + 去空格 + 长度≤25
     */
    private static void validateServiceMachineName(AdmonitorServiceMachineForm form) {
        String name = form.getServiceMachineName();
        if (name == null || name.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_NAME_EMPTY);
        }
        String trimmedName = name.trim();
        form.setServiceMachineName(trimmedName);
        if (trimmedName.length() > SERVICE_MACHINE_NAME_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_NAME_TOO_LONG);
        }
    }

    /**
     * 校验外网IP/域名：非空 + 去空格 + 长度≤255
     */
    private static void validateServiceMachineHost(AdmonitorServiceMachineForm form) {
        String host = form.getServiceMachineHost();
        if (host == null || host.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_HOST_EMPTY);
        }
        String trimmedHost = host.trim();
        form.setServiceMachineHost(trimmedHost);
        if (trimmedHost.length() > SERVICE_MACHINE_HOST_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_HOST_TOO_LONG);
        }
    }

    /**
     * 校验SSH端口：非空 + 位数≤10
     */
    private static void validateServiceMachinePort(AdmonitorServiceMachineForm form) {
        Integer port = form.getServiceMachinePort();
        if (port == null) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PORT_EMPTY);
        }
        if (String.valueOf(port).length() > SERVICE_MACHINE_PORT_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PORT_TOO_LONG);
        }
    }

    /**
     * 校验登录账号：非空 + 去空格 + 长度≤60
     */
    private static void validateServiceMachineUsername(AdmonitorServiceMachineForm form) {
        String username = form.getServiceMachineUsername();
        if (username == null || username.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_USERNAME_EMPTY);
        }
        String trimmedUsername = username.trim();
        form.setServiceMachineUsername(trimmedUsername);
        if (trimmedUsername.length() > SERVICE_MACHINE_USERNAME_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_USERNAME_TOO_LONG);
        }
    }

    /**
     * 校验登录密码：非空 + 去空格 + 长度≤60
     */
    private static void validateServiceMachinePassword(AdmonitorServiceMachineForm form) {
        String password = form.getServiceMachinePassword();
        if (password == null || password.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PASSWORD_EMPTY);
        }
        String trimmedPassword = password.trim();
        form.setServiceMachinePassword(trimmedPassword);
        if (trimmedPassword.length() > SERVICE_MACHINE_PASSWORD_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_PASSWORD_TOO_LONG);
        }
    }

    /**
     * 校验备注：非空 + 去空格 + 长度≤200
     */
    private static void validateServiceMachineRemark(AdmonitorServiceMachineForm form) {
        String remark = form.getServiceMachineRemark();
        if (remark == null || remark.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_REMARK_EMPTY);
        }
        String trimmedRemark = remark.trim();
        form.setServiceMachineRemark(trimmedRemark);
        if (trimmedRemark.length() > SERVICE_MACHINE_REMARK_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_REMARK_TOO_LONG);
        }
    }

    /**
     * 校验主键ID：修改场景专用，id不能为空
     */
    private static void validateServiceMachineId(AdmonitorServiceMachineForm form) {
        Integer id = form.getServiceMachineId();
        if (id == null || id <= 0) {
            throw new BusException(AdMonitorCodeEnum.SERVICE_MACHINE_ID_ILLEGAL);
        }
    }
}