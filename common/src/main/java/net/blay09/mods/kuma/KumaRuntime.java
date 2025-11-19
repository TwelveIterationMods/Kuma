package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public interface KumaRuntime {
    ManagedKeyMapping.Builder createKeyMapping(Identifier id);

    boolean areMultiBindingsSupported();

    boolean areModifiersSupported();

    boolean areMultiModifiersSupported();

    boolean areCustomModifiersSupported();

    KeyModifiers getKeyModifiers(KeyMapping keyMapping);
}
