package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserCollectionsMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserCollections;
import com.zyh.easyapplyresume.service.user.UserCollectionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class UserCollectionsServiceImpl implements UserCollectionsService {

    @Autowired
    private UserCollectionsMapper userCollectionsMapper;

    @Override
    public boolean isUserCollectResumeTemplate(Integer userId, Integer rtid) {
        try {
            log.info("用户是否收藏简历模版开始");
            LambdaQueryWrapper<UserCollections> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserCollections::getUid, userId);
            queryWrapper.eq(UserCollections::getRtid, rtid);
            if (userCollectionsMapper.selectOne(queryWrapper) != null) {
                return true;
            }
            log.info("用户是否收藏简历模版结束");
        }catch (Exception e){
            log.error("用户是否收藏简历模版异常");
        }
        return false;
    }

    @Override
    public void saveResumeTemplateByUserId(Integer userId, Integer rtid, boolean isCollect) {
        try{
            log.info("用户收藏/取消收藏简历模版");
            // 已经收藏了，取消收藏
            if (isCollect){
                LambdaQueryWrapper<UserCollections> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(UserCollections::getUid, userId);
                queryWrapper.eq(UserCollections::getRtid, rtid);
                userCollectionsMapper.delete(queryWrapper);
            }else {
                // 没有收藏，收藏
                UserCollections userCollections = new UserCollections();
                userCollections.setUid(userId);
                userCollections.setRtid(rtid);
                userCollectionsMapper.insert(userCollections);
            }
            log.info("用户收藏/取消收藏简历模版结束");
        }catch (Exception e){
            log.error("用户收藏/取消收藏简历模版异常");
        }

    }
}
