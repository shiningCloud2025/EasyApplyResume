package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminTeamIntroduceForm;

/**
 * AdminTeamIntroduceForm检查工具类，用于验证团队介绍表单数据
 * 核心规则：标题和内容不能为空
 * @author shiningCloud2025
 */
public class AdminTeamIntroduceValidator {

    /**
     * 新增团队介绍校验
     */
    public static void validateForAdd(AdminTeamIntroduceForm teamIntroduceForm) {
        validateTitle(teamIntroduceForm);
        validateContent(teamIntroduceForm);
    }

    /**
     * 修改团队介绍校验
     */
    public static void validateForUpdate(AdminTeamIntroduceForm teamIntroduceForm) {
        validateId(teamIntroduceForm);
        validateTitle(teamIntroduceForm);
        validateContent(teamIntroduceForm);
    }

    /**
     * 校验ID
     */
    private static void validateId(AdminTeamIntroduceForm teamIntroduceForm) {
        Integer id = teamIntroduceForm.getTeamIntroduceId();
        if (id == null || id <= 0) {
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 校验标题：不能为空
     */
    private static void validateTitle(AdminTeamIntroduceForm teamIntroduceForm) {
        String title = teamIntroduceForm.getTeamIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.TEAM_INTRODUCE_TITLE_EMPTY);
        }
        teamIntroduceForm.setTeamIntroduceTitle(title.trim());
    }

    /**
     * 校验内容：不能为空
     */
    private static void validateContent(AdminTeamIntroduceForm teamIntroduceForm) {
        String content = teamIntroduceForm.getTeamIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.TEAM_INTRODUCE_CONTENT_EMPTY);
        }
        teamIntroduceForm.setTeamIntroduceContent(content.trim());
    }
}