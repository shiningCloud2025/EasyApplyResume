package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.user.UniversityMapMapper;
import com.zyh.easyapplyresume.model.pojo.user.UniversityMap;
import com.zyh.easyapplyresume.service.user.UniversityMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class UniversityMapServiceImpl implements UniversityMapService {

    @Autowired
    private UniversityMapMapper universityMapMapper;

    @Override
    public List<UniversityMap> getAllUniversityMap() {
        try{
            log.info("获取所有大学信息成功");
            return universityMapMapper.selectList(null);
        }
        catch (BusException e){
            throw e;
        }
        catch (Exception e){
            log.error("获取所有大学信息失败", e);
            throw new RuntimeException("获取所有大学信息失败");
        }
    }

    @Override
    public List<UniversityMap> getAllUniversityMapByName(String universityMapName) {
        try{
            log.info("根据大学名称模糊查询大学");
            LambdaQueryWrapper<UniversityMap> queryWrapper = new LambdaQueryWrapper<>();
            if (universityMapName == null|| universityMapName.isEmpty()){
                return universityMapMapper.selectList(null);
            }else{
                queryWrapper.like(UniversityMap::getUniversityMapName, universityMapName);
            }
            return universityMapMapper.selectList(queryWrapper);
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("根据大学名称模糊查询大学失败", e);
            throw new RuntimeException("根据大学名称模糊查询大学失败");
        }
    }
}
