package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.AppPlugin;
import com.jwcjlu.gateway.api.entity.AppPluginExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface AppPluginMapper {
    int countByExample(AppPluginExample example);

    int deleteByExample(AppPluginExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppPlugin record);

    int insertSelective(AppPlugin record);

    List<AppPlugin> selectByExampleWithRowbounds(AppPluginExample example, RowBounds rowBounds);

    List<AppPlugin> selectByExample(AppPluginExample example);

    AppPlugin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppPlugin record, @Param("example") AppPluginExample example);

    int updateByExample(@Param("record") AppPlugin record, @Param("example") AppPluginExample example);

    int updateByPrimaryKeySelective(AppPlugin record);

    int updateByPrimaryKey(AppPlugin record);
}