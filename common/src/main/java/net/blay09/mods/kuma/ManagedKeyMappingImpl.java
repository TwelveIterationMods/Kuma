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

import java.util.function.Supplier;

public class ManagedKeyMappingImpl implements ManagedKeyMapping {

    private final Supplier<KeyMapping> mappingSupplier;
    private KeyMapping mapping;

    private final Identifier id;
    private final KeyConflictContext context;
    private final ScreenInputEventHandler screenInputEventHandler;
    private final WorldInputEventHandler worldInputEventHandler;
    private final boolean keyRepeat;
    private final boolean ignoresScreenFocus;
    private final InputBinding defaultBinding;
    private final KeyMappingStorage storage;
    private boolean wasDown;

    public ManagedKeyMappingImpl(Identifier id, KeyConflictContext context, ScreenInputEventHandler screenInputEventHandler, WorldInputEventHandler worldInputEventHandler, boolean keyRepeat, boolean ignoresScreenFocus, InputBinding defaultBinding, KeyMappingStorage storage, Supplier<KeyMapping> mappingSupplier) {
        this.id = id;
        this.context = context;
        this.screenInputEventHandler = screenInputEventHandler;
        this.worldInputEventHandler = worldInputEventHandler;
        this.keyRepeat = keyRepeat;
        this.ignoresScreenFocus = ignoresScreenFocus;
        this.defaultBinding = defaultBinding;
        this.storage = storage;
        this.mappingSupplier = mappingSupplier;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public void setBinding(InputBinding binding) {
        if (mapping != null) {
            mapping.setKey(binding.key());
            if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
                kumaKeyMapping.kuma$setModifiers(binding.modifiers());
                storage.saveKeyMapping(this);
            }
        }
    }

    @Override
    public InputBinding getBinding() {
        return mapping != null ? InputBinding.of(mapping) : InputBinding.none();
    }

    public KeyMapping register() {
        mapping = mappingSupplier.get();
        if (mapping instanceof KumaKeyMapping kumaKeyMapping) {
            kumaKeyMapping.kuma$setManagedKeyMapping(this);
            kumaKeyMapping.kuma$setConflictContext(context);
            kumaKeyMapping.kuma$setDefaultModifiers(getDefaultBinding().modifiers());
        }
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

    public void tick() {
        wasDown = isDown();
    }

    @Override
    public KeyMappingStorage getStorage() {
        return storage;
    }
}
