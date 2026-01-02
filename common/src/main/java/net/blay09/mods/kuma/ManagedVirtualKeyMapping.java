package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class ManagedVirtualKeyMapping extends AbstractManagedKeyMapping {

    private InputBinding binding;

    public ManagedVirtualKeyMapping(Identifier id, KeyConflictContext context, @Nullable ScreenInputEventHandler screenInputEventHandler, @Nullable WorldInputEventHandler worldInputEventHandler, boolean keyRepeat, boolean ignoresScreenFocus, InputBinding defaultBinding, KeyMappingStorage storage) {
        super(id, context, screenInputEventHandler, worldInputEventHandler, keyRepeat, ignoresScreenFocus, defaultBinding, storage);
        this.binding = defaultBinding;
    }

    @Override
    public void setBinding(InputBinding binding) {
        this.binding = binding;
    }

    @Override
    public InputBinding getBinding() {
        return binding;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public boolean isUnbound() {
        return false;
    }

}
