package com.jwcjlu.gateway.api.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-06-09 16:08
 **/
@Data
public class RoleDTO {

    private Integer id;

    private String competence;

    private String role;

    private String[] ids;

    private Integer pageSize;

    private Integer pageNumber;

    public String getRole() {
        if(StringUtils.isNotEmpty(role)){
            return role.trim();
        }
        return role;
    }

    public String getCompetence() {
        if(StringUtils.isNotEmpty(competence)){
            return competence.trim();
        }
        return competence;
    }
}
