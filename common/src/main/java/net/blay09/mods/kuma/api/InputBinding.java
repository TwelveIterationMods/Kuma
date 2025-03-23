package net.blay09.mods.kuma.api;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

/**
 * Represents an input binding, which is a combination of a key or mouse button and optional key modifiers.
 * This class provides static factory methods to create various types of input bindings.
 */
public record InputBinding(InputConstants.Key key, KeyModifiers modifiers) {

    /**
     * Creates a new {@link InputBinding} for a mouse button with no key modifiers.
     *
     * @param button the mouse button index
     * @return a new {@link InputBinding} for the specified mouse button
     */
    public static InputBinding mouse(int button) {
        return mouse(button, KeyModifiers.none());
    }

    /**
     * Creates a new {@link InputBinding} for a mouse button with the specified key modifiers.
     *
     * @param button    the mouse button index
     * @param modifiers the key modifiers to apply to the mouse button
     * @return a new {@link InputBinding} for the specified mouse button and modifiers
     */
    public static InputBinding mouse(int button, KeyModifiers modifiers) {
        return new InputBinding(InputConstants.Type.MOUSE.getOrCreate(button), modifiers);
    }

    /**
     * Creates a new {@link InputBinding} for a key with no key modifiers.
     *
     * @param keyCode the key code of the key
     * @return a new {@link InputBinding} for the specified key
     */
    public static InputBinding key(int keyCode) {
        return key(keyCode, KeyModifiers.none());
    }

    /**
     * Creates a new {@link InputBinding} for a key with the specified key modifiers.
     *
     * @param keyCode   the key code of the key
     * @param modifiers the key modifiers to apply to the key
     * @return a new {@link InputBinding} for the specified key and modifiers
     */
    public static InputBinding key(int keyCode, KeyModifiers modifiers) {
        return new InputBinding(InputConstants.getKey(keyCode, -1), modifiers);
    }

    /**
     * Creates a new empty {@link InputBinding} that is not bound to anything.
     *
     * @return a new {@link InputBinding} with no key or mouse button assigned
     */
    public static InputBinding none() {
        return new InputBinding(InputConstants.UNKNOWN, KeyModifiers.none());
    }

    /**
     * Creates a new {@link InputBinding} from the specified {@link KeyMapping}.
     *
     * @param mapping the {@link KeyMapping} to create the {@link InputBinding} from
     * @return a new {@link InputBinding} with the key and modifiers from the specified {@link KeyMapping}
     */
    public static InputBinding of(KeyMapping mapping) {
        return new InputBinding(((KeyMappingAccessor) mapping).kuma_getKey(), KeyModifiers.of(mapping));
    }
}