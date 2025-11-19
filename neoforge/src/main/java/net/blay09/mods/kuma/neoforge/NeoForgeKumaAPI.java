package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.AbstractManagedKeyMapping;
import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.VanillaManagedKeyMapping;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "kuma_api", dist = Dist.CLIENT)
public class NeoForgeKumaAPI {

    public NeoForgeKumaAPI(IEventBus modEventBus) {
        modEventBus.addListener((RegisterKeyMappingsEvent event) -> {
            for (final var managedKeyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (managedKeyMapping instanceof VanillaManagedKeyMapping vanillaManagedKeyMapping) {
                    event.register(vanillaManagedKeyMapping.register());
                }
            }
        });

        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping instanceof AbstractManagedKeyMapping managedKeyMapping) {
                    managedKeyMapping.tick();
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
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesKey(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                    final var client = Minecraft.getInstance();
                    final var window = client.getWindow();
                    int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                    int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                    if (keyMapping.ignoresScreenFocus() || !event.getScreen().isFocused()) {
                        if (keyMapping.handleScreenInput(new ScreenInputEvent(event.getScreen(), event.getKeyEvent(), mouseX, mouseY, keyMapping))) {
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
