package com.jwcjlu.gateway.common.util;

import java.lang.reflect.Field;

public final class ReflectUtil {
    public static Field[] getAllField(Class clazz){
        return clazz.getDeclaredFields();
    }
    public static Object getValueByField(Field field,Object obj){
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
