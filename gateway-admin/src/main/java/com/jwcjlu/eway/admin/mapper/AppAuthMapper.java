package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.entity.AppAuthExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface AppAuthMapper {
    int countByExample(AppAuthExample example);

    int deleteByExample(AppAuthExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppAuth record);

    int insertSelective(AppAuth record);

    List<AppAuth> selectByExampleWithRowbounds(AppAuthExample example, RowBounds rowBounds);

    List<AppAuth> selectByExample(AppAuthExample example);

    AppAuth selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppAuth record, @Param("example") AppAuthExample example);

    int updateByExample(@Param("record") AppAuth record, @Param("example") AppAuthExample example);

    int updateByPrimaryKeySelective(AppAuth record);

    int updateByPrimaryKey(AppAuth record);
}