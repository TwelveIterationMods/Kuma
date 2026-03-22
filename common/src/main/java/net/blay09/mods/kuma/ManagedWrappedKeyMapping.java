package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ManagedWrappedKeyMapping extends AbstractManagedKeyMapping {

    private final KeyMapping mapping;

    public ManagedWrappedKeyMapping(Identifier id, KeyMapping mapping, KeyConflictContext context, @Nullable ScreenInputEventHandler screenInputEventHandler, @Nullable WorldInputEventHandler worldInputEventHandler, boolean keyRepeat, boolean ignoresScreenFocus, InputBinding defaultBinding, KeyMappingStorage storage) {
        super(id, context, screenInputEventHandler, worldInputEventHandler, keyRepeat, ignoresScreenFocus, defaultBinding, storage);
        this.mapping = mapping;
    }

    @Override
    public void setBinding(InputBinding binding) {
        mapping.setKey(binding.key());
        if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
            kumaKeyMapping.kuma$setModifiers(binding.modifiers());
            getStorage().saveKeyMapping(this);
        }
    }

    @Override
    public InputBinding getBinding() {
        return InputBinding.of(mapping);
    }

    public KeyMapping wrap() {
        if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
            kumaKeyMapping.kuma$setManagedKeyMapping(this);
            kumaKeyMapping.kuma$setConflictContext(getContext());
            kumaKeyMapping.kuma$setDefaultModifiers(getDefaultBinding().modifiers());
            kumaKeyMapping.kuma$setModifiers(getDefaultBinding().modifiers());
        }
        return mapping;
    }

    @Override
    public boolean isBound() {
        return !mapping.isUnbound();
    }

    @Override
    public boolean isUnbound() {
        return mapping.isUnbound();
    }

}
