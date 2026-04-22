package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.ManagedVanillaKeyMapping;
import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.NativeKeyModifierReconciler;
import net.blay09.mods.kuma.TickableKeyMapping;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.blay09.mods.kuma.screen.KeyBindsScreenHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "kuma_api", dist = Dist.CLIENT)
public class NeoForgeKumaAPI {

    public NeoForgeKumaAPI(IEventBus modEventBus) {
        modEventBus.addListener((RegisterKeyMappingsEvent event) -> {
            for (final var managedKeyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (managedKeyMapping instanceof ManagedVanillaKeyMapping vanillaManagedKeyMapping) {
                    event.register(vanillaManagedKeyMapping.register());
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ClientStartedEvent _) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof ManagedVanillaKeyMapping vanillaManagedKeyMapping) {
                    NativeKeyModifierReconciler.reconcile(vanillaManagedKeyMapping.register());
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof TickableKeyMapping tickableKeyMapping) {
                    tickableKeyMapping.tick();
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((InputEvent.MouseButton.Pre event) -> {
            if (Minecraft.getInstance().screen != null) {
                return;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleWorldInput(new WorldInputEvent(event.getMouseButtonInfo(), keyMapping))) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((InputEvent.Key event) -> {
            if (Minecraft.getInstance().screen != null) {
                return;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (event.getAction() == 1 && keyMapping.isActiveAndMatchesKey(event.getKey(), event.getScanCode(), event.getModifiers())) {
                    keyMapping.handleWorldInput(new WorldInputEvent(event.getKeyEvent(), keyMapping));
                    // TODO cannot cancel?
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.KeyPressed.Pre event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                if (!KeyBindsScreenHooks.allowKeyPress(event.getScreen(), event.getKeyEvent())) {
                    event.setCanceled(true);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.KeyReleased.Pre event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                if (!KeyBindsScreenHooks.allowKeyRelease(event.getScreen(), event.getKeyEvent())) {
                    event.setCanceled(true);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.MouseButtonPressed.Pre event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                if (!KeyBindsScreenHooks.allowMouseClick(event.getScreen(), event.getMouseButtonEvent())) {
                    event.setCanceled(true);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.MouseButtonReleased.Pre event) -> {
            if (event.getScreen() instanceof KeyBindsScreen) {
                if (!KeyBindsScreenHooks.allowMouseRelease(event.getScreen(), event.getMouseButtonEvent())) {
                    event.setCanceled(true);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.KeyPressed.Pre event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesKey(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                    final var client = Minecraft.getInstance();
                    final var window = client.getWindow();
                    int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                    int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                    final var inputEvent = new ScreenInputEvent(event.getScreen(), event.getKeyEvent(), mouseX, mouseY, keyMapping);
                    if (keyMapping.ignoresScreenFocus() || !inputEvent.hasFocusedElement()) {
                        if (keyMapping.handleScreenInput(inputEvent)) {
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ScreenEvent.MouseButtonPressed.Pre event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleScreenInput(new ScreenInputEvent(event.getScreen(), event.getMouseButtonEvent(), event.getMouseX(), event.getMouseY(), keyMapping))) {
                        event.setCanceled(true);
                    }
                }
            }
        });
    }
}
