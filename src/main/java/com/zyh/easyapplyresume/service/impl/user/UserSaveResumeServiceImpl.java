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
import com.zyh.easyapplyresume.model.query.user.UserSaveResumeQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
    public List<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserId(Integer userSaveResumeUserId, UserSaveResumeQuery userSaveResumeQuery) {
        try{
            log.info("获取用户保存的简历信息开始");
            LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userSaveResumeUserId);
            lambdaQueryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
            if (userSaveResumeQuery != null){
                if (userSaveResumeQuery.getUserSaveResumeResumeName() != null&& !userSaveResumeQuery.getUserSaveResumeResumeName().isEmpty()){
                    lambdaQueryWrapper.like(UserSaveResume::getUserSaveResumeResumeName, userSaveResumeQuery.getUserSaveResumeResumeName());
                }
                if (userSaveResumeQuery.getUserSaveResumeIndustry() != null&& userSaveResumeQuery.getUserSaveResumeIndustry() != 0){
                    lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeIndustry, userSaveResumeQuery.getUserSaveResumeIndustry());
                }
            }


            List<UserSaveResume> userSaveResumeList = userSaveResumeMapper.selectList(lambdaQueryWrapper);
            if (userSaveResumeList != null){
                List<UserSaveResumeInfoVO> userSaveResumeInfoVOS = BeanUtil.copyToList(userSaveResumeList, UserSaveResumeInfoVO.class);
                userSaveResumeInfoVOS.forEach(userSaveResumeInfoVO -> {
                    userSaveResumeInfoVO.setUserSaveResumeIndustryName(industryMapMapper.selectById(userSaveResumeInfoVO.getUserSaveResumeIndustry()).getIndustryMapIndustryName());
                });
                return userSaveResumeInfoVOS;
            }
            log.info("获取用户保存的简历信息成功");

            return null;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("获取用户保存的简历信息失败", e);
            throw new RuntimeException("获取用户保存的简历信息失败");
        }
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
                UserSaveResumeInfoVO userSaveResumeInfoVO = BeanUtil.copyProperties(userSaveResume, UserSaveResumeInfoVO.class);
                userSaveResumeInfoVO.setUserSaveResumeIndustryName(industryMapMapper.selectById(userSaveResumeInfoVO.getUserSaveResumeIndustry()).getIndustryMapIndustryName());
                return userSaveResumeInfoVO;
            }
            log.info("获取用户保存的简历信息成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("获取用户保存的简历信息失败", e);
            throw new RuntimeException("获取用户保存的简历信息失败");
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
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("删除用户保存的简历信息失败", e);
            throw new RuntimeException("删除用户保存的简历信息失败");
        }

    }

    @Override
    public void saveUserSaveResumeInfo(UserSaveResumeInfoVO userSaveResumeInfoVO) {
        try{
            log.info("保存用户保存的简历信息开始");
            userSaveResumeMapper.updateById(BeanUtil.copyProperties(userSaveResumeInfoVO, UserSaveResume.class));
            log.info("保存用户保存的简历信息成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("保存用户保存的简历信息失败", e);
            throw new RuntimeException("保存用户保存的简历信息失败");
        }
    }


    @Override
    public void saveUserSaveResumeInfoFirst(ResumeTemplateInfoVO resumeTemplateInfoVO, Integer userId,String resumeName) {
        try{
            log.info("保存用户保存的简历信息开始");
            UserSaveResume userSaveResume = new UserSaveResume();
            if (resumeName != null || !resumeName.isEmpty()){
                userSaveResume.setUserSaveResumeResumeName(resumeName);
            }else{
                userSaveResume.setUserSaveResumeResumeName(resumeTemplateInfoVO.getResumeTemplateName());
            }
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
            if (userSaveResumes.isEmpty()){
                userSaveResume.setUserSaveResumeSortedNum(0);
            }else{
                Integer userSaveResumeSortedNum = userSaveResumes.getFirst().getUserSaveResumeSortedNum();
                userSaveResume.setUserSaveResumeSortedNum(userSaveResumeSortedNum+1);
                if (userSaveResumeSortedNum>=4){
                    throw new BusException(UserCodeEnum.USER_SAVE_RESUME_NOT_DAYU_FIVE);
                }
            }
            userSaveResume.setUserSaveResumeUserId(userId);
            userSaveResumeMapper.insert(userSaveResume);
            log.info("保存用户保存的简历信息成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("保存用户保存的简历信息失败", e);
            throw new RuntimeException("保存用户保存的简历信息失败");
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
