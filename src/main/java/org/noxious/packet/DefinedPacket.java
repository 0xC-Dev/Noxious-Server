package org.noxious.packet;

import io.netty.buffer.ByteBuf;

public abstract class DefinedPacket {

    public abstract void write(ByteBuf buf) throws Exception;
    public abstract void read(ByteBuf buf) throws Exception;

    public abstract int getPacketId();
}
