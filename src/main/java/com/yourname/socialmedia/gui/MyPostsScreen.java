package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.Post;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class MyPostsScreen extends Screen {
    private final String username;
    private final MainMenuScreen parent;
    private List<Post> myPosts;
    
    public MyPostsScreen(String username, MainMenuScreen parent) {
        super(Component.literal("Benim Gönderilerim"));
        this.username = username;
        this.parent = parent;
        this.myPosts = DataManager.getPosts().stream()
            .filter(post -> post.getUsername().equals(username))
            .collect(Collectors.toList());
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        
        // Back button
        this.addRenderableWidget(Button.builder(Component.literal("Ana Menüye Dön"), this::onBack)
            .bounds(centerX - 75, 30, 150, 20)
            .build());
        
        // My post buttons
        int yOffset = 60;
        for (int i = 0; i < Math.min(myPosts.size(), 10); i++) {
            Post post = myPosts.get(i);
            String buttonText = post.getUsername() + "\n" + post.getPreview();
            
            Button postButton = Button.builder(Component.literal(buttonText), (button) -> onPostClick(post))
                .bounds(centerX - 150, yOffset, 300, 40)
                .build();
            
            this.addRenderableWidget(postButton);
            yOffset += 45;
        }
        
        // Info text if no posts
        if (myPosts.isEmpty()) {
            // This will be shown in render method
        }
    }
    
    private void onPostClick(Post post) {
        this.minecraft.setScreen(new ViewPostScreen(post, this.username, this));
    }
    
    private void onBack(Button button) {
        this.minecraft.setScreen(this.parent);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Title
        guiGraphics.drawCenteredString(this.font, "Benim Gönderilerim - " + this.username, this.width / 2, 10, 0xFFFFFF);
        
        // No posts message
        if (myPosts.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "Henüz hiç gönderi paylaşmadınız.", this.width / 2, this.height / 2, 0xAAAAAA);
        } else {
            // Post count
            guiGraphics.drawCenteredString(this.font, "Toplam " + myPosts.size() + " gönderi", this.width / 2, this.height - 15, 0xAAAAAA);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    public void refresh() {
        this.myPosts = DataManager.getPosts().stream()
            .filter(post -> post.getUsername().equals(username))
            .collect(Collectors.toList());
        this.init();
    }
}
    
    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
        DataManager.initialize();
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
        // Client-side setup
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RegisterCommand.register(event.getDispatcher());
        SocialMediaCommand.register(event.getDispatcher());
    }
}