package net.blay09.mods.kuma;

import net.blay09.mods.kuma.api.*;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class AbstractManagedKeyMappingBuilder<T> {

    protected final Identifier id;
    protected InputBinding defaultBinding = InputBinding.none();
    @Nullable
    protected WorldInputEventHandler worldInputHandler;
    @Nullable
    protected ScreenInputEventHandler screenInputHandler;
    protected boolean keyRepeat = false;
    protected boolean ignoresScreenFocus = false;
    protected KeyMappingStorage storage = Kuma.getDefaultStorage();

    protected AbstractManagedKeyMappingBuilder(Identifier id) {
        this.id = id;
    }

    protected abstract T self();

    public T withCustomStorage(KeyMappingStorage storage) {
        this.storage = storage;
        return self();
    }

    public T handleWorldInput(WorldInputEventHandler handler) {
        this.worldInputHandler = handler;
        return self();
    }

    public T handleScreenInput(ScreenInputEventHandler handler) {
        this.screenInputHandler = handler;
        return self();
    }

    public T enableKeyRepeat() {
        keyRepeat = true;
        return self();
    }

    public T ignoreScreenFocus() {
        ignoresScreenFocus = true;
        return self();
    }

    protected KeyConflictContext determineContext() {
        if (worldInputHandler != null && screenInputHandler == null) {
            return KeyConflictContext.WORLD;
        }
        if (screenInputHandler != null && worldInputHandler == null) {
            return KeyConflictContext.SCREEN;
        }
        return KeyConflictContext.UNIVERSAL;
    }
}
