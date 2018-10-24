package com.jwcjlu.gateway.core.metric;

import com.jwcjlu.gateway.common.enums.CounterTypeEnum;

public interface Metric {
    void inc(CounterTypeEnum typeEnum);

    void inc(long n, CounterTypeEnum typeEnum);

    void dec(long n, CounterTypeEnum typeEnum);

    void dec(CounterTypeEnum typeEnum);

    void clear();

}
