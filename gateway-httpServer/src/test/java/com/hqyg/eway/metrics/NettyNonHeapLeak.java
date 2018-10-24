package com.jwcjlu.gateway.metrics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;


public class NettyNonHeapLeak {
    public static void main(String[] args) {
        int i=0;
        PooledByteBufAllocator allocator=  new PooledByteBufAllocator();
        try {
            for (; i < 1000000; i++) {
                ByteBuf bytebuf = allocator.directBuffer(1024);
                //ReferenceCountUtil.release(bytebuf);

            }
        }catch (Throwable e){
            e.printStackTrace();
            System.out.println(i);
        }
    }
}
