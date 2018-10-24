package com.jwcjlu.gateway.api.convert;

import lombok.Data;

import java.io.Serializable;

/**
 * DivideUpstream for upstream.
 *
 * @author xiaoyu(Myth).
 */
@Data
public class DivideUpstream implements Serializable {

    /**
     * host.
     */
    private String upstreamHost;

    /**
     * url.
     */
    private int upstreamPort;

    /**
     * timeout.
     */
    private long timeout;


    /**
     * http retry.
     */
    private int retry;

    /**
     * weight.
     */
    private int weight;

}
