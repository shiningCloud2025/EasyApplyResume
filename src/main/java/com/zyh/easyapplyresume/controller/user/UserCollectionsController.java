package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.user.UserCollectionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  用户收藏控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/userCollections")
@Tag(name="用户收藏控制器-用户端")
public class UserCollectionsController {
    @Autowired
    private UserCollectionsService userCollectionsService;

    @GetMapping("/isUserCollectResumeTemplate")
    @Operation(summary = "用户是否收藏简历模版")
    public BaseResult<Boolean> isUserCollectResumeTemplate(@RequestParam(required = true,value = "userId") Integer userId,
                                        @RequestParam(required = true,value = "rtid") Integer rtid) {
        return BaseResult.ok(userCollectionsService.isUserCollectResumeTemplate(userId, rtid));
    }

    @GetMapping("/saveResumeTemplateByUserId")
    @Operation(summary = "用户收藏/取消收藏简历模版")
    public BaseResult<?> saveResumeTemplateByUserId(@RequestParam(required = true,value = "userId") Integer userId,
                                           @RequestParam(required = true,value = "rtid") Integer rtid,
                                           @RequestParam(required = true,value = "isCollect") boolean isCollect) {
        userCollectionsService.saveResumeTemplateByUserId(userId, rtid, isCollect);
        return BaseResult.ok();
    }



}
