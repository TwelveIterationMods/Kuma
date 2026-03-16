package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.WrappedManagedKeyMappingBuilder;
import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.blay09.mods.kuma.storage.json.SharedJsonKeyMappingStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.io.File;

public class FabricKumaRuntime implements KumaRuntime {

    @Nullable
    private KeyMappingStorage defaultStorage;

    @Override
    public ManagedKeyMapping.RegistrationBuilder createKeyMapping(Identifier id) {
        return new FabricManagedKeyMappingRegistrationBuilder(id);
    }

    @Override
    public ManagedKeyMapping.WrapperBuilder wrapKeyMapping(KeyMapping keyMapping) {
        return new WrappedManagedKeyMappingBuilder(keyMapping);
    }

    @Override
    public KeyModifiers getNativeKeyModifiers(KeyMapping keyMapping) {
        return KeyModifiers.none();
    }

    @Override
    public KeyMappingStorage getDefaultStorage() {
        if (defaultStorage == null) {
            final var configDir = FabricLoader.getInstance().getConfigDir().toFile();
            defaultStorage = new SharedJsonKeyMappingStorage(new File(configDir, "kuma.json"));
        }

        return defaultStorage;
    }

    @Override
    public boolean isModInstalled(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
