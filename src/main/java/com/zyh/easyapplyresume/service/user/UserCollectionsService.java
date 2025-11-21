package com.zyh.easyapplyresume.service.user;
/**
 * @author shiningCloud2025
 */
public interface UserCollectionsService {
    /**
     * 用户是否收藏某简历模版
     */
    public boolean isUserCollectResumeTemplate(Integer userId, Integer rtid);

    /**
     * 根据用户id收藏/取消收藏简历模版
     */
    public void  saveResumeTemplateByUserId(Integer userId, Integer rtid,boolean isCollect);

}
