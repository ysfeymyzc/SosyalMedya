package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RegisterScreen extends Screen {
    private EditBox usernameField;
    private EditBox passwordField;
    private String errorMessage = "";
    
    public RegisterScreen() {
        super(Component.literal("Kayıt Ol"));
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Username field
        this.usernameField = new EditBox(this.font, centerX - 100, centerY - 50, 200, 20, Component.literal("Kullanıcı Adı"));
        this.usernameField.setHint(Component.literal("Kullanıcı Adı"));
        this.addWidget(this.usernameField);
        
        // Password field
        this.passwordField = new EditBox(this.font, centerX - 100, centerY - 20, 200, 20, Component.literal("Şifre"));
        this.passwordField.setHint(Component.literal("Şifre"));
        this.addWidget(this.passwordField);
        
        // Register button
        this.addRenderableWidget(Button.builder(Component.literal("Kayıt Ol"), this::onRegister)
            .bounds(centerX - 50, centerY + 10, 100, 20)
            .build());
        
        // Back button
        this.addRenderableWidget(Button.builder(Component.literal("Geri"), this::onBack)
            .bounds(centerX - 50, centerY + 40, 100, 20)
            .build());
    }
    
    private void onRegister(Button button) {
        String username = this.usernameField.getValue().trim();
        String password = this.passwordField.getValue().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            this.errorMessage = "Kullanıcı adı ve şifre boş olamaz!";
            return;
        }
        
        if (DataManager.registerUser(username, password, this.minecraft.player.getUUID())) {
            this.minecraft.setScreen(null);
        } else {
            this.errorMessage = "Bu kullanıcı adı zaten alınmış!";
        }
    }
    
    private void onBack(Button button) {
        this.minecraft.setScreen(null);
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