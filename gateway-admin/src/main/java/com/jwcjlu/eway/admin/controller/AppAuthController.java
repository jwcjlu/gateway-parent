package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.AppAuthService;
import com.jwcjlu.gateway.api.dto.AppAuthDTO;
import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.result.AjaxResult;
import com.jwcjlu.gateway.common.utils.LogUtils;
import com.jwcjlu.gateway.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

@RestController
@RequestMapping("/gateway")
public class AppAuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppAuthController.class);

    @Autowired
    private AppAuthService appAuthService;

    /**
     * this is a function.
     *
     * @param appAuthDTO
     * @return AjaxResult
     */
    @PostMapping("/appAuth/appList")
    public AjaxResult getList(@RequestBody final AppAuthDTO appAuthDTO) {
        LogUtils.debug(LOGGER, () -> appAuthDTO.toString());
        ResponseResult<AppAuth> responseResult = appAuthService.getList(appAuthDTO);
        return AjaxResult.success(responseResult);
    }

    /**
     * delete app auth.
     *
     * @param dto
     * @return AjaxResult
     */
    @PostMapping("/appAuth/remove")
    public AjaxResult delete(@RequestBody final AppAuthDTO dto) {
        String[] ids = dto.getIds();
        Arrays.stream(ids).forEach(x -> {
            appAuthService.deleteAppAuth(Long.parseLong(x));
            LogUtils.debug(LOGGER, () -> x + "已被删除");
        });

        return AjaxResult.success(true);
    }

    /**
     * update app auth.
     *
     * @param dto
     * @return AjaxResult
     */
    @PostMapping("/appAuth/update")
    public AjaxResult update(@RequestBody final AppAuthDTO dto) {
        LogUtils.debug(LOGGER, () -> dto.toString() + "update or save");
        //新增
        Calendar calendar = Calendar.getInstance();
        AppAuth appAuth = new AppAuth();
        appAuth.setId(Objects.nonNull(dto.getId()) ? dto.getId() : null);
        appAuth.setTimeUpdated(calendar.getTime());
        appAuth.setAppKey(dto.getAppAuth());
        appAuth.setUserId(dto.getUserId());
        appAuth.setEnabled(org.apache.commons.lang3.StringUtils.isEmpty(dto.getEnabled()) ? (byte) 0 : Byte.valueOf(dto.getEnabled()));
        if (Objects.isNull(appAuth.getId())) {
            //重复key判断
            if (Objects.nonNull(appAuthService.getAppAuthByAppKey(dto.getAppAuth(), dto.getUserId()).getId())) {
                return AjaxResult.error("appKey重复,请重新输入");
            }
            appAuth.setTimeCreated(calendar.getTime());
            appAuth.setAppSecret(StringUtil.getRandomString(8));
            appAuthService.insertAppAuth(appAuth);
        } else {
            appAuthService.updateAppAuth(appAuth);
        }
        return AjaxResult.success(appAuth);
    }

}
