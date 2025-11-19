package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class FabricKumaRuntime implements KumaRuntime {
    @Override
    public ManagedKeyMapping.Builder createKeyMapping(Identifier id) {
        return new FabricManagedKeyMappingBuilder(id);
    }

    @Override
    public boolean areMultiBindingsSupported() {
        return false;
    }

    @Override
    public boolean areModifiersSupported() {
        return false;
    }

    @Override
    public boolean areMultiModifiersSupported() {
        return false;
    }

    @Override
    public boolean areCustomModifiersSupported() {
        return false;
    }

    @Override
    public KeyModifiers getKeyModifiers(KeyMapping keyMapping) {
        return KeyModifiers.none();
    }
}
