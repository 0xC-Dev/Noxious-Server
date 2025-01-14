package org.noxious.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.noxious.netty.decoder.PacketDecoder;
import org.noxious.netty.encoder.PacketEncoder;
import org.noxious.netty.handlers.CDPHandler;
import org.noxious.netty.handlers.HandlerBoss;
import org.noxious.netty.utils.PipelineUtils;


public class NoxiousInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // Our client connected!

        // Add our decoder and enocder's to the netty pipeline
        ch.pipeline().addLast(PipelineUtils.DECODER,new PacketDecoder());
        ch.pipeline().addLast(PipelineUtils.ENCODER,new PacketEncoder());
        ch.pipeline().addLast(PipelineUtils.HANDLER_BOSS,new HandlerBoss());

        // Set our default packet handler now!
        ch.pipeline().get(HandlerBoss.class).setHandler(new CDPHandler());

    }
}