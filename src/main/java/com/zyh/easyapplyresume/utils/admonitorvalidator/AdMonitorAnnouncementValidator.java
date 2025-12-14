package com.zyh.easyapplyresume.utils.admonitorvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdMonitorCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAnnouncementForm;

/**
 * AdMonitorAnnouncementForm检查工具类，用于验证监测端公告表单数据
 * 支持新增场景和修改场景的字段校验
 * @author shiningCloud2025
 */
public class AdMonitorAnnouncementValidator {

    // 公告标题最大长度（35位）
    private static final int ANNOUNCEMENT_TITLE_MAX_LENGTH = 35;
    // 公告内容默认值（当内容为空时使用）
    private static final String ANNOUNCEMENT_CONTENT_DEFAULT = "暂无公告";

    /**
     * 监测端公告新增场景校验：
     * 1. 标题非空 + 空格处理 + 长度校验
     * 2. 内容可为空，为空时设置默认值“暂无公告”
     * @param adMonitorAnnouncementForm 待验证的监测端公告表单对象
     * @throws BusException 标题为空或长度超出限制时抛出
     */
    public static void validateForAdd(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        // 校验公告标题（新增/修改通用逻辑）
        validateTitle(adMonitorAnnouncementForm);
        // 处理新增场景的公告内容（空值设默认）
        processContentForAdd(adMonitorAnnouncementForm);
    }

    /**
     * 监测端公告修改场景校验：
     * 1. 标题非空 + 空格处理 + 长度校验
     * 2. 内容非空 + 空格处理（不允许为空）
     * @param adMonitorAnnouncementForm 待验证的监测端公告表单对象
     * @throws BusException 标题/内容为空或标题长度超出限制时抛出
     */
    public static void validateForUpdate(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        // 校验公告标题（新增/修改通用逻辑）
        validateTitle(adMonitorAnnouncementForm);
        // 校验修改场景的公告内容（必须非空）
        validateContentForUpdate(adMonitorAnnouncementForm);
    }

    /**
     * 私有辅助方法：校验公告标题（新增/修改通用）
     * 1. 非空校验（与表单@NotNull注解互补，二次保障）
     * 2. 去空格处理，并更新回表单对象
     * 3. 长度校验（trim()处理避免纯空格占用长度）
     * @param adMonitorAnnouncementForm 待验证的监测端公告表单对象
     * @throws BusException 标题为空或长度超出限制时抛出
     */
    private static void validateTitle(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        // 1. 非空校验（包含null和纯空格场景）
        if (adMonitorAnnouncementForm.getAnnouncementTitle() == null || adMonitorAnnouncementForm.getAnnouncementTitle().trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.ANNOUNCEMENT_TITLE_EMPTY);
        }

        // 2. 去空格处理，更新回表单（避免前端传参带多余空格）
        String trimedTitle = adMonitorAnnouncementForm.getAnnouncementTitle().trim();
        adMonitorAnnouncementForm.setAnnouncementTitle(trimedTitle);

        // 3. 长度校验（限制35字符内）
        if (trimedTitle.length() > ANNOUNCEMENT_TITLE_MAX_LENGTH) {
            throw new BusException(AdMonitorCodeEnum.ANNOUNCEMENT_TITLE_TOO_LONG);
        }
    }

    /**
     * 私有辅助方法：处理新增场景的公告内容
     * 内容为空（含null/纯空格）时，设置为默认值“暂无公告”；否则去空格后更新回表单
     * @param adMonitorAnnouncementForm 待处理的监测端公告表单对象
     */
    private static void processContentForAdd(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        String content = adMonitorAnnouncementForm.getAnnouncementContent();
        // 空值场景：设为默认值
        if (content == null || content.trim().isEmpty()) {
            adMonitorAnnouncementForm.setAnnouncementContent(ANNOUNCEMENT_CONTENT_DEFAULT);
        } else {
            // 非空场景：去空格后更新
            adMonitorAnnouncementForm.setAnnouncementContent(content.trim());
        }
    }

    /**
     * 私有辅助方法：校验修改场景的公告内容
     * 1. 非空校验（不允许null/纯空格）
     * 2. 去空格处理，并更新回表单对象
     * @param adMonitorAnnouncementForm 待验证的监测端公告表单对象
     * @throws BusException 内容为空时抛出
     */
    private static void validateContentForUpdate(AdMonitorAnnouncementForm adMonitorAnnouncementForm) {
        String content = adMonitorAnnouncementForm.getAnnouncementContent();
        // 1. 非空校验（修改场景必须有内容）
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdMonitorCodeEnum.ANNOUNCEMENT_CONTENT_EMPTY);
        }

        // 2. 去空格处理，更新回表单
        adMonitorAnnouncementForm.setAnnouncementContent(content.trim());
    }
}