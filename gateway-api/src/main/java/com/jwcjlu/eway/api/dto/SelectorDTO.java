package com.jwcjlu.gateway.api.dto;

import com.jwcjlu.gateway.api.convert.DivideUpstream;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * DTO.
 *
 * @author chengzhuantuo
 */
@Data
public class SelectorDTO implements Serializable {

    private String id;

    private String param;

    private String detailValue;

    private String pselected;

    private String oselected;

    private String tselected;

    private String continued;

    private String loged;

    private String plugin;

    private String matchMode;

    private String type;

    private String enabled;

    private String name;

    private Integer rank;

    private String cleanQuery;

    private String status;

    private String onceTime;

    private String times;

    private String access;

    private List<DivideUpstream> domains;

    private String commandKey;

    private String groupKey;

    private String loadBalance;

    private int maxConcurrentRequests;

    private int errorThresholdPercentage;

    private int sleepWindowInMilliseconds;

    private int requestVolumeThreshold;

    private String selectorId;

    private String rewriteURI;

    private List<ConditionDTO> conditions;

    private String registry;

    private String appName;

    private String protocol;

    private int port;

    private Integer timeout;

    private Integer retry;

    private String group;

    private String version;

    public String getRegistry() {
        if (StringUtils.isNotEmpty(registry)) {
            return registry.trim();
        }
        return registry;
    }

    public String getAppName() {
        if (StringUtils.isNotEmpty(appName)) {
            return appName.trim();
        }
        return appName;
    }

    public String getProtocol() {
        if (StringUtils.isNotEmpty(protocol)) {
            return protocol.trim();
        }
        return protocol;
    }

    public String getGroup() {
        if (StringUtils.isNotEmpty(group)) {
            return group.trim();
        }
        return group;
    }

    public String getVersion() {
        if (StringUtils.isNotEmpty(version)) {
            return version.trim();
        }
        return version;
    }

    public String getPlugin() {
        if (StringUtils.isNotEmpty(plugin)) {
            return plugin.trim();
        }
        return plugin;
    }

    public String getStatus() {
        if (StringUtils.isNotEmpty(status)) {
            return status.trim();
        }
        return status;
    }

    public String getTimes() {
        if (StringUtils.isNotEmpty(times)) {
            return times.trim();
        }
        return times;
    }


    public String getCommandKey() {
        if (StringUtils.isNotEmpty(commandKey)) {
            return commandKey.trim();
        }
        return commandKey;
    }

    public String getGroupKey() {
        if (StringUtils.isNotEmpty(groupKey)) {
            return groupKey.trim();
        }
        return groupKey;
    }

    public String getRewriteURI() {
        if (StringUtils.isNotEmpty(rewriteURI)) {
            return rewriteURI.trim();
        }
        return rewriteURI;
    }
}
