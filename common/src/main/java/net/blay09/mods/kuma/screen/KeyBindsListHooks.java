package net.blay09.mods.kuma.screen;

import net.blay09.mods.kuma.api.KumaKeyMapping;
import net.blay09.mods.kuma.mixin.ButtonAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;

public class KeyBindsListHooks {
    public static void updateResetButton(Button resetButton, KeyMapping keyMapping) {
        final var original = ((ButtonAccessor) resetButton).getOnPress();
        ((ButtonAccessor) resetButton).setOnPress((button) -> {
            if (keyMapping instanceof KumaKeyMapping kumaKeyMapping) {
                kumaKeyMapping.kuma$setModifiers(kumaKeyMapping.kuma$getDefaultModifiers());
            } else {
                throw new IllegalStateException("Expected Kuma-managed key to implement KumaKeyMapping");
            }
            original.onPress(button);
        });
    }
}
