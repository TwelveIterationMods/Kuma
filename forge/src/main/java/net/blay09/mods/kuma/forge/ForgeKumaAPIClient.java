package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.AbstractManagedKeyMapping;
import net.blay09.mods.kuma.ManagedKeyMappingRegistry;
import net.blay09.mods.kuma.VanillaManagedKeyMapping;
import net.blay09.mods.kuma.api.ScreenInputEvent;
import net.blay09.mods.kuma.api.WorldInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeKumaAPIClient {
    public static void init() {
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener((RegisterKeyMappingsEvent event) -> {
            for (final var managedKeyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (managedKeyMapping instanceof VanillaManagedKeyMapping vanillaManagedKeyMapping) {
                    event.register(vanillaManagedKeyMapping.register());
                }
            }
        });

        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                    if (keyMapping instanceof AbstractManagedKeyMapping managedKeyMapping) {
                        managedKeyMapping.tick();
                    }
                }
            }
        });

        MinecraftForge.EVENT_BUS.addListener((InputEvent.MouseButton.Pre event) -> {
            if (Minecraft.getInstance().screen != null) {
                return;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleWorldInput(new WorldInputEvent(keyMapping))) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        });

        MinecraftForge.EVENT_BUS.addListener((InputEvent.Key event) -> {
            if (Minecraft.getInstance().screen != null) {
                return;
            }

            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (event.getAction() == 1 && keyMapping.isActiveAndMatchesKey(event.getKey(), event.getScanCode(), event.getModifiers())) {
                    keyMapping.handleWorldInput(new WorldInputEvent(keyMapping));
                    // TODO cannot cancel?
                }
            }
        });

        MinecraftForge.EVENT_BUS.addListener((ScreenEvent.KeyPressed.Pre event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesKey(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                    final var client = Minecraft.getInstance();
                    final var window = client.getWindow();
                    int mouseX = Mth.floor(client.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
                    int mouseY = Mth.floor(client.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
                    if (keyMapping.handleScreenInput(new ScreenInputEvent(event.getScreen(), mouseX, mouseY, keyMapping))) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        });

        MinecraftForge.EVENT_BUS.addListener((ScreenEvent.MouseButtonPressed.Pre event) -> {
            for (final var keyMapping : ManagedKeyMappingRegistry.getKeyMappings()) {
                if (keyMapping.wasDown() && !keyMapping.isKeyRepeatEnabled()) {
                    continue;
                }

                if (keyMapping.isActiveAndMatchesMouse(event.getButton())) {
                    if (keyMapping.handleScreenInput(new ScreenInputEvent(event.getScreen(), event.getMouseX(), event.getMouseY(), keyMapping))) {
                        event.setCanceled(true);
                    }
                }
            }
        });
    }
}
