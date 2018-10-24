
package com.jwcjlu.gateway.admin.service;

import java.util.List;


/**
 * <p>Description: enum handler.</p>
 *
 * @author chengchuantuo
 */

public interface EnumService {


    /**
     * get matchMode list.
     *
     * @return java.util.List java.lang.String
     */


    List<String> getMatchModeList();


    /**
     * get Operator list.
     *
     * @return java.util.List java.lang.String
     */

    List<String> getOperatorTypeList();


    /**
     * get ParamType list.
     *
     * @return java.util.List java.lang.String
     */

    List<String> getParamTypeList();


    /**
     * get Plugin type list.
     *
     * @return java.util.List java.lang.String
     */

    List<String> getPluginTypeList();


    /**
     * get load balance type list.
     *
     * @return java.util.List java.lang.String
     */

    List<String> getLoadBalanceTypeList();


    /**
     * get rateLimiter type list.
     *
     * @return java.util.List java.lang.String
     */

    List<String> getRateLimiterTypeList();

}

