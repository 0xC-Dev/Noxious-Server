package org.noxious.packet;

import org.noxious.packet.impl.HandshakePacket;
import org.noxious.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketRegistry {

    private static PacketRegistry instance;

    public PacketRegistry() {
        instance = this;
    }

    private static final Map<Integer, Supplier<? extends DefinedPacket>> PACKET_MAP = new HashMap<>();

    // Initialize the registry
    public static void init() {
        registerPacket(HandshakePacket::new);
    }

    // Register a packet type with an ID and supplier
    public static void registerPacket(Supplier<? extends DefinedPacket> supplier) {
        int id = supplier.get().getPacketId();
        if (isPacketRegistered(id)) {
            Logger.error("Packet ID " + id + " is already registered.");
            return;
        }
        PACKET_MAP.put(id, supplier);
    }

    // Create a packet instance by ID
    public DefinedPacket createPacket(int id) {
        Supplier<? extends DefinedPacket> supplier = PACKET_MAP.get(id);
        if (supplier == null) {
            throw new IllegalArgumentException("No packet registered for ID " + id);
        }
        return supplier.get();
    }

    // Check if a packet ID is registered
    public static boolean isPacketRegistered(int id) {
        return PACKET_MAP.containsKey(id);
    }

    public static PacketRegistry getInstance() {
        if (instance == null) {
            return new PacketRegistry();
        }
        return instance;
    }
}