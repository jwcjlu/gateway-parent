package com.jwcjlu.gateway.sdk.future;

import com.jwcjlu.gateway.sdk.http.Response;

public interface ResponseFuture {

    Response get() throws Exception;

    Response get(long timeOut) throws Exception;

    boolean isDone();

    void receive(Response reponse);


}
