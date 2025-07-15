package com.yourname.socialmedia.utils;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.User;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class SecurityUtils {
    private static final Random random = new Random();
    
    public static String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    public static void showVerificationCodeToOwner(String username, String code) {
        User user = DataManager.getUser(username);
        if (user != null) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(user.getPlayerId());
            if (player != null) {
                player.displayClientMessage(Component.literal("Doğrulama Kodu: " + code), true);
            }
        }
    }
}