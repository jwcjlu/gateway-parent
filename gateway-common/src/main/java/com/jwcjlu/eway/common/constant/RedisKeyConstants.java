package com.jwcjlu.gateway.common.constant;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */
public final class RedisKeyConstants implements Constants {

    public static final String PLUGIN = "plugin";

    public static final String SELECTOR = "selector";

    public static final String RULE = "rule";

    private static final String PLUGIN_INFO = ":info";

    private static final String PLUGIN_SELECTOR = ":selector";

    /**
     * this is a function.
     *
     * @param pluginName 插件名
     * @return java.lang.String
     */
    public static String pluginInfoKey(final String pluginName) {
        return String.join("", pluginName, PLUGIN_INFO);

    }

    /**
     * this is a function.
     *
     * @param pluginName 插件名
     * @return java.lang.String
     */
    public static String pluginSelectorKey(final String pluginName) {
        return String.join("", pluginName, PLUGIN_SELECTOR);

    }

}
