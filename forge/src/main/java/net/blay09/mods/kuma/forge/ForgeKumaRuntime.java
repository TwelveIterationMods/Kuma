package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.WrappedManagedKeyMappingBuilder;
import net.blay09.mods.kuma.api.KeyMappingStorage;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.blay09.mods.kuma.mixin.KeyMappingAccessor;
import net.blay09.mods.kuma.storage.json.SharedJsonKeyMappingStorage;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jspecify.annotations.Nullable;

import java.io.File;

public class ForgeKumaRuntime implements KumaRuntime {

    @Nullable
    private KeyMappingStorage defaultStorage;

    @Override
    public ManagedKeyMapping.RegistrationBuilder createKeyMapping(Identifier id) {
        return new ForgeManagedKeyMappingBuilder(id);
    }

    @Override
    public ManagedKeyMapping.WrapperBuilder wrapKeyMapping(KeyMapping keyMapping) {
        return new WrappedManagedKeyMappingBuilder(keyMapping);
    }

    @Override
    public KeyModifiers getNativeKeyModifiers(KeyMapping keyMapping) {
        return ForgeKeyModifiers.fromNeoForge(keyMapping.getKeyModifier());
    }

    @Override
    public void setNativeKeyModifiers(KeyMapping keyMapping, KeyModifiers modifiers) {
        keyMapping.setKeyModifierAndCode(ForgeKeyModifiers.toNeoForge(modifiers), ((KeyMappingAccessor) keyMapping).getKey());
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
        return false;
    }

}
