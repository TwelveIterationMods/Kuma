package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeSpi;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class Kuma {
    private static final KumaRuntime runtime = KumaRuntimeSpi.create();

    public static ManagedKeyMapping.Builder createKeyMapping(ResourceLocation id) {
        return runtime.createKeyMapping(id);
    }

    public static boolean isContextActive(KeyConflictContext context) {
        final var client = Minecraft.getInstance();
        return switch (context) {
            case SCREEN -> client.screen != null;
            case WORLD -> client.screen == null && client.level != null;
            default -> true;
        };
    }

    public static boolean areModifiersActive(KeyModifiers modifiers) {
        if (modifiers.contains(KeyModifier.ALT) && !Screen.hasAltDown()) {
            return false;
        }
        if (modifiers.contains(KeyModifier.CONTROL) && !Screen.hasControlDown()) {
            return false;
        }
        if (modifiers.contains(KeyModifier.SHIFT) && !Screen.hasShiftDown()) {
            return false;
        }
        final var window = Minecraft.getInstance().getWindow();
        for (final var key : modifiers.getCustomModifiers()) {
            if (!InputConstants.isKeyDown(window.getWindow(), key.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static boolean areModifiersSupported() {
        return runtime.areModifiersSupported();
    }

    public static boolean areMultiModifiersSupported() {
        return runtime.areMultiModifiersSupported();
    }

    public static boolean areCustomModifiersSupported() {
        return runtime.areCustomModifiersSupported();
    }

    public static boolean isBindingSupported(InputBinding binding, KeyConflictContext context) {
        final var defaultModifiers = binding.modifiers();
        final var requiresKeyModifiers = !defaultModifiers.isEmpty();
        final var requiresMultipleKeyModifiers = defaultModifiers.size() > 1;
        final var requiresCustomKeyModifiers = defaultModifiers.hasCustomModifiers();
        if (requiresKeyModifiers && !Kuma.areModifiersSupported()) {
            return false;
        }
        if(requiresMultipleKeyModifiers && !Kuma.areMultiModifiersSupported()) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (requiresCustomKeyModifiers && !Kuma.areCustomModifiersSupported()) {
            return false;
        }
        return true;
    }

    public static KeyModifiers getKeyModifiers(KeyMapping keyMapping) {
        return runtime.getKeyModifiers(keyMapping);
    }
}
