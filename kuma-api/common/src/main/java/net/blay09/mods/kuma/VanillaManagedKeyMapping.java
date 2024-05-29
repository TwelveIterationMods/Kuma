package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyConflictContext;
import net.blay09.mods.kuma.api.ScreenInputEventHandler;
import net.blay09.mods.kuma.api.WorldInputEventHandler;
import net.minecraft.client.KeyMapping;

import java.util.function.Supplier;

public class VanillaManagedKeyMapping extends AbstractManagedKeyMapping {
    private final Supplier<KeyMapping> mappingSupplier;

    private KeyMapping mapping;

    public VanillaManagedKeyMapping(KeyConflictContext context, ScreenInputEventHandler screenInputEventHandler, WorldInputEventHandler worldInputEventHandler, Supplier<KeyMapping> mappingSupplier) {
        super(context, screenInputEventHandler, worldInputEventHandler);
        this.mappingSupplier = mappingSupplier;
    }

    @Override
    public void setBinding(InputBinding binding) {
        throw new UnsupportedOperationException("Vanilla key bindings cannot be modified");
    }

    @Override
    public InputBinding getBinding() {
        if (mapping == null) {
            return InputBinding.none();
        }

        return InputBinding.of(mapping);
    }

    public KeyMapping register() {
        mapping = mappingSupplier.get();
        return mapping;
    }
}
