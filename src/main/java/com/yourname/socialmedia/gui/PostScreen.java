package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.Post;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PostScreen extends Screen {
    private final String username;
    private final MainMenuScreen parent;
    private EditBox contentField;
    private EditBox passwordField;
    private boolean isEncrypted = false;
    private String errorMessage = "";
    
    public PostScreen(String username, MainMenuScreen parent) {
        super(Component.literal("Gönderi Paylaş"));
        this.username = username;
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Content field
        this.contentField = new EditBox(this.font, centerX - 150, centerY - 50, 300, 60, Component.literal("İçerik"));
        this.contentField.setHint(Component.literal("Gönderinizi buraya yazın..."));
        this.addWidget(this.contentField);
        
        // Encrypted toggle
        String toggleText = isEncrypted ? "☑ Şifreli gönderi" : "☐ Şifreli gönderi";
        this.addRenderableWidget(Button.builder(Component.literal(toggleText), this::onToggleEncryption)
            .bounds(centerX - 150, centerY + 20, 150, 20)
            .build());
        
        // Password field (only if encrypted)
        if (isEncrypted) {
            this.passwordField = new EditBox(this.font, centerX - 150, centerY + 50, 300, 20, Component.literal("Şifre"));
            this.passwordField.setHint(Component.literal("Gönderi şifresi"));
            this.addWidget(this.passwordField);
        }
        
        // Share button
        this.addRenderableWidget(Button.builder(Component.literal("Paylaş"), this::onShare)
            .bounds(centerX - 50, centerY + 80, 100, 20)
            .build());
        
        // Back button
        this.addRenderableWidget(Button.builder(Component.literal("Geri"), this::onBack)
            .bounds(centerX - 50, centerY + 110, 100, 20)
            .build());
    }
    
    private void onToggleEncryption(Button button) {
        this.isEncrypted = !this.isEncrypted;
        this.init(); // Reinitialize GUI
    }
    
    private void onShare(Button button) {
        String content = this.contentField.getValue().trim();
        
        if (content.isEmpty()) {
            this.errorMessage = "İçerik boş olamaz!";
            return;
        }
        
        String password = "";
        if (isEncrypted) {
            password = this.passwordField.getValue().trim();
            if (password.isEmpty()) {
                this.errorMessage = "Şifreli gönderi için şifre gerekli!";
                return;
            }
        }
        
        Post post = new Post(this.username, content, isEncrypted, password);
        DataManager.addPost(post);
        
        this.parent.refresh();
        this.minecraft.setScreen(this.parent);
    }
    
    private void onBack(Button button) {
        this.minecraft.setScreen(this.parent);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Title
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        
        // Error message
        if (!this.errorMessage.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, this.errorMessage, this.width / 2, this.height / 2 - 80, 0xFF0000);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}