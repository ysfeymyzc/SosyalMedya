package com.yourname.socialmedia.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VerificationCodePacket {
    private final String code;
    
    public VerificationCodePacket(String code) {
        this.code = code;
    }
    
    public static void encode(VerificationCodePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.code);
    }
    
    public static VerificationCodePacket decode(FriendlyByteBuf buffer) {
        return new VerificationCodePacket(buffer.readUtf());
    }
    
    public static void handle(VerificationCodePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player != null) {
                player.displayClientMessage(Component.literal("Doğrulama Kodu: " + packet.code), true);
            }
        });
        context.setPacketHandled(true);
    }
}