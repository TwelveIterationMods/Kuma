package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.blay09.mods.kuma.storage.json.SharedJsonKeyMappingStorage;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.jspecify.annotations.Nullable;

import java.io.File;

public class NeoForgeKumaRuntime implements KumaRuntime {

    @Nullable
    private KeyMappingStorage defaultStorage;

    @Override
    public ManagedKeyMapping.Builder createKeyMapping(Identifier id) {
        return new NeoForgeManagedKeyMappingBuilder(id);
    }

    @Override
    public KeyModifiers getNativeKeyModifiers(KeyMapping keyMapping) {
        return NeoForgeKeyModifiers.fromNeoForge(keyMapping.getKeyModifier());
    }

    @Override
    public KeyMappingStorage getDefaultStorage() {
        if (defaultStorage == null) {
            final var configDir = FMLPaths.CONFIGDIR.get().toFile();
            defaultStorage = new SharedJsonKeyMappingStorage(new File(configDir, "kuma.json"));
        }

        return defaultStorage;
    }

    @Override
    public boolean isModInstalled(String modId) {
        return ModList.get().isLoaded(modId);
    }

}
