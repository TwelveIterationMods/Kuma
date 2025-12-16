package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.ManagedKeyMappingImpl;
import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.screen.KeyBindsScreenHooks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.util.Mth;

public class FabricKumaAPI implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof ManagedKeyMappingImpl managedKeyMapping) {
                    managedKeyMapping.tick();
                }
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof KeyBindsScreen) {
                ScreenMouseEvents.afterMouseRelease(screen).register(KeyBindsScreenHooks::afterMouseRelease);
                ScreenKeyboardEvents.afterKeyRelease(screen).register(KeyBindsScreenHooks::afterKeyRelease);
            }

            ScreenMouseEvents.allowMouseClick(screen).register((clickedScreen, button) -> {
                for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                    if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                        continue;
                    }

                    if (keyMapping.isActiveAndMatchesMouse(button)) {
                        if (keyMapping.handleScreenInput(new ScreenInputEvent(clickedScreen, button, button.x(), button.y(), keyMapping))) {
                            return false;
                        }
                    }
                }
                return true;
            });

            ScreenKeyboardEvents.allowKeyPress(screen).register((pressedScreen, event) -> {
                for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                    if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                        continue;
                    }

                    if (keyMapping.isActiveAndMatchesKey(event)) {
                        final var window = client.getWindow();
                        int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                        int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                        if (keyMapping.ignoresScreenFocus() || !pressedScreen.isFocused()) {
                            if (keyMapping.handleScreenInput(new ScreenInputEvent(pressedScreen, event, mouseX, mouseY, keyMapping))) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            });
        });
    }
}
