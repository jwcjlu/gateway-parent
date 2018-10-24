
package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.Rule;


/**
 * etcdService.
 *
 * @author chengzhuantuo
 */


public interface EtcdService {

    /**
     * load single plugin message.
     *
     * @param plugin load plugin meassage to etcd.
     */
    void loadPlugin(Plugin plugin);

    /**
     * deletePlugin by id.
     *
     * @param pluginName plugin name
     */
    void deletePlugin(String pluginName);

    /**
     * insert rule.
     *
     * @param serviceKey servicekey
     * @param pluginName plugin name
     * @param rule       rule
     */
    void loadRule(String pluginName, String serviceKey, Rule rule);

    /**
     * insert rule.
     *
     * @param serviceKey servicekey
     * @param pluginName plugin name
     */
    void deleteRule(String pluginName, String serviceKey);

    /**
     * refresh appAuth etcd data.
     *
     * @param appAuth appAuth
     */
    void loadAppAuthToEtcd(AppAuth appAuth);

    /**
     * delete appAuth etcd data.
     *
     * @param appKey app key
     */
    void deleteAppAuthFromEtcd(String appKey);

    /**
     * load service-plugin data to etcd.
     *
     * @param serviceKey service key
     * @param pluginName plugin name
     * @param ruleId ruleId
     */
    void loadServicePlugins(String pluginName, String serviceKey,Long ruleId);

    /**
     * delete service-plugin data to etcd.
     *
     * @param serviceKey service key
     * @param pluginName plugin name
     */
    void deleteServicePlugins(String pluginName, String serviceKey);

    /**
     * if it had been existed In existed.
     *
     * @param serviceKey app key
     * @param pluginName plugin name
     * @param ruleId ruleId
     * @return boolean
     */
    Boolean ifExistedServicePlugins(String pluginName, String serviceKey, Long ruleId);
}

