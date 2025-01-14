package org.noxious.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.noxious.netty.bytebuf.ByteBufWrapper;
import org.noxious.packet.DefinedPacket;
import org.noxious.packet.PacketRegistry;
import org.noxious.utils.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (!ctx.channel().isActive() || !byteBuf.isReadable()) {
            byteBuf.release();
            return;
        }

        ByteBuf copy = byteBuf.retainedSlice(); // Safely borrow the buffer
        int id = 0;

        try {
            // Attempt to read the packet ID
            id = ByteBufWrapper.readVarInt(copy, 5);
            Logger.info("Packet ID: " + id);
            System.out.println(ctx.channel().remoteAddress());

            // Validate packet ID through 1 PacketRegistry instance vs static method
            PacketRegistry packetRegistry = PacketRegistry.getInstance();
            DefinedPacket packet = packetRegistry.createPacket(id);

            if (packet == null || !PacketRegistry.isPacketRegistered(id)) {
                Logger.warn("Unregistered or invalid packet ID: " + id + " from: " + ctx.channel().remoteAddress());
                return; // Don't close the connection immediately
            }

            // Deserialize the valid packet
            packet.read(byteBuf);
            out.add(packet); // Pass the decoded packet to the pipeline

        } catch (Exception e) {
            // Handle invalid data
            byte[] invalidBytes = new byte[Math.min(byteBuf.readableBytes(), 1024)]; // Limit length for logging
            byteBuf.readBytes(invalidBytes);
            String receivedData = new String(invalidBytes, StandardCharsets.UTF_8).trim();
            Logger.error("Received invalid data from: " + ctx.channel().remoteAddress());
            Logger.error("Invalid Byte length: " + invalidBytes.length);
            Logger.warn("Data: " + receivedData);

            // Todo Identifying compromised clients that have been detected and sending a kill packet to kill its self and remove all trace
            // Optionally close the connection if data seems malicious
            // ctx.close();
        }

        copy.release(); // Release the borrowed buffer
    }
}