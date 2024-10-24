package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractManagedKeyMappingBuilder implements ManagedKeyMapping.Builder {

    protected final ResourceLocation id;
    protected String category;
    protected KeyConflictContext context;
    protected InputBinding defaultBinding = InputBinding.none();
    protected List<InputBinding> fallbackBindings = new ArrayList<>();
    protected boolean forceVirtual;
    protected WorldInputEventHandler worldInputHandler;
    protected ScreenInputEventHandler screenInputHandler;

    public AbstractManagedKeyMappingBuilder(ResourceLocation id) {
        this.id = id;
        category = "key.categories." + id.getNamespace();
    }

    @Override
    public ManagedKeyMapping.Builder overrideCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder withContext(KeyConflictContext context) {
        this.context = context;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder withDefault(InputBinding binding) {
        this.defaultBinding = binding;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder withFallbackDefault(InputBinding binding) {
        this.fallbackBindings.add(binding);
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder handleWorldInput(WorldInputEventHandler handler) {
        this.worldInputHandler = handler;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder forceVirtual() {
        this.forceVirtual = true;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder handleScreenInput(ScreenInputEventHandler handler) {
        this.screenInputHandler = handler;
        return this;
    }

    private KeyConflictContext determineContext() {
        if (worldInputHandler != null && screenInputHandler == null) {
            return KeyConflictContext.WORLD;
        }
        if (screenInputHandler != null && worldInputHandler == null) {
            return KeyConflictContext.SCREEN;
        }
        return KeyConflictContext.UNIVERSAL;
    }

    private Optional<InputBinding> determineBinding() {
        if (Kuma.isBindingSupported(defaultBinding, context)) {
            return Optional.of(defaultBinding);
        }
        for (InputBinding binding : fallbackBindings) {
            if (Kuma.isBindingSupported(binding, context)) {
                return Optional.of(binding);
            }
        }
        return Optional.empty();
    }

    @Override
    public ManagedKeyMapping build() {
        final var name = String.format("key.%s.%s", id.getNamespace(), id.getPath());
        if (context == null) {
            context = determineContext();
        }
        var effectiveBinding = determineBinding();
        var managedKeyMapping = effectiveBinding.map(it -> {
                    if (forceVirtual) {
                        return null;
                    }
                    return (ManagedKeyMapping) createVanillaKeyMapping(name, it);
                })
                .orElseGet(() -> new VirtualManagedKeyMapping(context, screenInputHandler, worldInputHandler, defaultBinding));
        ManagedKeyMappingRegistry.register(managedKeyMapping);
        return managedKeyMapping;
    }

    protected abstract VanillaManagedKeyMapping createVanillaKeyMapping(String name, InputBinding binding);
}
