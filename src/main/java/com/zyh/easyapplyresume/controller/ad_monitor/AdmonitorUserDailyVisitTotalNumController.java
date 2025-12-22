package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitTotalNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 每日访问量统计控制器(某一天的总量)-用户端和监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/user/dailyVisitTotalNum")
@Tag(name = "每日访问量统计控制器(某一天的总量)-用户端和监控端")
public class AdmonitorUserDailyVisitTotalNumController {

    @Autowired
    private AdmonitorUserDailyVisitTotalNumService admonitorUserDailyVisitTotalNumService;


    @GetMapping("/findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum")
    @Operation(summary = "查询某段时间内每日访问量统计")
    public BaseResult<List<Integer>> findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(@RequestParam(required = true,name = "fromDate") Date fromDate,
                                                                                           @RequestParam(required = true,name = "endDate") Date endDate){
        return BaseResult.ok(admonitorUserDailyVisitTotalNumService.findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(fromDate,endDate));
    }



}
