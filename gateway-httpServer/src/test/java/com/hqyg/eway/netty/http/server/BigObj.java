package com.jwcjlu.gateway.netty.http.server;

public class BigObj {
    static int id = 0;
    int v;
    BigObj() {
        this.v = id++;
        System.out.println("create " + v);
    }
    public void destroy() {
        System.out.println("destroy " + v);
    }

    public int getV() {
        return v;
    }

}
