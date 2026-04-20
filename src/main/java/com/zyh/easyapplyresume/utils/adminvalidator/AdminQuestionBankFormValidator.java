package com.zyh.easyapplyresume.utils.adminvalidator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;

import java.util.HashSet;
import java.util.Set;

/**
 * 题库题目表单校验工具类
 *
 * 题目类型：
 * 1 单选
 * 2 多选
 * 3 判断
 * 4 填空
 * 5 简答
 *
 * 说明：
 * 1. 这里只校验明确的 varchar 字段长度
 * 2. questionBankCode 是 longtext，这里不做长度校验
 * 3. 新增和修改的区别：
 *    - 新增不校验 questionBankId
 *    - 修改必须校验 questionBankId
 *
 * @author shiningCloud2025
 */
public final class AdminQuestionBankFormValidator {

    private static final int TYPE_SINGLE = 1;
    private static final int TYPE_MULTIPLE = 2;
    private static final int TYPE_JUDGE = 3;
    private static final int TYPE_BLANK = 4;
    private static final int TYPE_SHORT_ANSWER = 5;

    private AdminQuestionBankFormValidator() {
    }

    /**
     * 新增校验
     */
    public static void validateForAdd(AdminQuestionBankForm form) {
        if (form == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_FORM_EMPTY);
        }

        validateQuestionType(form.getQuestionBankType());
        validateByQuestionType(form);
    }

    /**
     * 修改校验
     */
    public static void validateForUpdate(AdminQuestionBankForm form) {
        if (form == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_FORM_EMPTY);
        }
        if (form.getQuestionBankId() == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_ID_EMPTY);
        }

        validateQuestionType(form.getQuestionBankType());
        validateByQuestionType(form);
    }

    /**
     * 按题型分发到对应校验逻辑
     */
    private static void validateByQuestionType(AdminQuestionBankForm form) {
        switch (form.getQuestionBankType()) {
            case TYPE_SINGLE:
                validateSingleChoice(form);
                break;
            case TYPE_MULTIPLE:
                validateMultipleChoice(form);
                break;
            case TYPE_JUDGE:
                validateJudge(form);
                break;
            case TYPE_BLANK:
                validateBlank(form);
                break;
            case TYPE_SHORT_ANSWER:
                validateShortAnswer(form);
                break;
            default:
                throw new BusException(AdminCodeEnum.QUESTION_BANK_TYPE_INVALID);
        }
    }

    /**
     * 单选题：
     * - 题目描述、大类/小类id和名称、难度、状态 必填
     * - A/B/C/D 必填，长度 <= 255
     * - 正确答案必填，长度 <= 100，且只能是 A/B/C/D
     * - 图片、解析可空，但如果有值要满足长度
     * - 参考答案必须为空
     */
    private static void validateSingleChoice(AdminQuestionBankForm form) {
        validateBaseRequiredFields(form);
        validateOptionalCommonLength(form);

        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionA(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionB(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionC(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_C_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_C_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionD(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_D_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_D_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankCorrectAnswer(),
                100,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_EMPTY,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_TOO_LONG
        );

        String answer = form.getQuestionBankCorrectAnswer().trim();
        if (!isOptionABCD(answer)) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_INVALID);
        }

        if (!isEmpty(form.getQuestionBankReferenceAnswer())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_MUST_EMPTY);
        }
    }

    /**
     * 多选题：
     * - 题目描述、大类/小类id和名称、难度、状态 必填
     * - A/B/C/D 必填，长度 <= 255
     * - 正确答案必填，长度 <= 100
     * - 正确答案按中文逗号分隔，拆分后至少两个答案
     * - 每个答案只能是 A/B/C/D，且不能重复
     * - 图片、解析可空，但如果有值要满足长度
     * - 参考答案必须为空
     */
    private static void validateMultipleChoice(AdminQuestionBankForm form) {
        validateBaseRequiredFields(form);
        validateOptionalCommonLength(form);

        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionA(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionB(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionC(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_C_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_C_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionD(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_D_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_D_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankCorrectAnswer(),
                100,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_EMPTY,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_TOO_LONG
        );

        if (!isEmpty(form.getQuestionBankReferenceAnswer())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_MUST_EMPTY);
        }

        String answer = form.getQuestionBankCorrectAnswer().trim();
        String[] parts = answer.split("，", -1);

        if (parts.length < 2) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_MULTIPLE_ANSWER_INVALID);
        }

        Set<String> answerSet = new HashSet<>();
        for (String part : parts) {
            String item = part.trim();

            if (item.isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_MULTIPLE_ANSWER_INVALID);
            }
            if (!isOptionABCD(item)) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_MULTIPLE_ANSWER_INVALID);
            }
            if (!answerSet.add(item)) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_MULTIPLE_ANSWER_INVALID);
            }
        }

        if (answerSet.size() < 2) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_MULTIPLE_ANSWER_INVALID);
        }
    }

    /**
     * 判断题：
     * - 题目描述、大类/小类id和名称、难度、状态 必填
     * - A/B 必填，长度 <= 255
     * - C/D 必须为空
     * - 正确答案必填，长度 <= 100，且只能是 A/B
     * - 图片、解析可空，但如果有值要满足长度
     * - 参考答案必须为空
     */
    private static void validateJudge(AdminQuestionBankForm form) {
        validateBaseRequiredFields(form);
        validateOptionalCommonLength(form);

        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionA(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_A_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankOptionB(),
                255,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_EMPTY,
                AdminCodeEnum.QUESTION_BANK_OPTION_B_TOO_LONG
        );
        validateRequiredTextWithMaxLength(
                form.getQuestionBankCorrectAnswer(),
                100,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_EMPTY,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_TOO_LONG
        );

        if (!isEmpty(form.getQuestionBankOptionC()) || !isEmpty(form.getQuestionBankOptionD())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_JUDGE_OPTION_INVALID);
        }

        String answer = form.getQuestionBankCorrectAnswer().trim();
        if (!"A".equals(answer) && !"B".equals(answer)) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_INVALID);
        }

        if (!isEmpty(form.getQuestionBankReferenceAnswer())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_MUST_EMPTY);
        }
    }

    /**
     * 填空题：
     * - 题目描述、大类/小类id和名称、难度、状态 必填
     * - 正确答案必填，长度 <= 100
     * - A/B/C/D 必须为空
     * - 图片、解析可空，但如果有值要满足长度
     * - 参考答案必须为空
     */
    private static void validateBlank(AdminQuestionBankForm form) {
        validateBaseRequiredFields(form);
        validateOptionalCommonLength(form);

        validateRequiredTextWithMaxLength(
                form.getQuestionBankCorrectAnswer(),
                100,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_EMPTY,
                AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_TOO_LONG
        );

        if (!isEmpty(form.getQuestionBankOptionA())
                || !isEmpty(form.getQuestionBankOptionB())
                || !isEmpty(form.getQuestionBankOptionC())
                || !isEmpty(form.getQuestionBankOptionD())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_OPTION_MUST_EMPTY);
        }

        if (!isEmpty(form.getQuestionBankReferenceAnswer())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_MUST_EMPTY);
        }
    }

    /**
     * 简答题：
     * - 题目描述、大类/小类id和名称、难度、状态 必填
     * - 参考答案必填，长度 <= 4000
     * - A/B/C/D 必须为空
     * - 正确答案必须为空
     * - 图片、解析可空，但如果有值要满足长度
     */
    private static void validateShortAnswer(AdminQuestionBankForm form) {
        validateBaseRequiredFields(form);
        validateOptionalCommonLength(form);

        validateRequiredTextWithMaxLength(
                form.getQuestionBankReferenceAnswer(),
                4000,
                AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_EMPTY,
                AdminCodeEnum.QUESTION_BANK_REFERENCE_ANSWER_TOO_LONG
        );

        if (!isEmpty(form.getQuestionBankOptionA())
                || !isEmpty(form.getQuestionBankOptionB())
                || !isEmpty(form.getQuestionBankOptionC())
                || !isEmpty(form.getQuestionBankOptionD())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_OPTION_MUST_EMPTY);
        }

        if (!isEmpty(form.getQuestionBankCorrectAnswer())) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_CORRECT_ANSWER_MUST_EMPTY);
        }
    }

    /**
     * 各题型共用的基础必填字段
     *
     * 这里不校验：
     * - questionBankCode 长度（因为它是 longtext）
     * - questionBankAnalysis 必填（你定的是可传 ""）
     * - questionBankImage 必填（你定的是可传 ""）
     */
    private static void validateBaseRequiredFields(AdminQuestionBankForm form) {
        validateRequiredTextWithMaxLength(
                form.getQuestionBankDescription(),
                2000,
                AdminCodeEnum.QUESTION_BANK_DESCRIPTION_EMPTY,
                AdminCodeEnum.QUESTION_BANK_DESCRIPTION_TOO_LONG
        );

        if (form.getQuestionFirstCategoryId() == null || form.getQuestionFirstCategoryId() <= 0) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
        }

        validateRequiredTextWithMaxLength(
                form.getQuestionFirstCategoryName(),
                20,
                AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY,
                AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG
        );

        if (form.getQuestionSecondCategoryId() == null || form.getQuestionSecondCategoryId() <= 0) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY);
        }

        validateRequiredTextWithMaxLength(
                form.getQuestionSecondCategoryName(),
                20,
                AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_EMPTY,
                AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG
        );

        if (form.getQuestionBankDifficulty() == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_DIFFICULTY_EMPTY);
        }

        if (form.getQuestionBankState() == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_STATE_EMPTY);
        }
    }

    /**
     * 各题型共用的可空长度校验
     */
    private static void validateOptionalCommonLength(AdminQuestionBankForm form) {
        validateOptionalTextMaxLength(
                form.getQuestionBankImage(),
                1024,
                AdminCodeEnum.QUESTION_BANK_IMAGE_TOO_LONG
        );

        validateOptionalTextMaxLength(
                form.getQuestionBankAnalysis(),
                2000,
                AdminCodeEnum.QUESTION_BANK_ANALYSIS_TOO_LONG
        );
    }

    /**
     * 题型本身必须存在，且只能是 1~5
     */
    private static void validateQuestionType(Integer questionType) {
        if (questionType == null) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_TYPE_EMPTY);
        }
        if (questionType < TYPE_SINGLE || questionType > TYPE_SHORT_ANSWER) {
            throw new BusException(AdminCodeEnum.QUESTION_BANK_TYPE_INVALID);
        }
    }

    /**
     * 必填字符串：
     * - 不能为空
     * - 不能超过最大长度
     */
    private static void validateRequiredTextWithMaxLength(String value,
                                                          int maxLength,
                                                          AdminCodeEnum emptyCode,
                                                          AdminCodeEnum tooLongCode) {
        if (isEmpty(value)) {
            throw new BusException(emptyCode);
        }
        if (value.trim().length() > maxLength) {
            throw new BusException(tooLongCode);
        }
    }

    /**
     * 非必填字符串：
     * - 如果为空，直接通过
     * - 如果不为空，则校验长度
     */
    private static void validateOptionalTextMaxLength(String value,
                                                      int maxLength,
                                                      AdminCodeEnum tooLongCode) {
        if (!isEmpty(value) && value.trim().length() > maxLength) {
            throw new BusException(tooLongCode);
        }
    }

    /**
     * 判断是否是单个 A/B/C/D 选项
     */
    private static boolean isOptionABCD(String value) {
        return "A".equals(value)
                || "B".equals(value)
                || "C".equals(value)
                || "D".equals(value);
    }

    /**
     * 这里把 null、""、"   " 都当成空
     */
    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
