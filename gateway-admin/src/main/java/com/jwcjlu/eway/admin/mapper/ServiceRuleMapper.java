package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.ServiceRule;
import com.jwcjlu.gateway.api.entity.ServiceRuleExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface ServiceRuleMapper {
    int countByExample(ServiceRuleExample example);

    int deleteByExample(ServiceRuleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ServiceRule record);

    int insertSelective(ServiceRule record);

    List<ServiceRule> selectByExampleWithRowbounds(ServiceRuleExample example, RowBounds rowBounds);

    List<ServiceRule> selectByExample(ServiceRuleExample example);

    ServiceRule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ServiceRule record, @Param("example") ServiceRuleExample example);

    int updateByExample(@Param("record") ServiceRule record, @Param("example") ServiceRuleExample example);

    int updateByPrimaryKeySelective(ServiceRule record);

    int updateByPrimaryKey(ServiceRule record);
}