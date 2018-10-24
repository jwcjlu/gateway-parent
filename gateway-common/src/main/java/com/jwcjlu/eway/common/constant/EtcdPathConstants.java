package com.jwcjlu.gateway.common.constant;

/**
 * <p>Description: .</p>
 * zk 路径工具类
 *
 * @author xiaoyu(Myth)
 */
public final class EtcdPathConstants implements Constants {

    private static final String PLUGIN_PARENT = "/gateway/plugin";

    public static final String APP_AUTH_PARENT = "/gateway/app_auth";

    public static final String BASE_PARENT = "/gateway";

    /**
     * acquire app_auth_path.
     * @param appKey appKey
     * @return  app_auth_path
     */
    public static String buildAppAuthPath(final String appKey){
        return String.join("/", APP_AUTH_PARENT, appKey);
    }

    /**
     * etcd上插件的路径名称.
     *
     * @param pluginName 插件名称
     * @return etcd上插件的路径名称
     */
    public static String buildPluginPath(final String pluginName) {
        return String.join("/", PLUGIN_PARENT, pluginName);
    }

    /**
     * etcd上名称.
     *
     * @param serviceKey serviceKey
     * @return java.lang.String
     */
    public static String buildServicePluginListParentPath(final String pluginName,final String serviceKey) {
        String path = String.join("/", BASE_PARENT, "service");
        path = String.join("/", path, serviceKey);
        return String.join("/", path, pluginName);
    }

    /**
     * etcd上名称.
     *
     * @param pluginName keyName
     * @param serviceKey serviceKey
     * @return java.lang.String
     */
    public static String buildRuleParentPath(final String pluginName,final String serviceKey) {
        String path = String.join("/", BASE_PARENT, "rule");
        path = String.join("/", path,pluginName );
        return String.join("/", path, serviceKey);
    }


}
