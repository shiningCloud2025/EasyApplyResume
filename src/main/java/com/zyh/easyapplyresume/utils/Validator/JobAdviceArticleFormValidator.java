package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.model.form.admin.JobAdviceArticleForm;

/**
 * 求职攻略文章表单校验工具类（含默认值填充）
 * @author shiningCloud2025
 */
public class JobAdviceArticleFormValidator {

    // 统一长度限制：30字
    private static final int MAX_LENGTH = 30;
    // 默认值常量（统一管理，便于修改）
    private static final String DEFAULT_CATEGORY = "无分类";
    private static final String DEFAULT_TAGS = "无标签";
    private static final String DEFAULT_AUTHOR = "匿名者";
    private static final String DEFAULT_CONTENT = "无内容";

    /**
     * 新增场景校验：空字段填充默认值 + 长度限制
     * @param form 求职攻略文章表单
     * @throws BusException 校验失败抛出业务异常
     */
    public static void validateForAdd(JobAdviceArticleForm form) {
        // -------------------------- 第一步：填充默认值（空字段替换）--------------------------
        // 1. 文章分类：null/空串 → 无分类
        if (form.getJobAdviceArticleCategory() == null || form.getJobAdviceArticleCategory().trim().isEmpty()) {
            form.setJobAdviceArticleCategory(DEFAULT_CATEGORY);
        }
        // 2. 文章标签：null/空串 → 无标签
        if (form.getJobAdviceArticleTags() == null || form.getJobAdviceArticleTags().trim().isEmpty()) {
            form.setJobAdviceArticleTags(DEFAULT_TAGS);
        }
        // 3. 作者名称：null/空串 → 匿名者
        if (form.getJobAdviceArticleAuthorName() == null || form.getJobAdviceArticleAuthorName().trim().isEmpty()) {
            form.setJobAdviceArticleAuthorName(DEFAULT_AUTHOR);
        }
        // 4. 文章正文：null/空串 → 无内容
        if (form.getJobAdviceArticleContent() == null || form.getJobAdviceArticleContent().trim().isEmpty()) {
            form.setJobAdviceArticleContent(DEFAULT_CONTENT);
        }

        // -------------------------- 第二步：字段校验（填充后校验）--------------------------
        // 1. 标题校验：非空（呼应@NotNull注解）+ 长度≤30
        if (form.getJobAdviceArticleTitle() == null || form.getJobAdviceArticleTitle().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_TITLE_EMPTY);
        }
        if (form.getJobAdviceArticleTitle().trim().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_TITLE_TOO_LONG);
        }

        // 2. 文章分类：填充后长度≤30（默认值已满足，防手动修改默认值超标）
        if (form.getJobAdviceArticleCategory().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_CATEGORY_TOO_LONG);
        }

        // 3. 文章标签：填充后长度≤30
        if (form.getJobAdviceArticleTags().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_TAGS_TOO_LONG);
        }

        // 4. 作者名称：填充后长度≤30
        if (form.getJobAdviceArticleAuthorName().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_AUTHOR_NAME_TOO_LONG);
        }

        // 正文：用户未提长度限制，暂不校验（如需可添加，例如≤10000字）
    }

    /**
     * 修改场景校验：ID必填 + 空字段填充默认值 + 长度限制
     * @param form 求职攻略文章表单
     * @throws BusException 校验失败抛出业务异常
     */
    public static void validateForUpdate(JobAdviceArticleForm form) {
        // -------------------------- 第一步：核心字段非空校验（修改必填）--------------------------
        // 1. 文章ID：修改必须传主键
        if (form.getJobAdviceArticleId() == null) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_ID_EMPTY);
        }
        // 2. 文章标题：修改必须传标题
        if (form.getJobAdviceArticleTitle() == null || form.getJobAdviceArticleTitle().trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_UPDATE_TITLE_EMPTY);
        }

        // -------------------------- 第二步：填充默认值（空字段替换）--------------------------
        // 1. 文章分类：null/空串 → 无分类
        if (form.getJobAdviceArticleCategory() == null || form.getJobAdviceArticleCategory().trim().isEmpty()) {
            form.setJobAdviceArticleCategory(DEFAULT_CATEGORY);
        }
        // 2. 文章标签：null/空串 → 无标签
        if (form.getJobAdviceArticleTags() == null || form.getJobAdviceArticleTags().trim().isEmpty()) {
            form.setJobAdviceArticleTags(DEFAULT_TAGS);
        }
        // 3. 作者名称：null/空串 → 匿名者
        if (form.getJobAdviceArticleAuthorName() == null || form.getJobAdviceArticleAuthorName().trim().isEmpty()) {
            form.setJobAdviceArticleAuthorName(DEFAULT_AUTHOR);
        }
        // 4. 文章正文：null/空串 → 无内容
        if (form.getJobAdviceArticleContent() == null || form.getJobAdviceArticleContent().trim().isEmpty()) {
            form.setJobAdviceArticleContent(DEFAULT_CONTENT);
        }

        // -------------------------- 第三步：字段长度校验--------------------------
        // 1. 文章标题：长度≤30
        if (form.getJobAdviceArticleTitle().trim().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_TITLE_TOO_LONG);
        }
        // 2. 文章分类：长度≤30
        if (form.getJobAdviceArticleCategory().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_CATEGORY_TOO_LONG);
        }
        // 3. 文章标签：长度≤30
        if (form.getJobAdviceArticleTags().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_TAGS_TOO_LONG);
        }
        // 4. 作者名称：长度≤30
        if (form.getJobAdviceArticleAuthorName().length() > MAX_LENGTH) {
            throw new BusException(AdminCodeEnum.JOB_ADVICE_AUTHOR_NAME_TOO_LONG);
        }
    }
}