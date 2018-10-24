package com.jwcjlu.gateway.core.node;

public class ServerInfo {
    private String host;
    private int port;
    private int weight = 100;
    private int retry;
    private long timeout;

    public ServerInfo() {

    }

    public ServerInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ServerInfo(String host, int port, int weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public ServerInfo(String host, int port, int weight, int retry, long timeout) {
        this.host = host;
        this.port = port;
        this.weight = weight;
        this.retry = retry;
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight){
        this.weight=weight;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        if (port != that.port) return false;
        return host != null ? host.equals(that.host) : that.host == null;
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + retry;
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
            "host='" + host + '\'' +
            ", port=" + port +
            ", weight=" + weight +
            ", retry=" + retry +
            ", timeout=" + timeout +
            '}';
    }
}
