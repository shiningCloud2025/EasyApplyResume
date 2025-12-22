package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 管理员每日访问量统计-管理端和监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admin/dailyVisitNum")
@Tag(name = "管理员每日访问量统计-管理端和监控端")
public class AdmonitorAdminDailyVisitNumController {
    @Autowired
    private AdmonitorAdminDailyVisitNumService admonitorAdminDailyVisitNumService;

    @Operation(summary = "计算某天的访问量")
    @GetMapping("/calculateAdmonitorAdminDailyVisitNum")
    public BaseResult<Integer> calculateAdmonitorAdminDailyVisitNum(@RequestParam(required = true, value = "time") Date time){
        return BaseResult.ok(admonitorAdminDailyVisitNumService.calculateAdmonitorAdminDailyVisitNum(time));
    }

    @Operation(summary = "总访问量")
    @GetMapping("/calculateAdmonitorAdminDailyVisitNumTotal")
    public BaseResult<Integer> calculateAdmonitorAdminDailyVisitNumTotal(){
        return BaseResult.ok(admonitorAdminDailyVisitNumService.calculateAdmonitorAdminDailyVisitNumTotal());
    }

    @Operation(summary = "今天新增访问量")
    @GetMapping("/calculateDayIncreaseAdmonitorAdminDailyVisitNum")
    public BaseResult<Integer> calculateDayIncreaseAdmonitorAdminDailyVisitNum(){
        return BaseResult.ok(admonitorAdminDailyVisitNumService.calculateDayIncreaseAdmonitorAdminDailyVisitNum());
    }
}
