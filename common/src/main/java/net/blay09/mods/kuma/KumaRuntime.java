package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public interface KumaRuntime {
    ManagedKeyMapping.RegistrationBuilder createKeyMapping(Identifier id);

    ManagedKeyMapping.WrapperBuilder wrapKeyMapping(KeyMapping keyMapping);

    KeyModifiers getNativeKeyModifiers(KeyMapping keyMapping);

    KeyMappingStorage getDefaultStorage();

    boolean isModInstalled(String modId);
}
