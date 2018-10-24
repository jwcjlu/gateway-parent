package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.PluginExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface PluginMapper {
    int countByExample(PluginExample example);

    int deleteByExample(PluginExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Plugin record);

    int insertSelective(Plugin record);

    List<Plugin> selectByExampleWithRowbounds(PluginExample example, RowBounds rowBounds);

    List<Plugin> selectByExample(PluginExample example);

    Plugin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Plugin record, @Param("example") PluginExample example);

    int updateByExample(@Param("record") Plugin record, @Param("example") PluginExample example);

    int updateByPrimaryKeySelective(Plugin record);

    int updateByPrimaryKey(Plugin record);
}