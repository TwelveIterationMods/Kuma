package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.AbstractManagedKeyMappingBuilder;
import net.blay09.mods.kuma.ManagedVanillaKeyMapping;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyConflictContext;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class NeoForgeManagedKeyMappingBuilder extends AbstractManagedKeyMappingBuilder {

    public NeoForgeManagedKeyMappingBuilder(Identifier id) {
        super(id);
    }

    @Override
    protected ManagedKeyMapping createVanillaKeyMapping(Identifier id, String name, InputBinding binding, KeyConflictContext context) {
        return new ManagedVanillaKeyMapping(id, context, screenInputHandler, worldInputHandler, keyRepeat, ignoresScreenFocus, binding, storage, () -> {
            final var defaultKey = binding.key();
            return new KeyMapping(name, defaultKey.getType(), defaultKey.getValue(), category);
        });
    }
}
