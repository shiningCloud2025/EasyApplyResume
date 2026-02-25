package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.exception.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminTeamIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCodeEnum;

public class AdminTeamIntroduceValidator {
    // 标题最大长度
    private static final int TITLE_MAX_LENGTH = 100;

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
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验标题
     */
    private static void validateTitle(AdminTeamIntroduceForm teamIntroduceForm) {
        String title = teamIntroduceForm.getTeamIntroduceTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        String trimmedTitle = title.trim();
        teamIntroduceForm.setTeamIntroduceTitle(trimmedTitle);
        if (trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
    }

    /**
     * 校验内容
     */
    private static void validateContent(AdminTeamIntroduceForm teamIntroduceForm) {
        String content = teamIntroduceForm.getTeamIntroduceContent();
        if (content == null || content.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.PARAM_ERROR);
        }
        teamIntroduceForm.setTeamIntroduceContent(content.trim());
    }
}