package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 每日访问量统计控制器(某一天的总量)-管理端和监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admin/dailyVisitTotalNum")
@Tag(name = "每日访问量统计控制器(某一天的总量)-管理端和监控端")
public class AdmonitorAdminDailyVisitTotalNumController {

    @Autowired
    private AdmonitorAdminDailyVisitTotalNumService admonitorAdminDailyVisitTotalNumService;


    @GetMapping("/findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum")
    @Operation(summary = "查询某段时间内每日访问量统计")
    public BaseResult<List<Integer>> findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(@RequestParam(required = true,name = "fromDate")
                                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                               Date fromDate,
                                                                                           @RequestParam(required = true,name = "endDate")
                                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                           Date endDate){
        return BaseResult.ok(admonitorAdminDailyVisitTotalNumService.findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(fromDate,endDate));
    }


}
