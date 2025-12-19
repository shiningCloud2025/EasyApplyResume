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

import java.util.List;

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

}
