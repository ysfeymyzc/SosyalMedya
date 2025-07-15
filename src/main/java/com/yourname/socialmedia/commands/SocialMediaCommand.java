package com.yourname.socialmedia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.yourname.socialmedia.gui.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SocialMediaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sosyal_medya")
            .executes(SocialMediaCommand::execute));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static int execute(CommandContext<CommandSourceStack> context) {
        Minecraft.getInstance().setScreen(new LoginScreen());
        return 1;
    }
}