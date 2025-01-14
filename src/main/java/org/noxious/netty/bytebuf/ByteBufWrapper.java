package org.noxious.netty.bytebuf;

//import com.google.common.base.Charsets;
// Attempting netty utils Charsets

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;
import org.noxious.packet.exception.OverflowPacketException;

public class ByteBufWrapper {

    public static int readVarInt(ByteBuf input) {
        return readVarInt(input, 5);
    }

    public static int readVarInt(ByteBuf input, int maxBytes) {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = input.readByte();

            out |= (in & 0x7F) << (bytes++ * 7);

            if (bytes > maxBytes) {
                throw new RuntimeException("VarInt too big");
            }

            if ((in & 0x80) != 0x80) {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, ByteBuf output) {
        int part;
        while (true) {
            part = value & 0x7F;

            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }

            output.writeByte(part);

            if (value == 0) {
                break;
            }
        }
    }

    public static void writeString(String s, ByteBuf buf) throws Exception {
        writeString(s, buf, Short.MAX_VALUE);
    }

    public static void writeString(String s, ByteBuf buf, int maxLength) throws Exception {
        if (s.length() > maxLength) {
            throw new OverflowPacketException("Cannot send string longer than " + maxLength + " (got " + s.length() + " characters)");
        }

        byte[] b = s.getBytes(CharsetUtil.UTF_8);
        if (b.length > maxLength * 3) {
            throw new OverflowPacketException("Cannot send string longer than " + (maxLength * 3) + " (got " + b.length + " bytes)");
        }

        writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }

    public static String readString(ByteBuf buf) throws Exception {
        return readString(buf, Short.MAX_VALUE);
    }

    public static String readString(ByteBuf buf, int maxLen) throws Exception {
        int len = readVarInt(buf);
        if (len > maxLen * 3) {
            throw new OverflowPacketException("Cannot receive string longer than " + maxLen * 3 + " (got " + len + " bytes)");
        }

        String s = buf.toString(buf.readerIndex(), len, CharsetUtil.UTF_8);
        buf.readerIndex(buf.readerIndex() + len);

        if (s.length() > maxLen) {
            throw new OverflowPacketException("Cannot receive string longer than " + maxLen + " (got " + s.length() + " characters)");
        }

        return s;
    }

    public static void write21BitVarInt(ByteBuf buf, int value) {
        // See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
        int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
        buf.writeMedium(w);
    }

    public static ByteBuf copyBuffer(ByteBufAllocator alloc, ByteBuf buf) {
        ByteBuf newBuf = alloc.directBuffer(buf.readByte());
        newBuf.writeBytes(buf);
        return newBuf;
    }

    public static void writeArray(byte[] b, ByteBuf buf) {
        //writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }


    public static byte[] readArray(ByteBuf buf) {
        //int len = readVarInt(buf);
        byte[] ret = new byte[buf.readableBytes()];
        buf.readBytes(ret);
        return ret;
    }

}