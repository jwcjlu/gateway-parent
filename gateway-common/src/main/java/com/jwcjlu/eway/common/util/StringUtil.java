package com.jwcjlu.gateway.common.util;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {

        return !isEmpty(str);
    }
    /**
     * 拆分根据指定的字符串拆分成相应的数据组
     * @param body
     * @param regex
     * @return
     */
    public static String[] splitString(String body,String regex){
        if(isEmpty(body)){
            return null;
        }
        return body.split(regex);
    }
    public static String piece(Object start,Object end,String separator){
        return start+separator+end;
    }

}
