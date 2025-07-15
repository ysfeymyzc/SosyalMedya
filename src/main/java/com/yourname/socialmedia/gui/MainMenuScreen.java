package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.Post;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MainMenuScreen extends Screen {
    private final String username;
    private List<Post> posts;
    
    public MainMenuScreen(String username) {
        super(Component.literal("Ana Menü"));
        this.username = username;
        this.posts = DataManager.getPosts();
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        
        // Share post button
        this.addRenderableWidget(Button.builder(Component.literal("Gönderi Paylaş"), this::onSharePost)
            .bounds(centerX - 75, 30, 150, 20)
            .build());
        
        // My posts button
        this.addRenderableWidget(Button.builder(Component.literal("Benim Gönderilerim"), this::onMyPosts)
            .bounds(centerX - 75, 55, 150, 20)
            .build());
        
        // Logout button
        this.addRenderableWidget(Button.builder(Component.literal("Çıkış Yap"), this::onLogout)
            .bounds(centerX - 50, this.height - 30, 100, 20)
            .build());
        
        // Post buttons
        int yOffset = 85;
        for (int i = 0; i < Math.min(posts.size(), 10); i++) {
            Post post = posts.get(i);
            String buttonText = post.getUsername() + "\n" + post.getPreview();
            
            Button postButton = Button.builder(Component.literal(buttonText), (button) -> onPostClick(post))
                .bounds(centerX - 150, yOffset, 300, 40)
                .build();
            
            this.addRenderableWidget(postButton);
            yOffset += 45;
        }
    }
    
    private void onSharePost(Button button) {
        this.minecraft.setScreen(new PostScreen(this.username, this));
    }
    
    private void onMyPosts(Button button) {
        this.minecraft.setScreen(new MyPostsScreen(this.username, this));
    }
    
    private void onPostClick(Post post) {
        this.minecraft.setScreen(new ViewPostScreen(post, this.username, this));
    }
    
    private void onLogout(Button button) {
        DataManager.logoutUser(this.minecraft.player.getUUID());
        this.minecraft.setScreen(null);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Title
        guiGraphics.drawCenteredString(this.font, "Ana Menü - " + this.username, this.width / 2, 10, 0xFFFFFF);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    public void refresh() {
        this.posts = DataManager.getPosts();
        this.init();
    }
}