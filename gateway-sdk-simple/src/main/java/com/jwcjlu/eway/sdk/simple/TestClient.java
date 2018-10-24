package com.jwcjlu.gateway.sdk.simple;

import com.jwcjlu.gateway.sdk.client.Client;
import com.jwcjlu.gateway.sdk.client.NettyClient;
import com.jwcjlu.gateway.sdk.http.Header;
import com.jwcjlu.gateway.sdk.http.Request;
import com.jwcjlu.gateway.sdk.service.ServiceDiscovery;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class TestClient {
    public static void main(String[] args) throws Exception {
        ServiceDiscovery serviceDiscovery=new ServiceDiscovery("http://10.60.38.51:2379");
        serviceDiscovery.start();

            try {
                Client client = new NettyClient();

                Header header = Header.http().serverKey("cctTestService").module("pdm").timestamp("123456789").build();
                header.addHeaders("Content-Type","application/json");
              //  Request request=Request.post().uri("/helloWorld").header(header).build();
                Request request=Request.post().uri("/delay").context("{\n" +
                    "\"time\":1000,\n" +
                    "\"module\":\"tasgts\"\n" +
                    "}").header(header).build();
                client.asyncRequest(request,(response)->{
                    System.out.println(response);
                });
            }finally {
                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
            }

    }
}
