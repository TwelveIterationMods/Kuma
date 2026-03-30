package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ManagedVanillaKeyMapping extends AbstractManagedKeyMapping {

    private final Supplier<KeyMapping> mappingSupplier;
    @Nullable
    private KeyMapping mapping;

    public ManagedVanillaKeyMapping(Identifier id, KeyConflictContext context, @Nullable ScreenInputEventHandler screenInputEventHandler, @Nullable WorldInputEventHandler worldInputEventHandler, boolean keyRepeat, boolean ignoresScreenFocus, InputBinding defaultBinding, KeyMappingStorage storage, Supplier<KeyMapping> mappingSupplier) {
        super(id, context, screenInputEventHandler, worldInputEventHandler, keyRepeat, ignoresScreenFocus, defaultBinding, storage);
        this.mappingSupplier = mappingSupplier;
    }

    @Override
    public void setBinding(InputBinding binding) {
        if (mapping != null) {
            mapping.setKey(binding.key());
            if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
                kumaKeyMapping.kuma$setModifiers(binding.modifiers());
                getStorage().saveKeyMapping(this);
            }
        }
    }

    @Override
    public InputBinding getBinding() {
        return mapping != null ? InputBinding.of(mapping) : getDefaultBinding();
    }

    public KeyMapping register() {
        if (mapping != null) {
            return mapping;
        }

        mapping = mappingSupplier.get();
        if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
            kumaKeyMapping.kuma$setManagedKeyMapping(this);
            kumaKeyMapping.kuma$setConflictContext(getContext());
            kumaKeyMapping.kuma$setDefaultModifiers(getDefaultBinding().modifiers());
            kumaKeyMapping.kuma$setModifiers(getDefaultBinding().modifiers());
        }
        getStorage().loadKeyMapping(this);
        return mapping;
    }

    @Override
    public boolean isBound() {
        return mapping != null && !mapping.isUnbound();
    }

    @Override
    public boolean isUnbound() {
        return mapping == null || mapping.isUnbound();
    }

}
