package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.mapper.admin.StreetMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.service.admin.StreetMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class StreetMapServiceImpl implements StreetMapService {
    @Autowired
    private StreetMapMapper streetMapMapper;
    @Override
    public List<StreetMap> getAllStreet() {
        return streetMapMapper.selectList(null);
    }
}
