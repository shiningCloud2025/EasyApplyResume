package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorServiceMachineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器设备管理-监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/servicemachine/jiankong")
@Tag(name = "服务器设备管理-监控端")
public class AdmonitorServiceMachineJianKongController {

    @Autowired
    private AdmonitorServiceMachineService admonitorServiceMachineService;


}
