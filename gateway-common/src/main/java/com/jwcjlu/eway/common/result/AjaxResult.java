package com.jwcjlu.gateway.common.result;

import com.jwcjlu.gateway.common.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * AjaxResult .
 * @author xiaoyu
 **/
@Data
public class AjaxResult implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private Integer code;

    private String message;

    private Object data;

    public AjaxResult() {

    }

    public AjaxResult(final Integer code, final String message, final Object data) {

        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功.
     * @return AjaxResult
     */
    public static AjaxResult success() {
        return success("");
    }

    /**
     * 返回成功.
     * @param msg 附带信息
     * @return AjaxResult
     */
    public static AjaxResult success(final String msg) {
        return success(msg, null);
    }

    /**
     * 返回成功.
     * @param data 数据
     * @return AjaxResult
     */
    public static AjaxResult success(final Object data) {
        return success(null, data);
    }

    /**
     * 返回成功.
     * @param data 数据
     * @param msg 附带信息
     * @return AjaxResult
     */
    public static AjaxResult success(final String msg, final Object data) {
        return get(ErrorCode.SUCCESSFUL, msg, data);
    }

    /**
     * 返回错误.
     * @param msg 附带信息
     * @return AjaxResult
     */
    public static AjaxResult error(final String msg) {
        return error(ErrorCode.ERROR, msg);
    }

    /**
     * 返回错误.
     * @param code 代码
     * @param msg 附带信息
     * @return AjaxResult
     */
    public static AjaxResult error(final int code, final String msg) {
        return get(code, msg, null);
    }

    /**
     * 获取信息.
     * @param code 代码
     * @param msg 附带信息
     * @param data 数据
     * @return AjaxResult
     */
    private static AjaxResult get(final int code, final String msg, final Object data) {
        return new AjaxResult(code, msg, data);
    }

}
