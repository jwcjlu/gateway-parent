package com.jwcjlu.gateway.api.convert;

import lombok.Data;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */

@Data
public class RateLimiterHandle implements  Handler{

    private double replenishRate;

    private double burstCapacity;

    /**
     * this is a message.
     * 0:redis to do 1:count token
     */
    private int rateLimiterType;


}
