package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.user.UserQuery;
import com.zyh.easyapplyresume.model.query.user.UserSaveResumeQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * 用户保存简历控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/saveResume")
@Tag(name="用户保存简历控制器-用户端")
public class UserSaveResumeController {

    @Autowired
    private UserSaveResumeService userSaveResumeService;

    @PostMapping("/getUserSaveResumeInfoByUserId")
    @Operation(summary = "根据用户id查询用户保存的所有简历")
    public BaseResult<List<UserSaveResumeInfoVO>> getUserSaveResumeInfoByUserId(@RequestParam(required = true,value ="userId") Integer userId,
                                                                                @RequestBody(required = false) UserSaveResumeQuery userSaveResumeQuery){
        return BaseResult.ok(userSaveResumeService.getUserSaveResumeInfoByUserId(userId,userSaveResumeQuery));
    }

    @GetMapping("/getUserSaveResumeInfoByUserIdAndResumeId")
    @Operation(summary = "根据用户id和简历排序查询用户保存的简历")
    public BaseResult<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserIdAndResumeId(@RequestParam(required = true,value ="userId") Integer userId,
                                                                                     @RequestParam(required = true,value ="userSaveResumeSortedNum") Integer userSaveResumeSortedNum){
        return BaseResult.ok(userSaveResumeService.getUserSaveResumeInfoByUserIdAndResumeId(userId,userSaveResumeSortedNum));
    }

    @DeleteMapping("/deleteUserSaveResumeInfoByUserIdAndResumeId")
    @Operation(summary = "根据用户id和简历排序删除用户保存的简历")
    public BaseResult<?> deleteUserSaveResumeInfoByUserIdAndResumeId(@RequestParam(required = true,value ="userId") Integer userId,
                                                                          @RequestParam(required = true,value ="userSaveResumeSortedNum") Integer userSaveResumeSortedNum){
        userSaveResumeService.deleteUserSaveResumeInfoByUserIdAndResumeId(userId,userSaveResumeSortedNum);
        return BaseResult.ok();
    }

    @PostMapping("/saveUserSaveResumeInfo")
    @Operation(summary = "保存用户保存的简历")
    public BaseResult<String> saveUserSaveResumeInfo(@RequestBody UserSaveResumeInfoVO userSaveResumeInfoVO){
        userSaveResumeService.saveUserSaveResumeInfo(userSaveResumeInfoVO);
        return BaseResult.ok();
    }

    @PostMapping("/saveUserSaveResumeInfoFirst")
    @Operation(summary = "保存用户的简历(第一次添加，通过简历模版)")
    public BaseResult<String> saveUserSaveResumeInfoFirst(@RequestBody ResumeTemplateInfoVO resumeTemplateInfoVO,
                                                         @RequestParam(required = true,value ="userId") Integer userId,
                                                          @RequestParam(required = false,value ="resumeName") String resumeName){
        userSaveResumeService.saveUserSaveResumeInfoFirst(resumeTemplateInfoVO,userId,resumeName);
        return BaseResult.ok();
    }

    @PostMapping("/saveUserSaveResumeInfoFirstByImport")
    @Operation(summary = "保存用户的简历(第一次添加，通过已有简历导入)")
    public BaseResult<String> saveUserSaveResumeInfoFirstByImport(@RequestParam(required = true,value ="file") MultipartFile file,
                                                                  @RequestParam(required = true,value ="userId") Integer userId,
                                                                  @RequestParam(required = false,value ="resumeName") String resumeName,
                                                                  @RequestParam(required = true,value ="industryCode") Integer industryCode){
        userSaveResumeService.saveUserSaveResumeInfoFirstByImport(file,userId,resumeName,industryCode);
        return BaseResult.ok();
    }

    @PostMapping("/assistReactCodeByAI")
    @Operation(summary = "AI辅助优化React代码")
    public BaseResult<String> assistReactCodeByAI(@RequestParam(required = true,value ="userRequest") String userRequest,
                                                   @RequestParam(required = true,value ="currentReactCode") String currentReactCode){
        String optimizedCode = userSaveResumeService.assistReactCodeByAI(userRequest,currentReactCode);
        return BaseResult.ok(optimizedCode);
    }

    @GetMapping("/extractKeywordsByAI")
    @Operation(summary = "AI提取简历关键词")
    public BaseResult<Object> extractKeywordsByAI(@RequestParam(required = true,value ="userId") Integer userId,
                                                   @RequestParam(required = true,value ="resumeId") Integer resumeId){
        Object result = userSaveResumeService.extractResumeKeywordsByAI(userId, resumeId);
        return BaseResult.ok(result);
    }

    @GetMapping("/scoreByAI")
    @Operation(summary = "AI智能评分简历")
    public BaseResult<Object> scoreByAI(@RequestParam(required = true,value ="userId") Integer userId,
                                        @RequestParam(required = true,value ="resumeId") Integer resumeId){
        Object result = userSaveResumeService.scoreResumeByAI(userId, resumeId);
        return BaseResult.ok(result);
    }

    @GetMapping("/getFeedbackByAI")
    @Operation(summary = "AI生成简历反馈建议")
    public BaseResult<Object> getFeedbackByAI(@RequestParam(required = true,value ="userId") Integer userId,
                                               @RequestParam(required = true,value ="resumeId") Integer resumeId){
        Object result = userSaveResumeService.getResumeFeedbackByAI(userId, resumeId);
        return BaseResult.ok(result);
    }

    @GetMapping("/scoreByModel")
    @Operation(summary = "基于XgBoost模型进行评分")
    public BaseResult<Integer> scoreByModel(@RequestParam(required = true, value = "userId") Integer userId,
                                           @RequestParam(required = true, value = "resumeId") Integer resumeId) {
        Integer result = userSaveResumeService.scoreResumeByModel(userId, resumeId);
        return BaseResult.ok(result);
    }

    @PostMapping("/updateUserDeleteResumeName")
    @Operation(summary = "根据用户id和简历排序以及简历名称去修改简历名称")
    public BaseResult<Void> updateUserDeleteResumeName(@RequestParam(required = true,value = "userId") Integer userId,
                                                       @RequestParam(required = true,value = "resumeSortedNum") Integer resumeSortedNum,
                                                       @RequestParam(required = true,value = "resumeName") String resumeName){
        userSaveResumeService.updateUserDeleteResumeNameByUserIdAndResumeSortedNumAndResumeName(userId,resumeSortedNum,resumeName);
        return BaseResult.ok();
    }

}
