package com.zyh.easyapplyresume.mapper.mysql.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.admin.RecruitPosition;
import com.zyh.easyapplyresume.model.query.admin.RecruitPositionQuery;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionPageVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author shiningCloud2025
 */
public interface RecruitPositionMapper extends BaseMapper<RecruitPosition> {
    RecruitPositionInfoVO findRecruitPositionInfoById(Integer recruitPositionId);
    Page<RecruitPositionPageVO> queryRecruitPositionPage(
            Page<RecruitPositionPageVO> page,
            @Param("query") RecruitPositionQuery query // @Param让XML能获取query中的字段
    );
}
