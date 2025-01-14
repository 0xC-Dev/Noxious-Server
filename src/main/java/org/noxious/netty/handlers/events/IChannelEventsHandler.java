package org.noxious.netty.handlers.events;

import io.netty.channel.ChannelHandlerContext;
import org.noxious.packet.DefinedPacket;

public interface IChannelEventsHandler {

    void connected(ChannelHandlerContext ch) throws Exception;

    void disconnected(ChannelHandlerContext ch) throws Exception;

    void handlePacket(ChannelHandlerContext ch, DefinedPacket packet) throws Exception;

}