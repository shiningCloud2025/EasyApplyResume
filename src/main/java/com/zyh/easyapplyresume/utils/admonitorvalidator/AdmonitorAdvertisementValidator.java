package com.zyh.easyapplyresume.utils.admonitorvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdMonitorCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdvertisementForm;

import java.util.Date;

/**
 * 广告表单检查工具类，用于验证广告表单数据（非管理员端通用场景）
 * 支持新增场景和修改场景的字段校验
 * @author shiningCloud2025
 */
public class AdmonitorAdvertisementValidator {

    // 广告名称最大长度（25位）
    private static final int ADVERTISEMENT_NAME_MAX_LENGTH = 25;
    // 广告链接最大长度（3000位）
    private static final int ADVERTISEMENT_LINK_MAX_LENGTH = 3000;

    /**
     * 广告新增场景校验：
     * 1. 广告名称：非空 + 空格处理 + 长度≤25
     * 2. 广告URL：非空 + 空格处理
     * 3. 广告链接：非空 + 空格处理 + 长度≤3000
     * 4. 开始时间/结束时间：非空 + 开始时间≤结束时间
     * @param admonitorAdvertisementForm 待验证的广告表单对象
     * @throws BusException 字段不满足规则时抛出
     */
    public static void validateForAdd(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        // 通用字段校验（新增/修改一致）
        validateCommonFields(admonitorAdvertisementForm);
    }

    /**
     * 广告修改场景校验：
     * 1. 广告名称：非空 + 空格处理 + 长度≤25
     * 2. 广告URL：非空 + 空格处理
     * 3. 广告链接：非空 + 空格处理 + 长度≤3000
     * 4. 开始时间/结束时间：非空 + 开始时间≤结束时间
     * 5. 广告主键：非空且大于0（修改场景必须有合法ID）
     * @param admonitorAdvertisementForm 待验证的广告表单对象
     * @throws BusException 字段不满足规则时抛出
     */
    public static void validateForUpdate(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        // 通用字段校验（新增/修改一致）
        validateCommonFields(admonitorAdvertisementForm);
        // 修改场景专属：校验广告主键ID
        validateAdvertisementId(admonitorAdvertisementForm);
    }

    /**
     * 私有辅助方法：校验新增/修改通用字段
     * @param form 待验证的广告表单对象
     * @throws BusException 字段不满足规则时抛出
     */
    private static void validateCommonFields(AdmonitorAdvertisementForm form) {
        // 1. 校验广告名称
        validateAdvertisementName(form);
        // 2. 校验广告URL
        validateAdvertisementUrl(form);
        // 3. 校验广告链接
        validateAdvertisementLink(form);
        // 4. 校验广告时间（开始/结束）
        validateAdvertisementTime(form);
    }

    /**
     * 私有辅助方法：校验广告名称
     * 1. 非空校验（null/纯空格）
     * 2. 去空格处理，更新回表单
     * 3. 长度校验（≤25）
     * @param form 待验证的广告表单对象
     * @throws BusException 名称不满足规则时抛出
     */
    private static void validateAdvertisementName(AdmonitorAdvertisementForm form) {
        String name = form.getAdvertisementName();
        // 非空校验
        if (name == null || name.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_NAME_EMPTY);
        }
        // 去空格处理，更新回表单（避免前端传参带多余空格）
        String trimedName = name.trim();
        form.setAdvertisementName(trimedName);
        // 长度校验
        if (trimedName.length() > ADVERTISEMENT_NAME_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_NAME_TOO_LONG);
        }
    }

    /**
     * 私有辅助方法：校验广告URL
     * 1. 非空校验（null/纯空格）
     * 2. 去空格处理，更新回表单
     * @param form 待验证的广告表单对象
     * @throws BusException URL不满足规则时抛出
     */
    private static void validateAdvertisementUrl(AdmonitorAdvertisementForm form) {
        String url = form.getAdvertisementUrl();
        // 非空校验
        if (url == null || url.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_URL_EMPTY);
        }
        // 去空格处理，更新回表单
        form.setAdvertisementUrl(url.trim());
    }

    /**
     * 私有辅助方法：校验广告链接
     * 1. 非空校验（null/纯空格）
     * 2. 去空格处理，更新回表单
     * 3. 长度校验（≤3000）
     * @param form 待验证的广告表单对象
     * @throws BusException 链接不满足规则时抛出
     */
    private static void validateAdvertisementLink(AdmonitorAdvertisementForm form) {
        String link = form.getAdvertisementLink();
        // 非空校验
        if (link == null || link.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_LINK_EMPTY);
        }
        // 去空格处理，更新回表单
        String trimedLink = link.trim();
        form.setAdvertisementLink(trimedLink);
        // 长度校验
        if (trimedLink.length() > ADVERTISEMENT_LINK_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_LINK_TOO_LONG);
        }
    }

    /**
     * 私有辅助方法：校验广告开始/结束时间
     * 1. 开始时间非空
     * 2. 结束时间非空
     * 3. 开始时间≤结束时间
     * @param form 待验证的广告表单对象
     * @throws BusException 时间不满足规则时抛出
     */
    private static void validateAdvertisementTime(AdmonitorAdvertisementForm form) {
        Date startTime = form.getAdvertisementStartedTime();
        Date endTime = form.getAdvertisementEndTime();

        // 开始时间非空校验
        if (startTime == null) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_START_TIME_EMPTY);
        }
        // 结束时间非空校验
        if (endTime == null) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_END_TIME_EMPTY);
        }
        // 开始时间不能晚于结束时间
        if (startTime.after(endTime)) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_TIME_ILLEGAL);
        }
    }

    /**
     * 私有辅助方法：校验广告主键（修改场景专用）
     * 1. 主键非空
     * 2. 主键大于0（合法的自增主键规则）
     * @param form 待验证的广告表单对象
     * @throws BusException 主键不满足规则时抛出
     */
    private static void validateAdvertisementId(AdmonitorAdvertisementForm form) {
        Integer id = form.getAdvertisementId();
        if (id == null || id <= 0) {
            throw new BusException(AdMonitorCodeEnum.ADVERTISEMENT_ID_ILLEGAL);
        }
    }
}