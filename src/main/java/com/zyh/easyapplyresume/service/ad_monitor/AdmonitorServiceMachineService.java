package com.zyh.easyapplyresume.service.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineConnectForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineJianKongForm;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineJianKongVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachinePageVO;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorServiceMachineService {

    /**
     * 新增服务器
     * @param admonitorServiceMachineForm
     * @return
     */
    public Integer addAdmonitorServiceMachine(AdmonitorServiceMachineForm admonitorServiceMachineForm);

    /**
     * 修改服务器
     * @param admonitorServiceMachineForm
     * @return
     */
    public Integer updateAdmonitorServiceMachine(AdmonitorServiceMachineForm admonitorServiceMachineForm);

    /**
     * 删除服务器
     * @param id
     * @return
     */
    public Integer deleteAdmonitorServiceMachine(Integer id);

    /**
     * 获取服务器信息
     * @param id
     * @return
     */
    public AdmonitorServiceMachineInfoVO getAdmonitorServiceMachineInfo(Integer id);

    /**
     * 获取服务器列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<AdmonitorServiceMachinePageVO> getAdmonitorServiceMachinePage(Integer pageNum, Integer pageSize);

    /**
     * 测试服务器连接
     * @param admonitorServiceMachineConnectForm
     * @return
     */
    public boolean testServiceMachineConnect(AdmonitorServiceMachineConnectForm admonitorServiceMachineConnectForm);


    //  ----------------------------上面是服务器管理，下面是服务器监控--------------------------------//

    public AdmonitorServiceMachineJianKongVO getAdmonitorServiceMachineJianKongInfo(AdmonitorServiceMachineJianKongForm admonitorServiceMachineJianKongForm);


}
