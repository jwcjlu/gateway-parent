package com.jwcjlu.gateway.api.convert;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DivideHandle.
 * @author chengchuantuo(Myth)
 */
@Data
public class DivideHandle implements Handler, Serializable {

    /**
     * job style.
     */
    private String loadBalance;

    /**
     * weight type.
     * 0: 智能分配  1
     */
    private int weightType;

    /**
     * upstream list.
     */
    private List<DivideUpstream> upstreamList;
}
