package com.jwcjlu.gateway.api.convert;

import lombok.Data;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */

@Data
public class ConcurrencyHandle implements  Handler{


    private int burstCapacity;

    /**
     * this is a message.
     * 0:redis to do 1:count token
     */
    private int concurrencyType;


}
