package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.api.KeyModifier;
import net.blay09.mods.kuma.api.KeyModifiers;

public class NeoForgeKeyModifiers {

    public static net.neoforged.neoforge.client.settings.KeyModifier toNeoForge(KeyModifiers modifiers) {
        if (modifiers.contains(KeyModifier.SHIFT)) {
            return net.neoforged.neoforge.client.settings.KeyModifier.SHIFT;
        } else if (modifiers.contains(KeyModifier.CONTROL)) {
            return net.neoforged.neoforge.client.settings.KeyModifier.CONTROL;
        } else if (modifiers.contains(KeyModifier.ALT)) {
            return net.neoforged.neoforge.client.settings.KeyModifier.ALT;
        }
        return net.neoforged.neoforge.client.settings.KeyModifier.NONE;
    }

    public static KeyModifiers fromNeoForge(net.neoforged.neoforge.client.settings.KeyModifier modifiers) {
        if (modifiers == net.neoforged.neoforge.client.settings.KeyModifier.SHIFT) {
            return KeyModifiers.of(KeyModifier.SHIFT);
        } else if (modifiers == net.neoforged.neoforge.client.settings.KeyModifier.CONTROL) {
            return KeyModifiers.of(KeyModifier.CONTROL);
        } else if (modifiers == net.neoforged.neoforge.client.settings.KeyModifier.ALT) {
            return KeyModifiers.of(KeyModifier.ALT);
        }
        return KeyModifiers.none();
    }
}
