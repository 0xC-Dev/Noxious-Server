package org.noxious.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.noxious.netty.bytebuf.ByteBufWrapper;
import org.noxious.packet.DefinedPacket;

public class PacketEncoder extends MessageToByteEncoder<DefinedPacket> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, DefinedPacket definedPacket, ByteBuf byteBuf) throws Exception {
        // Write the packet id first
        int packetId = definedPacket.getPacketId();
        System.out.println("packetId: " + packetId + " " + this.getClass().getSimpleName());
        if (packetId == 0) {
            throw new IllegalArgumentException("Invalid packet ID: 0");
        }

        // Write the packet ID
        ByteBufWrapper.writeVarInt(packetId, byteBuf);
        // Write the contents
        definedPacket.write(byteBuf);
    }
}