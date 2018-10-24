package com.jwcjlu.gateway.admin.mapper;

import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.entity.ServicesExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface ServicesMapper {
    int countByExample(ServicesExample example);

    int deleteByExample(ServicesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Services record);

    int insertSelective(Services record);

    List<Services> selectByExampleWithRowbounds(ServicesExample example, RowBounds rowBounds);

    List<Services> selectByExample(ServicesExample example);

    Services selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Services record, @Param("example") ServicesExample example);

    int updateByExample(@Param("record") Services record, @Param("example") ServicesExample example);

    int updateByPrimaryKeySelective(Services record);

    int updateByPrimaryKey(Services record);
}