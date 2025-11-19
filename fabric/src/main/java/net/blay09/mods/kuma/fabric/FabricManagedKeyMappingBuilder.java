package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.AbstractManagedKeyMappingBuilder;
import net.blay09.mods.kuma.VanillaManagedKeyMapping;
import net.blay09.mods.kuma.api.*;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class FabricManagedKeyMappingBuilder extends AbstractManagedKeyMappingBuilder {

    public FabricManagedKeyMappingBuilder(Identifier id) {
        super(id);
    }

    @Override
    protected VanillaManagedKeyMapping createVanillaKeyMapping(String name, InputBinding binding) {
        final var managedKeyMapping = new VanillaManagedKeyMapping(context, screenInputHandler, worldInputHandler, keyRepeat, () -> {
            final var defaultKey = binding.key();
            return new KeyMapping(name, defaultKey.getType(), defaultKey.getValue(), category);
        });
        final var keyMapping = managedKeyMapping.register();

        FabricKeyMappingContexts.setKeyMappingContext(keyMapping, context);
        KeyBindingHelper.registerKeyBinding(keyMapping);
        return managedKeyMapping;
    }
}
