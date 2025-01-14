package org.noxious.netty.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.noxious.netty.handlers.events.IChannelEventsHandler;
import org.noxious.packet.DefinedPacket;

public class HandlerBoss extends ChannelInboundHandlerAdapter {
    private IChannelEventsHandler handler;

    public void setHandler(IChannelEventsHandler handler){
        this.handler = handler;
    }

    // On connect
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if ( handler != null ) {
            handler.connected(ctx);
        }
    }

    // On disconnect
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(handler != null){
            handler.disconnected(ctx);
        }
    }

    // Any and all content coming through
    // Note: any polymorphic logic would be here and identifying packet would be handled here
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(handler != null) {
            DefinedPacket packet = (DefinedPacket) msg;
            handler.handlePacket(ctx, packet);
        }
    }
}
