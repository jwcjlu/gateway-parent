package com.jwcjlu.gateway.core.metric;

import com.jwcjlu.gateway.common.enums.CounterTypeEnum;

import java.util.concurrent.atomic.AtomicLong;

public class EwayMetric implements Metric {
    private AtomicLong error;
    private AtomicLong total;
    private AtomicLong lostTime;
    private long maxTime;
    private long minTime=Long.MAX_VALUE;

    public EwayMetric() {
        error = new AtomicLong(0);
        total = new AtomicLong(0);
        lostTime = new AtomicLong(0);
    }

    @Override
    public void inc(CounterTypeEnum typeEnum) {
        inc(1, typeEnum);
    }

    @Override
    public void inc(long n, CounterTypeEnum typeEnum) {
        switch (typeEnum) {
            case ERROR:
                error.addAndGet(n);
                total.addAndGet(n);
                break;
            case TOTAL:
                total.addAndGet(n);
                break;
            case LOSTTIME:
                lostTime.addAndGet(n);
                max(n);
                min(n);
        }

    }

    @Override
    public void dec(long n, CounterTypeEnum typeEnum) {
        switch (typeEnum) {
            case ERROR:
                error.addAndGet(-n);
                total.addAndGet(-n);
                break;
            case TOTAL:
                total.addAndGet(-n);
                break;
            case LOSTTIME:
                lostTime.addAndGet(-n);
        }
    }

    @Override
    public void dec(CounterTypeEnum typeEnum) {
        dec(1, typeEnum);
    }

    public void max(long value) {
        maxTime = maxTime > value ? maxTime : value;
    }

    public void min(long value) {
        minTime = minTime < value ? minTime : value;
    }

    public AtomicLong getError() {
        return error;
    }

    public AtomicLong getTotal() {
        return total;
    }

    public AtomicLong getLostTime() {
        return lostTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public void clear() {
        error = new AtomicLong(0);
        total = new AtomicLong(0);
        lostTime = new AtomicLong(0);
        minTime =Long.MAX_VALUE;
        maxTime = 0;
    }

    @Override
    public String toString() {
        return "EwayMetric{" +
            "error=" + error +
            ", total=" + total +
            ", lostTime=" + lostTime +
            ", maxTime=" + maxTime +
            ", minTime=" + minTime +
            '}';
    }
}
