package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyConflictContext;
import net.blay09.mods.kuma.api.ScreenInputEventHandler;
import net.blay09.mods.kuma.api.WorldInputEventHandler;

public class VirtualManagedKeyMapping extends AbstractManagedKeyMapping {

    private InputBinding binding;

    public VirtualManagedKeyMapping(KeyConflictContext context, ScreenInputEventHandler screenInputHandler, WorldInputEventHandler worldInputHandler, InputBinding binding) {
        super(context, screenInputHandler, worldInputHandler);
        this.binding = binding;
    }

    @Override
    public void setBinding(InputBinding binding) {
        this.binding = binding;
    }

    @Override
    public InputBinding getBinding() {
        return binding;
    }
}
