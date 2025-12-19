package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public interface KumaRuntime {
    ManagedKeyMapping.Builder createKeyMapping(Identifier id);

    KeyModifiers getNativeKeyModifiers(KeyMapping keyMapping);

    KeyMappingStorage getDefaultStorage();
}
