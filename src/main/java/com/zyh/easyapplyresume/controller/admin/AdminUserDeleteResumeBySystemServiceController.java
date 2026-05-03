package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.query.user.UserDeleteResumeQuery;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeBySystemInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeBySystemPageVO;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统回收用户删除的简历控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/userDeleteResumeBySystemService")
@Tag(name = "系统回收用户删除的简历控制器-管理端")
public class AdminUserDeleteResumeBySystemServiceController {
    @Autowired
    private UserDeleteResumeBySystemService userDeleteResumeBySystemService;

    @PostMapping("/addExpiredUserDeleteResume")
    @Operation(summary = "添加系统回收用户删除的简历")
    public BaseResult<?> addExpiredUserDeleteResume(@RequestBody List<UserDeleteResume> userDeleteResumes) {
        userDeleteResumeBySystemService.addExpiredUserDeleteResume(userDeleteResumes);
        return BaseResult.ok();
    }

    @PostMapping("/clearExpiredUserDeleteResumeEveryThreeMonth")
    @Operation(summary = "主动清理系统回收用户删除的简历")
    public BaseResult<?> clearExpiredUserDeleteResumeEveryThreeMonth() {
        userDeleteResumeBySystemService.clearExpiredUserDeleteResumeEveryThreeMonth();
        return BaseResult.ok();
    }

    @GetMapping("/getUserDeleteResumeInfoById")
    @Operation(summary = "根据系统删除简历ID查询系统删除简历信息")
    @PreAuthorize("hasAuthority('/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoById')")
    public BaseResult<UserDeleteResumeBySystemInfoVO> getUserDeleteResumeInfoById(@RequestParam(required = true,value = "userDeleteResumeId") Integer userDeleteResumeId) {
        UserDeleteResumeBySystemInfoVO userDeleteResumeInfoVO = userDeleteResumeBySystemService.getUserDeleteResumeInfoById(userDeleteResumeId);
        return BaseResult.ok(userDeleteResumeInfoVO);
    }

    @PostMapping("/getUserDeleteResumeInfoPage")
    @Operation(summary = "获取系统删除简历信息分页")
    @PreAuthorize("hasAuthority('/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoPage')")
    public BaseResult<Page<UserDeleteResumeBySystemPageVO>> getUserDeleteResumeInfoPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                                                        @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                                        @RequestBody(required = false) UserDeleteResumeQuery userDeleteResumeQuery) {
        Page<UserDeleteResumeBySystemPageVO> userDeleteResumeInfoVOPage = userDeleteResumeBySystemService.getUserDeleteResumeInfoPage(pageNum, pageSize, userDeleteResumeQuery);
        return BaseResult.ok(userDeleteResumeInfoVOPage);

    }

}
