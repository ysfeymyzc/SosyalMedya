package com.yourname.socialmedia.network;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.Post;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PostUpdatePacket {
    private final Post post;
    private final boolean isDelete;
    
    public PostUpdatePacket(Post post, boolean isDelete) {
        this.post = post;
        this.isDelete = isDelete;
    }
    
    public static void encode(PostUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.isDelete);
        if (!packet.isDelete) {
            buffer.writeUtf(packet.post.getUsername());
            buffer.writeUtf(packet.post.getContent());
            buffer.writeBoolean(packet.post.isEncrypted());
            buffer.writeUtf(packet.post.getEncryptionPassword());
        }
    }
    
    public static PostUpdatePacket decode(FriendlyByteBuf buffer) {
        boolean isDelete = buffer.readBoolean();
        if (isDelete) {
            return new PostUpdatePacket(null, true);
        } else {
            String username = buffer.readUtf();
            String content = buffer.readUtf();
            boolean encrypted = buffer.readBoolean();
            String password = buffer.readUtf();
            return new PostUpdatePacket(new Post(username, content, encrypted, password), false);
        }
    }
    
    public static void handle(PostUpdatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!packet.isDelete) {
                DataManager.addPost(packet.post);
            }
        });
        context.setPacketHandled(true);
    }
}