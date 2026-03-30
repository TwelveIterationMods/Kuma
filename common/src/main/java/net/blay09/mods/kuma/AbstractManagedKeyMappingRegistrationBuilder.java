package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractManagedKeyMappingRegistrationBuilder extends AbstractManagedKeyMappingBuilder<ManagedKeyMapping.RegistrationBuilder> implements ManagedKeyMapping.RegistrationBuilder {

    protected KeyMapping.Category category;
    @Nullable
    protected String name;
    @Nullable
    protected KeyConflictContext context;
    protected boolean skipRegistration = false;

    public AbstractManagedKeyMappingRegistrationBuilder(Identifier id) {
        super(id);
        category = KumaKeyCategories.getDefaultCategory(id.getNamespace());
    }

    @Override
    protected ManagedKeyMapping.RegistrationBuilder self() {
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder overrideName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder overrideName(Function<Identifier, String> nameFunction) {
        this.name = nameFunction.apply(id);
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder overrideCategory(KeyMapping.Category category) {
        this.category = category;
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder withContext(KeyConflictContext context) {
        this.context = context;
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder withDefault(InputBinding binding) {
        this.defaultBinding = binding;
        return this;
    }

    @Override
    public ManagedKeyMapping.RegistrationBuilder skipRegistration() {
        skipRegistration = true;
        return this;
    }

    @Override
    public ManagedKeyMapping build() {
        if (name == null) {
            name = String.format("key.%s.%s", id.getNamespace(), id.getPath());
        }
        if (context == null) {
            context = determineContext();
        }
        final var managedKeyMapping = skipRegistration ? createVirtualKeyMapping(id, context) : createVanillaKeyMapping(id, name, defaultBinding, context);
        // If we're skipping registration, we load modifiers early. Otherwise we do it after registration.
        if (skipRegistration) {
            managedKeyMapping.getStorage().loadKeyMapping(managedKeyMapping);
        }
        ManagedKeyMappingRegistry.register(managedKeyMapping);
        return managedKeyMapping;
    }

    protected ManagedKeyMapping createVirtualKeyMapping(Identifier id, KeyConflictContext context) {
        return new ManagedVirtualKeyMapping(id, context, screenInputHandler, worldInputHandler, keyRepeat, ignoresScreenFocus, defaultBinding, storage);
    }

    protected abstract ManagedKeyMapping createVanillaKeyMapping(Identifier id, String name, InputBinding binding, KeyConflictContext context);
}
