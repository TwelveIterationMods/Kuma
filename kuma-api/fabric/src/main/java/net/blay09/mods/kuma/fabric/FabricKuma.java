package net.blay09.mods.kuma.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.api.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.util.Mth;

public class FabricKuma implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            ScreenMouseEvents.allowMouseClick(screen).register((clickedScreen, mouseX, mouseY, button) -> {
                for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                    if (keyMapping.isActiveAndMatchesMouse(button)) {
                        if (keyMapping.handleScreenInput(new ScreenInputEvent(clickedScreen, mouseX, mouseY))) {
                            // We only cancel click events that aren't sole left clicks, otherwise people might get stuck in menu screens
                            return button == InputConstants.MOUSE_BUTTON_LEFT && keyMapping.getBinding().modifiers().isEmpty();
                        }
                    }
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyPress(screen).register((pressedScreen, key, scanCode, modifiers) -> {
                for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                    if (keyMapping.isActiveAndMatchesKey(key, scanCode, modifiers)) {
                        final var window = client.getWindow();
                        int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                        int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                        if (keyMapping.handleScreenInput(new ScreenInputEvent(pressedScreen, mouseX, mouseY))) {
                            return false;
                        }
                    }
                }
                return true;
            });
        });
    }
}
