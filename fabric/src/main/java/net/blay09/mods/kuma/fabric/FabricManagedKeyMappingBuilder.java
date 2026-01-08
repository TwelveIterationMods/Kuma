package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.AbstractManagedKeyMappingBuilder;
import net.blay09.mods.kuma.ManagedVanillaKeyMapping;
import net.blay09.mods.kuma.api.*;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class FabricManagedKeyMappingBuilder extends AbstractManagedKeyMappingBuilder {

    public FabricManagedKeyMappingBuilder(Identifier id) {
        super(id);
    }

    @Override
    protected ManagedKeyMapping createVanillaKeyMapping(Identifier id, String name, InputBinding binding, KeyConflictContext context) {
        final var managedKeyMapping = new ManagedVanillaKeyMapping(id, context, screenInputHandler, worldInputHandler, keyRepeat, ignoresScreenFocus, binding, storage, () -> {
            final var defaultKey = binding.key();
            return new KeyMapping(name, defaultKey.getType(), defaultKey.getValue(), category);
        });
        final var keyMapping = managedKeyMapping.register();

        FabricKeyMappingContexts.setKeyMappingContext(keyMapping, context);
        KeyMappingHelper.registerKeyMapping(keyMapping);
        return managedKeyMapping;
    }
}
