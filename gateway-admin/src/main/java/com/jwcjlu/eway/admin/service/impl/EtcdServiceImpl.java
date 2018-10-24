package com.jwcjlu.gateway.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.api.dto.etcd.AppAuthEtcdDTO;
import com.jwcjlu.gateway.api.dto.etcd.PluginEtcdDTO;
import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.common.constant.EtcdPathConstants;
import com.jwcjlu.gateway.configuration.etcd.ClientUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-30 12:00
 **/
@Service("etcdService")
public class EtcdServiceImpl implements EtcdService {
    @Override
    public void loadPlugin(Plugin plugin) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildPluginPath(plugin.getPluginName());
        PluginEtcdDTO pluginDto = plugin.convert(plugin);
        try {
            kvClient.put(ByteSequence.fromString(path), ByteSequence.fromString(JSON.toJSONString(pluginDto))).get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println(" kong error");
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        }
    }

    @Override
    public void deletePlugin(String pluginName) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildPluginPath(pluginName);
        try {
            kvClient.delete(ByteSequence.fromString(path)).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        }
    }

    @Override
    public void loadRule(String pluginName, String serviceKey, Rule rule) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildRuleParentPath(pluginName, serviceKey);
        try {
            kvClient.put(ByteSequence.fromString(path), ByteSequence.fromString(rule.getHandler())).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        }
    }

    @Override
    public void deleteRule(String pluginName, String serviceKey) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildRuleParentPath(pluginName, serviceKey);
        try {
            kvClient.delete(ByteSequence.fromString(path)).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        }
    }

    @Override
    public void loadAppAuthToEtcd(AppAuth appAuth) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildAppAuthPath(appAuth.getAppKey());
        AppAuthEtcdDTO dto = appAuth.convert(appAuth);
        try {
            kvClient.put(ByteSequence.fromString(path), ByteSequence.fromString(JSON.toJSONString(dto))).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            //e.printStackTrace();
        }
    }

    @Override
    public void deleteAppAuthFromEtcd(String appKey) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildAppAuthPath(appKey);
        try {
            kvClient.delete(ByteSequence.fromString(path)).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        }
    }

    @Override
    public void loadServicePlugins(String pluginName, String serviceKey, Long ruleId) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildServicePluginListParentPath(pluginName, serviceKey);
        try {
            kvClient.put(ByteSequence.fromString(path), ByteSequence.fromString(ruleId.toString())).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        }
    }

    @Override
    public void deleteServicePlugins(String pluginName, String serviceKey) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildServicePluginListParentPath(pluginName, serviceKey);
        try {
            kvClient.delete(ByteSequence.fromString(path)).get();
        } catch (InterruptedException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(" kong error");
            // e.printStackTrace();
        }
    }

    @Override
    public Boolean ifExistedServicePlugins(String pluginName, String serviceKey, Long ruleId) {
        KV kvClient = ClientUtils.getInstence();
        String path = EtcdPathConstants.buildServicePluginListParentPath(pluginName, serviceKey);
        Boolean flag = false;
        try {
            // get the CompletableFuture
            CompletableFuture<GetResponse> getFuture = kvClient.get(ByteSequence.fromString(path));
            // get the value from CompletableFuture
            GetResponse response = getFuture.get();
            if (Objects.nonNull(ruleId)) {
                System.out.println();
                if (Objects.nonNull(response.getKvs()) && response.getKvs().size() > 0 && !response.getKvs().get(0).getValue().toStringUtf8().equals(ruleId.toString())) {
                    flag = true;
                }
            } else {
                if (Objects.nonNull(response.getKvs()) && response.getKvs().size() > 0) {
                    flag = true;
                }
            }


        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println(" kong error");
            return false;
        } catch (ExecutionException e) {
            //e.printStackTrace();
            System.out.println(" kong error");
            return false;

        }
        return flag;
    }
}
