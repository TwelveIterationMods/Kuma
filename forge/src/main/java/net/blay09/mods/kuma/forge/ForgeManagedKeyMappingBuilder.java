package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.AbstractManagedKeyMappingBuilder;
import net.blay09.mods.kuma.VanillaManagedKeyMapping;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyConflictContext;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class ForgeManagedKeyMappingBuilder extends AbstractManagedKeyMappingBuilder {

    public ForgeManagedKeyMappingBuilder(ResourceLocation id) {
        super(id);
    }

    private net.minecraftforge.client.settings.KeyConflictContext mapConflictContext(KeyConflictContext context) {
        return switch (context) {
            case WORLD -> net.minecraftforge.client.settings.KeyConflictContext.IN_GAME;
            case SCREEN -> net.minecraftforge.client.settings.KeyConflictContext.GUI;
            default -> net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL;
        };
    }

    @Override
    protected VanillaManagedKeyMapping createVanillaKeyMapping(String name, InputBinding binding) {
        return new VanillaManagedKeyMapping(context, screenInputHandler, worldInputHandler, () -> {
            final var defaultKey = binding.key();
            final var effectiveContext = mapConflictContext(context);
            final var effectiveModifier = ForgeKeyModifiers.toNeoForge(binding.modifiers());
            return new KeyMapping(name, effectiveContext, effectiveModifier, defaultKey.getType(), defaultKey.getValue(), category);
        });
    }
}
