package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class NeoForgeKumaRuntime implements KumaRuntime {
    @Override
    public ManagedKeyMapping.Builder createKeyMapping(ResourceLocation id) {
        return new NeoForgeManagedKeyMappingBuilder(id);
    }

    @Override
    public boolean areMultiBindingsSupported() {
        return true;
    }

    @Override
    public boolean areModifiersSupported() {
        return true;
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
        return NeoForgeKeyModifiers.fromNeoForge(keyMapping.getKeyModifier());
    }

}
