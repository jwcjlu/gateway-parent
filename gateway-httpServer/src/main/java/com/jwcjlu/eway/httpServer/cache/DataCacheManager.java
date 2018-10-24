package com.jwcjlu.gateway.httpServer.cache;

import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.api.dto.etcd.AppAuthEtcdDTO;
import com.jwcjlu.gateway.api.dto.etcd.PluginEtcdDTO;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.boot.DefaultImplementor;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.httpServer.adapter.BootServiceAdapter;
import com.jwcjlu.gateway.httpServer.etcd.EtcdService;
import com.jwcjlu.gateway.httpServer.etcd.listener.app.AppChildListener;
import com.jwcjlu.gateway.httpServer.etcd.listener.service.ServiceChildListener;
import com.jwcjlu.gateway.httpServer.etcd.listener.plugin.PluginChilderListener;
import com.jwcjlu.gateway.httpServer.etcd.listener.rule.RulePluginChildListener;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@DefaultImplementor
public class DataCacheManager extends BootServiceAdapter {
    private Logger logger = LoggerFactory.getLogger(DataCacheManager.class);
    private Map<String, PluginEtcdDTO> plugins = new ConcurrentHashMap<>();
    private Map<String, List<String>> servicePluginsMap = new ConcurrentHashMap<>();
    private Map<String, AppAuthEtcdDTO> apps = new ConcurrentHashMap<>();
    private ServiceRuleHandler serviceRuleHandler = new ServiceRuleHandler();
    ;

    @Override
    public void onComplete() {
        EtcdService etcdService = BootServiceManager.INSTANCE.findService(EtcdService.class);
        etcdService.addChildListener(Constants.ETCD_PLUGINS_PATH, new PluginChilderListener());
        etcdService.addChildListener(Constants.ETCD_RULE_PATH, new RulePluginChildListener());
        etcdService.addChildListener(Constants.ETCD_APP_PATH, new AppChildListener());
        etcdService.addChildListener(Constants.ETCD_SERVICE_PATH, new ServiceChildListener());
    }

    @Override
    public void shutdown() {

    }

    public <T extends Handler> T getHandler(String pluginName, String serviceName) {
        if (Objects.isNull(serviceRuleHandler))
            return null;
        return serviceRuleHandler.getHandler(pluginName, serviceName);
    }

    public List<PluginEtcdDTO> getPlugins(String serviceKeys) {
        List<String> pluginNames = servicePluginsMap.get(serviceKeys);
        if (CollectionUtils.isEmpty(pluginNames)) {
            return null;
        }
        return pluginNames.stream().map(pluginName -> {
            return plugins.get(pluginName);
        })
            .filter(p -> p != null && p.getEnabled()).collect(Collectors.toList());
    }

    public void putServicePlugins(String serviceKey, List<String> plugins) {
        servicePluginsMap.put(serviceKey, plugins);
    }

    public List<ServiceHandler> getServiceHandlers(String plugin) {
        return serviceRuleHandler.getServiceHandler(plugin);
    }

    public void pubServiceHandler(String plugin, List<ServiceHandler> serviceHandlers) {
        serviceRuleHandler.pubServiceHandler(plugin, serviceHandlers);
    }

    public void putPlugins(String pluginName, PluginEtcdDTO plugin) {
        plugins.put(pluginName, plugin);

    }

    public void putApps(String appName, AppAuthEtcdDTO appAuth) {
        apps.put(appName, appAuth);
    }


}
