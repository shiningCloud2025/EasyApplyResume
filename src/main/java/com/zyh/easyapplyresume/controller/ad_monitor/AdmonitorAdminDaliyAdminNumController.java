package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
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
 * 管理端管理员数量记录-监测端和管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admonitor/adminDaliyAdminNum")
@Tag(name = "管理端管理员数量记录-监测端和管理端")
public class AdmonitorAdminDaliyAdminNumController {

    @Autowired
    private AdmonitorAdminDaliyAdminNumService admonitorAdminDaliyAdminNumService;

    @GetMapping("/findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum")
    @Operation(summary = "查询规定时间内的管理员数量")
    public BaseResult<List<Integer>> findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(@RequestParam(required = true,name = "fromDate")
                                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                          Date  fromDate,
                                                                                      @RequestParam(required = true,name = "endDate")
                                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                      Date endDate){
        return BaseResult.ok(admonitorAdminDaliyAdminNumService.findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(fromDate,endDate));
    }


}
