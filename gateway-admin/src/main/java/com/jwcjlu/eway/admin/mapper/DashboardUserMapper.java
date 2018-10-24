package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.DashboardUser;
import com.jwcjlu.gateway.api.entity.DashboardUserExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface DashboardUserMapper {
    int countByExample(DashboardUserExample example);

    int deleteByExample(DashboardUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DashboardUser record);

    int insertSelective(DashboardUser record);

    List<DashboardUser> selectByExampleWithRowbounds(DashboardUserExample example, RowBounds rowBounds);

    List<DashboardUser> selectByExample(DashboardUserExample example);

    DashboardUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DashboardUser record, @Param("example") DashboardUserExample example);

    int updateByExample(@Param("record") DashboardUser record, @Param("example") DashboardUserExample example);

    int updateByPrimaryKeySelective(DashboardUser record);

    int updateByPrimaryKey(DashboardUser record);
}