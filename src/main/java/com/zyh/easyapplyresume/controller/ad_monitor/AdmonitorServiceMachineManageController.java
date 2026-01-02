package com.zyh.easyapplyresume.controller.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineConnectForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorServiceMachineQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachinePageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorServiceMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 服务器设备管理-监控端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/servicemachine/manage")
@Tag(name = "服务器设备管理-监控端")
public class AdmonitorServiceMachineManageController {

    @Autowired
    private AdmonitorServiceMachineService admonitorServiceMachineService;

    @PostMapping("/addAdmonitorServiceMachine")
    @Operation(summary = "添加服务器设备")
    public BaseResult<Integer> addAdmonitorServiceMachine(@RequestBody AdmonitorServiceMachineForm admonitorServiceMachineForm) {
        return BaseResult.ok(admonitorServiceMachineService.addAdmonitorServiceMachine(admonitorServiceMachineForm));
    }

    @PutMapping("/updateAdmonitorServiceMachine")
    @Operation(summary = "更新服务器设备")
    public BaseResult<Integer> updateAdmonitorServiceMachine(@RequestBody AdmonitorServiceMachineForm admonitorServiceMachineForm) {
        return BaseResult.ok(admonitorServiceMachineService.updateAdmonitorServiceMachine(admonitorServiceMachineForm));
    }

    @DeleteMapping("/deleteAdmonitorServiceMachine")
    @Operation(summary = "删除服务器设备")
    public BaseResult<Integer> deleteAdmonitorServiceMachine(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorServiceMachineService.deleteAdmonitorServiceMachine(id));
    }

    @GetMapping("/getAdmonitorServiceMachineInfo")
    @Operation(summary = "获取服务器设备信息")
    public BaseResult<AdmonitorServiceMachineInfoVO> getAdmonitorServiceMachineInfo(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorServiceMachineService.getAdmonitorServiceMachineInfo(id));
    }

    @PostMapping("/getAdmonitorServiceMachinePage")
    @Operation(summary = "获取服务器设备分页")
    public BaseResult<Page<AdmonitorServiceMachinePageVO>> getAdmonitorServiceMachinePage(@RequestParam(required = false, name = "pageNum", defaultValue = "1") Integer pageNum,
                                                                                          @RequestParam(required = false, name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                          @RequestBody AdmonitorServiceMachineQuery admonitorServiceMachineQuery) {
        return BaseResult.ok(admonitorServiceMachineService.getAdmonitorServiceMachinePage(pageNum, pageSize, admonitorServiceMachineQuery));

    }

    @PostMapping("/testServiceMachineConnect")
    @Operation(summary = "测试服务器设备连接")
    public BaseResult<Boolean> testServiceMachineConnect(@RequestBody AdmonitorServiceMachineConnectForm admonitorServiceMachineConnectForm) {
        return BaseResult.ok(admonitorServiceMachineService.testServiceMachineConnect(admonitorServiceMachineConnectForm));
    }


}
