package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.AppAuthDTO;
import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.vo.ResponseResult;

import java.util.List;

public interface AppAuthService {

    /**
     * insert app auth.
     *
     * @param appAuth object
     * @return int
     */
    int insertAppAuth(AppAuth appAuth);

    /**
     * update app auth.
     *
     * @param appAuth object
     * @return int
     */
    int updateAppAuth(AppAuth appAuth);

    /**
     * get app auth page list.
     *
     * @param appAuthDTO package to get param
     * @return ResponseResult
     */
    ResponseResult<AppAuth> getList(AppAuthDTO appAuthDTO);

    /**
     * get list.
     *
     * @return java.util.List
     */
    List<AppAuth> getList();

    /**
     * delete app auth.
     *
     * @param id id
     * @return int
     */
    int deleteAppAuth(Long id);

    /**
     * get app auth by id.
     *
     * @param id id
     * @return AppAuth
     */
    AppAuth getAppAuthById(Long id);

    /**
     * get app auth by key.
     *
     * @param key app key
     * @param userId user id
     * @return AppAuth
     */
    AppAuth getAppAuthByAppKey(String key,Long userId);
}


