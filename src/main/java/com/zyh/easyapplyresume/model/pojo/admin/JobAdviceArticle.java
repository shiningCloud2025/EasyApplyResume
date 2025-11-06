package com.zyh.easyapplyresume.model.pojo.admin;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_jobAdviceArticle")
public class JobAdviceArticle {
    /**
     * 求职攻略文章id
     */
    @TableId(value = "jobAdviceArticle_id", type = IdType.AUTO)
    private Integer jobAdviceArticleId;
    /**
     * 求职攻略文章title
     */
    @TableField("jobAdviceArticle_title")
    private String jobAdviceArticleTitle;
    /**
     * 求职攻略文章正文
     */
    @TableField("jobAdviceArticle_content")
    private String jobAdviceArticleContent;
    /**
     * 求职攻略文章分类
     */
    @TableField("jobAdviceArticle_category")
    private String jobAdviceArticleCategory;
    /**
     * 求职攻略文章标签
     */
    @TableField("jobAdviceArticle_tags")
    private String jobAdviceArticleTags;
    /**
     * 求职攻略文章作者名称
     */
    @TableField("jobAdviceArticle_authorName")
    private String jobAdviceArticleAuthorName;
    /**
     * 求职攻略状态
     */
    @TableField("jobAdviceArticle_publishedStatus")
    private Integer jobAdviceArticlePublishedStatus;
    /**
     * 求职攻略文章发布时间
     */
    @TableField("jobAdviceArticle_publishedTime")
    private DateTime jobAdviceArticlePublishedTime;
    /**
     * 求职攻略文章最后更新时间
     */
    @TableField("jobAdviceArticle_updatedTime")
    private DateTime jobAdviceArticleUpdatedTime;
    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;

}
