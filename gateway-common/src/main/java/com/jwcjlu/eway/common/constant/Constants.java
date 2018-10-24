package com.jwcjlu.gateway.common.constant;

/**
 * Constants.
 *
 * @author xiaoyu(Myth)
 */
public interface Constants {

    String REQUESTDTO = "requestDTO";

    String CLIENT_RESPONSE_ATTR = "webHandlerClientResponse";

    String DUBBO_RPC_RESULT = "dubbo_rpc_result";

    String CLIENT_RESPONSE_RESULT_TYPE = "webHandlerClientResponseResultType";

    String DUBBO_RPC_PARAMS = "dubbo_rpc_params";

    String MODULE = "module";

    String METHOD = "method";

    String CONTENT = "content";

    String APP_KEY = "appKey";

    String EXT_INFO = "extInfo";

    String HTTP_METHOD = "httpMethod";

    String RPC_TYPE = "rpcType";

    String SIGN = "sign";

    String TIMESTAMP = "timestamp";

    int RETRY = 3;

    String REJECT_MSG = " You are forbidden to visit";

    String REWRITE_URI = "rewrite_uri";

    String HTTP_ERROR_RESULT = "this is bad request or fuse ing  Please try again later";

    String DUBBO_ERROR_RESULT = "dubbo rpc have error or fuse ing  Please check your param and  try again later";

    String TIMEOUT_RESULT = "this request is time out  Please try again later";

    String UPSTREAM_NOT_FIND = "this can not rule upstream please check you config!";

    String TOO_MANY_REQUESTS = "the request is too fast please try again later";

    String SIGN_IS_NOT_PASS = "sign is not pass,Please check you sign algorithm!";

    String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * hystrix withExecutionIsolationSemaphoreMaxConcurrentRequests.
     */
    int MAX_CONCURRENT_REQUESTS = 100;

    /**
     * hystrix  withCircuitBreakerErrorThresholdPercentage.
     */
    int ERROR_THRESHOLD_PERCENTAGE = 50;

    /**
     * hystrix withCircuitBreakerRequestVolumeThreshold.
     */
    int REQUEST_VOLUME_THRESHOLD = 20;

    /**
     * hystrix withCircuitBreakerSleepWindowInMilliseconds.
     */
    int SLEEP_WINDOW_INMILLISECONDS = 5000;
    /**
     *
     */
    String PATH_SEPARATOR = "/";
    /**
     *
     */
    String HTTP_SUBFIX_KEY = "://";
    /**
     *
     */
    String HTTP_KEY = "http://";
    /**
     *
     */
    int ETCD_DEFAULT_KEEPALIVE_TIMEOUT = 5 * 1000;
    /**
     *
     */
    String COMMA_SEPARATOR = ",";
    /**
     *
     */
    String SERVER_PATH = "/gateway/server";
    /**
     *
     */
    String HOST_PORT_SEPARATE = ":";
    /**
     * endPoinits
     */
    String ETCD_ENDPOINTS = "etcd.endPoints";
    /**
     * plugin
     */
    String ETCD_PLUGINS_PATH = "/gateway/plugin";
    /**
     * serviceKey
     */
    String ETCD_SERVICE_PATH = "/gateway/service";
    /**
     * rule   plugin
     */
    String ETCD_RULE_PATH = "/gateway/rule";
    /**
     * app auth
     */
    String ETCD_APP_PATH = "/gateway/app_auth";
    /**
     * concurrency_control
     */
    String CONCURRENCY_CONTROL = "concurrency_control";
    /**
     * start_time
     */
    String START_TIME = "start_time";
    /**
     * real_server
     */
    String REAL_SERVER="real_server";
    /**
     * losttime_rate
     */
    double  LOSTTIME_RATE=0.4;


}

