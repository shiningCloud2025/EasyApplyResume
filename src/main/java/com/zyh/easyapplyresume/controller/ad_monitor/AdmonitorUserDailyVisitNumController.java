package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户每日访问量统计-用户端和监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/user/dailyVisitNum")
@Tag(name = "用户每日访问量统计-用户端和监控端")
public class AdmonitorUserDailyVisitNumController {
    @Autowired
    private AdmonitorUserDailyVisitNumService admonitorUserDailyVisitNumService;

    @Operation(summary = "计算某天的访问量")
    @GetMapping("/calculateAdmonitorUserDailyVisitNum")
    public BaseResult<Integer> calculateAdmonitorUserDailyVisitNum(@RequestParam(required = true, value = "time")
                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                       Date time){
        return BaseResult.ok(admonitorUserDailyVisitNumService.calculateAdmonitorUserDailyVisitNum(time));
    }

    @Operation(summary = "总访问量")
    @GetMapping("/calculateAdmonitorUserDailyVisitNumTotal")
    public BaseResult<Integer> calculateAdmonitorUserDailyVisitNumTotal(){
        return BaseResult.ok(admonitorUserDailyVisitNumService.calculateAdmonitorUserDailyVisitNumTotal());
    }

    @Operation(summary = "今天新增访问量")
    @GetMapping("/calculateDayIncreaseAdmonitorUserDailyVisitNum")
    public BaseResult<Integer> calculateDayIncreaseAdmonitorUserDailyVisitNum(){
        return BaseResult.ok(admonitorUserDailyVisitNumService.calculateDayIncreaseAdmonitorUserDailyVisitNum());
    }
}
