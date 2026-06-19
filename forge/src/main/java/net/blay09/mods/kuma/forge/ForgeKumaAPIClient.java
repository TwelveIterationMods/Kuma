package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.ManagedVanillaKeyMapping;
import net.blay09.mods.kuma.NativeKeyModifierReconciler;
import net.blay09.mods.kuma.TickableKeyMapping;
import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.blay09.mods.kuma.screen.KeyBindsScreenHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class ForgeKumaAPIClient {
    public static void init(BusGroup modBusGroup) {
        RegisterKeyMappingsEvent.BUS.addListener((event) -> {
            for (final var managedKeyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (managedKeyMapping instanceof ManagedVanillaKeyMapping vanillaManagedKeyMapping) {
                    event.register(vanillaManagedKeyMapping.register());
                }
            }
        });

        FMLLoadCompleteEvent.getBus(modBusGroup).addListener(_ -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof ManagedVanillaKeyMapping vanillaManagedKeyMapping) {
                    NativeKeyModifierReconciler.reconcile(vanillaManagedKeyMapping.register());
                }
            }
        });

        TickEvent.ClientTickEvent.Post.BUS.addListener((event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof TickableKeyMapping tickableKeyMapping) {
                    tickableKeyMapping.tick();
                }
            }
        });

        InputEvent.MouseButton.Pre.BUS.addListener((event) -> {
            if (Minecraft.getInstance().gui.screen() != null) {
                return false;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleWorldInput(new WorldInputEvent(event.getInfo(), keyMapping))) {
                        return true;
                    }
                }
            }

            return false;
        });

        InputEvent.Key.BUS.addListener((event) -> {
            if (Minecraft.getInstance().gui.screen() != null) {
                return;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (event.getAction() == 1 && keyMapping.isActiveAndMatchesKey(event.getKey(), event.getScanCode(), event.getModifiers())) {
                    keyMapping.handleWorldInput(new WorldInputEvent(event.getInfo(), keyMapping));
                    // TODO cannot cancel?
                }
            }
        });

        ScreenEvent.KeyPressed.Pre.BUS.addListener((event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                return !KeyBindsScreenHooks.allowKeyPress(event.getScreen(), event.getInfo());
            }
            return false;
        });

        ScreenEvent.KeyReleased.Pre.BUS.addListener((event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                return !KeyBindsScreenHooks.allowKeyRelease(event.getScreen(), event.getInfo());
            }
            return false;
        });

        ScreenEvent.MouseButtonPressed.Pre.BUS.addListener((event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                return !KeyBindsScreenHooks.allowMouseClick(event.getScreen(), event.getInfo());
            }
            return false;
        });

        ScreenEvent.MouseButtonReleased.Pre.BUS.addListener((event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                final var mouseButtonInfo = new MouseButtonInfo(event.getButton(), Kuma.getActiveModifierFlags());
                final var mouseButtonEvent = new MouseButtonEvent(event.getMouseX(), event.getMouseY(), mouseButtonInfo);
                return !KeyBindsScreenHooks.allowMouseRelease(event.getScreen(), mouseButtonEvent);
            }
            return false;
        });

        ScreenEvent.KeyPressed.Pre.BUS.addListener((event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesKey(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                    final var client = Minecraft.getInstance();
                    final var window = client.getWindow();
                    int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                    int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                    final var inputEvent = new ScreenInputEvent(event.getScreen(), event.getInfo(), mouseX, mouseY, keyMapping);
                    if (keyMapping.ignoresScreenFocus() || !inputEvent.hasFocusedElement()) {
                        if (keyMapping.handleScreenInput(inputEvent)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        });

        ScreenEvent.MouseButtonPressed.Pre.BUS.addListener((event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleScreenInput(new ScreenInputEvent(event.getScreen(), event.getInfo(), event.getMouseX(), event.getMouseY(), keyMapping))) {
                        return true;
                    }
                }
            }
            return false;
        });
    }
}
