package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserSaveResumeMapper;
import com.zyh.easyapplyresume.model.pojo.admin.IndustryMap;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserSaveResumeServiceImpl implements UserSaveResumeService {
    @Autowired
    private UserSaveResumeMapper userSaveResumeMapper;

    @Autowired
    private UserDeleteResumeService userDeleteResumeService;

    @Autowired
    private IndustryMapMapper industryMapMapper;
    @Override
    public List<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserId(Integer userSaveResumeUserId) {
        try{
            log.info("获取用户保存的简历信息开始");
            LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userSaveResumeUserId);
            lambdaQueryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
            List<UserSaveResume> userSaveResumeList = userSaveResumeMapper.selectList(lambdaQueryWrapper);
            if (userSaveResumeList != null){
                return BeanUtil.copyToList(userSaveResumeList, UserSaveResumeInfoVO.class);
            }
            log.info("获取用户保存的简历信息成功");
        }catch (Exception e){
            log.error("获取用户保存的简历信息失败");
        }

        return null;
    }

    @Override
    public UserSaveResumeInfoVO getUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum) {
        try{
            log.info("获取用户保存的简历信息开始");
            LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, userSaveResumeSortedNum);
            UserSaveResume userSaveResume = userSaveResumeMapper.selectOne(lambdaQueryWrapper);
            if (userSaveResume != null){
                return BeanUtil.copyProperties(userSaveResume, UserSaveResumeInfoVO.class);
            }
            log.info("获取用户保存的简历信息成功");
        }catch (Exception e){
            log.error("获取用户保存的简历信息失败");
        }
        return null;
    }

    @Override
    public void deleteUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum) {
        try{
            log.info("删除用户保存的简历信息开始");
            LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, userSaveResumeSortedNum);
            UserSaveResume userSaveResume = userSaveResumeMapper.selectOne(lambdaQueryWrapper);
            UserSaveResumeInfoVO userSaveResumeInfoVO = BeanUtil.copyProperties(userSaveResume, UserSaveResumeInfoVO.class);
            userDeleteResumeService.addUserDeleteSaveResume(userSaveResumeInfoVO);
            userSaveResumeMapper.delete(lambdaQueryWrapper);
            reorderResumeSortedNum(userId, userSaveResumeSortedNum);
            log.info("删除用户保存的简历信息成功");
        }catch (Exception e){
            log.error("删除用户保存的简历信息失败");
        }

    }

    @Override
    public void saveUserSaveResumeInfo(UserSaveResumeInfoVO userSaveResumeInfoVO) {
        try{
            log.info("保存用户保存的简历信息开始");
            userSaveResumeMapper.updateById(BeanUtil.copyProperties(userSaveResumeInfoVO, UserSaveResume.class));
            log.info("保存用户保存的简历信息成功");
        }catch (Exception e){
            log.error("保存用户保存的简历信息失败");
        }
    }


    @Override
    public void saveUserSaveResumeInfoFirst(ResumeTemplateInfoVO resumeTemplateInfoVO, Integer userId) {
        try{
            log.info("保存用户保存的简历信息开始");
            UserSaveResume userSaveResume = new UserSaveResume();
            userSaveResume.setUserSaveResumeResumeName(resumeTemplateInfoVO.getResumeTemplateName());
            LambdaQueryWrapper<IndustryMap> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(IndustryMap::getIndustryMapIndustryName, resumeTemplateInfoVO.getIndustryMapIndustryName());
            IndustryMap industryMap = industryMapMapper.selectOne(queryWrapper);
            userSaveResume.setUserSaveResumeIndustry(industryMap.getIndustryMapIndustryCode());
            userSaveResume.setUserSaveResumeResumeReactCode(resumeTemplateInfoVO.getResumeTemplateReactCode());
            userSaveResume.setUserSaveResumeCreatedTime(new Date());
            userSaveResume.setUserSaveResumeUpdatedTime(new Date());
            LambdaQueryWrapper<UserSaveResume> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper1.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
            List<UserSaveResume> userSaveResumes = userSaveResumeMapper.selectList(queryWrapper1);
            Integer userSaveResumeSortedNum = userSaveResumes.get(0).getUserSaveResumeSortedNum();
            if (userSaveResumeSortedNum>=4){
                throw new BusException(UserCodeEnum.USER_SAVE_RESUME_NOT_DAYU_FIVE);
            }
            userSaveResume.setUserSaveResumeSortedNum(userSaveResumeSortedNum+1);
            userSaveResume.setUserSaveResumeUserId(userId);
            userSaveResumeMapper.insert(userSaveResume);
            log.info("保存用户保存的简历信息成功");
        }catch (Exception e){
            log.error("保存用户保存的简历信息失败");
        }

    }

    private void reorderResumeSortedNum(Integer userId, Integer deletedSortedNum) {
        LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        queryWrapper.gt(UserSaveResume::getUserSaveResumeSortedNum, deletedSortedNum);
        queryWrapper.orderByAsc(UserSaveResume::getUserSaveResumeSortedNum);
        List<UserSaveResume> resumeList = userSaveResumeMapper.selectList(queryWrapper);
        
        if (resumeList != null && !resumeList.isEmpty()) {
            for (UserSaveResume resume : resumeList) {
                LambdaUpdateWrapper<UserSaveResume> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(UserSaveResume::getUserSaveResumeId, resume.getUserSaveResumeId());
                updateWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, resume.getUserSaveResumeSortedNum());
                updateWrapper.set(UserSaveResume::getUserSaveResumeSortedNum, resume.getUserSaveResumeSortedNum() - 1);
                userSaveResumeMapper.update(null, updateWrapper);
            }
        }
    }
}
