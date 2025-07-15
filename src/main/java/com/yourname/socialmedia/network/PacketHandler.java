package com.yourname.socialmedia.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("socialmedia", "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    public static void register() {
        int packetId = 0;
        INSTANCE.registerMessage(packetId++, VerificationCodePacket.class, 
            VerificationCodePacket::encode, VerificationCodePacket::decode, 
            VerificationCodePacket::handle);
        
        INSTANCE.registerMessage(packetId++, PostUpdatePacket.class,
            PostUpdatePacket::encode, PostUpdatePacket::decode,
            PostUpdatePacket::handle);
    }
}