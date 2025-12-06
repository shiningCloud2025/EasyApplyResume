package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  用户删除简历控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/deleteResume")
@Tag(name="用户删除简历控制器-用户端")
public class UserDeleteResumeController {
    @Autowired
    private UserDeleteResumeService userDeleteResumeService;

    @GetMapping("/getUserDeleteResumeInfoByUserId")
    @Operation(summary = "根据用户id查询用户删除的所有简历")
    public BaseResult<List<UserDeleteResumeInfoVO>> getUserDeleteResumeInfoByUserId(@RequestParam(required = true,value = "userId") Integer userId){
        return BaseResult.ok(userDeleteResumeService.getUserDeleteResumeInfoByUserId(userId));
    }

    @GetMapping("/getUserDeleteResumeInfoByUserIdAndResumeSortedNum")
    @Operation(summary = "根据用户id和简历排序查询用户删除的简历")
    public BaseResult<UserDeleteResumeInfoVO> getUserDeleteResumeInfoByUserIdAndResumeSortedNum(@RequestParam(required = true,value = "userId") Integer userId,
                                                                                                @RequestParam(required = true,value = "resumeSortedNum") Integer resumeSortedNum){
        return BaseResult.ok(userDeleteResumeService.getUserDeleteResumeInfoByUserIdAndResumeSortedNum(userId,resumeSortedNum));
    }

    @PostMapping("/addUserDeleteResumeToUserSaveResume")
    @Operation(summary = "从用户删除的简历中取出,放回用户保存的简历")
    public BaseResult<Void> addUserDeleteResumeToUserSaveResume(@RequestBody UserDeleteResumeInfoVO userDeleteResumeInfoVO){
        userDeleteResumeService.addUserDeleteResumeToUserSaveResume(userDeleteResumeInfoVO);
        return BaseResult.ok();
    }

    @DeleteMapping("/clearUserAllDeleteResume")
    @Operation(summary = "清空垃圾箱")
    public BaseResult<Void> clearUserAllDeleteResume(@RequestParam(required = true,value = "userId") Integer userId){
        userDeleteResumeService.clearUserAllDeleteResume(userId);
        return BaseResult.ok();
    }

    @PostMapping("/clearExpiredResume")
    @Operation(summary = "主动清理过时的简历")
    public BaseResult<Void> clearExpiredResume(){
        userDeleteResumeService.clearExpiredResume();
        return BaseResult.ok();
    }




}
