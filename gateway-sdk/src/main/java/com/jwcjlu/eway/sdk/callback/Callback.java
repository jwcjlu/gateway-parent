package com.jwcjlu.gateway.sdk.callback;

import com.jwcjlu.gateway.sdk.http.Response;

public interface Callback {
    public void callback(Response response);
}
