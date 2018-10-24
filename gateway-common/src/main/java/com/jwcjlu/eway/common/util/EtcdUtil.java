package com.jwcjlu.gateway.common.util;

import com.jwcjlu.gateway.common.constant.Constants;

public final class EtcdUtil {
    public static String getSimpleName(String path) {
        if (StringUtil.isNotEmpty(path)) {
            return path.substring(path.lastIndexOf(Constants.PATH_SEPARATOR) + 1);
        }
        return null;
    }

    public static String getChildPath(String parent, String path) {
        int index = parent.split(Constants.PATH_SEPARATOR).length;
        String child = path.split(Constants.PATH_SEPARATOR)[index];
        return parent + Constants.PATH_SEPARATOR + child;
    }

    public static String getParentNode(String path) {
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        String[] data = path.split(Constants.PATH_SEPARATOR);
        return data[data.length - 2];
    }


    public static void main(String[] args) {
        System.out.println(getChildPath("/gateway/service", "/gateway/service/aaa/cccc"));
        System.out.println(getParentNode("/gateway/service/aaa/cccc"));
    }
}
