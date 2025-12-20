package com.zyh.easyapplyresume.mapper.mysql.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.admin.ResumeTemplate;
import com.zyh.easyapplyresume.model.pojo.user.UserCollections;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserCollectionsMapper extends BaseMapper<UserCollections> {

    // 根据用户id查询用户收藏的简历模版
    List<ResumeTemplate> selectResumeTemplateByUserId(@Param("userId") Integer userId,@Param("resumeTemplateName") String resumeTemplateName);

}
