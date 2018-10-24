package com.jwcjlu.gateway.common.util;

import java.util.Arrays;

public final  class ArithmeticUtil {
    public static long min(long ...a){
        return Arrays.stream(a).min().getAsLong();
    }
    public static double min(double ...a){
        return Arrays.stream(a).min().getAsDouble();
    }
    public static long max(long ...a){
        return Arrays.stream(a).max().getAsLong();
    }
    public static double max(double ...a){
        return Arrays.stream(a).max().getAsDouble();
    }
}
