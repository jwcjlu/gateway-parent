package com.jwcjlu.gateway.httpServer.DTO;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

import org.junit.Test;

public class RequestDTOTest {
    @Test
    public void build(){
        HttpHeaders headers=new DefaultHttpHeaders();
        headers.add("serverKey","serverKey");
        HttpRequest request=new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"www.baidu.com", Unpooled.copiedBuffer("".getBytes()),headers,headers);
        RequestDTO requestDTO=  RequestDTO.build(request);
        System.out.println(requestDTO);
    }

}
