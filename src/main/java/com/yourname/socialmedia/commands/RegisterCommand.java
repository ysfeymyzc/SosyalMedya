package com.yourname.socialmedia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.yourname.socialmedia.gui.RegisterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RegisterCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("kayit")
            .executes(RegisterCommand::execute));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static int execute(CommandContext<CommandSourceStack> context) {
        Minecraft.getInstance().setScreen(new RegisterScreen());
        return 1;
    }
}