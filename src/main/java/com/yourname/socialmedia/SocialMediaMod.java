package com.yourname.socialmedia;

import com.yourname.socialmedia.commands.RegisterCommand;
import com.yourname.socialmedia.commands.SocialMediaCommand;
import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("socialmedia")
public class SocialMediaMod {
    public static final String MODID = "socialmedia";
    
    public SocialMediaMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }
