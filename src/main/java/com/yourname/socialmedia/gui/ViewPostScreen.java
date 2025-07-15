package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.data.Post;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ViewPostScreen extends Screen {
    private final Post post;
    private final String currentUser;
    private final Screen parent;
    private EditBox passwordField;
    private boolean needsPassword = false;
    private String errorMessage = "";
    private boolean isDecrypted = false;
    
    public ViewPostScreen(Post post, String currentUser, Screen parent) {
        super(Component.literal("Gönderi"));
        this.post = post;
        this.currentUser = currentUser;
        this.parent = parent;
        this.needsPassword = post.isEncrypted();
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        if (needsPassword && !isDecrypted) {
            // Password field
            this.passwordField = new EditBox(this.font, centerX - 100, centerY - 10, 200, 20, Component.literal("Şifre"));
            this.passwordField.setHint(Component.literal("Gönderi şifresi"));
            this.addWidget(this.passwordField);
            
            // Decrypt button
            this.addRenderableWidget(Button.builder(Component.literal("Aç"), this::onDecrypt)
                .bounds(centerX - 50, centerY + 20, 100, 20)
                .build());
        } else {
            // Delete button (only for post owner)
            if (post.getUsername().equals(currentUser)) {
                this.addRenderableWidget(Button.builder(Component.literal("Sil"), this::onDelete)
                    .bounds(centerX - 50, centerY + 100, 100, 20)
                    .build());
            }
        }
        
        // Back button
        this.addRenderableWidget(Button.builder(Component.literal("Geri"), this::onBack)
            .bounds(centerX - 50, this.height - 30, 100, 20)
            .build());
    }
    
    private void onDecrypt(Button button) {
        String password = this.passwordField.getValue().trim();
        
        if (password.equals(post.getEncryptionPassword())) {
            this.isDecrypted = true;
            this.needsPassword = false;
            this.init(); // Reinitialize GUI
        } else {
            this.errorMessage = "Şifre yanlış!";
        }
    }
    
    private void onDelete(Button button) {
        DataManager.removePost(post.getId(), currentUser);
        
        // Check if we came from MyPostsScreen
        if (this.parent instanceof MyPostsScreen) {
            ((MyPostsScreen) this.parent).refresh();
        } else {
            this.parent.refresh();
        }
        
        this.minecraft.setScreen(this.parent);
    }
    
    private void onBack(Button button) {
        this.minecraft.setScreen(this.parent);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Title
        guiGraphics.drawCenteredString(this.font, "Gönderi - " + post.getUsername(), this.width / 2, 20, 0xFFFFFF);
        
        // Content
        if (!needsPassword || isDecrypted) {
            String[] lines = post.getContent().split("\\n");
            int yOffset = 60;
            for (String line : lines) {
                guiGraphics.drawCenteredString(this.font, line, this.width / 2, yOffset, 0xFFFFFF);
                yOffset += 15;
            }
        } else {
            guiGraphics.drawCenteredString(this.font, "Bu gönderi şifreli. Görüntülemek için şifre girin.", this.width / 2, 60, 0xFFFFFF);
        }
        
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