package org.noxious.netty.handlers.client;

import org.noxious.netty.handlers.client.wrappers.ClientWrapper;

import java.util.HashMap;
import java.util.Map;

public class ClientChannelHandler {
    private static final Map<Integer, ClientWrapper> clientChannelMap = new HashMap<>();

    // Add a client-channel mapping
    public static void addClient(int clientId, ClientWrapper channel) {
        clientChannelMap.put(clientId, channel);
    }

    // Get the channel for a client
    public static ClientWrapper getChannel(int clientId) {
        return clientChannelMap.get(clientId);
    }

    public static boolean isDupe(int clientId) {
        return clientChannelMap.containsKey(clientId);
    }

    // Remove a client-channel mapping
    public static void removeClient(int clientId) {
        clientChannelMap.remove(clientId);
    }
}