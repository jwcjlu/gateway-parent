package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.api.entity.RuleExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface RuleMapper {
    int countByExample(RuleExample example);

    int deleteByExample(RuleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Rule record);

    int insertSelective(Rule record);

    List<Rule> selectByExampleWithRowbounds(RuleExample example, RowBounds rowBounds);

    List<Rule> selectByExample(RuleExample example);

    Rule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Rule record, @Param("example") RuleExample example);

    int updateByExample(@Param("record") Rule record, @Param("example") RuleExample example);

    int updateByPrimaryKeySelective(Rule record);

    int updateByPrimaryKey(Rule record);
}