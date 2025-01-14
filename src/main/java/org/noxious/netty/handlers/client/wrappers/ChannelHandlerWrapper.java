package org.noxious.netty.handlers.client.wrappers;

import io.netty.channel.ChannelHandlerContext;
import org.noxious.packet.DefinedPacket;
import org.noxious.utils.Logger;

public class ChannelHandlerWrapper {

    private final ChannelHandlerContext handler;

    public ChannelHandlerWrapper(ChannelHandlerContext handler) {
        this.handler = handler;
    }

    public void sendPacket(DefinedPacket packet) {
        if(this.getHandler() == null){
            Logger.error("A channel you are trying to send a packet to is null!");
            return;
        }
        this.getHandler().writeAndFlush(packet);
    }

    public ChannelHandlerContext getHandler() {
        return handler;
    }
}
