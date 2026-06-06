package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.query.admin.AdminPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminPageVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.qiniuoss.OssAdminBusinessTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminFormValidator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OssService ossService;

    @Override
    public Integer addAdmin(AdminForm adminForm) {
        try{
            if(adminForm==null) {
                return 0;
            }
            // 因为是新增，就是为空或者不为空，无法手动输入了，只能上传
            // 为了去解决事务问题，因为我是先上传，后保存。保存失败，上传但是成功了,新增没有id!!所以在修改处理
            AdminFormValidator.validateForAdd(adminForm);
            validateAdminUnique(adminForm);
            Admin admin = new Admin();
            admin.setAdminLoginTime(new Date());
            BeanUtils.copyProperties(adminForm, admin);
            admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));

            return adminMapper.insert(admin);
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加管理员失败");
        }

    }

    @Override
    public Integer updateAdmin(AdminForm adminForm) {
        try{
            if(adminForm==null) {
                return 0;
            }
            if (adminForm.getAdminId()==1){
                throw new BusException(AdminCodeEnum.NO_UPDATE_SUPER_ADMIN);
            }
            // 防空指针,因为是先做的这个校验,再做的整体校验
            if ("https://ts1.tc.mm.bing.net/th/id/R-C.928ef8908b5eb3666b2a27a1f6cfbe17?rik=h2FXLv1HNaxbTg&riu=http%3a%2f%2fp0.so.qhmsg.com%2ft018b5eb3666b2a27a1.jpg&ehk=QnGPPvZKq3cPW6%2bdkG%2b3zIRvAGXRsgYVTirfbvOBTaU%3d&risl=&pid=ImgRaw&r=0".equals(adminForm.getAdminImage())){
                // 说明用户修改的时候还是传的原始图片，不需要处理
            }else {
                // 说明用户传了新的
                List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.ADMIN, OssAdminBusinessTypeEnum.ADMIN_HEAD_IMG, adminForm.getAdminId(), false);
                if (strings.isEmpty()){
                    // 说明是第一次传新的，不用处理
                }else{
                    // 说明不是第一次传新的，要处理不等于当前的
                    for (String string : strings){
                        if (!string.equals(adminForm.getAdminImage())){
                            ossService.deleteByUrl(string, false);
                        }
                    }
                }

            }

            AdminFormValidator.validateForUpdate(adminForm);
            validateAdminUnique(adminForm);
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminForm, admin);
            admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
            return adminMapper.updateById(admin);
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("更新管理员失败");
        }
    }


    private void validateAdminUnique(AdminForm adminForm) {
        // 1. 校验账号是否重复
        LambdaQueryWrapper<Admin> accountWrapper = lambdaQuery(Admin.class);
        accountWrapper.eq(Admin::getDeleted, 0);
        accountWrapper.eq(Admin::getAdminAccount, adminForm.getAdminAccount());
        if (adminForm.getAdminId() != null) {
            accountWrapper.ne(Admin::getAdminId, adminForm.getAdminId());
        }
        Long accountCount = adminMapper.selectCount(accountWrapper);
        if (accountCount != null && accountCount > 0) {
            throw new BusException(AdminCodeEnum.ADMIN_USERNAME_DUPLICATE);
        }

        // 2. 校验手机号是否重复
        LambdaQueryWrapper<Admin> phoneWrapper = lambdaQuery(Admin.class);
        phoneWrapper.eq(Admin::getDeleted, 0);
        phoneWrapper.eq(Admin::getAdminPhone, adminForm.getAdminPhone());
        if (adminForm.getAdminId() != null) {
            phoneWrapper.ne(Admin::getAdminId, adminForm.getAdminId());
        }
        Long phoneCount = adminMapper.selectCount(phoneWrapper);
        if (phoneCount != null && phoneCount > 0) {
            throw new BusException(AdminCodeEnum.ADMIN_PHONE_DUPLICATE);
        }

        // 3. 校验邮箱是否重复
        LambdaQueryWrapper<Admin> emailWrapper = lambdaQuery(Admin.class);
        emailWrapper.eq(Admin::getDeleted, 0);
        emailWrapper.eq(Admin::getAdminEmail, adminForm.getAdminEmail());
        if (adminForm.getAdminId() != null) {
            emailWrapper.ne(Admin::getAdminId, adminForm.getAdminId());
        }
        Long emailCount = adminMapper.selectCount(emailWrapper);
        if (emailCount != null && emailCount > 0) {
            throw new BusException(AdminCodeEnum.ADMIN_EMAIL_DUPLICATE);
        }
    }


    @Override
    public Integer deleteAdmin(Integer adminId) {
        try{
            if (adminId==1){
                throw new BusException(AdminCodeEnum.NO_DELETE_SUPER_ADMIN);
            }
            // 删除 OSS 文件
            List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.ADMIN, OssAdminBusinessTypeEnum.ADMIN_HEAD_IMG, adminId, false);
            if (!strings.isEmpty()){
                for (String string : strings) {
                    ossService.deleteByUrl(string, false);
                }
            }
            // 逻辑删除：使用 LambdaUpdateWrapper，避免 updateById 在全局逻辑删除配置下生成非法 SQL
            LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Admin::getAdminId, adminId);
            wrapper.eq(Admin::getDeleted, 0);
            wrapper.set(Admin::getDeleted, 1);
            adminMapper.update(null, wrapper);
            return adminMapper.deleteRoleByAdminId(adminId);
        }catch (Exception  e){
            e.printStackTrace();
            throw new RuntimeException("删除管理员失败");
        }

    }

    @Override
    public AdminInfoVO findAdminById(Integer adminId) {
        try{
            return adminMapper.findAdminInfoById(adminId);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询管理员失败");
        }
    }

    @Override
    public Page<AdminPageVO> findAdminByPage(Integer pageNum, Integer pageSize, AdminPageQuery adminPageQuery) {
        // 核心改造：QueryWrapper → LambdaQueryWrapper，用 Lambda 引用字段
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = lambdaQuery(Admin.class);
        lambdaQueryWrapper.eq(Admin::getDeleted, 0);
        if (adminPageQuery.getAdminEmail() != null && !adminPageQuery.getAdminEmail().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminEmail, adminPageQuery.getAdminEmail());
        }
        if (adminPageQuery.getAdminPhone() != null && !adminPageQuery.getAdminPhone().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminPhone, adminPageQuery.getAdminPhone());
        }
        if (adminPageQuery.getAdminState() != null) {
            lambdaQueryWrapper.eq(Admin::getAdminState, adminPageQuery.getAdminState());
        }
        if (adminPageQuery.getAdminUsername() != null && !adminPageQuery.getAdminUsername().isEmpty()) {
            lambdaQueryWrapper.like(Admin::getAdminUsername, adminPageQuery.getAdminUsername());
        }
        Page<Admin> adminPage = adminMapper.selectPage(new Page(pageNum, pageSize), lambdaQueryWrapper);
        List<AdminPageVO> voList = adminPage.getRecords().stream()
                .map(admin -> {
                    AdminPageVO vo = new AdminPageVO();
                    // 复制属性（要求Admin和AdminPageVO的字段名、类型一致）
                    BeanUtils.copyProperties(admin, vo);
                    // 如果有字段名不一致的情况，需要手动补充
                    // 例如：vo.setNewName(admin.getOldName());
                    return vo;
                })
                .collect(Collectors.toList());

        Page<AdminPageVO> adminVOPage = new Page<>();
        adminVOPage.setRecords(voList);         // 设置转换后的VO列表
        adminVOPage.setSize(adminPage.getSize());   // 每页条数
        adminVOPage.setCurrent(adminPage.getCurrent()); // 当前页码
        adminVOPage.setPages(adminPage.getPages()); // 总页数
        adminVOPage.setTotal(adminPage.getTotal());
        return  adminVOPage;
        
    }

    // 查找用户拥有的角色
    @Override
    public List<RoleInfoVO> findRoleByAdmin(Integer adminId) {
        return adminMapper.findRoleByAdmin(adminId);
    }

    @Override
    public Integer assignRoleToAdmin(Integer adminId, Integer[] roleIds) {
        int count = 0;
        if (roleIds==null){
            return count;
        } else{
            adminMapper.deleteRoleByAdminId(adminId);
            for(int role:roleIds){
               count+= adminMapper.assignRoleToAdmin(adminId,role);
            }
        }
        return count;
    }

    @Override
    public String generateRandomAccount() {
        String account;
        int maxRetries = 10;
        int retryCount = 0;
        
        do {
            long timestamp = System.currentTimeMillis() % 100000000L;
            int randomNum = (int) (Math.random() * 100);
            account = String.format("%d%02d", timestamp, randomNum);
            
            if (account.length() > 10) {
                account = account.substring(account.length() - 10);
            } else if (account.length() < 7) {
                account = String.format("%07d", Long.parseLong(account));
            }
            
            retryCount++;
            if (retryCount > maxRetries) {
                throw new BusException(AdminCodeEnum.GENERATE_ACCOUNT_FAIL);
            }
        } while (adminMapper.findByAccountOrPhoneOrEmail(account) != null);
        
        return account;
    }

    @Override
    public void logout(Integer adminId) {
        String redisKey = "admin:token:" + adminId;
        stringRedisTemplate.delete(redisKey);
    }

}
