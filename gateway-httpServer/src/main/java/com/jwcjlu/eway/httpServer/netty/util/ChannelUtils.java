package com.jwcjlu.gateway.httpServer.netty.util;

import io.netty.channel.Channel;

public class ChannelUtils {
    public static String channelInfoForLogging(Channel ch) {
        if (ch == null) {
            return "null";
        }

        String channelInfo = ch.toString()
            + ", active=" + ch.isActive()
            + ", open=" + ch.isOpen()
            + ", registered=" + ch.isRegistered()
            + ", writable=" + ch.isWritable()
            + ", id=" + ch.id();


        return "Channel: " + channelInfo;
    }
}
