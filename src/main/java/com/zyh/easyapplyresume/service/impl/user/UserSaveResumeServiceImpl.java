package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.llmutils.doubao.ResumeToReactConverter;
import com.zyh.easyapplyresume.llmutils.doubao.AIResumeFeedbackGenerator;
import com.zyh.easyapplyresume.llmutils.zhipu.AIResumeScorer;
import com.zyh.easyapplyresume.llmutils.zhipu.ReactCodeAssistant;
import com.zyh.easyapplyresume.llmutils.zhipu.ResumeKeywordExtractor;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserSaveResumeMapper;
import com.zyh.easyapplyresume.model.pojo.admin.IndustryMap;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.query.user.UserSaveResumeQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
/**
 * @author shiningCloud2025
 */
@ServiceLog
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
    
    @Autowired
    private ResumeToReactConverter resumeToReactConverter;
    
    @Autowired
    private ReactCodeAssistant reactCodeAssistant;
    
    @Autowired
    private ResumeKeywordExtractor resumeKeywordExtractor;
    
    @Autowired
    private AIResumeScorer aiResumeScorer;
    
    @Autowired
    private AIResumeFeedbackGenerator aiResumeFeedbackGenerator;
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
    
    @Override
    public void saveUserSaveResumeInfoFirstByImport(MultipartFile file, Integer userId, String resumeName, Integer industryCode) {
        try{
            log.info("开始通过导入简历保存用户简历信息");
            
            String reactCode = resumeToReactConverter.convertResumeToReact(file);
            
            UserSaveResume userSaveResume = new UserSaveResume();
            userSaveResume.setUserSaveResumeResumeName(resumeName != null && !resumeName.isEmpty() ? resumeName : "导入的简历");
            userSaveResume.setUserSaveResumeIndustry(industryCode);
            userSaveResume.setUserSaveResumeResumeReactCode(reactCode);
            userSaveResume.setUserSaveResumeCreatedTime(new Date());
            userSaveResume.setUserSaveResumeUpdatedTime(new Date());
            
            LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
            List<UserSaveResume> userSaveResumes = userSaveResumeMapper.selectList(queryWrapper);
            
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
            log.info("通过导入简历保存用户简历信息成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("通过导入简历保存用户简历信息失败", e);
            throw new RuntimeException("通过导入简历保存用户简历信息失败");
        }
    }
    
    @Override
    public String assistReactCodeByAI(String userRequest, String currentReactCode) {
        try{
            log.info("开始AI辅助优化React代码");
            String optimizedCode = reactCodeAssistant.assistReactCode(userRequest, currentReactCode);
            log.info("AI辅助优化React代码成功");
            return optimizedCode;
        }catch (Exception e){
            log.error("AI辅助优化React代码失败", e);
            throw new RuntimeException("AI辅助优化React代码失败");
        }
    }
    
    @Override
    public Object extractResumeKeywordsByAI(Integer userId, Integer resumeId) {
        try{
            log.info("开始AI提取简历关键词");
            LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper.eq(UserSaveResume::getUserSaveResumeId, resumeId);
            UserSaveResume resume = userSaveResumeMapper.selectOne(queryWrapper);
            
            if (resume == null) {
                throw new RuntimeException("简历不存在或无权访问");
            }
            
            ResumeKeywordExtractor.KeywordExtractionResult result = 
                resumeKeywordExtractor.extractKeywords(resume.getUserSaveResumeResumeReactCode());
            
            log.info("AI提取简历关键词成功");
            return result;
        }catch (Exception e){
            log.error("AI提取简历关键词失败", e);
            throw new RuntimeException("AI提取简历关键词失败");
        }
    }
    
    @Override
    public Object scoreResumeByAI(Integer userId, Integer resumeId) {
        try{
            log.info("开始AI智能评分简历");
            LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper.eq(UserSaveResume::getUserSaveResumeId, resumeId);
            UserSaveResume resume = userSaveResumeMapper.selectOne(queryWrapper);
            
            if (resume == null) {
                throw new RuntimeException("简历不存在或无权访问");
            }
            
            AIResumeScorer.AIScoreResult result = 
                aiResumeScorer.scoreResume(resume.getUserSaveResumeResumeReactCode());
            
            log.info("AI智能评分简历成功，得分：{}", result.getTotalScore());
            return result;
        }catch (Exception e){
            log.error("AI智能评分简历失败", e);
            throw new RuntimeException("AI智能评分简历失败");
        }
    }
    
    @Override
    public Object getResumeFeedbackByAI(Integer userId, Integer resumeId) {
        try{
            log.info("开始AI生成简历反馈建议");
            LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper.eq(UserSaveResume::getUserSaveResumeId, resumeId);
            UserSaveResume resume = userSaveResumeMapper.selectOne(queryWrapper);
            
            if (resume == null) {
                throw new RuntimeException("简历不存在或无权访问");
            }
            
            AIResumeFeedbackGenerator.AIFeedbackResult result = 
                aiResumeFeedbackGenerator.generateFeedback(resume.getUserSaveResumeResumeReactCode());
            
            log.info("AI生成简历反馈建议成功");
            return result;
        }catch (Exception e){
            log.error("AI生成简历反馈建议失败", e);
            throw new RuntimeException("AI生成简历反馈建议失败");
        }
    }
    
    @Override
    public void updateUserDeleteResumeNameByUserIdAndResumeSortedNumAndResumeName(Integer userId, Integer resumeSortedNum, String resumeName) {
        LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        queryWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, resumeSortedNum);
        UserSaveResume userSaveResume = userSaveResumeMapper.selectOne(queryWrapper);
        userSaveResume.setUserSaveResumeResumeName(resumeName);
        userSaveResumeMapper.updateById(userSaveResume);
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
