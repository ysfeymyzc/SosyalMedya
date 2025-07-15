package com.yourname.socialmedia.gui;

import com.yourname.socialmedia.data.DataManager;
import com.yourname.socialmedia.utils.SecurityUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LoginScreen extends Screen {
    private EditBox usernameField;
    private EditBox passwordField;
    private EditBox verificationField;
    private String errorMessage = "";
    private boolean needsVerification = false;
    private String pendingUsername = "";
    
    public LoginScreen() {
        super(Component.literal("Giriş Yap"));
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        if (!needsVerification) {
            // Username field
            this.usernameField = new EditBox(this.font, centerX - 100, centerY - 50, 200, 20, Component.literal("Kullanıcı Adı"));
            this.usernameField.setHint(Component.literal("Kullanıcı Adı"));
            this.addWidget(this.usernameField);
            
            // Password field
            this.passwordField = new EditBox(this.font, centerX - 100, centerY - 20, 200, 20, Component.literal("Şifre"));
            this.passwordField.setHint(Component.literal("Şifre"));
            this.addWidget(this.passwordField);
            
            // Login button
            this.addRenderableWidget(Button.builder(Component.literal("Giriş Yap"), this::onLogin)
                .bounds(centerX - 50, centerY + 10, 100, 20)
                .build());
        } else {
            // Verification field
            this.verificationField = new EditBox(this.font, centerX - 100, centerY - 20, 200, 20, Component.literal("Doğrulama Kodu"));
            this.verificationField.setHint(Component.literal("Doğrulama Kodu"));
            this.addWidget(this.verificationField);
            
            // Verify button
            this.addRenderableWidget(Button.builder(Component.literal("Doğrula"), this::onVerify)
                .bounds(centerX - 50, centerY + 10, 100, 20)
                .build());
        }
        
        // Back button
        this.addRenderableWidget(Button.builder(Component.literal("Geri"), this::onBack)
            .bounds(centerX - 50, centerY + 40, 100, 20)
            .build());
    }
    
    private void onLogin(Button button) {
        String username = this.usernameField.getValue().trim();
        String password = this.passwordField.getValue().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            this.errorMessage = "Kullanıcı adı ve şifre boş olamaz!";
            return;
        }
        
        if (!DataManager.authenticateUser(username, password)) {
            this.errorMessage = "Şifre yanlış!";
            return;
        }
        
        if (DataManager.isUserLoggedIn(username)) {
            // Need verification
            String verificationCode = SecurityUtils.generateVerificationCode();
            DataManager.setVerificationCode(username, verificationCode);
            
            // Show verification code to account owner
            SecurityUtils.showVerificationCodeToOwner(username, verificationCode);
            
            this.pendingUsername = username;
            this.needsVerification = true;
            this.errorMessage = "";
            this.init(); // Reinitialize GUI
        } else {
            DataManager.loginUser(this.minecraft.player.getUUID(), username);
            this.minecraft.setScreen(new MainMenuScreen(username));
        }
    }
    
    private void onVerify(Button button) {
        String code = this.verificationField.getValue().trim();
        String correctCode = DataManager.getVerificationCode(this.pendingUsername);
        
        if (code.equals(correctCode)) {
            DataManager.loginUser(this.minecraft.player.getUUID(), this.pendingUsername);
            this.minecraft.setScreen(new MainMenuScreen(this.pendingUsername));
        } else {
            this.errorMessage = "Doğrulama kodu yanlış!";
        }
    }
    
    private void onBack(Button button) {
        this.minecraft.setScreen(null);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Title
        String title = needsVerification ? "Doğrulama Kodu Girin" : "Giriş Yap";
        guiGraphics.drawCenteredString(this.font, title, this.width / 2, 20, 0xFFFFFF);
        
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