package net.blay09.mods.kuma;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public abstract class AbstractManagedKeyMapping implements ManagedKeyMapping, TickableKeyMapping {

    private final Identifier id;
    private final KeyConflictContext context;
    @Nullable
    private final ScreenInputEventHandler screenInputEventHandler;
    @Nullable
    private final WorldInputEventHandler worldInputEventHandler;
    private final boolean keyRepeat;
    private final boolean ignoresScreenFocus;
    private final InputBinding defaultBinding;
    private final KeyMappingStorage storage;
    private boolean wasDown;

    public AbstractManagedKeyMapping(Identifier id, KeyConflictContext context, @Nullable ScreenInputEventHandler screenInputEventHandler, @Nullable WorldInputEventHandler worldInputEventHandler, boolean keyRepeat, boolean ignoresScreenFocus, InputBinding defaultBinding, KeyMappingStorage storage) {
        this.id = id;
        this.context = context;
        this.screenInputEventHandler = screenInputEventHandler;
        this.worldInputEventHandler = worldInputEventHandler;
        this.keyRepeat = keyRepeat;
        this.ignoresScreenFocus = ignoresScreenFocus;
        this.defaultBinding = defaultBinding;
        this.storage = storage;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public InputBinding getDefaultBinding() {
        return defaultBinding;
    }

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
        return isBound() && Kuma.isDown(getKey());
    }

    @Override
    public boolean wasDown() {
        return wasDown;
    }

    @Override
    public boolean matchesMouse(int button) {
        if (isUnbound()) {
            return false;
        }
        final var key = getKey();
        return key.getType().equals(InputConstants.Type.MOUSE) && key.getValue() == button;
    }

    @Override
    public boolean matchesKey(int key, int scanCode, int modifiers) {
        if (isUnbound()) {
            return false;
        }
        final var keyBinding = getKey();
        return keyBinding.getType().equals(InputConstants.Type.KEYSYM) && keyBinding.getValue() == key;
    }

    @Override
    public boolean handleScreenInput(ScreenInputEvent event) {
        if (screenInputEventHandler == null) {
            return false;
        }

        if (event.screen() instanceof KeyBindsScreen) {
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

    @Override
    public Component getBoundKeyDisplayName() {
        return getBinding().key().getDisplayName();
    }

    @Override
    public boolean isKeyRepeatEnabled() {
        return keyRepeat;
    }

    @Override
    public boolean ignoresScreenFocus() {
        return ignoresScreenFocus;
    }

    @Override
    public boolean isActiveAndMatchesInput(InputWithModifiers input) {
        if (!isContextActive() || !areModifiersActive()) {
            return false;
        }

        return switch (input) {
            case KeyEvent event -> matchesKey(event.key(), event.scancode(), event.modifiers());
            case MouseButtonEvent event -> matchesMouse(event.button());
            case MouseButtonInfo event -> matchesMouse(event.button());
            default -> false;
        };
    }

    @Override
    public void tick() {
        wasDown = isDown();
    }

    @Override
    public KeyMappingStorage getStorage() {
        return storage;
    }

    public KeyConflictContext getContext() {
        return context;
    }
}
