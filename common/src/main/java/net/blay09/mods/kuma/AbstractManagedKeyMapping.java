package net.blay09.mods.kuma;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.*;

public abstract class AbstractManagedKeyMapping implements ManagedKeyMapping {

    private final KeyConflictContext context;
    private final ScreenInputEventHandler screenInputEventHandler;
    private final WorldInputEventHandler worldInputEventHandler;

    protected AbstractManagedKeyMapping(KeyConflictContext context, ScreenInputEventHandler screenInputEventHandler, WorldInputEventHandler worldInputEventHandler) {
        this.context = context;
        this.screenInputEventHandler = screenInputEventHandler;
        this.worldInputEventHandler = worldInputEventHandler;
    }

    @Override
    public abstract void setBinding(InputBinding binding);

    @Override
    public abstract InputBinding getBinding();

    protected InputConstants.Key getKey() {
        return getBinding().key();
    }

    protected KeyModifiers getModifiers() {
        return getBinding().modifiers();
    }

    @Override
    public boolean isContextActive() {
        return Kuma.isContextActive(context);
    }

    @Override
    public boolean areModifiersActive() {
        return Kuma.areModifiersActive(getModifiers());
    }

    @Override
    public boolean isDown() {
        return Kuma.isDown(getKey());
    }

    @Override
    public boolean matchesMouse(int button) {
        final var key = getKey();
        return key.getType().equals(InputConstants.Type.MOUSE) && key.getValue() == button;
    }

    @Override
    public boolean matchesKey(int key, int scanCode, int modifiers) {
        final var keyBinding = getKey();
        return keyBinding.getType().equals(InputConstants.Type.KEYSYM) && keyBinding.getValue() == key;
    }

    @Override
    public boolean handleScreenInput(ScreenInputEvent event) {
        if (screenInputEventHandler == null) {
            return false;
        }

        return screenInputEventHandler.handle(event);
    }

    @Override
    public boolean handleWorldInput(WorldInputEvent event) {
        if (worldInputEventHandler == null) {
            return false;
        }

        return worldInputEventHandler.handle(event);
    }
}
