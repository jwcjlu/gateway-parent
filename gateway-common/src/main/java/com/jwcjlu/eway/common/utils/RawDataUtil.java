package com.jwcjlu.gateway.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * raw data util class .
 * @author chengchuantuo
 * @date 2018-06-15 13:50
 **/
public class RawDataUtil {

    /**
     * check if it is raw data.
     *
     * @param rawData raw data
     * @param id      id
     * @return java.lang.Boolean
     */
    public static Boolean checkRawData(final String rawData, final String id) {
        if (StringUtils.isNotEmpty(rawData)) {
            String[] ids = rawData.split(",");
            for (String string : ids) {
                if (string.equals(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
