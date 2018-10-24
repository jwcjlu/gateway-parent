package com.jwcjlu.gateway.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * DTO.
 * @author chengzhuantuo
 */
@Data
public class UserDTO {

    private static final long serialVersionUID = -3479973014221253748L;

    private String userName;

    private String password;

    private Integer pageSize;

    private Integer pageNumber;

    private String[] ids;

    private Long id;

    private String sid;

    private String token;

    private Integer role;

    private List<TreeDTO> treeData;

    private Map<String,Boolean> competence;


}
