package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class ForgeKumaRuntime implements KumaRuntime {
    @Override
    public ManagedKeyMapping.Builder createKeyMapping(ResourceLocation id) {
        return new ForgeManagedKeyMappingBuilder(id);
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
        return ForgeKeyModifiers.fromNeoForge(keyMapping.getKeyModifier());
    }

}
