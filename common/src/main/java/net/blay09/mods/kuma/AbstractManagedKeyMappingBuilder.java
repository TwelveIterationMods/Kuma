package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class AbstractManagedKeyMappingBuilder implements ManagedKeyMapping.Builder {

    protected final Identifier id;
    protected KeyMapping.Category category;
    @Nullable
    protected KeyConflictContext context;
    protected InputBinding defaultBinding = InputBinding.none();
    @Nullable
    protected WorldInputEventHandler worldInputHandler;
    @Nullable
    protected ScreenInputEventHandler screenInputHandler;
    protected boolean keyRepeat = false;
    protected boolean ignoresScreenFocus = false;
    protected KeyMappingStorage storage = Kuma.getDefaultStorage();

    public AbstractManagedKeyMappingBuilder(Identifier id) {
        this.id = id;
        category = KumaKeyCategories.getDefaultCategory(id.getNamespace());
    }

    @Override
    public ManagedKeyMapping.Builder overrideCategory(KeyMapping.Category category) {
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
    public ManagedKeyMapping.Builder withCustomStorage(KeyMappingStorage storage) {
        this.storage = storage;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder handleWorldInput(WorldInputEventHandler handler) {
        this.worldInputHandler = handler;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder handleScreenInput(ScreenInputEventHandler handler) {
        this.screenInputHandler = handler;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder enableKeyRepeat() {
        keyRepeat = true;
        return this;
    }

    @Override
    public ManagedKeyMapping.Builder ignoreScreenFocus() {
        ignoresScreenFocus = true;
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

    @Override
    public ManagedKeyMapping build() {
        final var name = String.format("key.%s.%s", id.getNamespace(), id.getPath());
        if (context == null) {
            context = determineContext();
        }
        final var managedKeyMapping = createVanillaKeyMapping(id, name, defaultBinding, context);
        managedKeyMapping.getStorage().loadKeyMapping(managedKeyMapping);
        ManagedKeyMappingRegistry.register(managedKeyMapping);
        return managedKeyMapping;
    }

    protected abstract ManagedKeyMapping createVanillaKeyMapping(Identifier id, String name, InputBinding binding, KeyConflictContext context1);
}
